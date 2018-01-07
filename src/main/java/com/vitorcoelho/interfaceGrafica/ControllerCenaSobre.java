/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vitorcoelho.interfaceGrafica;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * FXML Controller class
 *
 * @author Vítor
 */
public class ControllerCenaSobre extends ControllerCenaPadrao implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML TextFlow texto;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Text texto1 = new Text("Texto exemplo 1 \r\n");
        Text texto2 = new Text("Texto exemplo 2  \r\n");
        Text texto3 = new Text("Texto exemplo 3  \r\n");
        
        texto.getChildren().add(texto1);
        texto.getChildren().add(texto2);
        texto.getChildren().add(texto3);
        
        texto1.setFont(Font.font("GreekC"));
    }    

    @Override
    public void travarStage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void destravarStage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
