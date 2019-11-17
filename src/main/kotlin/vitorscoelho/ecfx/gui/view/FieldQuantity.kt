package vitorscoelho.ecfx.gui.view

import javafx.beans.binding.Bindings
import javafx.event.EventTarget
import javafx.scene.control.TextField
import javafx.scene.control.Tooltip
import javafx.util.StringConverter
import tornadofx.*
import vitorscoelho.ecfx.gui.estilo.DELAY_TOOLTIP
import vitorscoelho.ecfx.gui.model.QuantityProperty
import vitorscoelho.ecfx.utils.getTextoUnidade
import java.text.DecimalFormat
import javax.measure.Quantity
import javax.measure.Unit

fun <T : Quantity<T>> EventTarget.fieldQuantity(
    property: QuantityProperty<T>,
    op: Field.(tf: TextField) -> kotlin.Unit = { }
): Field {
    return this.field {
        textProperty.bind(
            Bindings.createStringBinding(
                {
                    val unidade: String = property.unitTextProperty.value
                    var retorno: String = property.nome
                    if (!unidade.isBlank()) retorno += " ($unidade)"
                    return@createStringBinding retorno
                },
                arrayOf(property.unitTextProperty)
            )
        )
        val tf = textfield(property = property.magnitudeProperty) {
//            bind(property = property.magnitudeProperty, converter = stringConverterTextField)
            tooltip = Tooltip(property.descricao)
            tooltip.showDelay = DELAY_TOOLTIP
        }
        op(this, tf)
    }
}

//private val stringConverterTextField: StringConverter<Number> = object : StringConverter<Number>() {
//    override fun toString(valor: Number?): String? {
//        if (valor == null) return null
//        return valor.toString()
//    }
//
//    override fun fromString(valor: String?): Double? {
//        if (valor == null) return 0.0
//        return valor.toDouble()
//    }
//}