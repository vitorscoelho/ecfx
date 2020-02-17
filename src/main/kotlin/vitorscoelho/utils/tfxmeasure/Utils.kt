package vitorscoelho.utils.tfxmeasure

import javafx.beans.property.ObjectProperty
import javafx.beans.property.Property
import tornadofx.ItemViewModel
import vitorscoelho.utils.tfx.conectar
import javax.measure.Quantity
import javax.measure.Unit
import kotlin.reflect.KProperty1

inline fun <T, reified N : Quantity<N>, reified PropertyType : Property<Quantity<N>>, ReturnType : PropertyType> ItemViewModel<T>.bind(
    property: KProperty1<T, PropertyType>,
    unit: ObjectProperty<Unit<N>>,
    autocommit: Boolean = false,
    forceObjectProperty: Boolean = false,
    defaultValue: N? = null
): ReturnType = bind(
    property = property,
    autocommit = autocommit,
    forceObjectProperty = forceObjectProperty,
    defaultValue = defaultValue
).apply { conectar(unitProperty = unit) } as ReturnType