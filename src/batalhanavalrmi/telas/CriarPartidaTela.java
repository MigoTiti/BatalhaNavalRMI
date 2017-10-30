package batalhanavalrmi.telas;

import batalhanavalrmi.BatalhaNavalRMIMain;
import batalhanavalrmi.enums.ComandosNet;
import batalhanavalrmi.rede.ComunicacaoOLD;
import static batalhanavalrmi.rede.ComunicacaoOLD.socket;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class CriarPartidaTela {

    public void iniciarTela() {
        Text texto = new Text("Aguardando oponente");
        texto.setFont(Font.font("Arial", FontWeight.NORMAL, 20));

        ProgressIndicator pi = new ProgressIndicator();

        VBox vBoxCentro = new VBox(texto, pi);
        vBoxCentro.setSpacing(20);
        vBoxCentro.setAlignment(Pos.CENTER);
        BorderPane painel = new BorderPane(vBoxCentro);

        Button voltar = new Button("Voltar");
        voltar.setOnAction(event -> {
            BatalhaNavalRMIMain.createScene();
            socket = null;
        });

        HBox hBoxBaixo = new HBox(voltar);
        hBoxBaixo.setPadding(new Insets(15));

        painel.setBottom(hBoxBaixo);

        BatalhaNavalRMIMain.fxContainer.setScene(new Scene(painel));

        new Thread(() -> {
            System.out.println("Servidor");
            iniciarServidor();
        }).start();
    }

    //https://stackoverflow.com/questions/43725556/java-rmi-client-server-chat
    private void iniciarServidor() {
        
    }
    
    /*private static void ouvirConexoes() {
        try {
            socket = new DatagramSocket(BatalhaNavalRMIMain.PORTA_PADRAO);
            byte[] mensagemAReceber = new byte[500];
            DatagramPacket pacoteAReceber = new DatagramPacket(mensagemAReceber, mensagemAReceber.length);
            socket.receive(pacoteAReceber);
            String mensagemRecebida = new String(pacoteAReceber.getData());
            
            ComunicacaoOLD.ipAEnviar = pacoteAReceber.getAddress();
            ComunicacaoOLD.portaAEnviar = pacoteAReceber.getPort();
            
            System.out.println(mensagemRecebida);

            DatagramPacket pacoteResposta = new DatagramPacket(ComandosNet.CONECTADO.getBytes(), ComandosNet.CONECTADO.getBytes().length, ComunicacaoOLD.ipAEnviar, ComunicacaoOLD.portaAEnviar);
            socket.send(pacoteResposta);

            new PreparacaoTela().iniciarTela();
            ComunicacaoOLD.vezDoUsuario = true;
            ComunicacaoOLD.conexaoAberta = true;
            ComunicacaoOLD.persistirConexao();
        } catch (IOException ex) {
            Logger.getLogger(ConectarTela.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
}
