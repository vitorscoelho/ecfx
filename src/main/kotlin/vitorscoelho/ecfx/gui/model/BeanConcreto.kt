package vitorscoelho.ecfx.gui.model

import javafx.beans.property.BooleanProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleBooleanProperty
import tornadofx.ItemViewModel
import tornadofx.onChange
import vitorscoelho.ecfx.gui.WithDescriptionsEcfx
import vitorscoelho.utils.measure.MEGAPASCAL
import vitorscoelho.utils.measure.pressureProp
import vitorscoelho.utils.measure.pressureSUOf
import vitorscoelho.utils.measure.toDoubleSU
import vitorscoelho.utils.tfx.doubleProp
import vitorscoelho.utils.tfx.objectProp
import vitorscoelho.utils.tfxmeasure.bind
import javax.measure.Unit
import javax.measure.quantity.Pressure

class BeanConcreto {
    val fckProperty = pressureProp(name = "fck", value = 20, unit = MEGAPASCAL)
    val gamaCProperty = doubleProp(name = "gamaC", value = 1.8)
    val ecsProperty = pressureProp(name = "ecs", value = 25_000, unit = MEGAPASCAL)
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