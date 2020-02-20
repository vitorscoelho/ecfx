package vitorscoelho.ecfx.gui.model

import javafx.beans.property.BooleanProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleBooleanProperty
import tornadofx.ItemViewModel
import tornadofx.onChange
import vitorscoelho.ecfx.dimensionamento.estrutural.AgregadoGraudo
import vitorscoelho.ecfx.dimensionamento.estrutural.AgregadoGraudo.Companion.ARENITO
import vitorscoelho.ecfx.dimensionamento.estrutural.AgregadoGraudo.Companion.BASALTO
import vitorscoelho.ecfx.dimensionamento.estrutural.AgregadoGraudo.Companion.CALCARIO
import vitorscoelho.ecfx.dimensionamento.estrutural.AgregadoGraudo.Companion.DIABASIO
import vitorscoelho.ecfx.dimensionamento.estrutural.AgregadoGraudo.Companion.GNAISSE
import vitorscoelho.ecfx.dimensionamento.estrutural.AgregadoGraudo.Companion.GRANITO
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
    val agregadoProperty = objectProp(name = "agregado", value = GRANITO)
}

private val AGREGADO_OUTRO = AgregadoGraudo(nome = "Outro", moduloDeformacaoSecante = { 0.0 })
val agregados = listOf(ARENITO, BASALTO, CALCARIO, DIABASIO, GNAISSE, GRANITO, AGREGADO_OUTRO)

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
    val agregado = bind(BeanConcreto::agregadoProperty)
    val ecsNaoPredefinido: BooleanProperty = SimpleBooleanProperty(agregado.value == AGREGADO_OUTRO)

    init {
        arrayOf(fck, agregado).forEach { it.onChange { atualizarEcs(unitModuloElasticidade) } }
        atualizarEcs(unitModuloElasticidade)
    }

    private fun atualizarEcs(unitModuloElasticidade: ObjectProperty<Unit<Pressure>>) {
        if (agregado.value != AGREGADO_OUTRO) {
            val ecsDouble = agregado.value.moduloDeformacaoSecante(fck = fck.toDoubleSU())
            val ecsQuantity = pressureSUOf(
                value = ecsDouble, toUnit = unitModuloElasticidade.value
            )
            ecs.value = ecsQuantity
        }
        ecsNaoPredefinido.value = (agregado.value == AGREGADO_OUTRO)
    }
}