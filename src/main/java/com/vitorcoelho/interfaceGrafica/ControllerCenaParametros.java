/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vitorcoelho.interfaceGrafica;

import com.vitorcoelho.dimensionamentoEstrutural.FlexoCompressao;
import com.vitorcoelho.dimensionamentoTubulao.Analise2DTubulao;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Vítor
 */
public class ControllerCenaParametros extends ControllerCenaPadrao implements Initializable {

    @FXML TextField textFieldCoeficienteDeSeguranca;
    @FXML TextField textFieldCoeficienteDeSegurancaMaximo;
    @FXML TextField textFieldMajoradorFlexoCompressao;
    @FXML TextField textFieldTaxaMinimaLongitudinal;
    @FXML TextField textFieldTaxaMaximaLongitudinal;
    @FXML TextArea textAreaDescricao;

    String coeficienteDeSegurancaAtual = "1.0";
    String coeficienteDeSegurancaMaximoAtual = "20.0";
    String majoradorFlexoCompressaoAtual = "1.3";
    String taxaMinimaLongitudinalAtual;
    String taxaMaximaLongitudinalAtual;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
                this.textAreaDescricao.setText("Descrição:\r\nCoeficiente de segurança mínimo\r\naos esforços horizontais para que\r\no elemento de fundação seja\r\nconsiderado como estável");
            }
        });
        this.textFieldCoeficienteDeSegurancaMaximo.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                this.textAreaDescricao.setText("Descrição:\r\nCoeficiente de segurança máximo\r\naos esforços horizontais que será\r\napresentado no gráfico dos\r\nresultados.");
            }
        });
        this.textFieldMajoradorFlexoCompressao.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                this.textAreaDescricao.setText("Descrição:\r\nMajorador da tensão admissível\r\nvertical para tensão máxima em\r\nflexo compressão na base.");
            }
        });
        this.textFieldTaxaMinimaLongitudinal.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                this.textAreaDescricao.setText("Descrição:\r\nPorcentagem mínima de área de\r\naço da armadura longitudinal em\r\nrelação a área de concreto da\r\nseção transversal.");
            }
        });
        this.textFieldTaxaMaximaLongitudinal.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (newValue) {
                this.textAreaDescricao.setText("Descrição:\r\nPorcentagem máxima de área de\r\naço da armadura longitudinal em\r\nrelação a área de concreto da\r\nseção transversal.");
            }
        });
    }

    @FXML
    void onActionButtonOk(ActionEvent event) {
        setDadosAtuais();
        Principal.getStageCenaParametros().close();
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

        ButtonType buttonTypeSim = new ButtonType("Sim");
        ButtonType buttonTypeNao = new ButtonType("Não");
        dialogAviso.getButtonTypes().setAll(buttonTypeSim, buttonTypeNao);

        Optional<ButtonType> escolha = dialogAviso.showAndWait();
        if (escolha.get() == buttonTypeSim) {
            textFieldCoeficienteDeSeguranca.setText("1.0");
            textFieldCoeficienteDeSegurancaMaximo.setText("20.0");
            textFieldMajoradorFlexoCompressao.setText("1.3");
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
