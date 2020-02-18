package vitorscoelho.utils.tfx

import javafx.beans.DefaultProperty
import javafx.beans.property.Property
import javafx.collections.ObservableList
import javafx.event.EventTarget
import javafx.geometry.Orientation
import javafx.scene.Node
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.geometry.Orientation.HORIZONTAL
import javafx.scene.control.ComboBox
import javafx.scene.control.Control
import javafx.scene.control.Tooltip
import tornadofx.*

abstract class InputFields<T, S : Control>(
    text: String?, forceLabelIndent: Boolean, orientation: Orientation, val property: Property<T>, val control: S
) : AbstractField(text, forceLabelIndent) {
    final override val inputContainer = if (orientation == HORIZONTAL) HBox() else VBox()
    override val inputs: ObservableList<Node> = inputContainer.children
    var value by property
    val descriptions: Descriptions?
        get() {
            val viewModel: ViewModel = property.viewModel ?: return null
            if (viewModel !is WithDescriptions) return null
            return viewModel.descriptions
        }

    init {
        inputContainer.addClass(Stylesheet.inputContainer)
        inputContainer.addPseudoClass(orientation.name.toLowerCase())
        children.add(inputContainer)
        inputContainer.add(control)
        colocarTitulo()
        adicionarTooltipDescricao()
        /*
        // Register/deregister with parent Fieldset
        parentProperty().addListener { _, oldParent, newParent ->
            ((oldParent as? Fieldset) ?: oldParent?.findParent<Fieldset>())?.fields?.remove(this)
            ((newParent as? Fieldset) ?: newParent?.findParent<Fieldset>())?.fields?.add(this)
        }
         */
    }

    protected open fun adicionarTooltipDescricao() {
        if (descriptions != null) {
            control.tooltip = Tooltip(descriptions!!.description(this.property.name)).apply {
                showDelay = descriptions!!.tooltipShowDelay
            }
        }
    }

    protected open fun colocarTitulo() {
        if (descriptions != null) {
            val nome = descriptions!!.name(property.name)
            this.text = nome
        }
    }
}

@DefaultProperty("inputs")
class ComboBoxField<T> internal constructor(
    text: String?,
    orientation: Orientation,
    forceLabelIndent: Boolean,
    property: Property<T>,
    values: ObservableList<T>? = null
) : InputFields<T, ComboBox<T>>(text, forceLabelIndent, orientation, property,ComboBox<T>()) {
    init {
        if (values != null) control.items = values
        control.bind(property,false)
//        control.bindSelected(property)
    }
}

fun <T> EventTarget.comboboxField(
    property: Property<T>,
    values: ObservableList<T>? = null,
    orientation: Orientation = HORIZONTAL,
    forceLabelIndent: Boolean = false,
    op: ComboBoxField<T>.() -> Unit = {}
): ComboBoxField<T> {
    val field = ComboBoxField<T>(
        text = null,
        orientation = orientation,
        forceLabelIndent = forceLabelIndent,
        property = property,
        values = values
    )
    opcr(this, field) {}
    op(field)
    return field
}