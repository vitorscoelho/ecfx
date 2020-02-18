package vitorscoelho.ecfx.gui.model

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleDoubleProperty
import tornadofx.ItemViewModel
import vitorscoelho.ecfx.gui.WithDescriptionsEcfx
import vitorscoelho.utils.measure.pressureProp
import vitorscoelho.utils.tfxmeasure.bind
import javax.measure.Unit
import javax.measure.quantity.Pressure

class BeanConcreto {
    val fckProperty = pressureProp(name = "fck", value = 0.0)
    val gamaCProperty = SimpleDoubleProperty(null, "gamaC").apply { value = 0.0 }
    val ecsProperty = pressureProp(name = "ecs", value = 0.0)
}

class BeanConcretoModel(
    unitResistencia: ObjectProperty<Unit<Pressure>>,
    unitModuloElasticidade: ObjectProperty<Unit<Pressure>>
) : ItemViewModel<BeanConcreto>(initialValue = BeanConcreto()), WithDescriptionsEcfx {
    constructor(unidades: BeanUnidades) : this(
        unitResistencia = unidades.unitResistenciaMaterialProperty,
        unitModuloElasticidade = unidades.unitModuloElasticidadeConcretoProperty
    )
    val fck = bind(BeanConcreto::fckProperty, unitResistencia)
    val gamaC = bind(BeanConcreto::gamaCProperty)
    val ecs = bind(BeanConcreto::ecsProperty, unitModuloElasticidade)
}