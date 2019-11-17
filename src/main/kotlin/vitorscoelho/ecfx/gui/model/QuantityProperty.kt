package vitorscoelho.ecfx.gui.model

import javafx.beans.property.*
import tech.units.indriya.quantity.Quantities
import tornadofx.onChange
import vitorscoelho.ecfx.utils.getTextoUnidade
import javax.measure.Quantity
import javax.measure.Unit
import tornadofx.getValue
import tornadofx.setValue

class QuantityProperty<T : Quantity<T>>(
    val nome: String = "",
    val descricao: String = "",
    val unitProperty: ObjectProperty<Unit<T>>
) : SimpleObjectProperty<T>() {
    val magnitudeProperty: DoubleProperty = SimpleDoubleProperty(0.0).apply {
        unitProperty.addListener { observable, oldValue, newValue ->
            if (oldValue == null || newValue == null) return@addListener
            value = Quantities.getQuantity(value, oldValue).to(newValue).value.toDouble()
        }
    }
    var magnitude by magnitudeProperty
    var unit by unitProperty
    val unitTextProperty: ReadOnlyStringProperty = SimpleStringProperty(getTextoUnidade(unit)).apply {
        unitProperty.onChange {
            value = getTextoUnidade(it as Unit<*>)
        }
    }

    fun toQuantity(): Quantity<T> = Quantities.getQuantity(magnitude, unit)
}