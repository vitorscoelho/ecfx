package vitorscoelho.ecfx.novagui.view

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.geometry.Orientation
import javafx.util.StringConverter
import tornadofx.*
import vitorscoelho.ecfx.gui.estilo.EstiloPrincipal
import vitorscoelho.ecfx.novagui.utils.*
import vitorscoelho.utils.measure.KILOPASCAL
import vitorscoelho.utils.measure.MEGAPASCAL
import vitorscoelho.utils.measure.PASCAL
import vitorscoelho.utils.measure.pressureOf
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import javax.measure.quantity.Pressure

/*
"" -> 0
"." -> 0
"-" -> 0 //Permitido apenas em campos que podem ser negativo
"+" -> 0
"#.#E" -> #.#E0
".#E" -> #.#E0
"#.E" -> #.#E0
"#E" -> #.#E0
"#.#E+" -> #.#E0
"#.#E-" -> #.#E0
Permitir inteiros com representação com mantissa. Mas a mantissa deve sempre ser positiva.
 */


//fun intOrNull(texto: String): Int? {
//    if (texto == "" || texto == "." || texto == "+") return 0
//}
//
//fun doubleOrNull(texto: String): Double? {
//    if (texto == "" || texto == "." || texto == "-" || texto == "+") return 0.0
//}

val unidade = SimpleObjectProperty(KILOPASCAL)
var contador = 0

class FckProp(initialValue: Double, format: ObservableValue<DecimalFormat>) : GuiQuantityProp<Pressure>(
    initialValue = pressureOf(value = 20, unit = MEGAPASCAL),
    name = "fck",
    label = "fck",
    description = "Resistência do concreto",
    format = format,
    unit = unidade,
    filterInput = { texto ->
        texto.isDouble()
//        println("${contador++} -> ${texto.inputIsPositiveDouble()}")
//        texto.inputIsPositiveDouble()
//        texto.isDouble() &&
//                !texto.contains("-") &&
//                !texto.contains(other = "d", ignoreCase = true)
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
                    filterInput { discriminator ->
                        val isValido = fck.filterInput(discriminator.controlNewText)
                        if (discriminator.isDeleted && !isValido) discriminator.setRange(0, 0)
                        isValido
                    }
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
            field("Unidade") {
                combobox(property = unidade, values = listOf(PASCAL, KILOPASCAL, MEGAPASCAL))
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