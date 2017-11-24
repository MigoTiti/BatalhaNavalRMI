package batalhanavalrmi.rede;

import batalhanavalrmi.telas.TelaInicial;
import batalhanavalrmi.telas.BatalhaTela;
import batalhanavalrmi.util.RectangleCoordenado;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.StringTokenizer;
import javafx.application.Platform;

public class Comunicacao extends UnicastRemoteObject implements ComunicacaoRMI {

    private int estadoJogador;

    public int jogadoresConectados;

    public static RectangleCoordenado[][] campoJogador;

    public static final int DESCONECTADO = 0;
    public static final int CONECTADO = 1;
    public static final int PRONTO = 2;
    public static final int VEZ_DO_JOGADOR = 3;
    public static final int GANHOU = 4;

    public Comunicacao(int nJogador) throws RemoteException {
        this.estadoJogador = CONECTADO;
        jogadoresConectados = 1;
    }

    @Override
    public void conectar(int nJogador) throws RemoteException {
        jogadoresConectados++;
    }

    @Override
    public void desconectar() throws RemoteException {
        jogadoresConectados--;
        Platform.runLater(() -> {
            TelaInicial.enviarMensagemErro("O BROTHER SE DESCONECTOU");
            TelaInicial.createScene();
        });
    }

    @Override
    public void pronto(int nJogador) throws RemoteException {
        if (nJogador == 1) {
            estadoJogador = VEZ_DO_JOGADOR;
        } else {
            estadoJogador = PRONTO;
        }
    }

    @Override
    public String jogada(String jogada) throws RemoteException {
        StringTokenizer st = new StringTokenizer(jogada, "&");
        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());

        estadoJogador = VEZ_DO_JOGADOR;

        Platform.runLater(() -> {
            TelaInicial.enviarMensagemInfo("Sua vez");
        });

        if (campoJogador[x][y].isOcupado()) {
            BatalhaTela.getInstancia().decrementarContagemUsuario();
            BatalhaTela.getInstancia().getCampoUsuarioMatriz()[x][y].setFill(BatalhaTela.COR_ACERTO);

            if (BatalhaTela.getInstancia().getContagemAdversario() == 0) {
                Platform.runLater(() -> {
                    TelaInicial.enviarMensagemInfo("TU PERDEU, OTÁRIO");
                });
            } else {
                Platform.runLater(() -> {
                    TelaInicial.enviarMensagemInfo("Sua vez");
                });
            }

            return "a";
        } else {
            BatalhaTela.getInstancia().getCampoUsuarioMatriz()[x][y].setFill(BatalhaTela.COR_ERRO);

            Platform.runLater(() -> {
                TelaInicial.enviarMensagemInfo("Sua vez");
            });

            return "e";
        }
    }

    @Override
    public int getEstadoJogador() {
        return estadoJogador;
    }

    @Override
    public void setEstadoJogador(int estadoJogador) {
        this.estadoJogador = estadoJogador;
    }

    @Override
    public RectangleCoordenado[][] getCampoJogador() {
        return campoJogador;
    }

    @Override
    public void setCampoJogador(RectangleCoordenado[][] campoJogador) {
        Comunicacao.campoJogador = campoJogador;
    }

    @Override
    public int getJogadoresConectados() {
        return jogadoresConectados;
    }
}
