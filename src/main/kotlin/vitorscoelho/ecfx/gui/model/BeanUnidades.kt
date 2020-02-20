package vitorscoelho.ecfx.gui.model

import javafx.beans.property.SimpleObjectProperty
import tornadofx.ItemViewModel
import vitorscoelho.utils.measure.*

class BeanUnidades {
    val unitResistenciaMaterialProperty = SimpleObjectProperty(MEGAPASCAL)
    val unitModuloElasticidadeConcretoProperty = SimpleObjectProperty(MEGAPASCAL)
    val unitModuloElasticidadeAcoProperty = SimpleObjectProperty(MEGAPASCAL)
    val unitBitolaProperty = SimpleObjectProperty(MILLIMETRE)
    val unitCobrimentoProperty = SimpleObjectProperty(CENTIMETRE)
    val unitDimensoesEstacaProperty = SimpleObjectProperty(CENTIMETRE)
    val unitProfundidadeProperty = SimpleObjectProperty(METRE)
    val unitForcaProperty = SimpleObjectProperty(KILONEWTON)
    val unitMomentoProperty = SimpleObjectProperty(KILONEWTON.multiply(METRE).asMoment())
    val unitCoeficienteReacaoProperty = SimpleObjectProperty(
        KILONEWTON.divide(CUBIC_METRE).asSpringStiffnessPerUnitArea()
    )
    val unitCoesaoProperty = SimpleObjectProperty(MEGAPASCAL)
    val unitAnguloDeAtritoProperty = SimpleObjectProperty(DEGREE)
    val unitPesoEspecificoProperty = SimpleObjectProperty(KILONEWTON.divide(CUBIC_METRE).asSpecificWeight())
    val unitTensaoSoloProperty = SimpleObjectProperty(KILOPASCAL)
}

class BeanUnidadesModel : ItemViewModel<BeanUnidades>() {
    val unitResistenciaMaterial = bind(BeanUnidades::unitResistenciaMaterialProperty)
    val unitModuloElasticidadeConcreto = bind(BeanUnidades::unitModuloElasticidadeConcretoProperty)
    val unitModuloElasticidadeAco = bind(BeanUnidades::unitModuloElasticidadeAcoProperty)
    val unitBitola = bind(BeanUnidades::unitBitolaProperty)
    val unitCobrimento = bind(BeanUnidades::unitCobrimentoProperty)
    val unitDimensoesEstaca = bind(BeanUnidades::unitDimensoesEstacaProperty)
    val unitProfundidade = bind(BeanUnidades::unitProfundidadeProperty)
    val unitForca = bind(BeanUnidades::unitForcaProperty)
    val unitMomento = bind(BeanUnidades::unitMomentoProperty)
    val unitCoeficienteReacao = bind(BeanUnidades::unitCoeficienteReacaoProperty)
    val unitCoesao = bind(BeanUnidades::unitCoesaoProperty)
    val unitAnguloDeAtrito = bind(BeanUnidades::unitAnguloDeAtritoProperty)
    val unitPesoEspecifico = bind(BeanUnidades::unitPesoEspecificoProperty)
    val unitTensaoSolo = bind(BeanUnidades::unitTensaoSoloProperty)
}
