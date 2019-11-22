package com.vitorcoelho.interfaceGrafica;

import com.vitorcoelho.dimensionamentoTubulao.Analise2DTubulao;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Optional;


public strictfp class Principal extends Application {

    public static final String VERSAO_DO_PROGRAMA = "0.2";

    private static Stage stageCenaInicial;
    private static ControllerCenaInicial controllerCenaInicial;

    private static Stage stageCenaSobre;
    private static ControllerCenaSobre controllerCenaSobre;

    private static Stage stageCenaValoresSugeridos;
    private static ControllerCenaValoresSugeridos controllerCenaValoresSugeridos;

    private static Stage stageCenaParametros;
    private static ControllerCenaParametros controllerCenaParametros;

    private static Stage stageCenaCalculadora;
    private static ControllerCenaCalculadora controllerCenaCalculadora;

    private static Stage stageCenaResultados;
    private static ControllerCenaResultados controllerCenaResultados;

    private static Stage stageCenaResultadosFuste;
    private static ControllerCenaResultadosFuste controllerCenaResultadosFuste;

    private static Analise2DTubulao analiseTubulao;
    private static Resultados resultados;

    @Override
    public void start(Stage stageCenaInicial) throws Exception {
        this.carregarFontes();

        criarCenaInicial();
        criarCenaSobre();
        criarCenaValoresSugeridos();
        criarCenaParametros();
        criarCenaCalculadora();
        criarCenaResultados();
        criarCenaResultadosFuste();

        criarFuncionalidadesAdicionais();

        Principal.stageCenaSobre.showAndWait();
        Principal.stageCenaInicial.show();
    }

    private void carregarFontes() {
        //Carregando fontes
        /*
        Para poder usar fontes no arquivo css, o procedimento é o seguinte:
        1) Carregar a fonte através do método  "javafx.scene.text.Font.loadFont", como no exemplo abaixo (tanto faz o tamanho que colocar para a fonte no método):
            javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("fonts/GloriaHallelujah.ttf"), 15);
        2) Clicar duas vezes na fonte para ver qual é o nome real dela.
        3) No arquivo css, fazer como no exemplo abaixo:
            -fx-font-family: "First Shine";
        4) Para a fonte aparecer normalmente no scene builder, deve estar instalada no sistema operacional
         */

        javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/vitorscoelho/interfacegrafica/fontes/NotoSerif-Regular.ttf"), 15);
        javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/vitorscoelho/interfacegrafica/fontes/NotoSerif-Bold.ttf"), 15);
    }

    //Métodos de criação
    private void criarCenaInicial() throws IOException {
        //Cria stage da tela inicial
        FXMLLoader loaderCenaInicial = new FXMLLoader();
        loaderCenaInicial.setLocation(getClass().getResource("CenaInicial.fxml"));
        Principal.stageCenaInicial = new Stage();
        Scene sceneCenaInicial = new Scene(loaderCenaInicial.load());

        Principal.stageCenaInicial.setScene(sceneCenaInicial);
        Principal.stageCenaInicial.setTitle("ECFX: Versão " + Principal.VERSAO_DO_PROGRAMA + " - Programa auxiliar para dimensionamento de estacas curtas");
        Principal.stageCenaInicial.setResizable(false);
        Principal.stageCenaInicial.centerOnScreen();

        Principal.controllerCenaInicial = loaderCenaInicial.getController();

        //Image icone = new Image(getClass().getResourceAsStream("ImagemTubulao.jpg"));
        //Principal.stageCenaInicial.getIcons().add(icone);
    }

    private void criarCenaSobre() throws IOException {
        //Cria stage da tela de apresentação do software
        FXMLLoader loaderCenaSobre = new FXMLLoader();
        loaderCenaSobre.setLocation(getClass().getResource("CenaSobre.fxml"));
        Principal.stageCenaSobre = new Stage();
        Scene sceneCenaSobre = new Scene(loaderCenaSobre.load());

        Principal.stageCenaSobre.setScene(sceneCenaSobre);
        Principal.stageCenaSobre.setTitle("Sobre");
        Principal.stageCenaSobre.setResizable(false);
        Principal.stageCenaSobre.centerOnScreen();
        Principal.stageCenaSobre.initStyle(StageStyle.UTILITY);//Faz com que a janela não possa ser nem minimizada nem maximizada

        stageCenaSobre.initModality(Modality.WINDOW_MODAL);
        stageCenaSobre.initOwner(stageCenaInicial);

        Principal.controllerCenaSobre = loaderCenaSobre.getController();
    }

    private void criarCenaValoresSugeridos() throws IOException {
        //Cria stage da tela de parâmetros
        FXMLLoader loaderCenaValoresSugeridos = new FXMLLoader();
        loaderCenaValoresSugeridos.setLocation(getClass().getResource("CenaValoresSugeridos.fxml"));
        Principal.stageCenaValoresSugeridos = new Stage();
        Scene sceneCenaValoresSugeridos = new Scene(loaderCenaValoresSugeridos.load());

        Principal.stageCenaValoresSugeridos.setScene(sceneCenaValoresSugeridos);
        Principal.stageCenaValoresSugeridos.setTitle("Valores sugeridos para os parâmetros dos solos");
        Principal.stageCenaValoresSugeridos.setResizable(false);
        Principal.stageCenaValoresSugeridos.centerOnScreen();

        stageCenaValoresSugeridos.initModality(Modality.WINDOW_MODAL);
        stageCenaValoresSugeridos.initOwner(stageCenaInicial);

        Principal.controllerCenaValoresSugeridos = loaderCenaValoresSugeridos.getController();
    }

    private void criarCenaParametros() throws IOException {
        //Cria stage da tela de parâmetros
        FXMLLoader loaderCenaParametros = new FXMLLoader();
        loaderCenaParametros.setLocation(getClass().getResource("CenaParametros.fxml"));
        Principal.stageCenaParametros = new Stage();
        Scene sceneCenaParametros = new Scene(loaderCenaParametros.load());

        Principal.stageCenaParametros.setScene(sceneCenaParametros);
        Principal.stageCenaParametros.setTitle("Parâmetros");
        Principal.stageCenaParametros.setResizable(false);
        Principal.stageCenaParametros.centerOnScreen();

        stageCenaParametros.initModality(Modality.WINDOW_MODAL);
        stageCenaParametros.initOwner(stageCenaInicial);

        Principal.controllerCenaParametros = loaderCenaParametros.getController();
    }

    private void criarCenaCalculadora() throws IOException {
        //Cria stage da tela das calculadora
        FXMLLoader loaderCenaCalculadora = new FXMLLoader();
        loaderCenaCalculadora.setLocation(getClass().getResource("CenaCalculadora.fxml"));
        Principal.stageCenaCalculadora = new Stage();
        Scene sceneCenaCalculadora = new Scene(loaderCenaCalculadora.load());

        Principal.stageCenaCalculadora.setScene(sceneCenaCalculadora);
        Principal.stageCenaCalculadora.setTitle("Calculadoras de dimensionamento estrutural de seções circulares de concreto armado");
        Principal.stageCenaCalculadora.setResizable(false);
        Principal.stageCenaCalculadora.centerOnScreen();

        stageCenaCalculadora.initModality(Modality.WINDOW_MODAL);

        Principal.controllerCenaCalculadora = loaderCenaCalculadora.getController();
    }

    private void criarCenaResultados() throws IOException {
        //Cria stage da tela de resultados
        FXMLLoader loaderCenaResultados = new FXMLLoader();
        loaderCenaResultados.setLocation(getClass().getResource("CenaResultados.fxml"));
        Principal.stageCenaResultados = new Stage();
        Scene sceneCenaResultados = new Scene(loaderCenaResultados.load());

        Principal.stageCenaResultados.setScene(sceneCenaResultados);
        Principal.stageCenaResultados.setTitle("Resultados");
        Principal.stageCenaResultados.setResizable(false);
        Principal.stageCenaResultados.centerOnScreen();

        stageCenaResultados.initModality(Modality.WINDOW_MODAL);
        stageCenaResultados.initOwner(stageCenaInicial);

        Principal.controllerCenaResultados = loaderCenaResultados.getController();
    }

    private void criarCenaResultadosFuste() throws IOException {
        //Cria stage da tela de resultados do fuste
        FXMLLoader loaderCenaResultadosFuste = new FXMLLoader();
        loaderCenaResultadosFuste.setLocation(getClass().getResource("CenaResultadosFuste.fxml"));
        Principal.stageCenaResultadosFuste = new Stage();
        Scene sceneCenaResultadosFuste = new Scene(loaderCenaResultadosFuste.load());

        Principal.stageCenaResultadosFuste.setScene(sceneCenaResultadosFuste);

        Principal.stageCenaResultadosFuste.setTitle("Gráficos do fuste");
        Principal.stageCenaResultadosFuste.setResizable(false);
        Principal.stageCenaResultadosFuste.centerOnScreen();

        stageCenaResultadosFuste.initModality(Modality.WINDOW_MODAL);
        stageCenaResultadosFuste.initOwner(stageCenaResultados);

        Principal.controllerCenaResultadosFuste = loaderCenaResultadosFuste.getController();
    }

    private void criarFuncionalidadesAdicionais() {
        //Adicionando pergunta ao fechar a janela da cena inicial
        Principal.stageCenaInicial.setOnCloseRequest((WindowEvent we) -> {
            we.consume();
            Alert dialogAviso = new Alert(Alert.AlertType.CONFIRMATION);
            dialogAviso.setTitle("Janela de confirmação");
            dialogAviso.setHeaderText("Deseja fechar o programa?");
            dialogAviso.setContentText("");
            dialogAviso.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

            ButtonType buttonTypeSim = new ButtonType("Sim");
            ButtonType buttonTypeNao = new ButtonType("Não");
            dialogAviso.getButtonTypes().setAll(buttonTypeSim, buttonTypeNao);

            Optional<ButtonType> escolha = dialogAviso.showAndWait();
            if (escolha.get() == buttonTypeSim) {
                Principal.stageCenaInicial.close();
                Principal.stageCenaCalculadora.close();
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    //Getters
    public static Stage getStageCenaInicial() {
        return stageCenaInicial;
    }

    public static ControllerCenaInicial getControllerCenaInicial() {
        return controllerCenaInicial;
    }

    public static Stage getStageCenaSobre() {
        return stageCenaSobre;
    }

    public static ControllerCenaSobre getControllerCenaSobre() {
        return controllerCenaSobre;
    }

    public static Stage getStageCenaValoresSugeridos() {
        return stageCenaValoresSugeridos;
    }

    public static ControllerCenaValoresSugeridos getControllerCenaValoresSugeridos() {
        return controllerCenaValoresSugeridos;
    }

    public static Stage getStageCenaParametros() {
        return stageCenaParametros;
    }

    public static ControllerCenaParametros getControllerCenaParametros() {
        return controllerCenaParametros;
    }

    public static Stage getStageCenaCalculadora() {
        return stageCenaCalculadora;
    }

    public static void abrirCalculadora() {
        Principal.controllerCenaCalculadora.setDadosCalculadora();
        Principal.stageCenaCalculadora.show();
    }

    public static ControllerCenaCalculadora getControllerCenaCalculadora() {
        return controllerCenaCalculadora;
    }

    public static Stage getStageCenaResultados() {
        return stageCenaResultados;
    }

    public static ControllerCenaResultados getControllerCenaResultados() {
        return controllerCenaResultados;
    }

    public static Stage getStageCenaResultadosFuste() {
        return stageCenaResultadosFuste;
    }

    public static ControllerCenaResultadosFuste getControllerCenaResultadosFuste() {
        return controllerCenaResultadosFuste;
    }

    public static Analise2DTubulao getAnaliseTubulao() {
        return Principal.analiseTubulao;
    }

    public static Resultados getResultadosAnalise() {
        return Principal.resultados;
    }

    //Setters
    public static void setAnaliseTubulao(Analise2DTubulao analiseTubulao) {
        Principal.analiseTubulao = analiseTubulao;
        Principal.resultados = new Resultados(analiseTubulao);

        Principal.resultados.processarGraficos();
        Principal.controllerCenaResultados.setDadosResumo();
    }

}
