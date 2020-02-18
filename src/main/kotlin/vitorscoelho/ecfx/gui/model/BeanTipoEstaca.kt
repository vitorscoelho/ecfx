package vitorscoelho.ecfx.gui.model

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.ItemViewModel
import vitorscoelho.ecfx.gui.WithDescriptionsEcfx
import vitorscoelho.utils.measure.lengthProp
import vitorscoelho.utils.measure.pressureProp
import vitorscoelho.utils.tfxmeasure.bind
import javax.measure.Unit
import javax.measure.quantity.Length
import javax.measure.quantity.Pressure

class BeanTipoEstaca {
    val tipo = SimpleObjectProperty<TipoEstaca>(null, "tipoestaca.tipo").apply { value = ESTACA_PERSONALIZADA }
    val comprimentoMinimoArmaduraProperty = lengthProp(name = "tipoestaca.comprimentoMinimoArmadura", value = 0.0)
    val tensaoMediaMaximaProperty = pressureProp(name = "tipoestaca.tensaoMediaMaxima", value = 0.0)
}

class BeanTipoEstacaModel(
    unitComprimentoArmadura: ObjectProperty<Unit<Length>>,
    unitTensaoConcreto: ObjectProperty<Unit<Pressure>>
) : ItemViewModel<BeanTipoEstaca>(initialValue = BeanTipoEstaca()), WithDescriptionsEcfx {
    val tipo = bind(BeanTipoEstaca::tipo)
    val comprimentoMinimoArmadura = bind(BeanTipoEstaca::comprimentoMinimoArmaduraProperty, unitComprimentoArmadura)
    val tensaoMediaMaxima = bind(BeanTipoEstaca::tensaoMediaMaximaProperty, unitTensaoConcreto)
}
