package vitorscoelho.utils.tfx

import javafx.beans.property.*
import javafx.scene.layout.Region

fun Region.setMinMaxPrefWidth(value: Double) {
    prefWidth = value; minWidth = value; maxWidth = value
}

fun Region.setMinMaxPrefHeight(value: Double) {
    prefHeight = value; minHeight = value; maxHeight = value
}

fun doubleProp(name: String? = null, value: Double): DoubleProperty = SimpleDoubleProperty(
    null, name
).apply { this.value = value }

fun intProp(name: String? = null, value: Int): IntegerProperty = SimpleIntegerProperty(
    null, name
).apply { this.value = value }

fun booleanProp(name: String? = null, value: Boolean): BooleanProperty = SimpleBooleanProperty(
    null, name
).apply { this.value = value }

fun <T> objectProp(name: String? = null, value: T): ObjectProperty<T> = SimpleObjectProperty<T>(
    null, name
).apply { this.value = value }

fun <T : Any> T?.toProperty(name: String? = null) = SimpleObjectProperty<T>(null,name,this)