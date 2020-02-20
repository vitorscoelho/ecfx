package vitorscoelho.utils.measure

import tech.units.indriya.format.SimpleUnitFormat
import tech.units.indriya.function.MultiplyConverter
import tech.units.indriya.unit.TransformedUnit
import javax.measure.Unit
import tech.units.indriya.unit.Units
import javax.measure.MetricPrefix.*
import javax.measure.quantity.*

val NEWTON: Unit<Force>
    get() = Units.NEWTON

val PASCAL: Unit<Pressure>
    get() = Units.PASCAL

val RADIAN: Unit<Angle>
    get() = Units.RADIAN

val METRE: Unit<Length>
    get() = Units.METRE

val SQUARE_METRE: Unit<Area>
    get() = Units.SQUARE_METRE

val CUBIC_METRE: Unit<Volume>
    get() = Units.CUBIC_METRE

val LITRE: Unit<Volume>
    get() = Units.LITRE

val KILOGRAM: Unit<Mass>
    get() = Units.KILOGRAM

val DEGREE: Unit<Angle> = TransformedUnit<Angle>(
    Units.RADIAN,
    MultiplyConverter.ofPiExponent(1).concatenate(MultiplyConverter.ofRational(1, 180))
).also { SimpleUnitFormat.getInstance().label(it, "°") }

val KILOGRAM_FORCE = KILO(NEWTON).divide(100).also { SimpleUnitFormat.getInstance().label(it, "kgf") }
val TON_FORCE = KILOGRAM_FORCE.multiply(1_000).also { SimpleUnitFormat.getInstance().label(it, "tf") }

val MILLIMETRE: Unit<Length> = MILLI(METRE)
val CENTIMETRE: Unit<Length> = CENTI(METRE)

val SQUARE_MILLIMETRE: Unit<Area> = MILLIMETRE.multiply(MILLIMETRE).asType(Area::class.java)
val SQUARE_CENTIMETRE: Unit<Area> = CENTIMETRE.multiply(CENTIMETRE).asType(Area::class.java)

val CUBIC_MILLIMETRE: Unit<Volume> = SQUARE_MILLIMETRE.multiply(MILLIMETRE).asType(Volume::class.java)
val CUBIC_CENTIMETRE: Unit<Volume> = SQUARE_CENTIMETRE.multiply(CENTIMETRE).asType(Volume::class.java)

val KILONEWTON: Unit<Force> = KILO(NEWTON)
val MEGANEWTON: Unit<Force> = MEGA(NEWTON)

val KILOPASCAL: Unit<Pressure> = KILO(PASCAL)
val MEGAPASCAL: Unit<Pressure> = MEGA(PASCAL)
val GIGAPASCAL: Unit<Pressure> = GIGA(PASCAL)

val KILONEWTON_METRE: Unit<Moment> = KILONEWTON.multiply(METRE).asMoment()

