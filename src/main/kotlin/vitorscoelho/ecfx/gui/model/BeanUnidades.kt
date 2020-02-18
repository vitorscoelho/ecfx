package vitorscoelho.ecfx.gui.model

import javafx.beans.property.SimpleObjectProperty
import tech.units.indriya.unit.Units
import javax.measure.quantity.Moment
import javax.measure.quantity.SpecificWeight
import javax.measure.quantity.SpringStiffnessPerUnitArea

class BeanUnidades {
    val unitResistenciaMaterialProperty = SimpleObjectProperty(Units.PASCAL)
    val unitModuloElasticidadeConcretoProperty = SimpleObjectProperty(Units.PASCAL)
    val unitModuloElasticidadeAcoProperty = SimpleObjectProperty(Units.PASCAL)
    val unitBitolaProperty = SimpleObjectProperty(Units.METRE)
    val unitCobrimento = SimpleObjectProperty(Units.METRE)
    val unitDimensoesEstaca = SimpleObjectProperty(Units.METRE)
    val unitProfundidade = SimpleObjectProperty(Units.METRE)
    val unitForca = SimpleObjectProperty(Units.NEWTON)
    val unitMomento = SimpleObjectProperty(Units.NEWTON.multiply(Units.METRE).asType(Moment::class.java))
    val unitComprimentoArmadura = SimpleObjectProperty(Units.METRE)
    val unitTensaoConcreto = SimpleObjectProperty(Units.PASCAL)
    val unitCoeficienteReacao = SimpleObjectProperty(
        Units.NEWTON.divide(Units.CUBIC_METRE).asType(SpringStiffnessPerUnitArea::class.java)
    )
    val unitCoesao = SimpleObjectProperty(Units.PASCAL)
    val unitAnguloDeAtrito = SimpleObjectProperty(Units.RADIAN)
    val unitPesoEspecifico =
        SimpleObjectProperty(Units.NEWTON.divide(Units.CUBIC_METRE).asType(SpecificWeight::class.java))
    val unitTensaoSolo = SimpleObjectProperty(Units.PASCAL)
}