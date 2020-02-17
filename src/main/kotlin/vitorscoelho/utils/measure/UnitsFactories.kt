package vitorscoelho.measure

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javax.measure.Quantity
import javax.measure.Unit

/*
companion object {
        val unitDimensoesPadrao = CENTI(METRE)
        val unitEsforcoNormalPadrao = KILO(NEWTON)
        val unitEsforcoMomentoPadrao = KILO(NEWTON).multiply(METRE).asType(Moment::class.java)
        val unitModuloReacaoPadrao = MEGA(PASCAL).divide(METRE).asType(SpringStiffnessPerUnitArea::class.java)
        val unitDeformacoesPadrao = CENTI(METRE)
        val unitRotacoesPadrao = RADIAN
    }
 */

fun <T : Quantity<T>> unitProp(unit: Unit<T>): ObjectProperty<Unit<T>> = SimpleObjectProperty(unit)