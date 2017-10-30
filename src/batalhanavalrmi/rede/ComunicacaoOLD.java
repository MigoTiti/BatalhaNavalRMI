package batalhanavalrmi.rede;

import batalhanavalrmi.BatalhaNavalRMIMain;
import batalhanavalrmi.enums.ComandosNet;
import batalhanavalrmi.telas.BatalhaTela;
import batalhanavalrmi.telas.PreparacaoTela;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

public class ComunicacaoOLD {

    public static DatagramSocket socket = null;
    public static boolean conexaoAberta = false;
    public static InetAddress ipAEnviar = null;
    public static int portaAEnviar = BatalhaNavalRMIMain.PORTA_PADRAO;
    public static boolean vezDoUsuario = false;

    public static void persistirConexao() {
        while (conexaoAberta) {
            try {
                byte[] buffer = new byte[500];
                DatagramPacket pacoteResposta = new DatagramPacket(buffer, buffer.length);
                socket.receive(pacoteResposta);
                String respostaString = new String(pacoteResposta.getData()).trim();

                StringTokenizer st = new StringTokenizer(respostaString, "&");
                String comando = st.nextToken();

                if (comando.equals(ComandosNet.DESCONECTAR.comando)) {
                    BatalhaNavalRMIMain.createScene();
                    desconectar();
                } else if (comando.equals(ComandosNet.PRONTO.comando)) {
                    PreparacaoTela.oponentePronto = true;
                } else if (comando.equals(ComandosNet.JOGADA.comando)) {
                    if (BatalhaTela.pronto) {
                        int x = Integer.parseInt(st.nextToken());
                        int y = Integer.parseInt(st.nextToken());

                        if (BatalhaTela.campoUsuarioMatriz[x][y].isOcupado()) {
                            Platform.runLater(() -> {
                                BatalhaTela.campoUsuarioMatriz[x][y].setFill(BatalhaTela.COR_ACERTO);
                                BatalhaTela.contagemUsuario--;

                                if (BatalhaTela.contagemUsuario == 0) {
                                    BatalhaNavalRMIMain.enviarMensagemInfo("TU PERDEU MANO FOTASE");
                                } else {
                                    BatalhaNavalRMIMain.enviarMensagemInfo("Sua vez");
                                }
                            });
                            enviarMensagem(ComandosNet.REPORTAR_JOGADA.comando + "&" + x + "&" + y + "&a");
                        } else {
                            Platform.runLater(() -> {
                                BatalhaTela.campoUsuarioMatriz[x][y].setFill(BatalhaTela.COR_ERRO);
                                BatalhaNavalRMIMain.enviarMensagemInfo("Sua vez");
                            });
                            enviarMensagem(ComandosNet.REPORTAR_JOGADA.comando + "&" + x + "&" + y + "&e");
                        }

                        vezDoUsuario = true;
                    } else {
                        enviarMensagem(ComandosNet.NAO_PRONTO.comando);
                    }
                } else if (comando.equals(ComandosNet.REPORTAR_JOGADA.comando)) {
                    int x = Integer.parseInt(st.nextToken());
                    int y = Integer.parseInt(st.nextToken());

                    if (st.nextToken().equals("a")) {
                        Platform.runLater(() -> {
                            BatalhaTela.campoAdversarioMatriz[x][y].setFill(BatalhaTela.COR_ACERTO);
                            BatalhaTela.contagemAdversario--;

                            if (BatalhaTela.contagemAdversario == 0) {
                                BatalhaNavalRMIMain.enviarMensagemInfo("TU GANHOU ENOS");
                            }
                            //BatalhaNavalMain.enviarMensagemInfo("Sua vez");
                        });
                    } else {
                        Platform.runLater(() -> {
                            BatalhaTela.campoAdversarioMatriz[x][y].setFill(BatalhaTela.COR_ERRO);
                            //BatalhaNavalMain.enviarMensagemInfo("Sua vez");
                        });
                    }
                } else if (comando.equals(ComandosNet.NAO_PRONTO.comando)){
                    Platform.runLater(() -> {
                        BatalhaNavalRMIMain.enviarMensagemErro("O CARA N TÁ PRONTO, MANO");
                    });
                    vezDoUsuario = true;
                } else {
                    System.out.println(comando);
                }
            } catch (IOException ex) {
                Logger.getLogger(ComunicacaoOLD.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void desconectar() {
        socket.close();
        socket = null;
        conexaoAberta = false;
        ipAEnviar = null;
        portaAEnviar = BatalhaNavalRMIMain.PORTA_PADRAO;
    }

    public static void enviarMensagem(String mensagemString) {
        try {
            byte[] mensagem = mensagemString.getBytes();
            DatagramPacket pacoteAEnviar = new DatagramPacket(mensagem, mensagem.length, ComunicacaoOLD.ipAEnviar, ComunicacaoOLD.portaAEnviar);
            socket.send(pacoteAEnviar);
        } catch (IOException ex) {
            Logger.getLogger(ComunicacaoOLD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}