package vitorscoelho.utils.measure

import tech.units.indriya.quantity.Quantities
import javax.measure.MetricPrefix.*
import javax.measure.Quantity
import javax.measure.Unit
import javax.measure.quantity.*
import kotlin.text.StringBuilder

private val medidas = listOf(
    "Acceleration",
    "AmountOfSubstance",
    "Angle",
    "Area",
    "CatalyticActivity",
    "Dimensionless",
    "ElectricCapacitance",
    "ElectricCharge",
    "ElectricConductance",
    "ElectricCurrent",
    "ElectricInductance",
    "ElectricPotential",
    "ElectricResistance",
    "Energy",
    "Force",
    "Frequency",
    "Illuminance",
    "Length",
    "LuminousFlux",
    "LuminousIntensity",
    "MagneticFlux",
    "MagneticFluxDensity",
    "Mass",
    "Power",
    "Pressure",
    "RadiationDoseAbsorbed",
    "RadiationDoseEffective",
    "Radioactivity",
    "SolidAngle",
    "Speed",
    "Temperature",
    "Time",
    "Volume",
    "Moment",
    "SpringStiffness",
    "SpringStiffnessPerUnitLength",
    "SpringStiffnessPerUnitArea",
    "ForcePerUnitLength",
    "SpecificWeight"
)

val unitAngle: Unit<Angle> = RADIAN
val unitLength: Unit<Length> = CENTIMETRE
val unitArea: Unit<Area> = SQUARE_CENTIMETRE
val unitVolume: Unit<Volume> = CUBIC_CENTIMETRE
val unitForce: Unit<Force> = KILO(NEWTON)
val unitMass: Unit<Mass> = KILOGRAM
val unitPressure: Unit<Pressure> = unitForce.divide(unitArea).asType(Pressure::class.java)
val unitMoment: Unit<Moment> = unitForce.multiply(unitLength).asType(Moment::class.java)
val unitSpringStiffness: Unit<SpringStiffness> = unitForce.divide(unitLength).asType(SpringStiffness::class.java)
val unitSpringStiffnessPerUnitLength: Unit<SpringStiffnessPerUnitLength> =
    unitForce.divide(unitLength).divide(unitLength).asType(SpringStiffnessPerUnitLength::class.java)
val unitSpringStiffnessPerUnitArea: Unit<SpringStiffnessPerUnitArea> =
    unitForce.divide(unitArea).divide(unitLength).asType(SpringStiffnessPerUnitArea::class.java)
val unitForcePerUnitLength: Unit<ForcePerUnitLength> =
    unitForce.divide(unitLength).asType(ForcePerUnitLength::class.java)
val unitSpecificWeight: Unit<SpecificWeight> = unitForce.divide(unitVolume).asType(SpecificWeight::class.java)

private val sistemaDeUnidadesAlternativo = listOf(
    "Angle",
    "Area",
    "Force",
    "Length",
    "Mass",
    "Pressure",
    "Volume",
    "Moment",
    "SpringStiffness",
    "SpringStiffnessPerUnitLength",
    "SpringStiffnessPerUnitArea",
    "ForcePerUnitLength",
    "SpecificWeight"
)

internal fun nomeMinusculo(nome: String): String {
    val primeiraLetra = nome.first().toLowerCase()
    return primeiraLetra + nome.drop(1)
}

internal fun criarMapSistemaDeUnidadesAlternativo():String {
    return with(StringBuilder()) {
        appendln("private val mapSistemaDeUnidadesAlternativo: Map<Class<out Quantity<*>>, Unit<*>> = mapOf(")
        sistemaDeUnidadesAlternativo.forEach { nome ->
            appendln("$nome::class.java to unit$nome,")
        }
        deleteCharAt(lastIndex)
        deleteCharAt(lastIndex)
        appendln()
        appendln(")")
    }.toString()
}

internal fun criar(nome: String): String {
    val nomeMinusculo: String = nomeMinusculo(nome = nome)
    val funcaoDouble = if (sistemaDeUnidadesAlternativo.contains(nome)) {
        """
            @JvmName("toDoubleSU$nome")
            fun Quantity<$nome>.toDoubleSU():Double = this.to(unit$nome).value.toDouble()
            @JvmName("propToDoubleSu${nome}")
            fun Property<Quantity<$nome>>.toDoubleSU():Double = this.value.toDoubleSU()
            fun Number.to${nome}SU():Quantity<$nome> = criarQuantity(this,unit$nome)
            fun ${nomeMinusculo}SUOf(value:Number):Quantity<$nome> = criarQuantity(value,unit$nome)
            fun ${nomeMinusculo}SUOf(value:Number, toUnit:Unit<$nome>):Quantity<$nome> = criarQuantity(value,unit$nome).to(toUnit)
        """.trimIndent()
    } else {
        ""
    }
    return """
        $funcaoDouble
        fun ${nomeMinusculo}Of(value: Number, unit: Unit<$nome>): Quantity<$nome> = criarQuantity(value, unit)
        fun ${nomeMinusculo}Prop(name: String?): ObjectProperty<Quantity<$nome>> = 
            SimpleObjectProperty<Quantity<$nome>>(null, name)
        fun ${nomeMinusculo}Prop(name: String? = null, value: Number, unit: Unit<$nome>): ObjectProperty<Quantity<$nome>> {
            return ${nomeMinusculo}Prop(name = name).apply { this.value = ${nomeMinusculo}Of(value = value, unit = unit) }
        }
        fun ${nomeMinusculo}Prop(name: String? = null, value: Number, unit: ObjectProperty<Unit<$nome>>): ObjectProperty<Quantity<$nome>> {
            return ${nomeMinusculo}Prop(name = name, value = value, unit = unit.value).apply {
                conectar(unitProperty = unit)
            }
        }
        fun ${nomeMinusculo}Of(value: Number): Quantity<$nome> {
            return ${nomeMinusculo}Of(
                value = value,
                unit = Units.getInstance().getUnit($nome::class.java)
            )
        }
        fun ${nomeMinusculo}Prop(name: String? = null, value: Number): ObjectProperty<Quantity<$nome>> =
            SimpleObjectProperty(null, name, ${nomeMinusculo}Of(value))
        @JvmName("asType$nome")
        fun Unit<*>.as$nome():Unit<$nome> = this.asType($nome::class.java)
    """.trimIndent()
}//KILONEWTON.divide(CUBIC_METRE).asType(SpringStiffnessPerUnitArea::class.java)

fun main() {
    println(criarMapSistemaDeUnidadesAlternativo())
    medidas.forEach {
        println(criar(it))
        println()
    }
}