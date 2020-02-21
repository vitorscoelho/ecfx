package vitorscoelho.ecfx.gui.model

import javafx.beans.property.SimpleObjectProperty
import tornadofx.ItemViewModel
import tornadofx.getValue
import tornadofx.setValue
import vitorscoelho.utils.measure.*

class BeanUnidades {
    val unitResistenciaMaterialProperty = SimpleObjectProperty(MEGAPASCAL)
    var unitResistenciaMaterial by unitResistenciaMaterialProperty
    val unitModuloElasticidadeConcretoProperty = SimpleObjectProperty(MEGAPASCAL)
    var unitModuloElasticidadeConcreto by unitModuloElasticidadeConcretoProperty
    val unitModuloElasticidadeAcoProperty = SimpleObjectProperty(MEGAPASCAL)
    var unitModuloElasticidadeAco by unitModuloElasticidadeAcoProperty
    val unitBitolaProperty = SimpleObjectProperty(MILLIMETRE)
    var unitBitola by unitBitolaProperty
    val unitCobrimentoProperty = SimpleObjectProperty(CENTIMETRE)
    var unitCobrimento by unitCobrimentoProperty
    val unitDimensoesEstacaProperty = SimpleObjectProperty(CENTIMETRE)
    var unitDimensoesEstaca by unitDimensoesEstacaProperty
    val unitProfundidadeProperty = SimpleObjectProperty(METRE)
    var unitProfundidade by unitProfundidadeProperty
    val unitForcaProperty = SimpleObjectProperty(KILONEWTON)
    var unitForca by unitForcaProperty
    val unitMomentoProperty = SimpleObjectProperty(KILONEWTON.multiply(METRE).asMoment())
    var unitMomento by unitMomentoProperty
    val unitCoeficienteReacaoProperty = SimpleObjectProperty(
        KILONEWTON.divide(CUBIC_METRE).asSpringStiffnessPerUnitArea()
    )
    var unitCoeficienteReacao by unitCoeficienteReacaoProperty
    val unitCoesaoProperty = SimpleObjectProperty(MEGAPASCAL)
    var unitCoesao by unitCoesaoProperty
    val unitAnguloDeAtritoProperty = SimpleObjectProperty(DEGREE)
    var unitAnguloDeAtrito by unitAnguloDeAtritoProperty
    val unitPesoEspecificoProperty = SimpleObjectProperty(KILONEWTON.divide(CUBIC_METRE).asSpecificWeight())
    var unitPesoEspecifico by unitPesoEspecificoProperty
    val unitTensaoSoloProperty = SimpleObjectProperty(KILOPASCAL)
    var unitTensaoSolo by unitTensaoSoloProperty
    val unitDeslocamentoProperty = SimpleObjectProperty(MILLIMETRE)
    var unitDeslocamento by unitDeslocamentoProperty
    val unitRotacaoProperty = SimpleObjectProperty(RADIAN)
    var unitRotacao by unitRotacaoProperty
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
