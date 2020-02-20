package vitorscoelho.ecfx.gui.model

import javafx.beans.property.ObjectProperty
import tornadofx.ItemViewModel
import vitorscoelho.ecfx.gui.WithDescriptionsEcfx
import vitorscoelho.utils.measure.*
import vitorscoelho.utils.tfx.doubleProp
import vitorscoelho.utils.tfxmeasure.bind
import javax.measure.Unit
import javax.measure.quantity.Length
import javax.measure.quantity.Pressure

class BeanArmadura {
    val fykEstriboProperty = pressureProp(name = "estribo.fyk", value = 500, unit = MEGAPASCAL)
    val bitolaEstriboProperty = lengthProp(name = "estribo.bitola", value = 8, unit = MILLIMETRE)
    val fykLongitudinalProperty = pressureProp(name = "longitudinal.fyk", value = 500, unit = MEGAPASCAL)
    val bitolaLongitudinalProperty = lengthProp(name = "longitudinal.bitola", value = 10, unit = MILLIMETRE)
    val gamaSProperty = doubleProp(name = "aco.gamaS", value = 1.15)
    val moduloElasticidadeProperty = pressureProp(
        name = "aco.moduloElasticidade", value = 210, unit = GIGAPASCAL
    )
}

class BeanArmaduraModel(
    unitResistencia: ObjectProperty<Unit<Pressure>>,
    unitModuloElasticidade: ObjectProperty<Unit<Pressure>>,
    unitBitola: ObjectProperty<Unit<Length>>
) : ItemViewModel<BeanArmadura>(initialValue = BeanArmadura()), WithDescriptionsEcfx {
    constructor(unidades: BeanUnidades) : this(
        unitResistencia = unidades.unitResistenciaMaterialProperty,
        unitModuloElasticidade = unidades.unitModuloElasticidadeAcoProperty,
        unitBitola = unidades.unitBitolaProperty
    )

    val fykEstribo = bind(BeanArmadura::fykEstriboProperty, unitResistencia)
    val bitolaEstribo = bind(BeanArmadura::bitolaEstriboProperty, unitBitola)
    val fykLongitudinal = bind(BeanArmadura::fykLongitudinalProperty, unitResistencia)
    val bitolaLongitudinal = bind(BeanArmadura::bitolaLongitudinalProperty, unitBitola)
    val gamaS = bind(BeanArmadura::gamaSProperty)
    val moduloElasticidade = bind(BeanArmadura::moduloElasticidadeProperty, unitModuloElasticidade)
}