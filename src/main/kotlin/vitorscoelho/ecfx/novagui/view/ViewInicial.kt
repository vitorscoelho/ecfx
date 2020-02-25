package vitorscoelho.ecfx.novagui.view

import javafx.beans.value.ObservableValue
import javafx.geometry.Orientation
import javafx.util.StringConverter
import tornadofx.*
import vitorscoelho.ecfx.gui.estilo.EstiloPrincipal
import vitorscoelho.ecfx.novagui.utils.GuiDoubleProp
import vitorscoelho.ecfx.novagui.utils.GuiProp
import vitorscoelho.ecfx.novagui.utils.SEMPRE_VALIDO
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class FckProp(initialValue: Double, format: ObservableValue<DecimalFormat>) : GuiDoubleProp(
    initialValue = initialValue,
    name = "fck",
    label = "fck",
    description = "Resistência do concreto",
    format = format,
    filterInput = { texto ->
        texto.isDouble() &&
//                !texto.contains("-") &&
                !texto.contains(other = "d", ignoreCase = true)
    },//FILTER_INPUT_POSITIVE_REAL,
    valido = SEMPRE_VALIDO
)

class Formato(initialPattern: String) : GuiProp<DecimalFormat>(
    initialValue = DecimalFormat(initialPattern, DecimalFormatSymbols(Locale("en", "US"))),
    name = "formato",
    label = "Formato",
    description = "Formato do valor",
    converter = object : StringConverter<DecimalFormat>() {
        override fun toString(value: DecimalFormat?): String {
            return value?.toPattern() ?: ""
        }

        override fun fromString(string: String?): DecimalFormat {
            return DecimalFormat(string, DecimalFormatSymbols(Locale("en", "US")))
        }
    }
) {
    private var pattern: String = initialPattern
}

class ViewInicial : View() {
    val formato = Formato(initialPattern = "#.00")
    val fck = FckProp(initialValue = 2000000.0, format = formato.commitValueProp)
    /*
    val FILTER_INPUT_POSITIVE_REAL: (formatter: TextFormatter.Change) -> Boolean by lazy {
    { formatter: TextFormatter.Change ->
        formatter.controlNewText.isDouble() &&
                !formatter.controlNewText.contains("-") &&
                !formatter.controlNewText.contains(other = "d", ignoreCase = true)
    }
}
     */

    private val conteudo = form {
        fieldset("Concreto") {
            field("fck") {
                textfield(property = fck.textProp) {
                    filterInput { discriminator -> fck.filterInput.invoke(discriminator.controlNewText) }
                    focusedProperty().onChange { noFoco ->
                        if (!noFoco) {
                            val double = fck.converter.fromString(fck.textProp.value)
                            val string = fck.converter.toString(double)
                            fck.textProp.value = string
                        }
                    }
                }
            }
            labelPosition = Orientation.VERTICAL
            field("Formato") {
                textfield(property = formato.textProp)
            }
            button("Aplica formato") { action { formato.commit() } }
            button("Commit") { action { fck.commit() } }
            button("Rollback") { action { fck.rollback() } }
        }
    }

    override val root = borderpane {
        top {
            //            this += menu
        }
        center {
            hbox {
                addClass(EstiloPrincipal.vboxDados)
                this += conteudo
            }
        }
    }
}
/*
form {
            fieldset(text = descricoes.rb["fieldsetConcreto"]) {
                labelPosition = Orientation.VERTICAL
                with(controller.concreto) {
                    inputTextFieldPositiveDouble(property = fck).maiorQueZero()
                    inputTextFieldPositiveDouble(property = gamaC).maiorQueZero()
                    inputTextFieldPositiveDouble(property = ecs).maiorQueZero()
                }
            }
            fieldset(text = descricoes.rb["fieldsetArmaduras"]) {
                labelPosition = Orientation.VERTICAL
                with(controller.armaduras) {
                    inputTextFieldPositiveDouble(property = fykEstribo).maiorQueZero()
                    inputTextFieldPositiveDouble(property = bitolaEstribo).maiorQueZero()
                    inputTextFieldPositiveDouble(property = fykLongitudinal).maiorQueZero()
                    inputTextFieldPositiveDouble(property = bitolaLongitudinal).maiorQueZero()
                    inputTextFieldPositiveDouble(property = gamaS).maiorQueZero()
                    inputTextFieldPositiveDouble(property = moduloElasticidade).maiorQueZero()
                }
            }
        }
 */