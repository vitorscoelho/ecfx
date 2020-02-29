package vitorscoelho.ecfx.novagui.view

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import javafx.geometry.Orientation
import javafx.scene.control.TextField
import javafx.util.StringConverter
import tornadofx.*
import vitorscoelho.ecfx.gui.estilo.EstiloPrincipal
import vitorscoelho.ecfx.novagui.utils.*
import vitorscoelho.utils.measure.KILOPASCAL
import vitorscoelho.utils.measure.MEGAPASCAL
import vitorscoelho.utils.measure.PASCAL
import vitorscoelho.utils.measure.pressureOf
import vitorscoelho.utils.tfx.MessageDecorator
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import javax.measure.quantity.Pressure

val unidade = SimpleObjectProperty(KILOPASCAL)
var contador = 0

class Formato(initialPattern: String) : GuiProp<DecimalFormat>(
    initialValue = DecimalFormat(initialPattern, DecimalFormatSymbols(Locale("en", "US")))
) {
    override val name = "formato"
    override val label = "Formato"
    override val description = "Formato do valor"
    override val converter = object : StringConverter<DecimalFormat>() {
        override fun toString(value: DecimalFormat?): String {
            return value?.toPattern() ?: ""
        }

        override fun fromString(string: String?): DecimalFormat {
            return DecimalFormat(string, DecimalFormatSymbols(Locale("en", "US")))
        }
    }
}

class ViewInicial : View() {
    val validationContext = ValidationContext().apply {
        decorationProvider = {
            MessageDecorator(
                message = it.message,
                severity = it.severity,
                tooltipCssRule = vitorscoelho.utils.tfx.exemplo.EstiloPrincipal.tooltipErro
            )
        }
    }

    val formato = Formato(initialPattern = "#.00")
    val fck = FckProp(initialValue = 2000000.0, format = formato.commitValueProp)

    private val conteudo = form {
        fieldset("Concreto") {
            fieldTextField(property = fck,validationContext = validationContext)
//            field("fck") {
//                textfield(property = fck.textProp)
//            }
            labelPosition = Orientation.VERTICAL
//            textfield(property = fck)
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

interface WithValidationContext {
    val validationContext: ValidationContext
}