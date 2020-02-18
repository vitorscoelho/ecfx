package vitorscoelho.ecfx.gui.model

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleDoubleProperty
import tornadofx.ItemViewModel
import vitorscoelho.ecfx.gui.WithDescriptionsEcfx
import vitorscoelho.utils.measure.forceProp
import vitorscoelho.utils.measure.momentProp
import vitorscoelho.utils.tfxmeasure.bind
import javax.measure.Unit
import javax.measure.quantity.Force
import javax.measure.quantity.Moment

class BeanCargasNoTopo {
    val normalProperty = forceProp(name = "esforco.normal", value = 0.0)
    val forcaHorizontalProperty = forceProp(name = "esforco.horizontal", value = 0.0)
    val momentoProperty = momentProp(name = "esforco.momento", value = 0.0)
    val gamaNProperty = SimpleDoubleProperty(null, "esforco.gamaN").apply { value = 0.0 }
}

class BeanCargasNoTopoModel(
    unitForca: ObjectProperty<Unit<Force>>,
    unitMomento: ObjectProperty<Unit<Moment>>
) : ItemViewModel<BeanCargasNoTopo>(initialValue = BeanCargasNoTopo()), WithDescriptionsEcfx {
    val normal = bind(BeanCargasNoTopo::normalProperty, unitForca)
    val forcaHorizontal = bind(BeanCargasNoTopo::forcaHorizontalProperty, unitForca)
    val momento = bind(BeanCargasNoTopo::momentoProperty, unitMomento)
    val gamaN = bind(BeanCargasNoTopo::gamaNProperty)
}