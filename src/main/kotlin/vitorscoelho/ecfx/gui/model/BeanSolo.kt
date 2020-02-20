package vitorscoelho.ecfx.gui.model

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.ItemViewModel
import tornadofx.get
import vitorscoelho.ecfx.gui.WithDescriptionsEcfx
import vitorscoelho.ecfx.gui.descricoes
import vitorscoelho.utils.measure.*
import vitorscoelho.utils.tfxmeasure.bind
import javax.measure.Unit
import javax.measure.quantity.Angle
import javax.measure.quantity.Pressure
import javax.measure.quantity.SpecificWeight
import javax.measure.quantity.SpringStiffnessPerUnitArea

class BeanSolo {
    val tipo = SimpleObjectProperty<TipoSolo>(null, "solo.tipo").apply { value = TipoSolo.AREIA_OU_ARGILA_MOLE }
    val kh = springStiffnessPerUnitAreaProp(
        name = "solo.${tipo.value.idCoeficienteReacaoHorizontal}", value = 12_500,
        unit = KILONEWTON.divide(CUBIC_METRE).asSpringStiffnessPerUnitArea()
    )
    val kv = springStiffnessPerUnitAreaProp(
        name = "solo.kv", value = 120_000,
        unit = KILONEWTON.divide(CUBIC_METRE).asSpringStiffnessPerUnitArea()
    )
    val coesao = pressureProp(name = "solo.coesao", value = 0, unit = KILOPASCAL)
    val anguloDeAtrito = angleProp(name = "solo.anguloDeAtrito", value = 30, unit = DEGREE)
    val pesoEspecifico = specificWeightProp(
        name = "solo.pesoEspecifico", value = 18,
        unit = KILONEWTON.divide(CUBIC_METRE).asSpecificWeight()
    )
    val tensaoAdmissivel = pressureProp(name = "solo.tensaoAdmissivel", value = 200, unit = KILOPASCAL)
}

class BeanSoloModel(
    unitCoeficienteReacao: ObjectProperty<Unit<SpringStiffnessPerUnitArea>>,
    unitCoesao: ObjectProperty<Unit<Pressure>>,
    unitAnguloDeAtrito: ObjectProperty<Unit<Angle>>,
    unitPesoEspecifico: ObjectProperty<Unit<SpecificWeight>>,
    unitTensaoSolo: ObjectProperty<Unit<Pressure>>
) : ItemViewModel<BeanSolo>(initialValue = BeanSolo()), WithDescriptionsEcfx {
    constructor(unidades: BeanUnidades) : this(
        unitCoeficienteReacao = unidades.unitCoeficienteReacaoProperty,
        unitCoesao = unidades.unitCoesaoProperty,
        unitAnguloDeAtrito = unidades.unitAnguloDeAtritoProperty,
        unitPesoEspecifico = unidades.unitPesoEspecificoProperty,
        unitTensaoSolo = unidades.unitTensaoSoloProperty
    )

    val tipo = bind(BeanSolo::tipo)
    val kh = bind(BeanSolo::kh, unitCoeficienteReacao)
    val kv = bind(BeanSolo::kv, unitCoeficienteReacao)
    val coesao = bind(BeanSolo::coesao, unitCoesao)
    val anguloDeAtrito = bind(BeanSolo::anguloDeAtrito, unitAnguloDeAtrito)
    val pesoEspecifico = bind(BeanSolo::pesoEspecifico, unitPesoEspecifico)
    val tensaoAdmissivel = bind(BeanSolo::tensaoAdmissivel, unitTensaoSolo)
}

enum class TipoSolo {
    AREIA_OU_ARGILA_MOLE {
        override val descricao = descricoes.rb["descricao.areiaOuArgilaMole"]
        override val idCoeficienteReacaoHorizontal = "khAreiaOuArgilaMole"
    },
    ARGILA_RIJA_A_DURA {
        override val descricao = descricoes.rb["descricao.argilaRijaADura"]
        override val idCoeficienteReacaoHorizontal = "khArgilaRijaADura"
    };

    abstract val descricao: String
    abstract val idCoeficienteReacaoHorizontal: String

    override fun toString() = descricao
}