package vitorscoelho.ecfx.gui.view

import javafx.beans.binding.Bindings
import javafx.event.EventTarget
import javafx.scene.control.TextField
import javafx.scene.control.TextFormatter
import javafx.scene.control.Tooltip
import javafx.util.StringConverter
import tornadofx.*
import vitorscoelho.ecfx.gui.estilo.DELAY_TOOLTIP
import vitorscoelho.ecfx.gui.model.QuantityDoubleProperty
import vitorscoelho.ecfx.gui.model.QuantityIntProperty
import vitorscoelho.ecfx.gui.model.QuantityProperty
import java.lang.IllegalArgumentException
import javax.measure.Quantity

fun <T : Quantity<T>> EventTarget.fieldQuantity(
    property: QuantityProperty<T>,
    op: Field.(tf: TextField) -> kotlin.Unit = { }
): Field {
    return this.field {
        textProperty.bind(
            Bindings.createStringBinding(
                {
                    val unidade: String = property.unitTextProperty.value
                    var retorno: String = property.nome
                    if (!unidade.isBlank()) retorno += " ($unidade)"
                    return@createStringBinding retorno
                },
                arrayOf(property.unitTextProperty)
            )
        )
        val tf = textfield {
            val (converter, tipoInput) = when (property) {
                is QuantityDoubleProperty<*> -> Pair(
                    stringConverterDouble,
                    if (property.minimo >= 0.0) TipoInput.REAL_POSITIVO else TipoInput.REAL
                )
                is QuantityIntProperty<*> -> Pair(
                    stringConverterInt,
                    if (property.minimo >= 0) TipoInput.INTEIRO_POSITIVO else TipoInput.INTEIRO
                )
            }
            bind(property = property.magnitudeProperty, converter = converter)
            filterInput { tipoInput.filterInput(it) }
            tooltip = Tooltip(property.descricao)
            tooltip.showDelay = DELAY_TOOLTIP
        }
        op(this, tf)
    }
}

private val stringConverterDouble: StringConverter<Number> = object : StringConverter<Number>() {
    override fun toString(valor: Number?): String? {
        if (valor == null) return null
        return valor.toString()
    }

    override fun fromString(valor: String?): Double {
        if (valor.isNullOrBlank() || valor == "-") return 0.0
        return valor.toDouble()
    }
}

private val stringConverterInt: StringConverter<Number> = object : StringConverter<Number>() {
    override fun toString(valor: Number?): String? {
        if (valor == null) return null
        return valor.toString()
    }

    override fun fromString(valor: String?): Int {
        if (valor.isNullOrBlank() || valor == "-") return 0
        return valor.toInt()
    }
}

private enum class TipoInput {
    INTEIRO {
        override fun filterInput(formatter: TextFormatter.Change): Boolean {
            return formatter.controlNewText.isInt() || formatter.controlNewText == "-"
        }
    },
    INTEIRO_POSITIVO {
        override fun filterInput(formatter: TextFormatter.Change): Boolean {
            return formatter.controlNewText.isInt() && !formatter.controlNewText.contains("-")
        }
    },
    REAL {
        override fun filterInput(formatter: TextFormatter.Change): Boolean {
            return (formatter.controlNewText.isDouble() && !formatter.controlNewText.contains(
                other = "d",
                ignoreCase = true
            )) || formatter.controlNewText == "-"
        }
    },
    REAL_POSITIVO {
        override fun filterInput(formatter: TextFormatter.Change): Boolean {
            return formatter.controlNewText.isDouble() &&
                    !formatter.controlNewText.contains("-") &&
                    !formatter.controlNewText.contains(other = "d", ignoreCase = true)
        }
    };

    abstract fun filterInput(formatter: TextFormatter.Change): Boolean

    companion object {
        fun <T : Quantity<T>> fromQuantityProperty(property: QuantityProperty<T>): TipoInput {
            if (property is QuantityDoubleProperty<T>) {
                if (property.minimo >= 0.0) REAL_POSITIVO else REAL
            } else if (property is QuantityIntProperty<T>) {
                if (property.minimo >= 0) INTEIRO_POSITIVO else INTEIRO
            }
            throw IllegalArgumentException("Tipo de QuantityProperty não suportado")
        }
    }
    /*
    fun <T : Quantity<T>> EventTarget.fieldQuantity(
    property: QuantityProperty<T>,
    op: Field.(tf: TextField) -> kotlin.Unit = { }
): Field {
     */
    /*
    val converter = when (property) {
                is QuantityDoubleProperty<*> -> stringConverterDouble
                is QuantityIntProperty<*> -> stringConverterInt
            }
     */
}