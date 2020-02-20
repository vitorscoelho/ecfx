package vitorscoelho.ecfx.gui.model

import javafx.beans.property.BooleanProperty
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleBooleanProperty
import tornadofx.ItemViewModel
import tornadofx.onChange
import vitorscoelho.ecfx.gui.WithDescriptionsEcfx
import vitorscoelho.utils.measure.*
import vitorscoelho.utils.tfx.booleanProp
import vitorscoelho.utils.tfx.objectProp
import vitorscoelho.utils.tfxmeasure.bind
import javax.measure.Unit
import javax.measure.quantity.Length
import javax.measure.quantity.Pressure

class BeanTipoEstaca {
    val tipoProperty = objectProp(name = "tipoestaca.tipo", value = ESTACA_PERSONALIZADA)
    val armaduraIntegralProperty = booleanProp(name = "tipoestaca.armaduraIntegral", value = false)
    val comprimentoMinimoArmaduraProperty = lengthProp(
        name = "tipoestaca.comprimentoMinimoArmadura", value = 3, unit = METRE
    )
    val tensaoMediaMaximaProperty = pressureProp(name = "tipoestaca.tensaoMediaMaxima", value = 5, unit = MEGAPASCAL)
}

private val ESTACA_PERSONALIZADA = TipoEstaca(
    nome = "Estaca personalizada", fck = pressureOf(0), gamaC = 0.0, gamaS = 0.0, tubulao = false
)

private val TUBULAO_PERSONALIZADO = TipoEstaca(
    nome = "Tubulão personalizado", fck = pressureOf(0), gamaC = 0.0, gamaS = 0.0, tubulao = true
)

val tiposDeEstaca = listOf(
    TUBULAO_PERSONALIZADO,
    TUBULAO_NAO_ENCAMISADO,
    ESTACA_PERSONALIZADA,
    HELICE, ESCAVADA_SEM_FLUIDO, ESCAVADA_COM_FLUIDO, STRAUSS, FRANKI, RAIZ, MICRO_ESTACA, ESTACA_TRADO_VAZADO
)

class BeanTipoEstacaModel(
    unitComprimentoArmadura: ObjectProperty<Unit<Length>>,
    unitTensaoConcreto: ObjectProperty<Unit<Pressure>>
) : ItemViewModel<BeanTipoEstaca>(initialValue = BeanTipoEstaca()), WithDescriptionsEcfx {
    constructor(unidades: BeanUnidades) : this(
        unitComprimentoArmadura = unidades.unitComprimentoArmadura,
        unitTensaoConcreto = unidades.unitTensaoConcreto
    )

    val tipo = bind(BeanTipoEstaca::tipoProperty)
    val armaduraIntegral = bind(BeanTipoEstaca::armaduraIntegralProperty)
    val comprimentoMinimoArmadura = bind(BeanTipoEstaca::comprimentoMinimoArmaduraProperty, unitComprimentoArmadura)
    val tensaoMediaMaxima = bind(BeanTipoEstaca::tensaoMediaMaximaProperty, unitTensaoConcreto)

    val tipoPersonalizado: BooleanProperty = SimpleBooleanProperty(isPersonalizado())

    init {
        tipo.onChange {
            tipoPersonalizado.value = isPersonalizado()
        }
    }

    private fun isPersonalizado(): Boolean = (tipo.value == ESTACA_PERSONALIZADA || tipo.value == TUBULAO_PERSONALIZADO)
}
