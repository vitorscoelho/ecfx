package com.vitorcoelho.interfaceGrafica;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

public class ControllerCenaSobre extends ControllerCenaPadrao implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    TextFlow texto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Text texto1 = new Text("ECFX");
        Text texto2 = new Text(" - Versão " + Principal.VERSAO_DO_PROGRAMA);
        Text texto3 = new Text("\r\nPrograma auxiliar para dimensionamento de estacas curtas.");

        Text texto4 = new Text("\r\n\r\nDesenvolvido por:");
        Text texto5 = new Text("\r\nEng° Civil Vítor Silva Coelho");
        Text texto6 = new Text("\r\nE-mail: vitor.eec@hotmail.com");

        Text texto7 = new Text("\r\n\r\nConsultoria técnica:");
        Text texto8 = new Text("\r\nEng° Civil Douglas Magalhães Albuquerque Bittencourt ");
        Text texto9 = new Text("\r\nE-mail: engenheirobittencourt@gmail.com");

        Text texto10 = new Text("\r\n\r\nAtenção:");
        Text texto11 = new Text("\r\nSoftware desenvolvido para fins didáticos. Sua distribuição é gratuita, portanto o usuário é livre para usá-lo e compartilhá-lo. É proibida sua comercialização.");
        Text texto12 = new Text("\r\nO autor não é responsável pelo mau uso do software ou por seu resultados.");
        Text texto13 = new Text("\r\nQualquer conclusão feita com o uso do programa é de inteira responsabilidade do usuário, não havendo, da parte do autor, nenhum dever legal ou responsabilidade por danos causados pelo uso de alguma informação gerada pelo software. Não existe nenhum compromisso de bom funcionamento ou qualquer garantia.");

        String nomeFonte = "Noto Serif";

        FontWeight weight1 = FontWeight.NORMAL;
        FontWeight weight2 = FontWeight.BOLD;

        double size1 = 18.0;
        double size2 = 15.0;
        double size3 = 14.0;

        texto1.setFont(Font.font(nomeFonte, weight2, size1));
        texto2.setFont(Font.font(nomeFonte, weight1, size3));
        texto3.setFont(Font.font(nomeFonte, weight1, size3));
        texto4.setFont(Font.font(nomeFonte, weight2, size2));
        texto5.setFont(Font.font(nomeFonte, weight1, size3));
        texto6.setFont(Font.font(nomeFonte, weight1, size3));
        texto7.setFont(Font.font(nomeFonte, weight2, size2));
        texto8.setFont(Font.font(nomeFonte, weight1, size3));
        texto9.setFont(Font.font(nomeFonte, weight1, size3));
        texto10.setFont(Font.font(nomeFonte, weight2, size2));
        texto11.setFont(Font.font(nomeFonte, weight1, size3));
        texto12.setFont(Font.font(nomeFonte, weight1, size3));
        texto13.setFont(Font.font(nomeFonte, weight1, size3));

        texto.getChildren().add(texto1);
        texto.getChildren().add(texto2);
        texto.getChildren().add(texto3);
        texto.getChildren().add(texto4);
        texto.getChildren().add(texto5);
        texto.getChildren().add(texto6);
        texto.getChildren().add(texto7);
        texto.getChildren().add(texto8);
        texto.getChildren().add(texto9);
        texto.getChildren().add(texto10);
        texto.getChildren().add(texto11);
        texto.getChildren().add(texto12);
        texto.getChildren().add(texto13);
    }

    @FXML
    private void onActionButtonOk(ActionEvent event) {
        Principal.getStageCenaSobre().close();
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
