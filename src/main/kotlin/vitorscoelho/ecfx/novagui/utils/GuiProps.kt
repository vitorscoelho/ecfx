package vitorscoelho.ecfx.novagui.utils

import javafx.beans.binding.Bindings
import javafx.beans.property.*
import javafx.beans.value.ObservableValue
import javafx.scene.Node
import javafx.util.StringConverter
import tornadofx.View
import tornadofx.isDouble
import tornadofx.isInt
import tornadofx.onChange
import vitorscoelho.utils.measure.createQuantity
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import javax.measure.Quantity
import javax.measure.Unit
import javax.measure.quantity.Pressure

val SEM_FILTER_INPUT: (input: String) -> Boolean = { _ -> true }
val SEMPRE_VALIDO: (input: String) -> Boolean = { _ -> true }
val regexNumero = """(([0-9]+\.?)|\.)[0-9]*[e|E]?[+|-]?[0-9]*""".toRegex()

/*
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
 */

open class GuiQuantityProp<T : Quantity<T>>(
    initialValue: Quantity<T>,
    name: String? = null,
    label: String? = null,
    description: String? = null,
    private val format: ObservableValue<DecimalFormat>,
    private val unit: ObservableValue<Unit<T>>,
    filterInput: (input: String) -> Boolean = SEM_FILTER_INPUT,
    valido: (input: String) -> Boolean = SEMPRE_VALIDO
) : GuiProp<Quantity<T>>(
    initialValue = initialValue,
    name = name,
    label = label,
    description = description,
    converter = object : StringConverter<Quantity<T>>() {
        override fun toString(valor: Quantity<T>): String {
            return format.value.format(valor.to(unit.value).value)
        }

        override fun fromString(string: String): Quantity<T> {
            val magnitude = string.inputToNumber()
            return createQuantity(value = magnitude, unit = unit.value)
        }
    },
    filterInput = filterInput,
    valido = valido
) {
    init {
        format.onChange { atualizarTexto() }
        unit.onChange { atualizarTexto() }
    }

    private fun atualizarTexto() {
        val double = converter.fromString(textProp.value)
        val string = converter.toString(double)
        textProp.value = string
    }
}

open class GuiDoubleProp(
    initialValue: Double,
    name: String? = null,
    label: String? = null,
    description: String? = null,
    private val format: ObservableValue<DecimalFormat>,
    filterInput: (input: String) -> Boolean = SEM_FILTER_INPUT,
    valido: (input: String) -> Boolean = SEMPRE_VALIDO
) : GuiProp<Double>(
    initialValue = initialValue,
    name = name,
    label = label,
    description = description,
    converter = object : StringConverter<Double>() {
        override fun toString(valor: Double?) = format.value.format(valor) ?: "0"
        override fun fromString(string: String?): Double {
            if (string.isNullOrBlank() || string == "-") return 0.0
            return string.toDouble()
        }
    },
    filterInput = filterInput,
    valido = valido
) {
    init {
        format.onChange {
            val double = converter.fromString(textProp.value)
            val string = converter.toString(double)
            textProp.value = string
        }
    }
}

open class GuiProp<T>(
    initialValue: T,
    val name: String? = null,
    val label: String? = null,
    val description: String? = null,
    val converter: StringConverter<T>,
    val filterInput: (input: String) -> Boolean = SEM_FILTER_INPUT,
    val valido: (input: String) -> Boolean = SEMPRE_VALIDO
) {
    val textProp: StringProperty = SimpleStringProperty(converter.toString(initialValue))
    private val _commitValueProp = SimpleObjectProperty(initialValue).apply {
        onChange { println(value) }
    }
    val commitValueProp: ReadOnlyObjectProperty<T>
        get() = _commitValueProp
    val dirty = Bindings.createBooleanBinding(
        { commitValueProp.value != converter.fromString(textProp.value) },
        arrayOf(commitValueProp, textProp)
    )

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