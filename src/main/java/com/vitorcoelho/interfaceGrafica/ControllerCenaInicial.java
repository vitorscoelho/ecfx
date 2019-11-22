package com.vitorcoelho.interfaceGrafica;

import com.vitorcoelho.dimensionamentoEstrutural.*;
import com.vitorcoelho.dimensionamentoTubulao.*;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;

import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.StrictMath.min;

public strictfp class ControllerCenaInicial extends ControllerCenaPadrao implements Initializable {

    @FXML
    TextField textFieldFck;
    @FXML
    TextField textFieldGamaC;
    @FXML
    TextField textFieldEci;
    @FXML
    TextField textFieldEcs;
    @FXML
    CheckBox checkBoxEcAutomatico;
    @FXML
    ComboBox comboBoxAgregado;

    @FXML
    TextField textFieldFykLong;
    @FXML
    TextField textFieldEsLong;
    @FXML
    TextField textFieldBitolaLong;
    @FXML
    TextField textFieldGamaSLong;
    @FXML
    TextField textFieldFykTransv;
    @FXML
    TextField textFieldBitolaTransv;
    @FXML
    TextField textFieldGamaSTransv;

    @FXML
    TextField textFieldCobrimento;
    @FXML
    TextField textFieldDiametroFuste;
    @FXML
    TextField textFieldDiametroBase;
    @FXML
    TextField textFieldHbase;
    @FXML
    TextField textFieldRodape;
    @FXML
    TextField textFieldProfundidade;

    @FXML
    TextField textFieldNk;
    @FXML
    TextField textFieldHk;
    @FXML
    TextField textFieldMk;
    @FXML
    TextField textFieldGamaN;

    @FXML
    TextField textFieldEtaOuKh;
    @FXML
    TextField textFieldKv;
    @FXML
    TextField textFieldCoesao;
    @FXML
    TextField textFieldAnguloAtrito;
    @FXML
    TextField textFieldPesoEspecificoSolo;
    @FXML
    TextField textFieldTensaoAdmissivel;

    @FXML
    Label labelEtaOuKh;
    @FXML
    Label labelUnidadeEtaOuKh;
    @FXML
    RadioButton radioButtonArgila;
    @FXML
    RadioButton radioButtonAreia;

    @FXML
    ProgressIndicator progressIndicator;

    @FXML
    ImageView imageViewVista;
    @FXML
    ImageView imageViewCoeficiente;

    @FXML
    Label labelComprimentoArmadura1;
    @FXML
    Label labelComprimentoArmadura2;
    @FXML
    ComboBox comboBoxTipoFundacao;
    @FXML
    Spinner<Double> spinnerComprimentoMinimoArmadura;
    @FXML
    Spinner<Double> spinnerTensaoMaximaSemArmadura;

    @FXML
    Button buttonVisualizarResultados;

    private boolean prosseguirDimensionamento = true;

    private StringConverter<Double> stringConverterSpinner = new StringConverter<Double>() {
        private final DecimalFormat df = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.US));


        @Override
        public String toString(Double value) {
            // If the specified value is null, return a zero-length String
            if (value == null) {
                return "";
            }

            return df.format(value);
        }

        @Override
        public Double fromString(String value) {
            try {
                // If the specified value is null or zero-length, return null
                if (value == null) {
                    return null;
                }

                value = value.trim();

                if (value.length() < 1) {
                    return null;
                }

                // Perform the requested parsing
                return df.parse(value).doubleValue();
            } catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
        }
    };

    private void aplicarTooltips() {
        this.textFieldFck.setTooltip(new Tooltip("Resistência característica à compressão do concreto."));
        this.textFieldGamaC.setTooltip(new Tooltip("Coeficiente de ponderação da resistência do concreto."));
        this.textFieldEci.setTooltip(new Tooltip("Módulo de elasticidade inicial (ou tangente) do concreto."));
        this.textFieldEcs.setTooltip(new Tooltip("Módulo de elasticidade secante do concreto."));
        this.checkBoxEcAutomatico.setTooltip(new Tooltip("Se marcado, os módulos de elasticidade serão calculados\r\nnautomaticamente de acordo com a NBR:6118 (2014)."));
        this.comboBoxAgregado.setTooltip(new Tooltip("Agregado graúdo utilizado no concreto."));

        this.textFieldFykLong.setTooltip(new Tooltip("Resistência característica ao escoamento do aço da armadura longitudinal."));
        this.textFieldEsLong.setTooltip(new Tooltip("Módulo de deformação do aço da armadura longitudinal."));
        this.textFieldBitolaLong.setTooltip(new Tooltip("Bitola da barra utilizada na armadura longitudinal."));
        this.textFieldGamaSLong.setTooltip(new Tooltip("Coeficiente de ponderação da resistência do aço da armadura longitudinal."));
        this.textFieldFykTransv.setTooltip(new Tooltip("Resistência característica ao escoamento do aço da armadura transversal."));
        this.textFieldBitolaTransv.setTooltip(new Tooltip("Bitola da barra utilizada na armadura transversal."));
        this.textFieldGamaSTransv.setTooltip(new Tooltip("Coeficiente de ponderação da resistência do aço da armadura transversal."));

        Tooltip tooltipTextFieldCobrimento = new Tooltip();
        Tooltip tooltipTextFieldDiametroFuste = new Tooltip();
        Tooltip tooltipTextFieldProfundidade = new Tooltip();
        if (((dimensionamentoEstruturalTubulao.TipoEstaca) this.comboBoxTipoFundacao.getSelectionModel().selectedItemProperty().get()).isTubulao()) {
            tooltipTextFieldCobrimento.setText("Cobrimento do estribo do fuste do tubulão.");
            tooltipTextFieldDiametroFuste.setText("Diâmetro do fuste do tubulão.");
            tooltipTextFieldProfundidade.setText("Profundidade total do tubulão.");
        } else {
            tooltipTextFieldCobrimento.setText("Cobrimento do estribo do fuste da estaca.");
            tooltipTextFieldDiametroFuste.setText("Diâmetro do fuste da estaca.");
            tooltipTextFieldProfundidade.setText("Profundidade total da estaca.");
        }
        this.comboBoxTipoFundacao.getSelectionModel().selectedItemProperty().addListener((ObservableValue observable, Object oldValue, Object newValue) -> {
            dimensionamentoEstruturalTubulao.TipoEstaca tipo = (dimensionamentoEstruturalTubulao.TipoEstaca) newValue;
            if (tipo.isTubulao()) {
                tooltipTextFieldCobrimento.setText("Cobrimento do estribo do fuste do tubulão.");
                tooltipTextFieldDiametroFuste.setText("Diâmetro do fuste do tubulão.");
                tooltipTextFieldProfundidade.setText("Profundidade total do tubulão.");
            } else {
                tooltipTextFieldCobrimento.setText("Cobrimento do estribo do fuste da estaca.");
                tooltipTextFieldDiametroFuste.setText("Diâmetro do fuste da estaca.");
                tooltipTextFieldProfundidade.setText("Profundidade total da estaca.");
            }
        });
        this.textFieldCobrimento.setTooltip(tooltipTextFieldCobrimento);
        this.textFieldDiametroFuste.setTooltip(tooltipTextFieldDiametroFuste);
        this.textFieldDiametroBase.setTooltip(new Tooltip("Diâmetro da base do tubulão."));
        this.textFieldHbase.setTooltip(new Tooltip("Altura da base do tubulão."));
        this.textFieldRodape.setTooltip(new Tooltip("Altura do rodapé da base do tubulão."));
        this.textFieldProfundidade.setTooltip(tooltipTextFieldProfundidade);

        this.textFieldNk.setTooltip(new Tooltip("Esforço normal característico atuante no topo da estaca."));
        this.textFieldHk.setTooltip(new Tooltip("Esforço horizontal característico atuante no topo da estaca."));
        this.textFieldMk.setTooltip(new Tooltip("Esforço de momento fletor característico atuante no topo da estaca."));
        this.textFieldGamaN.setTooltip(new Tooltip("Majorador de esforços para ELU. Utilizado para o dimensionamento das armaduras."));

        Tooltip tooltipTextFieldEtaOuKh = new Tooltip();
        if (this.radioButtonAreia.isSelected()) {
            tooltipTextFieldEtaOuKh.setText("Taxa de crescimento do coeficiente de reação horizontal.");
        } else {
            tooltipTextFieldEtaOuKh.setText("Coeficiente de reação horizontal máximo.");
        }
        this.radioButtonAreia.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                tooltipTextFieldEtaOuKh.setText("Taxa de crescimento do coeficiente de reação horizontal.");
            } else {
                tooltipTextFieldEtaOuKh.setText("Coeficiente de reação horizontal máximo.");
            }
        });
        this.textFieldEtaOuKh.setTooltip(tooltipTextFieldEtaOuKh);
        this.textFieldKv.setTooltip(new Tooltip("Coeficiente de reação vertical\r\nna base do elemento de fundação."));
        this.textFieldCoesao.setTooltip(new Tooltip("Coesão do solo."));
        this.textFieldAnguloAtrito.setTooltip(new Tooltip("Ângulo de atrito do solo."));
        this.textFieldPesoEspecificoSolo.setTooltip(new Tooltip("Peso específico do solo."));
        this.textFieldTensaoAdmissivel.setTooltip(new Tooltip("Tensão vertical admissível do solo\r\nna base do elemento de fundação"));

        this.comboBoxTipoFundacao.setTooltip(new Tooltip("Tipo de fundação."));
        this.spinnerComprimentoMinimoArmadura.getEditor().setTooltip(new Tooltip("Profundidade mínima  exigida\r\npara a armadura longitudinal."));
        this.spinnerTensaoMaximaSemArmadura.getEditor().setTooltip(new Tooltip("Tensão média atuante (no fuste) abaixo da qual não é\r\nnecessário armar (exceto até a profundidade mínima informada)."));

        //Listener para fazer a base ficar igual ao fuste quando não é tubulão
        this.textFieldDiametroFuste.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!((dimensionamentoEstruturalTubulao.TipoEstaca) comboBoxTipoFundacao.getSelectionModel().getSelectedItem()).isTubulao()) {
                textFieldDiametroBase.setText(newValue);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Inicializando os Spinners
        SpinnerValueFactory<Double> svf1 = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000, 0, 0.5);
        svf1.setConverter(this.stringConverterSpinner);
        this.spinnerComprimentoMinimoArmadura.setValueFactory(svf1);
        this.spinnerComprimentoMinimoArmadura.getEditor().setStyle("-fx-alignment: CENTER_RIGHT;");

        SpinnerValueFactory<Double> svf2 = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 1000, 0, 0.5);
        svf2.setConverter(this.stringConverterSpinner);
        this.spinnerTensaoMaximaSemArmadura.setValueFactory(svf2);
        this.spinnerTensaoMaximaSemArmadura.getEditor().setStyle("-fx-alignment: CENTER_RIGHT;");

        ajeitarComboBoxAgregadoGraudo();
        ajeitarComboBoxTipoFundacao();

        //Selecionando a checkBox e adicionando listener na CheckBox responsável pelo cálculo automático do Ec
        this.checkBoxEcAutomatico.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                this.textFieldEci.setDisable(true);
                this.textFieldEcs.setDisable(true);
                this.comboBoxAgregado.setDisable(false);
                mudarModuloDeElasticidade(this.textFieldFck, this.comboBoxAgregado);
            } else {
                this.textFieldEci.setDisable(false);
                this.textFieldEcs.setDisable(false);
                this.comboBoxAgregado.setDisable(true);
            }
        });

        //Fazendo as seleções iniciais
        this.comboBoxAgregado.getSelectionModel().select(5);
        this.checkBoxEcAutomatico.setSelected(true);

        //Adicionando listener no RadioButton para mudar a label de EtaH para Kh
        this.radioButtonAreia.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            dimensionamentoEstruturalTubulao.TipoEstaca tipoEstaca = (dimensionamentoEstruturalTubulao.TipoEstaca) this.comboBoxTipoFundacao.getSelectionModel().getSelectedItem();

            if (newValue) {
                //Linearmente crescente
                this.labelEtaOuKh.setText("ηh=");
                this.labelUnidadeEtaOuKh.setText("kN/m³");

                if (tipoEstaca.isTubulao()) {
                    this.imageViewCoeficiente.setImage(new Image(getClass().getResourceAsStream("ImagemTubulaoCrescente.jpg")));
                } else {
                    this.imageViewCoeficiente.setImage(new Image(getClass().getResourceAsStream("ImagemEstacaCrescente.jpg")));
                }

                this.buttonVisualizarResultados.setDisable(false);

            } else {
                //Degrau
                this.labelEtaOuKh.setText("Kh=");
                this.labelUnidadeEtaOuKh.setText("kN/m²");

                if (tipoEstaca.isTubulao()) {
                    this.imageViewCoeficiente.setImage(new Image(getClass().getResourceAsStream("ImagemTubulaoDegrau.jpg")));
                } else {
                    this.imageViewCoeficiente.setImage(new Image(getClass().getResourceAsStream("ImagemEstacaDegrau.jpg")));
                }

                this.buttonVisualizarResultados.setDisable(true);

            }
        });
        this.aplicarTooltips();
    }

    //Eventos de Menu
    @FXML
    private void onActionMenuItemCalculadoras(ActionEvent event) {
        Principal.abrirCalculadora();
    }

    @FXML
    private void onActionMenuItemParametros(ActionEvent event) {
        Principal.getStageCenaParametros().show();
    }

    @FXML
    private void onActionMenuItemSobre(ActionEvent event) {
        Principal.getStageCenaSobre().show();
    }

    @FXML
    private void onActionMenuItemAbrir(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir arquivo");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SER", "*.ser"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        File file = fileChooser.showOpenDialog(Principal.getStageCenaInicial());

        if (file != null) {
            file = new File(file.getPath());
            try {
                FileInputStream fileStream = new FileInputStream(file);
                ObjectInputStream os = new ObjectInputStream(fileStream);
                DadosSalvos dadosSalvos = (DadosSalvos) os.readObject();
                os.close();

                carregarDados(dadosSalvos);

                System.out.println(file.getParentFile());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ControllerCenaInicial.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ControllerCenaInicial.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ControllerCenaInicial.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void onActionMenuItemSalvar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar arquivo");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("SER", "*.ser"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));

        File file = fileChooser.showSaveDialog(Principal.getStageCenaInicial());

        if (file != null) {
            file = new File(file.getPath());
            salvarDados(file);
        }
    }

    @FXML
    private void onActionMenuItemFechar(ActionEvent event) {
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
            Principal.getStageCenaInicial().close();
            Principal.getStageCenaCalculadora().close();
        }
    }

    //Eventos de Button
    private void validacaoDosDadosDeEntrada() {
        //Condições que impedem o prosseguimento do dimensionamento
        double valorMinimo = com.vitorcoelho.auxiliarMath.Utils.minimo(textFieldFck.getText(), textFieldGamaC.getText(), textFieldEci.getText(),
                textFieldEcs.getText(), textFieldFykLong.getText(), textFieldGamaSLong.getText(), textFieldEsLong.getText(),
                textFieldBitolaLong.getText(), textFieldFykTransv.getText(), textFieldGamaSTransv.getText(), textFieldBitolaTransv.getText(),
                textFieldCobrimento.getText(), textFieldDiametroFuste.getText(), textFieldDiametroBase.getText(), textFieldProfundidade.getText(),
                textFieldEtaOuKh.getText(), textFieldKv.getText(), textFieldPesoEspecificoSolo.getText(), textFieldTensaoAdmissivel.getText());

        if (valorMinimo < 0.00001) {
            prosseguirDimensionamento = false;
            String tituloErro = "Impossível dimensionar!";
            String textoPrincipalErro = "Dados incompatíveis.";
            String descricaoErro = "Existem valores zerados que impedem o prosseguimento do dimensionamento.\r\n"
                    + "Altere os dados e tente novamente.";

            Alert dialogErro = new Alert(AlertType.ERROR);
            dialogErro.setTitle(tituloErro);
            dialogErro.setHeaderText(textoPrincipalErro);
            dialogErro.setContentText(descricaoErro);
            dialogErro.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            dialogErro.showAndWait();
        } else {
            //Avisos que não impedem o prosseguimento do dimensionamento, mas o torna desaconselhável
            String descricaoDoAviso = "";
            boolean comAviso = false;

            if (Double.parseDouble(textFieldBitolaTransv.getText()) < Double.parseDouble(textFieldBitolaLong.getText()) / 4) {
                comAviso = true;
                descricaoDoAviso = descricaoDoAviso + "*A bitola do estribo não é maior do que um quarto da bitola da armadura longitudinal, que é o que recomenda a NBR6118:2014.\r\n\r\n";
            }

            if (comAviso) {
                descricaoDoAviso = descricaoDoAviso + "Deseja prosseguir mesmo assim?";
                Alert dialogAviso = new Alert(AlertType.WARNING);
                dialogAviso.setTitle("Aviso!");
                dialogAviso.setHeaderText("Aviso importante!");
                dialogAviso.setContentText(descricaoDoAviso);
                dialogAviso.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

                ButtonType buttonTypeSim = new ButtonType("Sim");
                ButtonType buttonTypeNao = new ButtonType("Não", ButtonBar.ButtonData.CANCEL_CLOSE);
                dialogAviso.getButtonTypes().setAll(buttonTypeSim, buttonTypeNao);

                Optional<ButtonType> escolha = dialogAviso.showAndWait();
                if (escolha.get() == buttonTypeNao) {
                    prosseguirDimensionamento = false;
                }
            }
        }
    }

    private void verificacoesAntesDoProcessamentoDosGraficos(Analise2DTubulao analise2D) {
        ////Verificando se ocorreu tudo bem no dimensionamento do tubulão////
        double comprimentoTubulao = analise2D.getTubulao().getComprimento();
        double comprimentoMaximoCurto = analise2D.limiteComprimentoTubulaoCurto();

        double tensaoMinimaBase = analise2D.getTensaoMinimaBase();
        double tensaoMaximaBase = analise2D.getTensaoMaximaBase();
        double majoradorFlexoCompressao = Principal.getControllerCenaParametros().getMajoradorFlexoCompressao();
        double tensaoAdmissivelBase = analise2D.getTensaoVAdmissivel();

        String tituloErro = null;
        String textoPrincipalErro = null;
        String descricaoErro = null;

        if (analise2D.getTubulao().getSecaoFuste().getDimensaoX() > analise2D.getTubulao().getSecaoBase().getDimensaoX()) {
            //Verificando se o diâmetro do fuste é maior do que o diâmetro da base
            prosseguirDimensionamento = false;
            tituloErro = "Impossível dimensionar!";
            textoPrincipalErro = "Dimensões incompatíveis.";
            descricaoErro = "O diâmetro da base é menor do que o diâmetro do fuste.\r\n"
                    + "Altere os dados e tente novamente.";
        } else if ((analise2D.getTubulao().getSecaoFuste().getSecaoConcretoArmado().getConcreto().getFck() > 9.0) || (analise2D.getTubulao().getSecaoFuste().getSecaoConcretoArmado().getConcreto().getFck() < 0.1)) {
            prosseguirDimensionamento = false;
            tituloErro = "Impossível dimensionar!";
            textoPrincipalErro = "fck incompatível.";
            descricaoErro = "O fck deve estar entre 1MPa e 90MPa.\r\n"
                    + "Altere os dados e tente novamente.";
        } else if (!analise2D.isCurto()) {
            //Verificando se o tubulão é curto. Se não for curto, o método não é válido
            prosseguirDimensionamento = false;
            tituloErro = "Impossível dimensionar!";
            textoPrincipalErro = "O tubulão não é curto!\r\nO método adotado não pode ser utilizado!";
            descricaoErro = "O tubulão possui " + arredondarToString(comprimentoTubulao / 100, 2) + "m de comprimento.\r\n"
                    + "Para ser considerado como curto, o comprimento deveria ser de, no máximo " + arredondarToString(comprimentoMaximoCurto / 100, 2) + "m"
                    + ", considerando a seção transversal e o material adotado.\r\n"
                    + "É impossível o dimensionamento através do método utilizado por este sofware.";
        } else if (tensaoMinimaBase < 0.0) {
            //Verificando se ocorre tração na base
            prosseguirDimensionamento = false;
            tituloErro = "Impossível dimensionar!";
            textoPrincipalErro = "Tração na base!";
            descricaoErro = "Ocorrem esforços de tração (" + arredondarToString(min(tensaoMinimaBase * 10000, -0.0001), 4) + "kPa) na base do tubulão.\r\n"
                    + "O método utilizado por este software é válido somente nos casos em que o solo na base do tubulão está completamente comprimido.";
        } else if (((tensaoMaximaBase + tensaoMinimaBase) / 2) > tensaoAdmissivelBase) {
            //Verificando se a tensão na base excede a tensão admissível
            prosseguirDimensionamento = false;
            tituloErro = "Impossível dimensionar!";
            textoPrincipalErro = "Tensão na base excede o limite!";
            descricaoErro = "A tensão atuante média na base, " + arredondarToString(((tensaoMaximaBase + tensaoMinimaBase) / 2) * 10000, 2) + "kPa, ultrapassa a tensão admissível, " + arredondarToString(tensaoAdmissivelBase * 10000, 2) + "kPa.\r\n"
                    + "Altere os dados e tente novamente.";
        } else if (tensaoMaximaBase > tensaoAdmissivelBase * majoradorFlexoCompressao) {
            //Verificando se a tensão na base excede a tensão admissível
            prosseguirDimensionamento = false;
            tituloErro = "Impossível dimensionar!";
            textoPrincipalErro = "Tensão na base excede o limite!";
            descricaoErro = "A tensão atuante máxima na base, " + arredondarToString(tensaoMaximaBase * 10000, 2) + "kPa, ultrapassa a tensão admissível, " + arredondarToString(tensaoAdmissivelBase * majoradorFlexoCompressao * 10000, 2) + "kPa.\r\n"
                    + "Altere os dados e tente novamente.";
        }

        if (!prosseguirDimensionamento) {
            Alert dialogErro = new Alert(AlertType.ERROR);
            dialogErro.setTitle(tituloErro);
            dialogErro.setHeaderText(textoPrincipalErro);
            dialogErro.setContentText(descricaoErro);

            dialogErro.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

            dialogErro.showAndWait();
        }
    }

    private void verificacoesDepoisDoProcessamentoDosGraficos(Analise2DTubulao analise2D, Resultados resultados) {
        //////////Verificações que não precisam dos gráficos//////////
        double coeficienteDeSegurancaMinimo = resultados.getGraficoCoeficienteSegurancaEstabilidade().getValorMaximo();
        double profCoeficienteDeSegurancaMinimo = resultados.getGraficoCoeficienteSegurancaEstabilidade().getProfundidadeValorMaximo() / 100.0;
        double coeficienteDeSegurancaAdmissivel = Principal.getControllerCenaParametros().getCoeficienteDeSeguranca();

        double gamaN = analise2D.getSolicitacaoTopo().getNd() / analise2D.getSolicitacaoTopo().getNk();
        double vSdMaximo = resultados.getGraficoCortante().getValorMaximo() * gamaN;
        double vRd2 = Cisalhamento.vrd2(analise2D.getTubulao().getSecaoFuste().getSecaoConcretoArmado());

        double asAdotado = resultados.getGraficoNBarrasLongitudinais().getValorMaximo() * analise2D.getTubulao().getSecaoFuste().getSecaoConcretoArmado().getBarraLongitudinal().getArea();
        double asMaximo = FlexoCompressao.asMax(analise2D.getTubulao().getSecaoFuste().getSecaoConcretoArmado());
        double porcentagemAsMaximo = 100 * asMaximo / analise2D.getTubulao().getSecaoFuste().getSecaoConcretoArmado().getArea();

        String tituloErro = null;
        String textoPrincipalErro = null;
        String descricaoErro = null;

        if (coeficienteDeSegurancaMinimo < coeficienteDeSegurancaAdmissivel) {
            //Verificando a estabilidade do solo no fuste
            prosseguirDimensionamento = false;
            tituloErro = "Impossível dimensionar!";
            textoPrincipalErro = "Tensão no fuste excede o limite!\r\nO coeficiente de segurança não foi atendido.";
            descricaoErro = "Na profundidade de " + arredondarToString(profCoeficienteDeSegurancaMinimo, 2) + "m, a razão entre a tensão lateral admissível e a tensão lateral atuante é igual a " + arredondarToString(coeficienteDeSegurancaMinimo, 2) + ".\r\n"
                    + "Portanto, não atende ao coeficiente de segurança adotado nos parâmetros, que é igual a " + arredondarToString(coeficienteDeSegurancaAdmissivel, 2) + ".\r\n"
                    + "Assim, não fica assegurada a estabilidade do solo na face lateral do fuste.\r\n"
                    + "Altere os dados e tente novamente.";
        } else if (vSdMaximo > vRd2) {
            //Verificando se houve ruptura da biela
            prosseguirDimensionamento = false;
            tituloErro = "Impossível dimensionar!";
            textoPrincipalErro = "Ruptura na biela!";
            descricaoErro = "Vsd > Vrd2\r\n"
                    + "Vsd,máx= " + arredondarToString(vSdMaximo, 2) + "kN\r\n"
                    + "Vrd2= " + arredondarToString(vRd2, 2) + "kN\r\n"
                    + "Altere os dados e tente novamente.";
        } else if (asAdotado > asMaximo) {
            //Verificando se a armadura longitudinal ultrapassa a armadura máxima permitida
            prosseguirDimensionamento = false;
            tituloErro = "Impossível dimensionar!";
            textoPrincipalErro = "Armadura longitudinal necessária é maior do que a permitida.";
            descricaoErro = "A armadura longitudinal necessária, " + arredondarToString(asAdotado, 2) + "cm², é maior do que a armadura máxima permitida, " + arredondarToString(asMaximo, 2) + "cm².\r\n"
                    + "A armadura longitudinal máxima equivale a " + arredondarToString(porcentagemAsMaximo, 2) + "% da área da seção transversal bruta de concreto.";
        }

        if (!prosseguirDimensionamento) {
            Alert dialogErro = new Alert(AlertType.ERROR);
            dialogErro.setTitle(tituloErro);
            dialogErro.setHeaderText(textoPrincipalErro);
            dialogErro.setContentText(descricaoErro);
            dialogErro.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            dialogErro.showAndWait();
        }
    }

    private void processarDimensionamento() {
        dimensionamentoEstruturalTubulao.TipoEstaca tipoEstaca = (dimensionamentoEstruturalTubulao.TipoEstaca) this.comboBoxTipoFundacao.getSelectionModel().getSelectedItem();
        dimensionamentoEstruturalTubulao.tensaoMediaMaximaConcretoSimples = tipoEstaca.getTensaoMediaLimiteSemArmadura(this.spinnerTensaoMaximaSemArmadura);
        dimensionamentoEstruturalTubulao.comprimentoMinimoArmadura = tipoEstaca.getComprimentoMinimoArmadura(this.spinnerComprimentoMinimoArmadura);

        this.prosseguirDimensionamento = true;
        validacaoDosDadosDeEntrada();

        if (prosseguirDimensionamento) {
            Analise2DTubulao analise2D = gerarAnalise2DTubulao();
            verificacoesAntesDoProcessamentoDosGraficos(analise2D);

            if (prosseguirDimensionamento) {

                progressIndicator.progressProperty().setValue(-1);
                progressIndicator.setVisible(true);

                Task taskProcessamento = new Task() {
                    @Override
                    protected Object call() {
                        Principal.setAnaliseTubulao(analise2D);
                        return null;
                    }

                    @Override
                    protected void succeeded() {
                        progressIndicator.setVisible(false);
                        progressIndicator.progressProperty().setValue(0);

                        verificacoesDepoisDoProcessamentoDosGraficos(analise2D, Principal.getResultadosAnalise());

                        if (prosseguirDimensionamento) {
                            Principal.getControllerCenaResultadosFuste().resetarGraficosIniciais();
                            Principal.getStageCenaResultados().show();
                        }
                    }
                };

                Thread t = new Thread(taskProcessamento);
                t.setDaemon(true);
                t.start();
            }
        }
    }

    @FXML
    private void onActionButtonResultados(ActionEvent event) {
        processarDimensionamento();
    }

    @FXML
    private void onActionButtonValoresSugeridos(ActionEvent event) {
        Principal.getStageCenaValoresSugeridos().show();
    }

    //Eventos de ComboBox
    @FXML
    private void onActionComboBoxAgregado(ActionEvent event) {
        mudarModuloDeElasticidade(this.textFieldFck, this.comboBoxAgregado);
    }

    //Eventyos de TextField
    @FXML
    private void onKeyReleasedAtualizaEc(KeyEvent event) {
        if (this.checkBoxEcAutomatico.isSelected()) {
            mudarModuloDeElasticidade(this.textFieldFck, this.comboBoxAgregado);
        }
    }

    //Métodos de alterações
    private void mudarModuloDeElasticidade(TextField fck, ComboBox agregadoGraudo) {
        double fckD = Double.parseDouble(fck.getText()) / 10;
        Concreto.AgregadoGraudo agregado = (Concreto.AgregadoGraudo) agregadoGraudo.getSelectionModel().getSelectedItem();

        double eCi = Concreto.moduloDeDeformacaoInicial(fckD, agregado);
        double eCs = Concreto.moduloDeDeformacaoSecante(fckD, eCi);

        this.textFieldEci.setText(arredondarToString(eCi * 10, 2));
        this.textFieldEcs.setText(arredondarToString(eCs * 10, 2));
    }

    private void alterarJanelaPeloTipoDeFundacao(dimensionamentoEstruturalTubulao.TipoEstaca tipoDeFundacao) {
        this.textFieldFck.setText(arredondarToString(tipoDeFundacao.getFck() * 10, 1));
        this.textFieldGamaC.setText(arredondarToString(tipoDeFundacao.getGamaC(), 2));
        this.textFieldGamaSLong.setText(arredondarToString(tipoDeFundacao.getGamaS(), 3));
        this.textFieldGamaSTransv.setText(arredondarToString(tipoDeFundacao.getGamaS(), 3));

        this.textFieldFck.setDisable(!tipoDeFundacao.isPersonalizada());
        this.textFieldGamaC.setDisable(!tipoDeFundacao.isPersonalizada());
        this.textFieldGamaSLong.setDisable(!tipoDeFundacao.isPersonalizada());
        this.textFieldGamaSTransv.setDisable(!tipoDeFundacao.isPersonalizada());

        this.textFieldHbase.setDisable(!tipoDeFundacao.isTubulao());
        this.textFieldRodape.setDisable(!tipoDeFundacao.isTubulao());
        this.textFieldDiametroBase.setDisable(!tipoDeFundacao.isTubulao());

        this.spinnerComprimentoMinimoArmadura.setDisable(!tipoDeFundacao.isPersonalizada());
        this.spinnerTensaoMaximaSemArmadura.setDisable(!tipoDeFundacao.isPersonalizada());

        if (tipoDeFundacao.getComprimentoMinimoArmadura(this.spinnerComprimentoMinimoArmadura) > 0.0) {
            this.labelComprimentoArmadura1.setText("Compr. mín. armadura=");

            this.spinnerComprimentoMinimoArmadura.setVisible(true);
            this.labelComprimentoArmadura2.setVisible(true);

            this.spinnerComprimentoMinimoArmadura.getValueFactory().setValue(tipoDeFundacao.getComprimentoMinimoArmadura(this.spinnerComprimentoMinimoArmadura) / 100);
        } else {
            this.labelComprimentoArmadura1.setText("Armadura integral.");

            this.spinnerComprimentoMinimoArmadura.setVisible(false);
            this.labelComprimentoArmadura2.setVisible(false);
        }

        this.spinnerTensaoMaximaSemArmadura.getValueFactory().setValue(tipoDeFundacao.getTensaoMediaLimiteSemArmadura(this.spinnerTensaoMaximaSemArmadura) * 10);

        if (tipoDeFundacao.isTubulao()) {

            this.imageViewVista.setImage(new Image(getClass().getResourceAsStream("ImagemTubulao.jpg")));

            if (this.radioButtonAreia.isSelected()) {
                this.imageViewCoeficiente.setImage(new Image(getClass().getResourceAsStream("ImagemTubulaoCrescente.jpg")));
            } else {
                this.imageViewCoeficiente.setImage(new Image(getClass().getResourceAsStream("ImagemTubulaoDegrau.jpg")));
            }

        } else {
            this.textFieldHbase.setText(arredondarToString(0.0, 0));
            this.textFieldRodape.setText(arredondarToString(0.0, 0));
            this.textFieldDiametroBase.setText(this.textFieldDiametroFuste.getText());

            this.imageViewVista.setImage(new Image(getClass().getResourceAsStream("ImagemEstaca.jpg")));

            if (this.radioButtonAreia.isSelected()) {
                this.imageViewCoeficiente.setImage(new Image(getClass().getResourceAsStream("ImagemEstacaCrescente.jpg")));
            } else {
                this.imageViewCoeficiente.setImage(new Image(getClass().getResourceAsStream("ImagemEstacaDegrau.jpg")));
            }
        }
    }

    //Privados
    private Analise2DTubulao gerarAnalise2DTubulao() {
        //Criação do objeto Concreto
        double fck = Double.parseDouble(this.textFieldFck.getText()) / 10;
        double gamaC = Double.parseDouble(this.textFieldGamaC.getText());
        double moduloDeDeformacaoInicial = Double.parseDouble(this.textFieldEci.getText()) / 10;
        double moduloDeDeformacaoSecante = Double.parseDouble(this.textFieldEcs.getText()) / 10;
        Concreto.AgregadoGraudo agregadoGraudo = (Concreto.AgregadoGraudo) this.comboBoxAgregado.getSelectionModel().getSelectedItem();

        Concreto concreto = new Concreto(fck, gamaC, moduloDeDeformacaoInicial, moduloDeDeformacaoSecante, agregadoGraudo);

        //Criação do objetos Aco e Barra para armadura longitudinal
        double fyk = Double.parseDouble(this.textFieldFykLong.getText()) / 10;
        double esLong = Double.parseDouble(this.textFieldEsLong.getText()) / 10;
        double bitolaLong = Double.parseDouble(this.textFieldBitolaLong.getText()) / 10;
        double gamaSLong = Double.parseDouble(this.textFieldGamaSLong.getText());

        Aco acoLong = new Aco(fyk, gamaSLong, esLong);
        BarraAco barraLong = new BarraAco(bitolaLong, acoLong);

        //Criação do objetos Aco e Barra para armadura transversal
        double fywk = Double.parseDouble(this.textFieldFykTransv.getText()) / 10;
        double bitolaTransv = Double.parseDouble(this.textFieldBitolaTransv.getText()) / 10;
        double gamaSTrans = Double.parseDouble(this.textFieldGamaSTransv.getText());

        Aco acoTransv = new Aco(fywk, gamaSTrans);
        BarraAco barraTransv = new BarraAco(bitolaTransv, acoTransv);

        //Criação do objeto Tubulao
        double cobrimento = Double.parseDouble(this.textFieldCobrimento.getText());
        double diametroFuste = Double.parseDouble(this.textFieldDiametroFuste.getText());
        double diametroBase = Double.parseDouble(this.textFieldDiametroBase.getText());
        double hBase = Double.parseDouble(this.textFieldHbase.getText());
        double rodape = Double.parseDouble(this.textFieldRodape.getText());
        double comprimento = Double.parseDouble(this.textFieldProfundidade.getText()) * 100;

        Secao secaoConcretoArmado = new Secao(diametroFuste, concreto, cobrimento, barraTransv, barraLong);
        SecaoFuste secaoFuste = new SecaoFuste(secaoConcretoArmado, diametroFuste, diametroFuste);
        SecaoBase secaoBase = new SecaoBaseCircular(diametroBase, hBase, rodape, diametroFuste);
        Tubulao tubulao = new Tubulao(secaoFuste, secaoBase, comprimento);

        //Criação do objeto Analise2DTubulao
        double normal = Double.parseDouble(this.textFieldNk.getText());
        double forcaHorizontal = Double.parseDouble(this.textFieldHk.getText());
        double momento = Double.parseDouble(this.textFieldMk.getText()) * 100;
        double gamaN = Double.parseDouble(this.textFieldGamaN.getText());
        double nhOuKh = Double.parseDouble(this.textFieldEtaOuKh.getText()) / 1000000;
        double kv = Double.parseDouble(this.textFieldKv.getText()) / 1000000;
        double coesao = Double.parseDouble(this.textFieldCoesao.getText()) / 10000;
        double anguloDeAtrito = Double.parseDouble(this.textFieldAnguloAtrito.getText());
        double pesoEspecificoSolo = Double.parseDouble(this.textFieldPesoEspecificoSolo.getText()) / 1000000;
        double tensaoVAdmissivel = Double.parseDouble(this.textFieldTensaoAdmissivel.getText()) / 10000;

        Analise2DTubulao analiseTubulao;
        if (this.radioButtonAreia.isSelected()) {
            analiseTubulao = new Analise2DTubulaoKhLinearmenteCrescente(tubulao, normal, forcaHorizontal, momento, gamaN, nhOuKh, kv, coesao, anguloDeAtrito, pesoEspecificoSolo, tensaoVAdmissivel);
        } else {
            analiseTubulao = new Analise2DTubulaoKhDegrau(tubulao, normal, forcaHorizontal, momento, gamaN, nhOuKh, kv, coesao, anguloDeAtrito, pesoEspecificoSolo, tensaoVAdmissivel);
        }

        return analiseTubulao;
    }

    //Métodos para salvamento e abertura de arquivos
    private void salvarDados(File file) {
        DadosSalvos dadosSalvos = new DadosSalvos();

        //Concreto
        dadosSalvos.fck = this.textFieldFck.getText();
        dadosSalvos.gamaC = this.textFieldGamaC.getText();
        dadosSalvos.eci = this.textFieldEci.getText();
        dadosSalvos.ecs = this.textFieldEcs.getText();
        dadosSalvos.selecionadoComboBoxAgregado = this.comboBoxAgregado.getSelectionModel().getSelectedIndex();
        dadosSalvos.isSelecionadoCheckBoxEcAutomatico = this.checkBoxEcAutomatico.isSelected();

        //Armadura longitudinal
        dadosSalvos.fyk = this.textFieldFykLong.getText();
        dadosSalvos.gamaSLongitudinal = this.textFieldGamaSLong.getText();
        dadosSalvos.esLongitudinal = this.textFieldEsLong.getText();
        dadosSalvos.bitolaLongitudinal = this.textFieldBitolaLong.getText();

        //Armadura transversal
        dadosSalvos.fywk = this.textFieldFykTransv.getText();
        dadosSalvos.gamaSTransversal = this.textFieldGamaSTransv.getText();
        dadosSalvos.bitolaTransversal = this.textFieldBitolaTransv.getText();

        //Características Geométricas
        dadosSalvos.cobrimento = this.textFieldCobrimento.getText();
        dadosSalvos.diametroFuste = this.textFieldDiametroFuste.getText();
        dadosSalvos.diametroBase = this.textFieldDiametroBase.getText();
        dadosSalvos.hBase = this.textFieldHbase.getText();
        dadosSalvos.rodape = this.textFieldRodape.getText();
        dadosSalvos.profundidade = this.textFieldProfundidade.getText();

        //Cargas no topo
        dadosSalvos.nk = this.textFieldNk.getText();
        dadosSalvos.hk = this.textFieldHk.getText();
        dadosSalvos.mk = this.textFieldMk.getText();
        dadosSalvos.gamaN = this.textFieldGamaN.getText();

        //Características do elemento de fundação
        dadosSalvos.selecionadoComboBoxTipoFundacao = this.comboBoxTipoFundacao.getSelectionModel().getSelectedIndex();
        dadosSalvos.comprimentoMinimoArmadura = this.spinnerComprimentoMinimoArmadura.getValueFactory().getValue();
        dadosSalvos.tensaoMediaConcretoSimples = this.spinnerTensaoMaximaSemArmadura.getValueFactory().getValue();

        //Tipo do solo
        dadosSalvos.isAreiaSelecionado = this.radioButtonAreia.isSelected();

        //Características do solo
        dadosSalvos.etaOuKh = this.textFieldEtaOuKh.getText();
        dadosSalvos.kv = this.textFieldKv.getText();
        dadosSalvos.coesao = this.textFieldCoesao.getText();
        dadosSalvos.anguloDeAtrito = this.textFieldAnguloAtrito.getText();
        dadosSalvos.pesoEspecifico = this.textFieldPesoEspecificoSolo.getText();
        dadosSalvos.tensaoAdmissivel = this.textFieldTensaoAdmissivel.getText();

        //Parâmetros
        dadosSalvos.coeficienteDeSegurancaAtual = Principal.getControllerCenaParametros().coeficienteDeSegurancaAtual;
        dadosSalvos.coeficienteDeSegurancaMaximoAtual = Principal.getControllerCenaParametros().coeficienteDeSegurancaMaximoAtual;
        dadosSalvos.majoradorFlexoCompressaoAtual = Principal.getControllerCenaParametros().majoradorFlexoCompressaoAtual;
        dadosSalvos.taxaMinimaLongitudinalAtual = Principal.getControllerCenaParametros().taxaMinimaLongitudinalAtual;
        dadosSalvos.taxaMaximaLongitudinalAtual = Principal.getControllerCenaParametros().taxaMaximaLongitudinalAtual;

        //Gravando o arquivo
        try {
            FileOutputStream fileStream = new FileOutputStream(file);
            ObjectOutputStream os = new ObjectOutputStream(fileStream);
            os.writeObject(dadosSalvos);
            os.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ControllerCenaInicial.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ControllerCenaInicial.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void carregarDados(DadosSalvos dadosSalvos) {
        this.textFieldFck.requestFocus(); //Colocando o cursor neste TextField

        //Concreto
        this.textFieldFck.setText(dadosSalvos.fck);
        this.textFieldGamaC.setText(dadosSalvos.gamaC);
        this.textFieldEci.setText(dadosSalvos.eci);
        this.textFieldEcs.setText(dadosSalvos.ecs);
        this.comboBoxAgregado.getSelectionModel().select(dadosSalvos.selecionadoComboBoxAgregado);
        this.checkBoxEcAutomatico.setSelected(dadosSalvos.isSelecionadoCheckBoxEcAutomatico);

        //Armadura longitudinal
        this.textFieldFykLong.setText(dadosSalvos.fyk);
        this.textFieldGamaSLong.setText(dadosSalvos.gamaSLongitudinal);
        this.textFieldEsLong.setText(dadosSalvos.esLongitudinal);
        this.textFieldBitolaLong.setText(dadosSalvos.bitolaLongitudinal);

        //Armadura transversal
        this.textFieldFykTransv.setText(dadosSalvos.fywk);
        this.textFieldGamaSTransv.setText(dadosSalvos.gamaSTransversal);
        this.textFieldBitolaTransv.setText(dadosSalvos.bitolaTransversal);

        //Características geométricas
        this.textFieldCobrimento.setText(dadosSalvos.cobrimento);
        this.textFieldDiametroFuste.setText(dadosSalvos.diametroFuste);
        this.textFieldDiametroBase.setText(dadosSalvos.diametroBase);
        this.textFieldHbase.setText(dadosSalvos.hBase);
        this.textFieldRodape.setText(dadosSalvos.rodape);
        this.textFieldProfundidade.setText(dadosSalvos.profundidade);

        //Cargas no topo
        this.textFieldNk.setText(dadosSalvos.nk);
        this.textFieldHk.setText(dadosSalvos.hk);
        this.textFieldMk.setText(dadosSalvos.mk);
        this.textFieldGamaN.setText(dadosSalvos.gamaN);

        //Características do elemento de fundação
        this.comboBoxTipoFundacao.getSelectionModel().select(dadosSalvos.selecionadoComboBoxTipoFundacao);
        this.spinnerComprimentoMinimoArmadura.getValueFactory().setValue(dadosSalvos.comprimentoMinimoArmadura);
        this.spinnerTensaoMaximaSemArmadura.getValueFactory().setValue(dadosSalvos.tensaoMediaConcretoSimples);

        //Tipo do solo
        if (dadosSalvos.isAreiaSelecionado) {
            this.radioButtonAreia.selectedProperty().set(true);
        } else {
            this.radioButtonArgila.selectedProperty().set(true);
        }

        //Características do solo
        this.textFieldEtaOuKh.setText(dadosSalvos.etaOuKh);
        this.textFieldKv.setText(dadosSalvos.kv);
        this.textFieldCoesao.setText(dadosSalvos.coesao);
        this.textFieldAnguloAtrito.setText(dadosSalvos.anguloDeAtrito);
        this.textFieldPesoEspecificoSolo.setText(dadosSalvos.pesoEspecifico);
        this.textFieldTensaoAdmissivel.setText(dadosSalvos.tensaoAdmissivel);

        //Parâmetros
        Principal.getControllerCenaParametros().textFieldCoeficienteDeSeguranca.setText(dadosSalvos.coeficienteDeSegurancaAtual);
        Principal.getControllerCenaParametros().textFieldCoeficienteDeSegurancaMaximo.setText(dadosSalvos.coeficienteDeSegurancaMaximoAtual);
        Principal.getControllerCenaParametros().textFieldMajoradorFlexoCompressao.setText(dadosSalvos.majoradorFlexoCompressaoAtual);
        Principal.getControllerCenaParametros().textFieldTaxaMinimaLongitudinal.setText(dadosSalvos.taxaMinimaLongitudinalAtual);
        Principal.getControllerCenaParametros().textFieldTaxaMaximaLongitudinal.setText(dadosSalvos.taxaMaximaLongitudinalAtual);

        Principal.getControllerCenaParametros().onActionButtonOk(null);
    }

    //Métodos de inicialização
    private void ajeitarComboBoxAgregadoGraudo() {
        //Alimentando a ComboBox de seleção do agregado graúdo
        this.comboBoxAgregado.getItems().add(0, Concreto.AgregadoGraudo.Arenito);
        this.comboBoxAgregado.getItems().add(1, Concreto.AgregadoGraudo.Basalto);
        this.comboBoxAgregado.getItems().add(2, Concreto.AgregadoGraudo.Calcario);
        this.comboBoxAgregado.getItems().add(3, Concreto.AgregadoGraudo.Diabasio);
        this.comboBoxAgregado.getItems().add(4, Concreto.AgregadoGraudo.Gnaisse);
        this.comboBoxAgregado.getItems().add(5, Concreto.AgregadoGraudo.Granito);
        this.comboBoxAgregado.setVisibleRowCount(30);
    }

    private void ajeitarComboBoxTipoFundacao() {
        //Alimentando a ComboBox
        this.comboBoxTipoFundacao.getItems().add(0, dimensionamentoEstruturalTubulao.TipoEstaca.EstacaPersonalizada);
        this.comboBoxTipoFundacao.getItems().add(1, dimensionamentoEstruturalTubulao.TipoEstaca.TubulaoPersonalizado);
        this.comboBoxTipoFundacao.getItems().add(2, dimensionamentoEstruturalTubulao.TipoEstaca.Helice);
        this.comboBoxTipoFundacao.getItems().add(3, dimensionamentoEstruturalTubulao.TipoEstaca.EscavadaSemFluido);
        this.comboBoxTipoFundacao.getItems().add(4, dimensionamentoEstruturalTubulao.TipoEstaca.EscavadaComFluido);
        this.comboBoxTipoFundacao.getItems().add(5, dimensionamentoEstruturalTubulao.TipoEstaca.Strauss);
        this.comboBoxTipoFundacao.getItems().add(6, dimensionamentoEstruturalTubulao.TipoEstaca.Franki);
        this.comboBoxTipoFundacao.getItems().add(7, dimensionamentoEstruturalTubulao.TipoEstaca.Tubulao);
        this.comboBoxTipoFundacao.getItems().add(8, dimensionamentoEstruturalTubulao.TipoEstaca.Raiz);
        this.comboBoxTipoFundacao.getItems().add(9, dimensionamentoEstruturalTubulao.TipoEstaca.MicroEstaca);
        this.comboBoxTipoFundacao.getItems().add(10, dimensionamentoEstruturalTubulao.TipoEstaca.EstacaTradoVazado);

        //Selecionando a checkBox e adicionando listener na CheckBox
        this.comboBoxTipoFundacao.getSelectionModel().selectedItemProperty().addListener((ObservableValue observable, Object oldValue, Object newValue) -> {
            dimensionamentoEstruturalTubulao.TipoEstaca tipo = (dimensionamentoEstruturalTubulao.TipoEstaca) newValue;
            alterarJanelaPeloTipoDeFundacao(tipo);
        });

        //Fazendo a seleção inicial
        this.comboBoxTipoFundacao.getSelectionModel().select(7);
    }

}
