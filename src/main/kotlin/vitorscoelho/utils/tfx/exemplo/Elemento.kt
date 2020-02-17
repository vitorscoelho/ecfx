package vitorscoelho.tfxutils.exemplo

import javafx.beans.property.*
import javafx.beans.value.ObservableValue
import javafx.scene.Node
import javafx.scene.control.TextField
import javafx.util.Duration
import tornadofx.*
import tornadofx.getValue
import tornadofx.setValue
import vitorscoelho.tfxutils.Descriptions
import vitorscoelho.tfxutils.DescriptionsWithResource
import vitorscoelho.tfxutils.ERROR_IF_NOT_POSITIVE_DOUBLE
import vitorscoelho.tfxutils.WithDescriptions
import java.util.*
import javax.measure.Quantity
import javax.measure.quantity.Area
import javax.measure.quantity.Length

internal class BeanElemento {
    val textoProperty = SimpleStringProperty(null, "elemento.texto")
    var texto by textoProperty
    val inteiroProperty = SimpleIntegerProperty(null, "elemento.inteiro")
    var inteiro by inteiroProperty
    val realProperty = SimpleDoubleProperty(null, "elemento.real")
    var real by realProperty
    val quantityInteiroProperty = SimpleObjectProperty<Quantity<Length>>(null, "elemento.inteiro_quantity")
    var quantityInteiro by quantityInteiroProperty
    val quantityRealProperty = SimpleObjectProperty<Quantity<Area>>(null, "elemento.real_quantity")
    var quantityReal by quantityRealProperty
}

internal class BeanElementoModel(item: BeanElemento) : ItemViewModel<BeanElemento>(initialValue = item),
    WithDescriptions {
    val texto = bind(BeanElemento::textoProperty)
    val inteiro = bind(BeanElemento::inteiroProperty)
    val real = bind(BeanElemento::realProperty)
    val quantityInteiro = bind(BeanElemento::quantityInteiroProperty)
    val quantityReal = bind(BeanElemento::quantityRealProperty)

    override val descriptions = DescriptionsWithResource(
        rb = ResourceBundle.getBundle("vitorscoelho.tfxutils.Textos"),
        nameSuffix = "nome",
        descriptionSuffix = "descricao",
        tooltipShowDelay = Duration(100.0)
    )

    init {
        println(texto.viewModel)
        texto.addValidator { ERROR_IF_NOT_POSITIVE_DOUBLE.invoke(validationContext, "") }
    }

    override fun toString(): String {
        return "OITTJ"
    }
}

val NODE_QUALQUER = TextField()
inline fun <reified T> ViewModel.addValidator(
    property: ObservableValue<T>,
    trigger: ValidationTrigger = ValidationTrigger.OnChange(),
    noinline validator: ValidationContext.(T?) -> ValidationMessage?
) = addValidator(node = NODE_QUALQUER, property = property, trigger = trigger, validator = validator)

fun Property<*>.addValidator(
    validator: ValidationContext.(String?) -> ValidationMessage?
) {
    if (this.viewModel == null) throw NullPointerException("Property fora de uma ViewModel. Não possui um validationContext")

}
