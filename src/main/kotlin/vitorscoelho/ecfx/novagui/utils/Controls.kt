package vitorscoelho.ecfx.novagui.utils

import javafx.event.EventTarget
import javafx.geometry.Orientation
import javafx.scene.control.*
import javafx.util.Duration
import tornadofx.*

var TOOLTIP_SHOW_DELAY: Duration = Duration(1000.0)

fun Control.adicionarTooltip(description: String) {
    if (description.isNotBlank()) {
        tooltip = Tooltip(description).apply {
            showDelay = TOOLTIP_SHOW_DELAY
        }
    }
}

fun <T> EventTarget.textfield(
    property: TextGuiProp<T>,
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
    focusedProperty().onChange { noFoco -> if (!noFoco) property.reformatText() }
    op(this)
}

fun <T> EventTarget.textfield(
    property: TextGuiProp<T>,
    validationContext: ValidationContext,
    trigger: ValidationTrigger = ValidationTrigger.OnChange(),
    op: TextField.() -> Unit = {}
) = textfield(property = property).apply {
    if (property is WithTextInputValidator) {
        validationContext.addValidator(node = this, trigger = trigger, validator = property.validator)
    }
    op(this)
}

fun <T> EventTarget.fieldTextField(
    property: TextGuiProp<T>,
    orientation: Orientation = Orientation.HORIZONTAL,
    forceLabelIndent: Boolean = false,
    validationContext: ValidationContext? = null,
    trigger: ValidationTrigger = ValidationTrigger.OnChange(),
    op: Field.() -> Unit = {}
): Field {
    val text: String = property.label ?: ""
    val field = Field(text, orientation, forceLabelIndent)
    if (property is QuantityTextGuiProp<*>) {
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

fun EventTarget.checkbox(
    property: BooleanGuiProp,
    op: CheckBox.() -> Unit = {}
) = checkbox(property = property.transitional).apply {
    text = property.label ?: ""
    adicionarTooltip(description = property.description ?: "")
    op(this)
}

fun EventTarget.fieldCheckbox(
    property: BooleanGuiProp,
    orientation: Orientation = Orientation.HORIZONTAL,
    forceLabelIndent: Boolean = false,
    op: Field.() -> Unit = {}
): Field {
    val field = Field(orientation = orientation, forceLabelIndent = forceLabelIndent)
    field += checkbox(property = property)
    opcr(this, field) {}
    op(field)
    return field
}

fun <T> EventTarget.combobox(
    property: ObjectGuiProp<T>,
    values: List<T>? = null,
    op: ComboBox<T>.() -> Unit = {}
) = combobox(property = property.transitional, values = values).apply {
    adicionarTooltip(description = property.description ?: "")
    op(this)
}

fun <T> EventTarget.fieldCombobox(
    property: ObjectGuiProp<T>,
    values: List<T>? = null,
    orientation: Orientation = Orientation.HORIZONTAL,
    forceLabelIndent: Boolean = false,
    op: Field.() -> Unit = {}
): Field {
    val text: String = property.label ?: ""
    val field = Field(text = text, orientation = orientation, forceLabelIndent = forceLabelIndent)
    field += combobox(property = property, values = values)
    opcr(this, field) {}
    op(field)
    return field
}