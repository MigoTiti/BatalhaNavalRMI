package batalhanavalrmi.rede;

import batalhanavalrmi.telas.TelaInicial;
import batalhanavalrmi.telas.BatalhaTela;
import batalhanavalrmi.util.RectangleCoordenado;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javafx.application.Platform;

public class Comunicacao extends UnicastRemoteObject implements ComunicacaoRMI {

    private int estadoJogador;

    private boolean oponenteConectado;
    private String ipOponente;

    private String nickName;
    private String nickNameAdversario;

    private RectangleCoordenado[][] campoJogador;

    public static final int DESCONECTADO = 0;
    public static final int CONECTADO = 1;
    public static final int PRONTO = 2;
    public static final int VEZ_DO_JOGADOR = 3;
    public static final int GANHOU = 4;
    public static final int NOME_REPETIDO = 5;
    public static final int NOME_CORRETO = 6;

    public Comunicacao(String nickName) throws RemoteException {
        this.estadoJogador = CONECTADO;
        this.nickName = nickName;
    }

    @Override
    public void conectar() throws RemoteException {
        oponenteConectado = true;
    }

    @Override
    public void desconectar() throws RemoteException {
        oponenteConectado = false;
        Platform.runLater(() -> {
            TelaInicial.enviarMensagemErro("O outro jogador foi desconectado");
            TelaInicial.iniciarTela();
        });
    }

    @Override
    public boolean jogada(int x, int y) throws RemoteException {
        if (campoJogador[x][y].isOcupado()) {
            BatalhaTela.getInstancia().decrementarContagemUsuario();
            BatalhaTela.getInstancia().getCampoUsuarioMatriz()[x][y].setFill(BatalhaTela.COR_ACERTO);

            if (BatalhaTela.getInstancia().getContagemUsuario() == 0) {
                Platform.runLater(() -> {
                    TelaInicial.enviarMensagemInfo("Você perdeu");
                });
            } else {
                estadoJogador = VEZ_DO_JOGADOR;
                Platform.runLater(() -> {
                    TelaInicial.enviarMensagemInfo("Sua vez");
                });
            }

            return true;
        } else {
            BatalhaTela.getInstancia().getCampoUsuarioMatriz()[x][y].setFill(BatalhaTela.COR_ERRO);
            estadoJogador = VEZ_DO_JOGADOR;
            Platform.runLater(() -> {
                TelaInicial.enviarMensagemInfo("Sua vez");
            });

            return false;
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

    public RectangleCoordenado[][] getCampoJogador() {
        return campoJogador;
    }

    public void setCampoJogador(RectangleCoordenado[][] campoJogador) {
        this.campoJogador = campoJogador;
    }

    public boolean isOponenteConectado() {
        return oponenteConectado;
    }

    public String getIP() {
        String ip = "";

        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Platform.runLater(() -> {
                TelaInicial.exibirException(ex);
            });
        }

        return ip;
    }

    @Override
    public void setIpOponente(String ip) throws RemoteException {
        ipOponente = ip;
    }

    public String getIpOponente() {
        return ipOponente;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String getNickName() throws RemoteException {
        return nickName;
    }

    @Override
    public void setNickNameAdversario(String nickNameAdversario) throws RemoteException {
        this.nickNameAdversario = nickNameAdversario;
    }

    public String getNickNameAdversario() {
        return nickNameAdversario;
    }
}
