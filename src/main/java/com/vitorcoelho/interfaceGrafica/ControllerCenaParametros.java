package com.vitorcoelho.interfaceGrafica;

import com.vitorcoelho.dimensionamentoEstrutural.FlexoCompressao;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ControllerCenaParametros extends ControllerCenaPadrao implements Initializable {

    @FXML
    TextField textFieldCoeficienteDeSeguranca;
    @FXML
    TextField textFieldCoeficienteDeSegurancaMaximo;
    @FXML
    TextField textFieldMajoradorFlexoCompressao;
    @FXML
    TextField textFieldTaxaMinimaLongitudinal;
    @FXML
    TextField textFieldTaxaMaximaLongitudinal;
    @FXML
    TextArea textAreaDescricao;

    String coeficienteDeSegurancaAtual = "2.0";
    String coeficienteDeSegurancaMaximoAtual = "20.0";
    String majoradorFlexoCompressaoAtual = "1.0";
    String taxaMinimaLongitudinalAtual;
    String taxaMaximaLongitudinalAtual;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.textAreaDescricao.setWrapText(true);

        //Mudando o funcionamento do botão "x" que fecha a janela
        Principal.getStageCenaParametros().setOnCloseRequest((WindowEvent we) -> {
            we.consume();
            onActionButtonCancelar(null);
        });
        this.taxaMinimaLongitudinalAtual = arredondarToString(FlexoCompressao.getPorcentagemAsMin(), 2);
        this.taxaMaximaLongitudinalAtual = arredondarToString(FlexoCompressao.getPorcentagemAsMax(), 2);

        //Adicionando os listeners nos TextFields para aparecer a descrição do campo
        this.textFieldCoeficienteDeSeguranca.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                this.textAreaDescricao.setText("Descrição: Coeficiente de segurança mínimo aos esforços horizontais para que o elemento de fundação seja considerado como estável");
            }
        });
        this.textFieldCoeficienteDeSegurancaMaximo.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                this.textAreaDescricao.setText("Descrição: Coeficiente de segurança máximo aos esforços horizontais que será apresentado no gráfico dos resultados.");
            }
        });
        this.textFieldMajoradorFlexoCompressao.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                this.textAreaDescricao.setText("Descrição: Majorador da tensão admissível vertical para tensão máxima em flexo compressão na base.");
            }
        });
        this.textFieldTaxaMinimaLongitudinal.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                this.textAreaDescricao.setText("Descrição: Porcentagem mínima de área de aço da armadura longitudinal em relação a área de concreto da seção transversal.");
            }
        });
        this.textFieldTaxaMaximaLongitudinal.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                this.textAreaDescricao.setText("Descrição: Porcentagem máxima de área de aço da armadura longitudinal em relação a área de concreto da seção transversal.");
            }
        });
    }

    @FXML
    void onActionButtonOk(ActionEvent event) {
        final double CSmin = Double.parseDouble(this.textFieldCoeficienteDeSeguranca.getText());
        final double CSmax = Double.parseDouble(this.textFieldCoeficienteDeSegurancaMaximo.getText());

        final double ASmin = Double.parseDouble(this.textFieldTaxaMinimaLongitudinal.getText());
        final double ASmax = Double.parseDouble(this.textFieldTaxaMaximaLongitudinal.getText());

        String descricaoErro = "";
        boolean erro = false;
        if (CSmin >= CSmax) {
            erro = true;
            descricaoErro = "CSmax deve ser maior do que CSmin.\r\nReedite os parâmetros.";
        }
        if (ASmin >= ASmax) {
            erro = true;
            descricaoErro = "A taxa de armadura máxima deve ser maior do que a mínima.\r\nReedite os parâmetros.";
        }
        if (erro) {
            Alert dialogErro = new Alert(Alert.AlertType.ERROR);
            dialogErro.setTitle("Erro!");
            dialogErro.setHeaderText("Parâmetros inconsistentes");
            dialogErro.setContentText(descricaoErro);
            dialogErro.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            dialogErro.showAndWait();
        } else {
            setDadosAtuais();
            Principal.getStageCenaParametros().close();
        }
    }

    @FXML
    void onActionButtonCancelar(ActionEvent event) {
        Principal.getStageCenaParametros().close();
        textFieldCoeficienteDeSeguranca.setText(arredondarToString(coeficienteDeSegurancaAtual, 2));
        textFieldCoeficienteDeSegurancaMaximo.setText(arredondarToString(coeficienteDeSegurancaMaximoAtual, 2));
        textFieldMajoradorFlexoCompressao.setText(arredondarToString(majoradorFlexoCompressaoAtual, 2));
        textFieldTaxaMinimaLongitudinal.setText(arredondarToString(taxaMinimaLongitudinalAtual, 2));
        textFieldTaxaMaximaLongitudinal.setText(arredondarToString(taxaMaximaLongitudinalAtual, 2));
    }

    @FXML
    void onActionRestaurarPadroes(ActionEvent event) {
        Alert dialogAviso = new Alert(Alert.AlertType.CONFIRMATION);
        dialogAviso.setTitle("Janela de confirmação");
        dialogAviso.setHeaderText("Deseja aplicar os valores padrões do software aos parâmetros?");
        dialogAviso.setContentText("");
        dialogAviso.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        ButtonType buttonTypeSim = new ButtonType("Sim");
        ButtonType buttonTypeNao = new ButtonType("Não");
        dialogAviso.getButtonTypes().setAll(buttonTypeSim, buttonTypeNao);

        Optional<ButtonType> escolha = dialogAviso.showAndWait();
        if (escolha.get() == buttonTypeSim) {
            textFieldCoeficienteDeSeguranca.setText("2.0");
            textFieldCoeficienteDeSegurancaMaximo.setText("20.0");
            textFieldMajoradorFlexoCompressao.setText("1.0");
            textFieldTaxaMinimaLongitudinal.setText("0.5");
            textFieldTaxaMaximaLongitudinal.setText("8.0");
        }
    }

    public double getCoeficienteDeSeguranca() {
        return Double.parseDouble(textFieldCoeficienteDeSeguranca.getText());
    }

    public double getCoeficienteDeSegurancaMaximo() {
        return Double.parseDouble(textFieldCoeficienteDeSegurancaMaximo.getText());
    }

    public double getMajoradorFlexoCompressao() {
        return Double.parseDouble(textFieldMajoradorFlexoCompressao.getText());
    }

    private void setDadosAtuais() {
        this.coeficienteDeSegurancaAtual = textFieldCoeficienteDeSeguranca.getText();
        this.coeficienteDeSegurancaMaximoAtual = textFieldCoeficienteDeSegurancaMaximo.getText();
        this.majoradorFlexoCompressaoAtual = textFieldMajoradorFlexoCompressao.getText();
        this.taxaMinimaLongitudinalAtual = textFieldTaxaMinimaLongitudinal.getText();
        this.taxaMaximaLongitudinalAtual = textFieldTaxaMaximaLongitudinal.getText();

        FlexoCompressao.setPorcentagemAsMin(Double.parseDouble(taxaMinimaLongitudinalAtual));
        FlexoCompressao.setPorcentagemAsMax(Double.parseDouble(taxaMaximaLongitudinalAtual));
        Resultados.coeficienteSegurancaMaximo = Double.parseDouble(coeficienteDeSegurancaMaximoAtual);
    }

}
