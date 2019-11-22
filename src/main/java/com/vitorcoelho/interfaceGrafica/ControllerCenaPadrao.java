package com.vitorcoelho.interfaceGrafica;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

abstract class ControllerCenaPadrao implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    protected void onKeyTypedNumerosPositivos(KeyEvent event) {
        TextField texto = (TextField) event.getTarget();

        String caracteres;
        if (!texto.getText().contains(".")) {
            caracteres = "0987654321.,";
        } else {
            caracteres = "0987654321";
        }

        if (!caracteres.contains(event.getCharacter() + "")) {
            event.consume();
        } else if (event.getCharacter().contains(",")) {
            event.consume();
            texto.appendText(".");
        }
    }

    @FXML
    protected void onKeyTypedNumerosReais(KeyEvent event) {
        //Sempre usar junto com o onKeyReleasedNumerosReais
        TextField texto = (TextField) event.getTarget();

        String caracteres;

        if (!texto.getText().contains(".")) {
            caracteres = "0987654321.,";
        } else {
            caracteres = "0987654321";
        }

        if (!caracteres.contains(event.getCharacter() + "")) {
            event.consume();
        } else if (event.getCharacter().contains(",")) {
            event.consume();
            texto.appendText(".");
        }

        String negativo = "-";
        if (negativo.contains(event.getCharacter()) && !texto.getText().contains(negativo)) {
            event.consume();
            String textoArmazenado = negativo + texto.getText();
            texto.setText(textoArmazenado);
        }
    }

    @FXML
    protected void onKeyReleasedNumerosReais(KeyEvent event) {
        //Sempre usar junto com o onKeyTypedNumerosReais
        TextField texto = (TextField) event.getTarget();

        String negativo = "-";
        if (texto.getText().contains(negativo) && !texto.getText(0, 1).equals(negativo)) {
            int indiceDoNegativo = texto.getText().indexOf(negativo);
            texto.setText(texto.getText(indiceDoNegativo, texto.getText().length()));
        }
    }

    @FXML
    protected void onKeyTypedNumerosNaturais(KeyEvent event) {
        TextField texto = (TextField) event.getTarget();

        String caracteres = "0987654321";
        if (!caracteres.contains(event.getCharacter() + "")) {
            event.consume();
        }
    }

    @FXML
    protected void onKeyReleasedAtualizaValores(KeyEvent event) {
        TextField textField = (TextField) event.getTarget();
        if (textField.getText().isEmpty()) {
            textField.appendText("1");
        }
        atualizaValores();
    }

    protected void atualizaValores() {
    }

    public static double arredondar(double valor, int nCasas) {
        double x = (int) Math.pow(10, nCasas);
        double N = (int) (valor * x);
        N = (double) (N / x);
        return N;
    }

    public static String arredondarToString(double valor, int nCasas) {
        double resposta = arredondar(valor, nCasas);
        return (Double.toString(resposta));
    }

    public static String arredondarToString(String valor, int nCasas) {
        double valorD = Double.parseDouble(valor);
        double resposta = arredondar(valorD, nCasas);
        return (Double.toString(resposta));
    }

    public void travarStage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void destravarStage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
