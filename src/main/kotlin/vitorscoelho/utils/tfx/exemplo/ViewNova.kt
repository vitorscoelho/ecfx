package vitorscoelho.tfxutils.exemplo

import javafx.application.Application
import javafx.beans.property.SimpleObjectProperty
import javafx.util.Duration
import tech.units.indriya.quantity.Quantities
import tech.units.indriya.unit.Units.METRE
import tech.units.indriya.unit.Units.SQUARE_METRE
import tornadofx.*
import vitorscoelho.tfxutils.*
import java.util.*
import javax.measure.MetricPrefix
import javax.measure.Quantity
import javax.measure.Unit
import javax.measure.quantity.Area
import javax.measure.quantity.Length

internal fun main(args: Array<String>) {
    Locale.setDefault(Locale.US)
    FX.locale = Locale("en", "US")
    quantityFactory = object : QuantityFactory {
        override fun <T : Quantity<T>> getQuantity(value: Number, unit: Unit<T>): Quantity<T> {
            return Quantities.getQuantity(value, unit)
        }
    }
    Application.launch(Aplicacao::class.java, *args)
}

internal class Aplicacao : App(ViewNova::class, EstiloPrincipal::class) {
    init {
        reloadStylesheetsOnFocus()
        val versaoJava = System.getProperty("java.version")
        val versaoJavaFX = System.getProperty("javafx.version")
        println("Versão Java: $versaoJava // Versão JavaFX: $versaoJavaFX")
    }
}

internal class ViewNova : View() {
    val validationContext = ValidationContext().apply {
        decorationProvider = {
            MessageDecorator(
                message = it.message,
                severity = it.severity,
                tooltipCssRule = EstiloPrincipal.tooltipErro
            )
        }
    }
    val elemento = BeanElementoModel(
        BeanElemento().apply {
            texto = "Texto"
            inteiro = 1
            real = 2.0
            quantityInteiro = Quantities.getQuantity(23, METRE)
            quantityReal = Quantities.getQuantity(12.0, SQUARE_METRE)
        }
    )

    val unitTeste = SimpleObjectProperty(METRE)
    override val root = form {
        fieldset("Dados de elemento") {
            inputTextFieldString(property = elemento.texto) {
                addValidator(validationContext) { texto ->
                    if (!texto.isNullOrBlank() && texto.last() == 'u') null else error("Deveria terminar com u")
                }
                addValidator(validationContext) { texto ->
                    if (!texto.isNullOrBlank() && texto.first() == 'l') null else error("Deveria começar com l")
                }
            }
            inputTextFieldInt(property = elemento.inteiro) {
                addValidator(validationContext, ERROR_IF_NEGATIVE_INT)
            }
            inputTextFieldDouble(property = elemento.real) {
                addValidator(validationContext = validationContext, validator = ERROR_IF_NOT_POSITIVE_DOUBLE)
            }
            inputTextFieldInt(property = elemento.quantityInteiro) {
                //                addValidator(validationContext=validationContext,validator = )
//                addValidator(validationContext, ERROR_IF_NOT_POSITIVE_INT)
                addValidator(validationContext) { texto ->
                    val valorDouble = texto?.toDouble() ?: 0.0
                    val qtd = quantityFactory.getQuantity(valorDouble, property.value.unit)
                    val qtdMinima =
                        quantityFactory.getQuantity(20.0, METRE).to(property.value.unit.asType(Length::class.java))
                    if (qtd.value.toDouble() < qtdMinima.value.toDouble()) {
                        error("Deveria ser maior que $qtdMinima")
                    } else null
                }
            }
            inputTextFieldDouble(property = elemento.quantityReal) {
                addValidator(
                    validationContext,
                    ifNotDoubleInInterval(
                        min = 20.0, minInclusive = true,
                        severity = ValidationSeverity.Error
                    )
                )
            }
            field("OIsss")
            elemento.quantityInteiro.conectar(unitTeste)
        }
        button("OI") {
            action {
                //                elemento.quantityInteiro.value = Quantities.getQuantity(333, MetricPrefix.CENTI(METRE))
                unitTeste.value = MetricPrefix.CENTI(METRE)
            }
        }
        validationContext.validate()
    }
}