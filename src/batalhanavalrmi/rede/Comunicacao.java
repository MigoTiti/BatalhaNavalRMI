package batalhanavalrmi.rede;

import batalhanavalrmi.BatalhaNavalRMIMain;
import batalhanavalrmi.telas.BatalhaTela;
import batalhanavalrmi.util.RectangleCoordenado;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.StringTokenizer;
import javafx.application.Platform;

public class Comunicacao extends UnicastRemoteObject implements ComunicacaoRMI {

    private String ipCliente;
    private String ipServidor;

    private int estadoJogador;

    public int jogadoresConectados;

    public static RectangleCoordenado[][] campoJogador;

    public static final int DESCONECTADO = 0;
    public static final int CONECTADO = 1;
    public static final int PRONTO = 2;
    public static final int VEZ_DO_JOGADOR = 3;
    public static final int GANHOU = 4;

    public Comunicacao(String ip, int nJogador) throws RemoteException {
        this.estadoJogador = CONECTADO;
        jogadoresConectados = 1;

        if (nJogador == 1) {
            this.ipServidor = ip;
        } else {
            this.ipCliente = ip;
        }
    }

    @Override
    public void conectar(String ip, int nJogador) throws RemoteException {
        jogadoresConectados++;

        if (nJogador == 1) {
            this.ipServidor = ip;
        } else {
            this.ipCliente = ip;
        }
    }

    @Override
    public void desconectar() throws RemoteException {
        jogadoresConectados--;
        Platform.runLater(() -> {
            BatalhaNavalRMIMain.enviarMensagemErro("O BROTHER SE DESCONECTOU");
            BatalhaNavalRMIMain.createScene();
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
            BatalhaNavalRMIMain.enviarMensagemInfo("Sua vez");
        });

        if (campoJogador[x][y].isOcupado()) {
            BatalhaTela.contagemUsuario--;
            BatalhaTela.campoUsuarioMatriz[x][y].setFill(BatalhaTela.COR_ACERTO);

            if (BatalhaTela.contagemUsuario == 0) {
                Platform.runLater(() -> {
                    BatalhaNavalRMIMain.enviarMensagemInfo("TU PERDEU, OTÁRIO");
                });
            } else {
                Platform.runLater(() -> {
                    BatalhaNavalRMIMain.enviarMensagemInfo("Sua vez");
                });
            }

            return "a";
        } else {
            BatalhaTela.campoUsuarioMatriz[x][y].setFill(BatalhaTela.COR_ERRO);

            Platform.runLater(() -> {
                BatalhaNavalRMIMain.enviarMensagemInfo("Sua vez");
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

    @Override
    public String getIpCliente() {
        return ipCliente;
    }

    @Override
    public String getIpServidor() {
        return ipServidor;
    }

    @Override
    public void setIpCliente(String ipCliente) {
        this.ipCliente = ipCliente;
    }

    @Override
    public void setIpServidor(String ipServidor) {
        this.ipServidor = ipServidor;
    }
}
