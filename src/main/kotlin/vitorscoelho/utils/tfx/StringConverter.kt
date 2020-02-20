package vitorscoelho.utils.tfx

import javafx.beans.property.Property
import javafx.util.StringConverter
import tech.units.indriya.quantity.Quantities
import javax.measure.Quantity
import javax.measure.Unit

val STRING_CONVERTER_STRING: StringConverter<String> by lazy {
    object : StringConverter<String>() {
        override fun toString(valor: String?): String? = valor
        override fun fromString(string: String?): String? = string
    }
}

val STRING_CONVERTER_DOUBLE: StringConverter<Number> by lazy {
    object : StringConverter<Number>() {
        override fun toString(valor: Number?): String? {
            if (valor == null) return null
            return valor.toString()
        }

        override fun fromString(valor: String?): Double {
            if (valor.isNullOrBlank() || valor == "-") return 0.0
            return valor.toDouble()
        }
    }
}

val STRING_CONVERTER_INT: StringConverter<Number> by lazy {
    object : StringConverter<Number>() {
        override fun toString(valor: Number?): String? {
            if (valor == null) return null
            return valor.toString()
        }

        override fun fromString(valor: String?): Int {
            if (valor.isNullOrBlank() || valor == "-") return 0
            return valor.toInt()
        }
    }
}

interface QuantityFactory {
    fun <T : Quantity<T>> getQuantity(value: Number, unit: javax.measure.Unit<T>): Quantity<T>
}

private val quantityFactory = object : QuantityFactory {
    override fun <T : Quantity<T>> getQuantity(value: Number, unit: Unit<T>): Quantity<T> {
        return Quantities.getQuantity(value, unit)
    }
}

fun <T : Quantity<T>> stringConverterQuantity(
    property: Property<Quantity<T>>,
    converterNumber: StringConverter<Number>
) = object : StringConverter<Quantity<T>>() {
    override fun toString(valor: Quantity<T>?) = valor?.value.toString() ?: ""

    override fun fromString(string: String?): Quantity<T> {
        val magnitude: Number = converterNumber.fromString(string)
        return quantityFactory.getQuantity(magnitude, property.value.unit)
    }
}

fun <T : Quantity<T>> stringConverterQuantity(
    property: Property<Quantity<T>>,
    converterNumber: StringConverter<Number>,
    quantity: (value: Number, unit: javax.measure.Unit<T>) -> Quantity<T>
) = object : StringConverter<Quantity<T>>() {
    override fun toString(valor: Quantity<T>?) = valor?.value.toString() ?: ""

    override fun fromString(string: String?): Quantity<T> {
        val magnitude: Number = converterNumber.fromString(string)
        return quantity(magnitude, property.value.unit)
    }
}