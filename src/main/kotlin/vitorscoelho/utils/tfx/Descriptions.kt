package vitorscoelho.tfxutils

import javafx.beans.property.Property
import javafx.util.Duration
import tornadofx.get
import java.util.*

interface Descriptions {
    val tooltipShowDelay: Duration
    fun name(id: String): String
    fun description(id: String): String
}

class DescriptionsWithResource(
    val rb: ResourceBundle,
    val nameSuffix: String,
    val descriptionSuffix: String,
    override val tooltipShowDelay: Duration
) : Descriptions {
    override fun name(id: String): String = rb["$id.${nameSuffix}"]
    override fun description(id: String): String = rb["$id.${descriptionSuffix}"]
}

interface WithDescriptions {
    val descriptions: Descriptions
}