package vitorscoelho.ecfx.gui.view

import javafx.event.EventTarget
import javafx.geometry.Pos
import javafx.scene.layout.Region
import javafx.scene.text.Text
import tornadofx.*
import vitorscoelho.ecfx.gui.descricoes
import vitorscoelho.ecfx.gui.estilo.EstiloPrincipal
import vitorscoelho.ecfx.utils.NOME_DO_PROGRAMA
import vitorscoelho.ecfx.utils.VERSAO_DO_PROGRAMA
import vitorscoelho.utils.tfx.setMinMaxPrefHeight
import vitorscoelho.utils.tfx.setMinMaxPrefWidth

internal class ViewSobre : View(title = descricoes.rb["tituloJanelaSobre"]) {
    override val root = vbox {
        addClass(EstiloPrincipal.vboxSobre)
        alignment = Pos.CENTER
        setMinMaxPrefWidth(value = 600.0)
        setMinMaxPrefHeight(value = Region.USE_COMPUTED_SIZE)
        textflow {
            textH1(NOME_DO_PROGRAMA)
            text(" - ${descricoes.rb["textoVersao"]} $VERSAO_DO_PROGRAMA")
            pularUmaLinha()
            text(descricoes.rb["descricaoDoPrograma"])
            pularDuasLinhas()
            textH2("${descricoes.rb["desenvolvidoPor"]}:")
            pularUmaLinha()
            text(nomeEmail(nome = "Vítor Silva Coelho", email = "vitor.eec@hotmail.com"))
            pularDuasLinhas()
            textH2("${descricoes.rb["consultoriaTecnica"]}:")
            pularUmaLinha()
            text(
                nomeEmail(
                    nome = "Douglas Magalhães Albuquerque Bittencourt",
                    email = "engenheirobittencourt@gmail.com"
                )
            )
            pularDuasLinhas()
            textH2("${descricoes.rb["atencao"]}:")
            pularUmaLinha()
            text(descricoes.rb["avisoSoftware"])
        }
        button(descricoes.rb["ok"]) { action { close() } }
    }

    private fun EventTarget.pularUmaLinha() {
        text("\r\n")
    }

    private fun EventTarget.pularDuasLinhas() {
        text("\r\n\r\n")
    }

    private fun nomeEmail(nome: String, email: String): String {
        return with(StringBuilder()) {
            append(descricoes.rb["engenheiroCivil"])
            append(" $nome")
            append("\r\n")
            append(descricoes.rb["email"])
            append(": $email")
        }.toString()
    }

    private fun EventTarget.textH1(initialValue: String, op: Text.() -> Unit = {}) = Text().attachTo(this, op) {
        it.text = initialValue
        it.addClass(EstiloPrincipal.h1Sobre)
    }

    private fun EventTarget.textH2(initialValue: String, op: Text.() -> Unit = {}) = Text().attachTo(this, op) {
        it.text = initialValue
        it.addClass(EstiloPrincipal.h2Sobre)
    }
}
/*
textflow {
    text("Tornado") {
        fill = Color.PURPLE
        font = Font(20.0)
    }
    text("FX") {
        fill = Color.ORANGE
        font = Font(28.0)
    }
}

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
 */