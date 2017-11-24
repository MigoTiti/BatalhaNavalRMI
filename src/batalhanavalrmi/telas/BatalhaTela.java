package batalhanavalrmi.telas;

import batalhanavalrmi.rede.Comunicacao;
import batalhanavalrmi.tabuleiros.TabuleiroPronto;
import batalhanavalrmi.util.RectangleCoordenado;
import batalhanavalrmi.util.RectangleNavio;
import java.rmi.RemoteException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class BatalhaTela extends TabuleiroPronto {
    
    private int contagemUsuario;
    private int contagemAdversario;

    private boolean pronto;

    private int nJogador;

    public static final Color COR_ACERTO = Color.RED;
    public static final Color COR_ERRO = Color.BLUE;
    public static final Color COR_BACKGROUND = Color.GREY;

    private static BatalhaTela instancia;
    
    private BatalhaTela() {
        pronto = false;
        nJogador = 0;
    }
    
    public static BatalhaTela getInstancia() {
        if (instancia == null)
            instancia = new BatalhaTela();
        
        return instancia;
    }
    
    public static void deletarInstancia() {
        instancia = null;
    }
    
    public void iniciarTela(Set<RectangleNavio> naviosUsuario, int contagem) {
        contagemUsuario = contagem;
        contagemAdversario = contagem;

        BorderPane root = new BorderPane();

        VBox vboxUsuario = new VBox();
        VBox vboxAdversario = new VBox();

        vboxUsuario.setSpacing(20);
        vboxAdversario.setSpacing(20);

        vboxUsuario.setAlignment(Pos.CENTER);
        vboxAdversario.setAlignment(Pos.CENTER);

        Text helpText = new Text(TelaInicial.nickName);
        helpText.setFill(Color.BLACK);
        helpText.setFont(Font.font("Arial", FontWeight.NORMAL, 20));

        Text helpText2 = new Text("Adversário");
        helpText2.setFill(Color.BLACK);
        helpText2.setFont(Font.font("Arial", FontWeight.NORMAL, 20));

        campoAdversario = new GridPane();
        campoUsuario = new GridPane();
        campoAdversario.setGridLinesVisible(true);
        campoUsuario.setGridLinesVisible(true);
        campoAdversario.setAlignment(Pos.CENTER);
        campoUsuario.setAlignment(Pos.CENTER);

        for (int i = 0; i < TAMANHO; i++) {
            campoAdversario.getColumnConstraints().add(new ColumnConstraints(TAMANHO_CELULA));
            campoUsuario.getColumnConstraints().add(new ColumnConstraints(TAMANHO_CELULA));

            campoAdversario.getRowConstraints().add(new RowConstraints(TAMANHO_CELULA));
            campoUsuario.getRowConstraints().add(new RowConstraints(TAMANHO_CELULA));
        }

        campoUsuarioMatriz = new RectangleCoordenado[TAMANHO][TAMANHO];
        campoAdversarioMatriz = new RectangleCoordenado[TAMANHO][TAMANHO];

        for (int i = 0; i < TAMANHO; i++) {
            for (int j = 0; j < TAMANHO; j++) {
                RectangleCoordenado rectUsuario = gerarRect(i, j, true);
                campoUsuario.add(rectUsuario, i, j);
                campoUsuarioMatriz[i][j] = rectUsuario;

                RectangleCoordenado rectAdversario = gerarRect(i, j, false);
                campoAdversario.add(rectAdversario, i, j);
                campoAdversarioMatriz[i][j] = rectAdversario;
            }
        }

        StackPane campoUsuarioPronto = new StackPane();
        StackPane campoAdversarioPronto = new StackPane();

        VBox vBoxCampoUsuario = new VBox();
        vBoxCampoUsuario.setAlignment(Pos.CENTER);
        vBoxCampoUsuario.getChildren().addAll(campoUsuario);

        VBox vBoxCampoAdversario = new VBox();
        vBoxCampoAdversario.setAlignment(Pos.CENTER);
        vBoxCampoAdversario.getChildren().addAll(campoAdversario);

        HBox hBoxCampoUsuario = new HBox(vBoxCampoUsuario);
        hBoxCampoUsuario.setAlignment(Pos.CENTER);

        HBox hBoxCampoAdversario = new HBox(vBoxCampoAdversario);
        hBoxCampoAdversario.setAlignment(Pos.CENTER);

        campoUsuarioPronto.getChildren().addAll(hBoxCampoUsuario);

        Platform.runLater(() -> {
            naviosUsuario.stream().forEach((rectangleNavio) -> {
                rectangleNavio.setTranslateX(rectangleNavio.getTranslateX() - ((TAMANHO_CELULA * 6) - 7));
                rectangleNavio.setTranslateY(rectangleNavio.getTranslateY() - (TAMANHO_CELULA * 3));
                campoUsuarioPronto.getChildren().add(rectangleNavio);

                int x = rectangleNavio.getxCoordenada();
                int y = rectangleNavio.getyCoordenada();

                campoUsuarioMatriz[x][y].setOcupado(true);

                Color corAPreencher = COR_BACKGROUND;

                campoUsuarioMatriz[x][y].setFill(corAPreencher);

                switch (rectangleNavio.getTamanho()) {
                    case 5:
                        switch (rectangleNavio.getRotacao()) {
                            case 1:
                                campoUsuarioMatriz[x + 1][y].setOcupado(true);
                                campoUsuarioMatriz[x + 1][y].setFill(corAPreencher);
                                campoUsuarioMatriz[x + 2][y].setOcupado(true);
                                campoUsuarioMatriz[x + 2][y].setFill(corAPreencher);
                                campoUsuarioMatriz[x + 3][y].setOcupado(true);
                                campoUsuarioMatriz[x + 3][y].setFill(corAPreencher);
                                campoUsuarioMatriz[x + 4][y].setOcupado(true);
                                campoUsuarioMatriz[x + 4][y].setFill(corAPreencher);
                                break;
                            case 2:
                                campoUsuarioMatriz[x][y + 1].setOcupado(true);
                                campoUsuarioMatriz[x][y + 1].setFill(corAPreencher);
                                campoUsuarioMatriz[x][y + 2].setOcupado(true);
                                campoUsuarioMatriz[x][y + 2].setFill(corAPreencher);
                                campoUsuarioMatriz[x][y + 3].setOcupado(true);
                                campoUsuarioMatriz[x][y + 3].setFill(corAPreencher);
                                campoUsuarioMatriz[x][y + 4].setOcupado(true);
                                campoUsuarioMatriz[x][y + 4].setFill(corAPreencher);
                                break;
                            case 3:
                                campoUsuarioMatriz[x - 1][y].setOcupado(false);
                                campoUsuarioMatriz[x - 1][y].setFill(corAPreencher);
                                campoUsuarioMatriz[x - 2][y].setOcupado(false);
                                campoUsuarioMatriz[x - 2][y].setFill(corAPreencher);
                                campoUsuarioMatriz[x - 3][y].setOcupado(false);
                                campoUsuarioMatriz[x - 3][y].setFill(corAPreencher);
                                campoUsuarioMatriz[x - 4][y].setOcupado(false);
                                campoUsuarioMatriz[x - 4][y].setFill(corAPreencher);
                                break;
                            case 4:
                                campoUsuarioMatriz[x][y - 1].setOcupado(true);
                                campoUsuarioMatriz[x][y - 1].setFill(corAPreencher);
                                campoUsuarioMatriz[x][y - 2].setOcupado(true);
                                campoUsuarioMatriz[x][y - 2].setFill(corAPreencher);
                                campoUsuarioMatriz[x][y - 3].setOcupado(true);
                                campoUsuarioMatriz[x][y - 3].setFill(corAPreencher);
                                campoUsuarioMatriz[x][y - 4].setOcupado(true);
                                campoUsuarioMatriz[x][y - 4].setFill(corAPreencher);
                                break;
                        }
                        break;
                    case 4:
                        switch (rectangleNavio.getRotacao()) {
                            case 1:
                                campoUsuarioMatriz[x + 1][y].setOcupado(true);
                                campoUsuarioMatriz[x + 1][y].setFill(corAPreencher);
                                campoUsuarioMatriz[x + 2][y].setOcupado(true);
                                campoUsuarioMatriz[x + 2][y].setFill(corAPreencher);
                                campoUsuarioMatriz[x + 3][y].setOcupado(true);
                                campoUsuarioMatriz[x + 3][y].setFill(corAPreencher);
                                break;
                            case 2:
                                campoUsuarioMatriz[x][y + 1].setOcupado(true);
                                campoUsuarioMatriz[x][y + 1].setFill(corAPreencher);
                                campoUsuarioMatriz[x][y + 2].setOcupado(true);
                                campoUsuarioMatriz[x][y + 2].setFill(corAPreencher);
                                campoUsuarioMatriz[x][y + 3].setOcupado(true);
                                campoUsuarioMatriz[x][y + 3].setFill(corAPreencher);
                                break;
                            case 3:
                                campoUsuarioMatriz[x - 1][y].setOcupado(false);
                                campoUsuarioMatriz[x - 1][y].setFill(corAPreencher);
                                campoUsuarioMatriz[x - 2][y].setOcupado(false);
                                campoUsuarioMatriz[x - 2][y].setFill(corAPreencher);
                                campoUsuarioMatriz[x - 3][y].setOcupado(false);
                                campoUsuarioMatriz[x - 3][y].setFill(corAPreencher);
                                break;
                            case 4:
                                campoUsuarioMatriz[x][y - 1].setOcupado(true);
                                campoUsuarioMatriz[x][y - 1].setFill(corAPreencher);
                                campoUsuarioMatriz[x][y - 2].setOcupado(true);
                                campoUsuarioMatriz[x][y - 2].setFill(corAPreencher);
                                campoUsuarioMatriz[x][y - 3].setOcupado(true);
                                campoUsuarioMatriz[x][y - 3].setFill(corAPreencher);
                                break;
                        }
                        break;
                    case 3:
                        switch (rectangleNavio.getRotacao()) {
                            case 1:
                                campoUsuarioMatriz[x + 1][y].setOcupado(true);
                                campoUsuarioMatriz[x + 1][y].setFill(corAPreencher);
                                campoUsuarioMatriz[x + 2][y].setOcupado(true);
                                campoUsuarioMatriz[x + 2][y].setFill(corAPreencher);
                                break;
                            case 2:
                                campoUsuarioMatriz[x][y + 1].setOcupado(true);
                                campoUsuarioMatriz[x][y + 1].setFill(corAPreencher);
                                campoUsuarioMatriz[x][y + 2].setOcupado(true);
                                campoUsuarioMatriz[x][y + 2].setFill(corAPreencher);
                                break;
                            case 3:
                                campoUsuarioMatriz[x - 1][y].setOcupado(false);
                                campoUsuarioMatriz[x - 1][y].setFill(corAPreencher);
                                campoUsuarioMatriz[x - 2][y].setOcupado(false);
                                campoUsuarioMatriz[x - 2][y].setFill(corAPreencher);
                                break;
                            case 4:
                                campoUsuarioMatriz[x][y - 1].setOcupado(true);
                                campoUsuarioMatriz[x][y - 1].setFill(corAPreencher);
                                campoUsuarioMatriz[x][y - 2].setOcupado(true);
                                campoUsuarioMatriz[x][y - 2].setFill(corAPreencher);
                                break;
                        }
                        break;
                    case 2:
                        switch (rectangleNavio.getRotacao()) {
                            case 1:
                                campoUsuarioMatriz[x + 1][y].setOcupado(true);
                                campoUsuarioMatriz[x + 1][y].setFill(corAPreencher);
                                break;
                            case 2:
                                campoUsuarioMatriz[x][y + 1].setOcupado(true);
                                campoUsuarioMatriz[x][y + 1].setFill(corAPreencher);
                                break;
                            case 3:
                                campoUsuarioMatriz[x - 1][y].setOcupado(false);
                                campoUsuarioMatriz[x - 1][y].setFill(corAPreencher);
                                break;
                            case 4:
                                campoUsuarioMatriz[x][y - 1].setOcupado(true);
                                campoUsuarioMatriz[x][y - 1].setFill(corAPreencher);
                                break;
                        }
                        break;
                }
            });
        });

        campoAdversarioPronto.getChildren().addAll(hBoxCampoAdversario);

        vboxUsuario.getChildren().addAll(helpText, campoUsuarioPronto);
        vboxAdversario.getChildren().addAll(helpText2, campoAdversarioPronto);

        HBox hBoxCompleto = new HBox(vboxUsuario, vboxAdversario);
        hBoxCompleto.setSpacing(50);
        hBoxCompleto.setAlignment(Pos.CENTER);
        campoUsuarioPronto.setAlignment(Pos.TOP_LEFT);
        campoAdversarioPronto.setAlignment(Pos.TOP_LEFT);

        root.setCenter(hBoxCompleto);

        Button voltar = new Button("Sair da partida");
        HBox hBoxTop = new HBox(voltar);
        hBoxTop.setPadding(new Insets(20));
        voltar.setOnAction(event -> {
            try {
                campoAdversarioMatriz = null;
                campoUsuarioMatriz = null;
                TelaInicial.comunicacaoUsuario = null;
                TelaInicial.comunicacaoAdversario.desconectar();
                TelaInicial.createScene();
            } catch (RemoteException ex) {
                Logger.getLogger(BatalhaTela.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        root.setTop(hBoxTop);

        TelaInicial.fxContainer.setScene(new Scene(root));
        pronto = true;
    }

    private RectangleCoordenado gerarRect(int x, int y, boolean usuario) {
        RectangleCoordenado rect = new RectangleCoordenado(x, y, TAMANHO_CELULA - 1, TAMANHO_CELULA - 1, COR_BACKGROUND);
        rect.setStroke(Color.YELLOW);
        rect.setStrokeWidth(1);

        if (!usuario) {
            rect.setOnMouseClicked(event -> {
                if (contagemUsuario == 0) {
                    TelaInicial.enviarMensagemErro("TU JÁ PERDEU MANO, TE AQUIETA");
                } else if (contagemAdversario == 0) {
                    TelaInicial.enviarMensagemErro("O CARA JÁ PERDEU MANO, TE AQUIETA");
                } else {
                    try {
                        if (TelaInicial.comunicacaoUsuario.getEstadoJogador() == Comunicacao.VEZ_DO_JOGADOR) {
                            if (!campoAdversarioMatriz[x][y].getFill().equals(COR_ACERTO) && !campoAdversarioMatriz[x][y].getFill().equals(COR_ERRO)) {
                                try {
                                    String resultado = TelaInicial.comunicacaoAdversario.jogada(x + "&" + y);
                                    TelaInicial.comunicacaoUsuario.setEstadoJogador(Comunicacao.PRONTO);
                                    if (resultado.equals("a")) {
                                        contagemAdversario--;
                                        campoAdversarioMatriz[x][y].setFill(COR_ACERTO);
                                    } else {
                                        campoAdversarioMatriz[x][y].setFill(COR_ERRO);
                                    }
                                } catch (RemoteException ex) {
                                    Logger.getLogger(BatalhaTela.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {
                                TelaInicial.enviarMensagemErro("TU JÁ JOGOU AÍ, SEU DOENTE, TU TÁ FICANDO MALUCO?");
                            }
                        } else {
                            TelaInicial.enviarMensagemErro("ESPERA O CARA JOGAR, BICHO");
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(BatalhaTela.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }

        return rect;
    }

    public void setnJogador(int nJogador) {
        this.nJogador = nJogador;
    }

    public void setPronto(boolean pronto) {
        this.pronto = pronto;
    }

    public int getContagemUsuario() {
        return contagemUsuario;
    }

    public int getContagemAdversario() {
        return contagemAdversario;
    }

    public int getnJogador() {
        return nJogador;
    }

    public boolean isPronto() {
        return pronto;
    }
    
    public void decrementarContagemUsuario() {
        contagemUsuario--;
    }
    
    public void decrementarContagemAdveersário() {
        contagemAdversario--;
    }
}
