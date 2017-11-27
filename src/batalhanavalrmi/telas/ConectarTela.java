package batalhanavalrmi.telas;

import batalhanavalrmi.rede.Comunicacao;
import batalhanavalrmi.rede.ComunicacaoRMI;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Optional;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ConectarTela {

    private Comunicacao comunicadorUsuario;
    private ComunicacaoRMI comunicadorAdversario;
    
    private boolean estaEmDialogoDeErro = false;

    public void iniciarTela(String ip, String nickName) {
        Text texto = new Text("Tentando conexão com: " + ip);
        texto.setFont(Font.font("Arial", FontWeight.NORMAL, 20));

        ProgressIndicator pi = new ProgressIndicator();

        VBox vBoxCentro = new VBox(texto, pi);
        vBoxCentro.setSpacing(20);
        vBoxCentro.setAlignment(Pos.CENTER);
        BorderPane painel = new BorderPane(vBoxCentro);

        Button voltar = new Button("Voltar");
        voltar.setOnAction(event -> {
            try {
                comunicadorAdversario.desconectar();
            } catch (RemoteException ex) {
                
            }
            
            TelaInicial.iniciarTela();
        });

        HBox hBoxBaixo = new HBox(voltar);
        hBoxBaixo.setPadding(new Insets(15));

        painel.setBottom(hBoxBaixo);

        TelaInicial.fxContainer.setScene(new Scene(painel));

        new Thread(() -> {
            System.out.println("Cliente");
            conectar(ip, nickName);
        }).start();
    }

    private void conectar(String ip, String nickName) {
        try {
            int nJogador = 2;
            BatalhaTela.getInstancia().setnJogador(nJogador);
            comunicadorUsuario = new Comunicacao(nickName);
            Registry registro = LocateRegistry.getRegistry(TelaInicial.PORTA_PADRAO_CLIENTE);
            registro.rebind("Comunicador", comunicadorUsuario);

            comunicadorAdversario = (ComunicacaoRMI) Naming.lookup("rmi://" + ip + ":" + TelaInicial.PORTA_PADRAO_SERVIDOR + "/Comunicador");
            comunicadorAdversario.setIpOponente(comunicadorUsuario.getIP());
            comunicadorAdversario.conectar();

            while (true) {
                Thread.yield();
                if (comunicadorUsuario.getEstadoJogador() == Comunicacao.NOME_REPETIDO && !estaEmDialogoDeErro) {
                    estaEmDialogoDeErro = true;
                    Platform.runLater(() -> {
                        TextInputDialog dialog = new TextInputDialog("");
                        dialog.setTitle("Apelido repetido");
                        dialog.setHeaderText("O apelido escolhido já está sendo usado");
                        dialog.setContentText("Escolha outro apelido: ");
                        Optional<String> result = dialog.showAndWait();
                        if (result.isPresent()) {
                            comunicadorUsuario.setNickName(result.get());
                            estaEmDialogoDeErro = false;
                        }
                    });
                } else if (comunicadorUsuario.getEstadoJogador() == Comunicacao.NOME_CORRETO) {
                    new PreparacaoTela().iniciarTela(comunicadorUsuario, comunicadorAdversario);
                    return;
                }
            }

        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            Platform.runLater(() -> {
                TelaInicial.exibirException(ex);
                TelaInicial.iniciarTela();
            });
        }
    }
}
