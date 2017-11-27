package batalhanavalrmi.telas;

import batalhanavalrmi.rede.Comunicacao;
import batalhanavalrmi.rede.ComunicacaoRMI;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javafx.application.Platform;
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

    private ComunicacaoRMI comunicadorAdversario;
    
    public void iniciarTela(String nickName) {
        Text texto = new Text("Aguardando oponente");
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
            System.out.println("Servidor");
            iniciarServidor(nickName);
        }).start();
    }

    private void iniciarServidor(String nickName) {
        try {
            int nJogador = 1;
            BatalhaTela.getInstancia().setnJogador(nJogador);
            Comunicacao comunicadorUsuario = new Comunicacao(nickName);
            comunicadorUsuario.setEstadoJogador(Comunicacao.NOME_CORRETO);
            Registry registro = LocateRegistry.getRegistry(TelaInicial.PORTA_PADRAO_SERVIDOR);
            registro.rebind("Comunicador", comunicadorUsuario);

            while (true) {
                Thread.yield();
                if (comunicadorUsuario.isOponenteConectado()) {
                    comunicadorAdversario = (ComunicacaoRMI) Naming.lookup("rmi://" + comunicadorUsuario.getIpOponente() + ":" + TelaInicial.PORTA_PADRAO_CLIENTE + "/Comunicador");
                    comunicadorAdversario.conectar();
                    while (true) {
                        Thread.yield();
                        String nickNameAdversario = comunicadorAdversario.getNickName();

                        if (nickNameAdversario.equals(nickName) && comunicadorAdversario.getEstadoJogador() != Comunicacao.NOME_REPETIDO) {
                            comunicadorAdversario.setEstadoJogador(Comunicacao.NOME_REPETIDO);
                        }

                        if (!nickNameAdversario.equals(nickName)) {
                            comunicadorAdversario.setEstadoJogador(Comunicacao.NOME_CORRETO);

                            comunicadorUsuario.setNickNameAdversario(nickNameAdversario);
                            comunicadorAdversario.setNickNameAdversario(nickName);

                            new PreparacaoTela().iniciarTela(comunicadorUsuario, comunicadorAdversario);
                            return;
                        }
                    }
                }
            }
        } catch (RemoteException | NotBoundException | MalformedURLException ex) {
            Platform.runLater(() -> {
                TelaInicial.exibirException(ex);
                TelaInicial.iniciarTela();
            });
        }
    }
}
