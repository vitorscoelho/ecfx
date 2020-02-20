package vitorscoelho.ecfx.gui.model

import javafx.beans.property.SimpleObjectProperty
import tornadofx.ItemViewModel
import vitorscoelho.utils.measure.*

class BeanUnidades {
    val unitResistenciaMaterialProperty = SimpleObjectProperty(MEGAPASCAL)
    val unitModuloElasticidadeConcretoProperty = SimpleObjectProperty(MEGAPASCAL)
    val unitModuloElasticidadeAcoProperty = SimpleObjectProperty(MEGAPASCAL)
    val unitBitolaProperty = SimpleObjectProperty(MILLIMETRE)
    val unitCobrimento = SimpleObjectProperty(CENTIMETRE)
    val unitDimensoesEstaca = SimpleObjectProperty(CENTIMETRE)
    val unitProfundidade = SimpleObjectProperty(METRE)
    val unitForca = SimpleObjectProperty(KILONEWTON)
    val unitMomento = SimpleObjectProperty(KILONEWTON.multiply(METRE).asMoment())
    val unitComprimentoArmadura = SimpleObjectProperty(METRE)
    val unitTensaoConcreto = SimpleObjectProperty(MEGAPASCAL)
    val unitCoeficienteReacao = SimpleObjectProperty(KILONEWTON.divide(CUBIC_METRE).asSpringStiffnessPerUnitArea())
    val unitCoesao = SimpleObjectProperty(MEGAPASCAL)
    val unitAnguloDeAtrito = SimpleObjectProperty(DEGREE)
    val unitPesoEspecifico = SimpleObjectProperty(KILONEWTON.divide(CUBIC_METRE).asSpecificWeight())
    val unitTensaoSolo = SimpleObjectProperty(MEGAPASCAL)
}

class BeanUnidadesModel : ItemViewModel<BeanUnidades>() {
    val unitResistenciaMaterial = bind(BeanUnidades::unitResistenciaMaterialProperty)
    val unitModuloElasticidadeConcreto = bind(BeanUnidades::unitModuloElasticidadeConcretoProperty)
    val unitModuloElasticidadeAco = bind(BeanUnidades::unitModuloElasticidadeAcoProperty)
    val unitBitola = bind(BeanUnidades::unitBitolaProperty)
    val unitCobrimento = bind(BeanUnidades::unitCobrimento)
    val unitDimensoesEstaca = bind(BeanUnidades::unitDimensoesEstaca)
    val unitProfundidade = bind(BeanUnidades::unitProfundidade)
    val unitForca = bind(BeanUnidades::unitForca)
    val unitMomento = bind(BeanUnidades::unitMomento)
    val unitComprimentoArmadura = bind(BeanUnidades::unitComprimentoArmadura)
    val unitTensaoConcreto = bind(BeanUnidades::unitTensaoConcreto)
    val unitCoeficienteReacao = bind(BeanUnidades::unitCoeficienteReacao)
    val unitCoesao = bind(BeanUnidades::unitCoesao)
    val unitAnguloDeAtrito = bind(BeanUnidades::unitAnguloDeAtrito)
    val unitPesoEspecifico = bind(BeanUnidades::unitPesoEspecifico)
    val unitTensaoSolo = bind(BeanUnidades::unitTensaoSolo)
}
