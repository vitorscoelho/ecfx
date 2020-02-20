package vitorscoelho.ecfx.gui

import javafx.util.Duration
import vitorscoelho.utils.tfx.Descriptions
import vitorscoelho.utils.tfx.DescriptionsWithResource
import vitorscoelho.utils.tfx.WithDescriptions
import java.util.*

internal val descricoes = DescriptionsWithResource(
    rb = ResourceBundle.getBundle("vitorscoelho.ecfx.gui.Textos"),
    nameSuffix = "nome", descriptionSuffix = "descricao",
    tooltipShowDelay = Duration(200.0)
)

internal interface WithDescriptionsEcfx : WithDescriptions {
    override val descriptions: Descriptions
        get() = descricoes
}