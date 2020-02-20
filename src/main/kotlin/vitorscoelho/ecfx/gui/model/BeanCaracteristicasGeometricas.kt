package vitorscoelho.ecfx.gui.model

import javafx.beans.property.ObjectProperty
import tornadofx.ItemViewModel
import vitorscoelho.ecfx.gui.WithDescriptionsEcfx
import vitorscoelho.utils.measure.CENTIMETRE
import vitorscoelho.utils.measure.METRE
import vitorscoelho.utils.measure.lengthProp
import vitorscoelho.utils.tfxmeasure.bind
import javax.measure.Unit
import javax.measure.quantity.Length

class BeanCaracteristicasGeometricas {
    val cobrimentoProperty = lengthProp(name = "estaca.cobrimento", value = 3, unit = CENTIMETRE)
    val diametroFusteProperty = lengthProp(name = "estaca.diametroFuste", value = 120, unit = CENTIMETRE)
    val diametroBaseProperty = lengthProp(name = "estaca.diametroBase", value = 220, unit = CENTIMETRE)
    val alturaBaseProperty = lengthProp(name = "estaca.alturaBase", value = 110, unit = CENTIMETRE)
    val rodapeProperty = lengthProp(name = "estaca.rodape", value = 20, unit = CENTIMETRE)
    val profundidadeProperty = lengthProp(name = "estaca.profundidade", value = 8, unit = METRE)
}

class BeanCaracteristicasGeometricasModel(
    unitCobrimento: ObjectProperty<Unit<Length>>,
    unitDimensoesEstaca: ObjectProperty<Unit<Length>>,
    unitProfundidade: ObjectProperty<Unit<Length>>
) : ItemViewModel<BeanCaracteristicasGeometricas>(initialValue = BeanCaracteristicasGeometricas()),
    WithDescriptionsEcfx {
    constructor(unidades: BeanUnidades) : this(
        unitCobrimento = unidades.unitCobrimento,
        unitDimensoesEstaca = unidades.unitDimensoesEstaca,
        unitProfundidade = unidades.unitProfundidade
    )

    val cobrimento = bind(BeanCaracteristicasGeometricas::cobrimentoProperty, unitCobrimento)
    val diametroFuste = bind(BeanCaracteristicasGeometricas::diametroFusteProperty, unitDimensoesEstaca)
    val diametroBase = bind(BeanCaracteristicasGeometricas::diametroBaseProperty, unitDimensoesEstaca)
    val alturaBase = bind(BeanCaracteristicasGeometricas::alturaBaseProperty, unitDimensoesEstaca)
    val rodape = bind(BeanCaracteristicasGeometricas::rodapeProperty, unitDimensoesEstaca)
    val profundidade = bind(BeanCaracteristicasGeometricas::profundidadeProperty, unitProfundidade)
}