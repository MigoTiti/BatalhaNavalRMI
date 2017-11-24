package batalhanavalrmi.telas;

import batalhanavalrmi.rede.Comunicacao;
import batalhanavalrmi.rede.ComunicacaoRMI;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
            TelaInicial.createScene();
        });

        HBox hBoxBaixo = new HBox(voltar);
        hBoxBaixo.setPadding(new Insets(15));

        painel.setBottom(hBoxBaixo);

        TelaInicial.fxContainer.setScene(new Scene(painel));

        new Thread(() -> {
            System.out.println("Servidor");
            iniciarServidor();
        }).start();
    }

    //https://stackoverflow.com/questions/43725556/java-rmi-client-server-chat
    private void iniciarServidor() {
        try {
            BatalhaTela.nJogador = 1;
            TelaInicial.comunicacaoUsuario = new Comunicacao(InetAddress.getLocalHost().getHostAddress(), BatalhaTela.nJogador);
            Registry registro = LocateRegistry.createRegistry(TelaInicial.PORTA_PADRAO_SERVIDOR);
            registro.rebind("Comunicador", TelaInicial.comunicacaoUsuario);
            
            while (true) {
                System.out.println("Jogadores conectados: " + TelaInicial.comunicacaoUsuario.getJogadoresConectados());
                int estado = TelaInicial.comunicacaoUsuario.getJogadoresConectados();
                if (estado == 2) {
                    TelaInicial.comunicacaoAdversario = (ComunicacaoRMI)Naming.lookup("rmi://" + TelaInicial.comunicacaoUsuario.getIpCliente() + ":" + TelaInicial.PORTA_PADRAO_CLIENTE + "/Comunicador");
                    TelaInicial.comunicacaoAdversario.conectar(InetAddress.getLocalHost().getHostAddress(), BatalhaTela.nJogador);
                    
                    new PreparacaoTela().iniciarTela();
                    break;
                }
            }
        } catch (RemoteException | UnknownHostException | NotBoundException | MalformedURLException ex) {
            Logger.getLogger(CriarPartidaTela.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
