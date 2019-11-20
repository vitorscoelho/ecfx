package vitorscoelho.ecfx.gui.model

import javafx.beans.property.*
import tech.units.indriya.quantity.Quantities
import tornadofx.onChange
import vitorscoelho.ecfx.utils.getTextoUnidade
import javax.measure.Quantity
import javax.measure.Unit
import tornadofx.getValue
import tornadofx.setValue

sealed class QuantityProperty<T : Quantity<T>>(
    val nome: String,
    val descricao: String,
    val unitProperty: ObjectProperty<Unit<T>>
) : SimpleObjectProperty<T>() {
    abstract val magnitudeProperty: Property<Number>
    var magnitude by magnitudeProperty
    var unit by unitProperty
    val unitTextProperty: ReadOnlyStringProperty = SimpleStringProperty(getTextoUnidade(unit)).apply {
        unitProperty.onChange {
            value = getTextoUnidade(it as Unit<*>)
        }
    }

    fun toQuantity(): Quantity<T> = Quantities.getQuantity(magnitude, unit)
}

class QuantityDoubleProperty<T : Quantity<T>>(
    nome: String = "",
    descricao: String = "",
    unitProperty: ObjectProperty<Unit<T>>,
    val minimo: Double = Double.NEGATIVE_INFINITY,
    val maximo: Double = Double.POSITIVE_INFINITY
) : QuantityProperty<T>(nome = nome, descricao = descricao, unitProperty = unitProperty) {
    override val magnitudeProperty: Property<Number> = SimpleDoubleProperty(0.0).apply {
        unitProperty.addListener { observable, oldValue, newValue ->
            if (oldValue == null || newValue == null) return@addListener
            value = Quantities.getQuantity(value, oldValue).to(newValue).value.toDouble()
        }
    }
}

class QuantityIntProperty<T : Quantity<T>>(
    nome: String = "",
    descricao: String = "",
    unitProperty: ObjectProperty<Unit<T>>,
    val minimo: Int = Int.MIN_VALUE,
    val maximo: Int = Int.MAX_VALUE
) : QuantityProperty<T>(nome = nome, descricao = descricao, unitProperty = unitProperty) {
    override val magnitudeProperty: Property<Number> = SimpleIntegerProperty(0).apply {
        unitProperty.addListener { observable, oldValue, newValue ->
            if (oldValue == null || newValue == null) return@addListener
            value = Quantities.getQuantity(value, oldValue).to(newValue).value.toInt()
        }
    }
}