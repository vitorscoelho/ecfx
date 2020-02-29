package vitorscoelho.ecfx.novagui.utils

import javafx.beans.binding.Bindings
import javafx.beans.binding.BooleanBinding
import javafx.beans.property.*
import javafx.beans.value.ObservableValue
import javafx.util.StringConverter
import tornadofx.*
import vitorscoelho.ecfx.novagui.view.unidade
import vitorscoelho.utils.measure.MEGAPASCAL
import vitorscoelho.utils.measure.createQuantity
import vitorscoelho.utils.measure.pressureOf
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import javax.measure.Quantity
import javax.measure.Unit
import javax.measure.quantity.Pressure

val SEM_FILTER_INPUT: (input: String) -> Boolean = { _ -> true }
val SEMPRE_VALIDO: (input: String) -> Boolean = { _ -> true }

val rb = ResourceBundle.getBundle("vitorscoelho.ecfx.gui.Textos")

private val fckMinimo = createQuantity(value = 20, unit = MEGAPASCAL)
private val fckMaximo = createQuantity(value = 90, unit = MEGAPASCAL)

class FckProp(initialValue: Double, format: ObservableValue<DecimalFormat>) :
    GuiQuantityProp<Pressure>(
        initialValue = pressureOf(value = 20, unit = MEGAPASCAL),
        name = "fck",
        rb = rb,
        format = format,
        unit = unidade
    ), WithTextInputFilter, WithTextInputValidator {
    override val filterInput = { texto: String -> inputIsPositiveDouble(texto) }
    override val validator: ValidationContext.(String?) -> ValidationMessage? = { texto: String? ->
        if ((texto == null) || valorValido(texto = texto)) {
            null
        } else {
            val fckMinimoString: String = converter.toString(fckMinimo)
            val fckMaximoString: String = converter.toString(fckMaximo)
            val unidadeString: String = unit.value.toString()
            ValidationMessage(
                message = "Deve estar entre $fckMinimoString$unidadeString e $fckMaximoString$unidadeString.",
                severity = ValidationSeverity.Error
            )
        }
    }

    private fun valorValido(texto: String): Boolean {
        val magnitude: Number = inputToNumberOrNull(texto) ?: return false
        val valorAtual = createQuantity(value = magnitude, unit = unit.value)
        return (valorAtual in fckMinimo..fckMaximo)
    }
}

interface WithTextInputValidator {
    val validator: ValidationContext.(String?) -> ValidationMessage?
}

interface WithTextInputFilter {
    val filterInput: (input: String) -> Boolean
}

open class GuiQuantityProp<T : Quantity<T>>(
    initialValue: Quantity<T>,
    override val name: String? = null,
    override val label: String? = null,
    override val description: String? = null,
    private val format: ObservableValue<DecimalFormat>,
    val unit: ObservableValue<Unit<T>>
) : GuiProp<Quantity<T>>(initialValue = initialValue) {
    constructor(
        initialValue: Quantity<T>,
        name: String? = null,
        rb: ResourceBundle,
        format: ObservableValue<DecimalFormat>,
        unit: ObservableValue<Unit<T>>
    ) : this(
        initialValue = initialValue,
        name = name, label = rb["$name.label"], description = rb["$name.description"],
        format = format, unit = unit
    )

    override val converter: StringConverter<Quantity<T>> = object : StringConverter<Quantity<T>>() {
        override fun toString(valor: Quantity<T>): String {
            return format.value.format(valor.to(unit.value).value)
        }

        override fun fromString(string: String): Quantity<T> {
            val magnitude = inputToNumber(string)
            return createQuantity(value = magnitude, unit = unit.value)
        }
    }

    init {
        format.onChange {
            val qtd = converter.fromString(textProp.value)
            val string = converter.toString(qtd)
            textProp.value = string
        }
        unit.addListener { _, oldValue, newValue ->
            val magnitude = inputToNumber(textProp.value)
            val qtdAnterior = createQuantity(value = magnitude, unit = oldValue)
            val qtdNova = qtdAnterior.to(newValue)
            textProp.value = converter.toString(qtdNova)
        }
    }
}

//open class GuiDoubleProp(
//    initialValue: Double,
//    name: String? = null,
//    label: String? = null,
//    description: String? = null,
//    private val format: ObservableValue<DecimalFormat>,
//    filterInput: (input: String) -> Boolean = SEM_FILTER_INPUT,
//    valido: (input: String) -> Boolean = SEMPRE_VALIDO
//) : GuiProp<Double>(
//    initialValue = initialValue,
//    name = name,
//    label = label,
//    description = description,
//    converter = object : StringConverter<Double>() {
//        override fun toString(valor: Double?) = format.value.format(valor) ?: "0"
//        override fun fromString(string: String?): Double {
//            if (string.isNullOrBlank() || string == "-") return 0.0
//            return string.toDouble()
//        }
//    },
//    filterInput = filterInput,
//    valido = valido
//) {
//    init {
//        format.onChange {
//            val double = converter.fromString(textProp.value)
//            val string = converter.toString(double)
//            textProp.value = string
//        }
//    }
//}

abstract class GuiProp<T>(initialValue: T) {
    abstract val name: String?
    abstract val label: String?
    abstract val description: String?
    abstract val converter: StringConverter<T>

    val textProp: StringProperty by lazy { SimpleStringProperty(converter.toString(initialValue)) }
    private val _commitValueProp = SimpleObjectProperty(initialValue).apply {
        onChange { println(value) }
    }
    val commitValueProp: ReadOnlyObjectProperty<T>
        get() = _commitValueProp
    val dirty: BooleanBinding by lazy {
        Bindings.createBooleanBinding(
            { commitValueProp.value != converter.fromString(textProp.value) },
            arrayOf(commitValueProp, textProp)
        )
    }

    fun commit() {
        _commitValueProp.value = converter.fromString(textProp.value)
    }

    fun rollback() {
        textProp.value = converter.toString(_commitValueProp.value)
    }
}

class UnidadesEFormatacao<T : Quantity<T>>(
    val unit: Unit<T>,
    val format: NumberFormat
)