package vitorscoelho.ecfx.novagui.view

import javafx.event.EventTarget
import javafx.geometry.Pos
import javafx.scene.layout.Region
import javafx.scene.text.Text
import tornadofx.*
import vitorscoelho.ecfx.gui.estilo.EstiloPrincipal
import vitorscoelho.ecfx.novagui.utils.rb
import vitorscoelho.ecfx.utils.NOME_DO_PROGRAMA
import vitorscoelho.ecfx.utils.VERSAO_DO_PROGRAMA
import vitorscoelho.utils.tfx.setMinMaxPrefHeight
import vitorscoelho.utils.tfx.setMinMaxPrefWidth

internal class ViewSobre : View(title = rb["tituloJanelaSobre"]) {
    override val root = vbox {
        addClass(EstiloPrincipal.vboxSobre)
        alignment = Pos.CENTER
        setMinMaxPrefWidth(value = 600.0)
        setMinMaxPrefHeight(value = Region.USE_COMPUTED_SIZE)
        textflow {
            textH1(NOME_DO_PROGRAMA)
            text(" - ${rb["textoVersao"]} $VERSAO_DO_PROGRAMA")
            pularUmaLinha()
            text(rb["descricaoDoPrograma"])
            pularDuasLinhas()
            textH2("${rb["desenvolvidoPor"]}:")
            pularUmaLinha()
            text(nomeEmail(nome = "Vítor Silva Coelho", email = "vitor.eec@hotmail.com"))
            pularDuasLinhas()
            textH2("${rb["consultoriaTecnica"]}:")
            pularUmaLinha()
            text(
                nomeEmail(
                    nome = "Douglas Magalhães Albuquerque Bittencourt",
                    email = "engenheirobittencourt@gmail.com"
                )
            )
            pularDuasLinhas()
            textH2("${rb["atencao"]}:")
            pularUmaLinha()
            text(rb["avisoSoftware"])
        }
        button(rb["ok"]) { action { close() } }
    }

    private fun EventTarget.pularUmaLinha() {
        text("\r\n")
    }

    private fun EventTarget.pularDuasLinhas() {
        text("\r\n\r\n")
    }

    private fun nomeEmail(nome: String, email: String): String {
        return with(StringBuilder()) {
            append(rb["engenheiroCivil"])
            append(" $nome")
            append("\r\n")
            append(rb["email"])
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