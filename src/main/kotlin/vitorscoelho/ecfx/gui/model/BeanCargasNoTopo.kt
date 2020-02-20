package vitorscoelho.ecfx.gui.model

import javafx.beans.property.ObjectProperty
import tornadofx.ItemViewModel
import vitorscoelho.ecfx.gui.WithDescriptionsEcfx
import vitorscoelho.utils.measure.KILONEWTON
import vitorscoelho.utils.measure.KILONEWTON_METRE
import vitorscoelho.utils.measure.forceProp
import vitorscoelho.utils.measure.momentProp
import vitorscoelho.utils.tfx.doubleProp
import vitorscoelho.utils.tfxmeasure.bind
import javax.measure.Unit
import javax.measure.quantity.Force
import javax.measure.quantity.Moment

class BeanCargasNoTopo {
    val normalProperty = forceProp(name = "esforco.normal", value = 200, unit = KILONEWTON)
    val forcaHorizontalProperty = forceProp(name = "esforco.horizontal", value = 30, unit = KILONEWTON)
    val momentoProperty = momentProp(name = "esforco.momento", value = 3, unit = KILONEWTON_METRE)
    val gamaNProperty = doubleProp(name = "esforco.gamaN", value = 1.4)
}

class BeanCargasNoTopoModel(
    unitForca: ObjectProperty<Unit<Force>>,
    unitMomento: ObjectProperty<Unit<Moment>>
) : ItemViewModel<BeanCargasNoTopo>(initialValue = BeanCargasNoTopo()), WithDescriptionsEcfx {
    constructor(unidades: BeanUnidades) : this(
        unitForca = unidades.unitForcaProperty,
        unitMomento = unidades.unitMomentoProperty
    )

    val normal = bind(BeanCargasNoTopo::normalProperty, unitForca)
    val forcaHorizontal = bind(BeanCargasNoTopo::forcaHorizontalProperty, unitForca)
    val momento = bind(BeanCargasNoTopo::momentoProperty, unitMomento)
    val gamaN = bind(BeanCargasNoTopo::gamaNProperty)
}