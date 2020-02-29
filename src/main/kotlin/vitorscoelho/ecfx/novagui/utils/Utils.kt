package vitorscoelho.ecfx.novagui.utils

import javafx.beans.value.ObservableValue

/**
 * A função [op] é invocada imediatamente (com o value inicial de [this]) e, também, toda vez que [this] mudar
 */
fun <T> ObservableValue<T>.doNowAndOnChange(op: (T?) -> Unit) = apply {
    op(this.value)
    addListener { o, oldValue, newValue -> op(newValue) }
}