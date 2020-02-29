package vitorscoelho.ecfx.novagui.utils

import javafx.event.EventTarget
import javafx.geometry.Orientation
import javafx.scene.control.Control
import javafx.scene.control.TextField
import javafx.scene.control.Tooltip
import javafx.util.Duration
import tornadofx.*

var TOOLTIP_SHOW_DELAY: Duration = Duration(1000.0)

private fun Control.adicionarTooltip(description: String) {
    if (description.isNotBlank()) {
        tooltip = Tooltip(description).apply {
            showDelay = TOOLTIP_SHOW_DELAY
        }
    }
}

fun <T> EventTarget.textfield(
    property: GuiProp<T>,
    op: TextField.() -> Unit = {}
) = textfield(property = property.textProp).apply {
    adicionarTooltip(description = property.description ?: "")
    if (property is WithTextInputFilter) {
        filterInput { discriminator ->
            val isValido = property.filterInput(discriminator.controlNewText)
            if (discriminator.isDeleted && !isValido) discriminator.setRange(0, 0)
            isValido
        }
    }
    focusedProperty().onChange { noFoco ->
        if (!noFoco) {
            val double = property.converter.fromString(property.textProp.value)
            val string = property.converter.toString(double)
            property.textProp.value = string
        }
    }
    op(this)
}

fun <T> EventTarget.textfield(
    property: GuiProp<T>,
    validationContext: ValidationContext,
    trigger: ValidationTrigger = ValidationTrigger.OnChange(),
    op: TextField.() -> Unit = {}
) = textfield(property = property).apply {
    if (property is WithTextInputValidator) {
        validationContext.addValidator(node = this, trigger = trigger, validator = property.validator)
    }
    op(this)
}

/**
 * Create a field with the given text and operate on it.
 * @param forceLabelIndent Indent the label even if it's empty, good for aligning buttons etc
 * @orientation Whether to create an HBox (HORIZONTAL) or a VBox (VERTICAL) container for the field content
 * @op Code that will run in the context of the content container (Either HBox or VBox per the orientation)
 *
 * @see buttonbar
 */
fun <T> EventTarget.fieldTextField(
    property: GuiProp<T>,
    orientation: Orientation = Orientation.HORIZONTAL,
    forceLabelIndent: Boolean = false,
    validationContext: ValidationContext? = null,
    trigger: ValidationTrigger = ValidationTrigger.OnChange(),
    op: Field.() -> Unit = {}
): Field {
    val text: String = property.label ?: ""
    val field = Field(text, orientation, forceLabelIndent)
    if (property is GuiQuantityProp<*>) {
        property.unit.doNowAndOnChange { unit ->
            var unidade = unit.toString()
            unidade = if (!unidade.isBlank() && unidade != "one") " ($unidade)" else ""
            field.text = text + unidade
        }
    }
    field += if (validationContext == null) {
        textfield(property = property)
    } else {
        textfield(property = property, validationContext = validationContext, trigger = trigger)
    }
    opcr(this, field) {}
    op(field)
    return field
}