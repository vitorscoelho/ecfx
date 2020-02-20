package vitorscoelho.ecfx.gui.model

import javafx.beans.property.ObjectProperty
import tornadofx.ItemViewModel
import tornadofx.onChange
import vitorscoelho.ecfx.gui.WithDescriptionsEcfx
import vitorscoelho.utils.measure.*
import vitorscoelho.utils.tfx.booleanProp
import vitorscoelho.utils.tfxmeasure.bind
import javax.measure.Unit
import javax.measure.quantity.Length
import javax.measure.quantity.Pressure

class BeanDadosDaFundacao {
    val armaduraIntegralProperty = booleanProp(name = "tipoestaca.armaduraIntegral", value = false)
    val comprimentoMinimoArmaduraProperty = lengthProp(
        name = "tipoestaca.comprimentoMinimoArmadura", value = 3, unit = METRE
    )
    val tensaoMediaMaximaProperty = pressureProp(name = "tipoestaca.tensaoMediaMaxima", value = 5, unit = MEGAPASCAL)
    val cobrimentoProperty = lengthProp(name = "estaca.cobrimento", value = 3, unit = CENTIMETRE)
    val diametroFusteProperty = lengthProp(name = "estaca.diametroFuste", value = 120, unit = CENTIMETRE)
    val diametroBaseProperty = lengthProp(name = "estaca.diametroBase", value = 220, unit = CENTIMETRE)
    val profundidadeProperty = lengthProp(name = "estaca.profundidade", value = 8, unit = METRE)
}

class BeanDadosDaFundacaoModel(
    unitCobrimento: ObjectProperty<Unit<Length>>,
    unitDimensoesEstaca: ObjectProperty<Unit<Length>>,
    unitProfundidade: ObjectProperty<Unit<Length>>,
    unitResistenciaMaterial: ObjectProperty<Unit<Pressure>>
) : ItemViewModel<BeanDadosDaFundacao>(initialValue = BeanDadosDaFundacao()),
    WithDescriptionsEcfx {
    constructor(unidades: BeanUnidades) : this(
        unitCobrimento = unidades.unitCobrimentoProperty,
        unitDimensoesEstaca = unidades.unitDimensoesEstacaProperty,
        unitProfundidade = unidades.unitProfundidadeProperty,
        unitResistenciaMaterial = unidades.unitResistenciaMaterialProperty
    )

    val armaduraIntegral = bind(BeanDadosDaFundacao::armaduraIntegralProperty)
    val comprimentoMinimoArmadura = bind(BeanDadosDaFundacao::comprimentoMinimoArmaduraProperty, unitProfundidade)
    val tensaoMediaMaxima = bind(BeanDadosDaFundacao::tensaoMediaMaximaProperty, unitResistenciaMaterial)
    val cobrimento = bind(BeanDadosDaFundacao::cobrimentoProperty, unitCobrimento)
    val diametroFuste = bind(BeanDadosDaFundacao::diametroFusteProperty, unitDimensoesEstaca)
    val diametroBase = bind(BeanDadosDaFundacao::diametroBaseProperty, unitDimensoesEstaca)
    val profundidade = bind(BeanDadosDaFundacao::profundidadeProperty, unitProfundidade)

    init {
        armaduraIntegral.onChange { integral ->
            if (integral!!) {
                comprimentoMinimoArmadura.bind(profundidade)
                tensaoMediaMaxima.value = pressureOf(value = 0, unit = tensaoMediaMaxima.value.unit)
            } else {
                comprimentoMinimoArmadura.unbind()
            }
        }
    }
}