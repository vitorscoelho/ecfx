package vitorscoelho.ecfx.gui.model

import javafx.beans.property.ObjectProperty
import tornadofx.ItemViewModel
import vitorscoelho.ecfx.gui.WithDescriptionsEcfx
import vitorscoelho.utils.measure.lengthProp
import vitorscoelho.utils.tfxmeasure.bind
import javax.measure.Unit
import javax.measure.quantity.Length

class BeanCaracteristicasGeometricas {
    val cobrimentoProperty = lengthProp(name = "estaca.cobrimento", value = 0.0)
    val diametroFusteProperty = lengthProp(name = "estaca.diametroFuste", value = 0.0)
    val diametroBaseProperty = lengthProp(name = "estaca.diametroBase", value = 0.0)
    val alturaBaseProperty = lengthProp(name = "estaca.alturaBase", value = 0.0)
    val rodapeProperty = lengthProp(name = "estaca.rodape", value = 0.0)
    val profundidadeProperty = lengthProp(name = "estaca.profundidade", value = 0.0)
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