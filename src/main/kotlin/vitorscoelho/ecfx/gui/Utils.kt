package vitorscoelho.ecfx.gui

import javafx.util.Duration
import tech.units.indriya.quantity.Quantities
import vitorscoelho.utils.tfx.Descriptions
import vitorscoelho.utils.tfx.DescriptionsWithResource
import vitorscoelho.utils.tfx.QuantityFactory
import vitorscoelho.utils.tfx.WithDescriptions
import java.util.*
import javax.measure.Quantity
import javax.measure.Unit

internal val descricoes = DescriptionsWithResource(
    rb = ResourceBundle.getBundle("vitorscoelho.ecfx.gui.Textos"),
    nameSuffix = "nome", descriptionSuffix = "descricao",
    tooltipShowDelay = Duration(200.0)
)

internal interface WithDescriptionsEcfx : WithDescriptions {
    override val descriptions: Descriptions
        get() = descricoes
}

val quantityFactory = object : QuantityFactory {
    override fun <T : Quantity<T>> getQuantity(value: Number, unit: Unit<T>): Quantity<T> {
        return Quantities.getQuantity(value, unit)
    }
}