package vitorscoelho.ecfx.gui.model

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import tech.units.indriya.AbstractUnit.ONE
import tech.units.indriya.unit.Units.METRE
import tech.units.indriya.unit.Units.PASCAL
import tornadofx.onChange
import vitorscoelho.ecfx.dimensionamento.estrutural.AgregadoGraudo
import vitorscoelho.ecfx.utils.GAMA_MINUSCULO
import vitorscoelho.ecfx.utils.PHI_MAIUSCULO
import vitorscoelho.ecfx.utils.QUILONEWTON_POR_CENTIMETRO_QUADRADO
import javax.measure.quantity.Pressure
import javax.measure.Unit
import javax.measure.quantity.Dimensionless
import javax.measure.quantity.Length

val AGREGADO_QUALQUER = AgregadoGraudo(nome = "Outro") {
    throw IllegalAccessException("Impossível calcular o módulo de deformação automaticamente com o agregado selecionado")
}
val AGREGADOS_DISPONIVEIS = listOf(
    AgregadoGraudo.ARENITO, AgregadoGraudo.BASALTO, AgregadoGraudo.CALCARIO,
    AgregadoGraudo.DIABASIO, AgregadoGraudo.GNAISSE, AgregadoGraudo.GRANITO,
    AGREGADO_QUALQUER
)

class Dados {
    //Unidades
    val unitTensaoMaterialProperty: ObjectProperty<Unit<Pressure>> = SimpleObjectProperty(PASCAL)
    val unitModuloElasticidadeProperty: ObjectProperty<Unit<Pressure>> = SimpleObjectProperty(PASCAL)
    val unitAdimensionalProperty: ObjectProperty<Unit<Dimensionless>> = SimpleObjectProperty(ONE)
    val unitBitolaProperty: ObjectProperty<Unit<Length>> = SimpleObjectProperty(METRE)

    //Concreto
    val fckProperty = QuantityProperty(
        nome = "fck",
        descricao = "Resistência característica à compressão do concreto",
        unitProperty = unitTensaoMaterialProperty
    )
    val gamaCProperty = QuantityProperty(
        nome = "${GAMA_MINUSCULO}c",
        descricao = "Coeficiente de ponderação da resistência do concreto",
        unitProperty = unitAdimensionalProperty
    )
    val agregadoProperty: ObjectProperty<AgregadoGraudo> = SimpleObjectProperty(AGREGADO_QUALQUER)
    val moduloDeformacaoConcretoProperty = QuantityProperty(
        nome = "Ecs",
        descricao = "Módulo de deformação secante do concreto",
        unitProperty = unitModuloElasticidadeProperty
    ).apply {
        agregadoProperty.onChange {
            try {
                magnitude = moduloDeformacaoSecante(agregado = it, fckProperty = fckProperty)
            } catch (e: Exception) {
            }
        }
        fckProperty.magnitudeProperty.onChange {
            try {
                magnitude = moduloDeformacaoSecante(agregado = agregadoProperty.value, fckProperty = fckProperty)
            } catch (e: Exception) {
            }
        }
    }

    //Armadura transversal (Estribos)
    val estriboFywkProperty = QuantityProperty(
        nome = "fywk",
        descricao = "Resistência característica ao escoamento do aço da armadura transversal",
        unitProperty = unitTensaoMaterialProperty
    )
    val estriboGamaS = QuantityProperty(
        nome = "${GAMA_MINUSCULO}s",
        descricao = "Coeficiente de ponderação da resistência do aço da armadura transversal",
        unitProperty = unitAdimensionalProperty
    )
    val estriboBitola = QuantityProperty(
        nome = "${PHI_MAIUSCULO}t",
        descricao = "Bitola da barra utilizada na armadura transversal",
        unitProperty = unitBitolaProperty
    )

    //Armadura longitudinal
    val longitudinalFykProperty = QuantityProperty(
        nome = "fyk",
        descricao = "Resistência característica ao escoamento do aço da armadura longitudinal",
        unitProperty = unitTensaoMaterialProperty
    )
    val longitudinalGamaS = QuantityProperty(
        nome = "${GAMA_MINUSCULO}s",
        descricao = "Coeficiente de ponderação da resistência do aço da armadura longitudinal",
        unitProperty = unitAdimensionalProperty
    )
    val longitudinalModuloDeformacao = QuantityProperty(
        nome = "Es",
        descricao = "Módulo de deformação do aço da armadura longitudinal",
        unitProperty = unitModuloElasticidadeProperty
    )
    val longitudinalBitola = QuantityProperty(
        nome = "${PHI_MAIUSCULO}l",
        descricao = "Bitola da barra utilizada na armadura longitudinal",
        unitProperty = unitBitolaProperty
    )
}

private fun moduloDeformacaoSecante(agregado: AgregadoGraudo?, fckProperty: QuantityProperty<Pressure>): Double {
    val fckConvertido = fckProperty.toQuantity().to(QUILONEWTON_POR_CENTIMETRO_QUADRADO).value.toDouble()
    return agregado!!.moduloDeformacaoSecante(fck = fckConvertido)
}