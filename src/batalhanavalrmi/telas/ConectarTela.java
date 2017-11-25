package batalhanavalrmi.telas;

import batalhanavalrmi.rede.Comunicacao;
import batalhanavalrmi.rede.ComunicacaoRMI;
import java.net.MalformedURLException;
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

public class ConectarTela {

    public void iniciarTela(String ip, String nickname) {
        Text texto = new Text("Tentando conexão com: " + ip);
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
            System.out.println("Cliente");
            conectar(ip);
        }).start();
    }

    private void conectar(String ip) {
        try {
            int nJogador = 2;
            BatalhaTela.getInstancia().setnJogador(nJogador);
            ComunicacaoRMI comunicadorUsuario = new Comunicacao(nJogador);
            Registry registro = LocateRegistry.createRegistry(TelaInicial.PORTA_PADRAO_CLIENTE);
            registro.rebind("Comunicador", comunicadorUsuario);
            
            ComunicacaoRMI comunicadorAdversario = (ComunicacaoRMI)Naming.lookup("rmi://" + ip + ":" + TelaInicial.PORTA_PADRAO_SERVIDOR + "/Comunicador");
            comunicadorAdversario.setIpOponente(comunicadorUsuario.getIP());
            comunicadorAdversario.conectar();
            
            new PreparacaoTela().iniciarTela();
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            TelaInicial.exibirException(ex);
        }
    }
}
