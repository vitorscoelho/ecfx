package vitorscoelho.utils.measure

import javax.measure.Quantity
import javax.measure.Unit
import javax.measure.UnitConverter

private val NEWTON_METRO = NEWTON.multiply(METRE)
private val NEWTON_POR_METRO = NEWTON.divide(METRE)
private val NEWTON_POR_METRO_QUADRADO = NEWTON.divide(SQUARE_METRE)
private val NEWTON_POR_METRO_CUBICO = NEWTON.divide(CUBIC_METRE)

private val KILONEWTON_CENTIMETRO = KILONEWTON.multiply(CENTIMETRE)
private val KILONEWTON_POR_CENTIMETRO = KILONEWTON.divide(CENTIMETRE)
private val KILONEWTON_POR_CENTIMETRO_QUADRADO = KILONEWTON.divide(SQUARE_CENTIMETRE)
private val KILONEWTON_POR_CENTIMETRO_CUBICO = KILONEWTON.divide(CUBIC_CENTIMETRE)

private val converterAngle = RADIAN.getConverterTo(DEGREE)
private val converterArea = SQUARE_METRE.getConverterTo(SQUARE_CENTIMETRE)
private val converterForce = NEWTON.getConverterTo(KILONEWTON)
private val converterLength = METRE.getConverterTo(CENTIMETRE)
private val converterMass = KILOGRAM.getConverterTo(KILOGRAM)
private val converterPressure = PASCAL.getConverterTo(KILONEWTON_POR_CENTIMETRO_QUADRADO.asPressure())
private val converterVolume = CUBIC_METRE.getConverterTo(CUBIC_CENTIMETRE)
private val converterMoment = NEWTON_METRO.asMoment().getConverterTo(KILONEWTON_CENTIMETRO.asMoment())
private val converterSpringStiffness = NEWTON_POR_METRO.asSpringStiffness()
    .getConverterTo(KILONEWTON_POR_CENTIMETRO.asSpringStiffness())
private val converterSpringStiffnessPerUnitLength = NEWTON_POR_METRO_QUADRADO.asSpringStiffnessPerUnitLength()
    .getConverterTo(KILONEWTON_POR_CENTIMETRO_QUADRADO.asSpringStiffnessPerUnitLength())
private val converterSpringStiffnessPerUnitArea = NEWTON_POR_METRO_CUBICO.asSpringStiffnessPerUnitArea()
    .getConverterTo(KILONEWTON_POR_CENTIMETRO_CUBICO.asSpringStiffnessPerUnitArea())
private val converterForcePerUnitLength = NEWTON_POR_METRO.asForcePerUnitLength()
    .getConverterTo(KILONEWTON_POR_CENTIMETRO.asForcePerUnitLength())
private val converterSpecificWeight = NEWTON_POR_METRO_CUBICO.asSpecificWeight()
    .getConverterTo(KILONEWTON_POR_CENTIMETRO_CUBICO.asSpecificWeight())

private val mapUnits: Map<Unit<*>, Unit<*>> = mapOf(
    RADIAN to DEGREE,
    SQUARE_METRE to SQUARE_CENTIMETRE,
    NEWTON to KILONEWTON,
    METRE to CENTIMETRE,
    KILOGRAM to KILOGRAM,
    PASCAL to KILONEWTON_POR_CENTIMETRO_QUADRADO,
    CUBIC_METRE to CUBIC_CENTIMETRE,
    NEWTON_METRO to KILONEWTON_CENTIMETRO,
    NEWTON_POR_METRO to KILONEWTON_POR_CENTIMETRO,
    NEWTON_POR_METRO_QUADRADO to KILONEWTON_POR_CENTIMETRO_QUADRADO,
    NEWTON_POR_METRO_CUBICO to KILONEWTON_POR_CENTIMETRO_CUBICO,
    NEWTON_POR_METRO to KILONEWTON_POR_CENTIMETRO,
    NEWTON_POR_METRO_CUBICO to KILONEWTON_POR_CENTIMETRO_CUBICO
)

private val map: Map<Unit<*>, UnitConverter> = mapOf(
    RADIAN to converterAngle,
    SQUARE_METRE to converterArea,
    NEWTON to converterForce,
    METRE to converterLength,
    KILOGRAM to converterMass,
    PASCAL to converterPressure,
    CUBIC_METRE to converterVolume,
    NEWTON_METRO to converterMoment,
    NEWTON_POR_METRO to converterSpringStiffness,
    NEWTON_POR_METRO_QUADRADO to converterSpringStiffnessPerUnitLength,
    NEWTON_POR_METRO_CUBICO to converterSpringStiffnessPerUnitArea,
    NEWTON_POR_METRO to converterForcePerUnitLength,
    NEWTON_POR_METRO_CUBICO to converterSpecificWeight
)

fun <T : Quantity<T>> Unit<T>.toSU(): Unit<T> {
    val unitSystemUnit = this.systemUnit
    val unitSU = mapUnits[unitSystemUnit] ?: throw IllegalArgumentException(
        "Não existe unidade alternativa para $this"
    )
    return unitSU as Unit<T>
}

fun <T : Quantity<T>> Quantity<T>.toDoubleSU(): Double {
    val qtdSystemUnit = this.toSystemUnit()
    val converter: UnitConverter = map[qtdSystemUnit.unit] ?: throw IllegalArgumentException(
        "Não existe unidade alternativa para ${qtdSystemUnit.unit}"
    )
    return converter.convert(qtdSystemUnit.value).toDouble()
}