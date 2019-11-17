package vitorscoelho.ecfx.utils

import tech.units.indriya.AbstractUnit
import tech.units.indriya.ComparableQuantity
import tech.units.indriya.quantity.Quantities
import tech.units.indriya.unit.ProductUnit
import tech.units.indriya.unit.Units
import tech.units.indriya.unit.Units.*
import java.lang.IllegalArgumentException
import javax.measure.MetricPrefix
import javax.measure.Quantity
import javax.measure.Unit
import javax.measure.quantity.Force
import javax.measure.quantity.Pressure

interface Moment : Quantity<Moment>
interface SpringStiffness : Quantity<SpringStiffness>
interface SpringStiffnessPerUnitLength : Quantity<SpringStiffnessPerUnitLength>
interface SpringStiffnessPerUnitArea : Quantity<SpringStiffnessPerUnitArea>
interface ForcePerUnitLength : Quantity<ForcePerUnitLength>

internal fun inicializarUnidadesDeMedidaExtras() {
    adicionarViaReflection(ProductUnit<Moment>(NEWTON.multiply(METRE)), Moment::class.java)
    adicionarViaReflection(ProductUnit<SpringStiffness>(NEWTON.divide(METRE)), SpringStiffness::class.java)
    adicionarViaReflection(
        ProductUnit<SpringStiffnessPerUnitLength>(NEWTON.divide(METRE).divide(METRE)),
        SpringStiffnessPerUnitLength::class.java
    )
    adicionarViaReflection(
        ProductUnit<SpringStiffnessPerUnitArea>(NEWTON.divide(SQUARE_METRE).divide(METRE)),
        SpringStiffnessPerUnitArea::class.java
    )
    adicionarViaReflection(ProductUnit<ForcePerUnitLength>(NEWTON.divide(METRE)), ForcePerUnitLength::class.java)
}

private fun <U : AbstractUnit<*>> adicionarViaReflection(unit: U, type: Class<out Quantity<*>>) {
    val instanciaUnits = Units.getInstance()
    val metodoAddUnits = Units::class.java.getDeclaredMethod("addUnit", AbstractUnit::class.java, Class::class.java)
    metodoAddUnits.isAccessible = true
    metodoAddUnits.invoke(instanciaUnits, unit, type)
}

val QUILONEWTON: Unit<Force> = MetricPrefix.KILO(NEWTON)
val MEGANEWTON: Unit<Force> = MetricPrefix.MEGA(NEWTON)
val QUILOGRAMA_FORCA: Unit<Force> = QUILONEWTON.multiply(100)
val TONELADA_FORCA: Unit<Force> = QUILONEWTON.divide(10)

val QUILONEWTON_POR_CENTIMETRO_QUADRADO: Unit<Pressure> =
    QUILONEWTON.divide(MetricPrefix.CENTI(SQUARE_METRE)).asType(Pressure::class.java)

fun getTextoUnidade(unidade: Unit<*>): String {
    return unidade.toString()
        .replace("kN*100", "kgf")
        .replace("kN/10", "tf")
        .replace("one", "")
}