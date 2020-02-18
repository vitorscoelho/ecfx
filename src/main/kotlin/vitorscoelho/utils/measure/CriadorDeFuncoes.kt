package vitorscoelho.utils.measure

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

internal fun criar(nome: String): String {
    val nomeMinusculo: String = run {
        val primeiraLetra = nome.first().toLowerCase()
        primeiraLetra + nome.drop(1)
    }
    return """
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
    """.trimIndent()
}

fun main() {
    medidas.forEach {
        println(criar(it))
        println()
    }
}