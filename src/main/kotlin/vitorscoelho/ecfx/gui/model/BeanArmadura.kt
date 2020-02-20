package vitorscoelho.ecfx.gui.model

import javafx.beans.property.ObjectProperty
import tornadofx.ItemViewModel
import vitorscoelho.ecfx.gui.WithDescriptionsEcfx
import vitorscoelho.utils.measure.*
import vitorscoelho.utils.tfx.doubleProp
import vitorscoelho.utils.tfx.objectProp
import vitorscoelho.utils.tfxmeasure.bind
import javax.measure.Unit
import javax.measure.quantity.Length
import javax.measure.quantity.Pressure

class BeanArmadura(tipo: TipoArmadura) {
    val fykProperty = pressureProp(name = "${tipo.nome}.fyk", value = 500, unit = MEGAPASCAL)
    val gamaSProperty = doubleProp(name = "${tipo.nome}.gamaS", value = 1.15)
    val moduloElasticidadeProperty = pressureProp(
        name = "${tipo.nome}.moduloElasticidade", value = 210, unit = GIGAPASCAL
    )
    val bitolaProperty = lengthProp(name = "${tipo.nome}.bitola", value = 10, unit = MILLIMETRE)
}

class BeanArmaduraModel(
    tipo: TipoArmadura,
    unitResistencia: ObjectProperty<Unit<Pressure>>,
    unitModuloElasticidade: ObjectProperty<Unit<Pressure>>,
    unitBitola: ObjectProperty<Unit<Length>>
) : ItemViewModel<BeanArmadura>(initialValue = BeanArmadura(tipo = tipo)), WithDescriptionsEcfx {
    constructor(tipo: TipoArmadura, unidades: BeanUnidades) : this(
        tipo = tipo,
        unitResistencia = unidades.unitResistenciaMaterialProperty,
        unitModuloElasticidade = unidades.unitModuloElasticidadeAcoProperty,
        unitBitola = unidades.unitBitolaProperty
    )

    val fyk = bind(BeanArmadura::fykProperty, unitResistencia)
    val gamaS = bind(BeanArmadura::gamaSProperty)
    val moduloElasticidade = bind(BeanArmadura::moduloElasticidadeProperty, unitModuloElasticidade)
    val bitola = bind(BeanArmadura::bitolaProperty, unitBitola)
}

enum class TipoArmadura {
    ESTRIBO {
        override val nome: String = "estribo"
    },
    LONGITUDINAL {
        override val nome: String = "longitudinal"
    };

    abstract val nome: String
}