package vitorscoelho.utils.measure

import javafx.beans.property.ObjectProperty
import javafx.beans.property.Property
import javafx.beans.property.SimpleObjectProperty
import tech.units.indriya.AbstractUnit
import tech.units.indriya.quantity.Quantities
import tech.units.indriya.unit.ProductUnit
import tech.units.indriya.unit.Units
import vitorscoelho.utils.tfx.conectar
import javax.measure.Quantity
import javax.measure.Unit
import javax.measure.quantity.*

internal fun inicializarUnidadesDeMedidaExtras() {
    adicionarViaReflection(ProductUnit<Moment>(Units.NEWTON.multiply(Units.METRE)), Moment::class.java)
    adicionarViaReflection(ProductUnit<SpringStiffness>(Units.NEWTON.divide(Units.METRE)), SpringStiffness::class.java)
    adicionarViaReflection(
        ProductUnit<SpringStiffnessPerUnitLength>(Units.NEWTON.divide(Units.METRE).divide(Units.METRE)),
        SpringStiffnessPerUnitLength::class.java
    )
    adicionarViaReflection(
        ProductUnit<SpringStiffnessPerUnitArea>(Units.NEWTON.divide(Units.SQUARE_METRE).divide(Units.METRE)),
        SpringStiffnessPerUnitArea::class.java
    )
    adicionarViaReflection(
        ProductUnit<ForcePerUnitLength>(Units.NEWTON.divide(Units.METRE)),
        ForcePerUnitLength::class.java
    )
    adicionarViaReflection(
        ProductUnit<SpecificWeight>(Units.NEWTON.divide(Units.CUBIC_METRE)),
        SpecificWeight::class.java
    )

//    corrigirNomesDasUnidadesAdicionais()
}

private fun <U : AbstractUnit<*>> adicionarViaReflection(unit: U, type: Class<out Quantity<*>>) {
    val instanciaUnits = Units.getInstance()
    val metodoAddUnits = Units::class.java.getDeclaredMethod("addUnit", AbstractUnit::class.java, Class::class.java)
    metodoAddUnits.isAccessible = true
    metodoAddUnits.invoke(instanciaUnits, unit, type)
}

private var unidadesExtrasInicializadas = false

private fun <T : Quantity<T>> criarQuantity(value: Number, unit: Unit<T>): Quantity<T> {
    if (!unidadesExtrasInicializadas) {
        inicializarUnidadesDeMedidaExtras()
        unidadesExtrasInicializadas = true
    }
    return Quantities.getQuantity(value, unit)
}

fun accelerationOf(value: Number, unit: Unit<Acceleration>): Quantity<Acceleration> = criarQuantity(value, unit)
fun accelerationProp(name: String?): ObjectProperty<Quantity<Acceleration>> =
    SimpleObjectProperty<Quantity<Acceleration>>(null, name)
fun accelerationProp(name: String? = null, value: Number, unit: Unit<Acceleration>): ObjectProperty<Quantity<Acceleration>> {
    return accelerationProp(name = name).apply { this.value = accelerationOf(value = value, unit = unit) }
}
fun accelerationProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<Acceleration>>): ObjectProperty<Quantity<Acceleration>> {
    return accelerationProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun accelerationOf(value: Number): Quantity<Acceleration> {
    return accelerationOf(
        value = value,
        unit = Units.getInstance().getUnit(Acceleration::class.java)
    )
}
fun accelerationProp(name: String? = null, value: Number): ObjectProperty<Quantity<Acceleration>> =
    SimpleObjectProperty(null, name, accelerationOf(value))
@JvmName("asTypeAcceleration")
fun Unit<*>.asAcceleration():Unit<Acceleration> = this.asType(Acceleration::class.java)


fun amountOfSubstanceOf(value: Number, unit: Unit<AmountOfSubstance>): Quantity<AmountOfSubstance> = criarQuantity(value, unit)
fun amountOfSubstanceProp(name: String?): ObjectProperty<Quantity<AmountOfSubstance>> =
    SimpleObjectProperty<Quantity<AmountOfSubstance>>(null, name)
fun amountOfSubstanceProp(name: String? = null, value: Number, unit: Unit<AmountOfSubstance>): ObjectProperty<Quantity<AmountOfSubstance>> {
    return amountOfSubstanceProp(name = name).apply { this.value = amountOfSubstanceOf(value = value, unit = unit) }
}
fun amountOfSubstanceProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<AmountOfSubstance>>): ObjectProperty<Quantity<AmountOfSubstance>> {
    return amountOfSubstanceProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun amountOfSubstanceOf(value: Number): Quantity<AmountOfSubstance> {
    return amountOfSubstanceOf(
        value = value,
        unit = Units.getInstance().getUnit(AmountOfSubstance::class.java)
    )
}
fun amountOfSubstanceProp(name: String? = null, value: Number): ObjectProperty<Quantity<AmountOfSubstance>> =
    SimpleObjectProperty(null, name, amountOfSubstanceOf(value))
@JvmName("asTypeAmountOfSubstance")
fun Unit<*>.asAmountOfSubstance():Unit<AmountOfSubstance> = this.asType(AmountOfSubstance::class.java)

@JvmName("toDoubleSUAngle")
fun Quantity<Angle>.toDoubleSU():Double = this.to(unitAngle).value.toDouble()
@JvmName("propToDoubleSuAngle")
fun Property<Quantity<Angle>>.toDoubleSU():Double = this.value.toDoubleSU()
fun angleSUOf(value:Double):Quantity<Angle> = criarQuantity(value,unitAngle)
fun angleSUOf(value:Double, toUnit:Unit<Angle>):Quantity<Angle> = criarQuantity(value,unitAngle).to(toUnit)
fun angleOf(value: Number, unit: Unit<Angle>): Quantity<Angle> = criarQuantity(value, unit)
fun angleProp(name: String?): ObjectProperty<Quantity<Angle>> =
    SimpleObjectProperty<Quantity<Angle>>(null, name)
fun angleProp(name: String? = null, value: Number, unit: Unit<Angle>): ObjectProperty<Quantity<Angle>> {
    return angleProp(name = name).apply { this.value = angleOf(value = value, unit = unit) }
}
fun angleProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<Angle>>): ObjectProperty<Quantity<Angle>> {
    return angleProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun angleOf(value: Number): Quantity<Angle> {
    return angleOf(
        value = value,
        unit = Units.getInstance().getUnit(Angle::class.java)
    )
}
fun angleProp(name: String? = null, value: Number): ObjectProperty<Quantity<Angle>> =
    SimpleObjectProperty(null, name, angleOf(value))
@JvmName("asTypeAngle")
fun Unit<*>.asAngle():Unit<Angle> = this.asType(Angle::class.java)

@JvmName("toDoubleSUArea")
fun Quantity<Area>.toDoubleSU():Double = this.to(unitArea).value.toDouble()
@JvmName("propToDoubleSuArea")
fun Property<Quantity<Area>>.toDoubleSU():Double = this.value.toDoubleSU()
fun areaSUOf(value:Double):Quantity<Area> = criarQuantity(value,unitArea)
fun areaSUOf(value:Double, toUnit:Unit<Area>):Quantity<Area> = criarQuantity(value,unitArea).to(toUnit)
fun areaOf(value: Number, unit: Unit<Area>): Quantity<Area> = criarQuantity(value, unit)
fun areaProp(name: String?): ObjectProperty<Quantity<Area>> =
    SimpleObjectProperty<Quantity<Area>>(null, name)
fun areaProp(name: String? = null, value: Number, unit: Unit<Area>): ObjectProperty<Quantity<Area>> {
    return areaProp(name = name).apply { this.value = areaOf(value = value, unit = unit) }
}
fun areaProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<Area>>): ObjectProperty<Quantity<Area>> {
    return areaProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun areaOf(value: Number): Quantity<Area> {
    return areaOf(
        value = value,
        unit = Units.getInstance().getUnit(Area::class.java)
    )
}
fun areaProp(name: String? = null, value: Number): ObjectProperty<Quantity<Area>> =
    SimpleObjectProperty(null, name, areaOf(value))
@JvmName("asTypeArea")
fun Unit<*>.asArea():Unit<Area> = this.asType(Area::class.java)


fun catalyticActivityOf(value: Number, unit: Unit<CatalyticActivity>): Quantity<CatalyticActivity> = criarQuantity(value, unit)
fun catalyticActivityProp(name: String?): ObjectProperty<Quantity<CatalyticActivity>> =
    SimpleObjectProperty<Quantity<CatalyticActivity>>(null, name)
fun catalyticActivityProp(name: String? = null, value: Number, unit: Unit<CatalyticActivity>): ObjectProperty<Quantity<CatalyticActivity>> {
    return catalyticActivityProp(name = name).apply { this.value = catalyticActivityOf(value = value, unit = unit) }
}
fun catalyticActivityProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<CatalyticActivity>>): ObjectProperty<Quantity<CatalyticActivity>> {
    return catalyticActivityProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun catalyticActivityOf(value: Number): Quantity<CatalyticActivity> {
    return catalyticActivityOf(
        value = value,
        unit = Units.getInstance().getUnit(CatalyticActivity::class.java)
    )
}
fun catalyticActivityProp(name: String? = null, value: Number): ObjectProperty<Quantity<CatalyticActivity>> =
    SimpleObjectProperty(null, name, catalyticActivityOf(value))
@JvmName("asTypeCatalyticActivity")
fun Unit<*>.asCatalyticActivity():Unit<CatalyticActivity> = this.asType(CatalyticActivity::class.java)


fun dimensionlessOf(value: Number, unit: Unit<Dimensionless>): Quantity<Dimensionless> = criarQuantity(value, unit)
fun dimensionlessProp(name: String?): ObjectProperty<Quantity<Dimensionless>> =
    SimpleObjectProperty<Quantity<Dimensionless>>(null, name)
fun dimensionlessProp(name: String? = null, value: Number, unit: Unit<Dimensionless>): ObjectProperty<Quantity<Dimensionless>> {
    return dimensionlessProp(name = name).apply { this.value = dimensionlessOf(value = value, unit = unit) }
}
fun dimensionlessProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<Dimensionless>>): ObjectProperty<Quantity<Dimensionless>> {
    return dimensionlessProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun dimensionlessOf(value: Number): Quantity<Dimensionless> {
    return dimensionlessOf(
        value = value,
        unit = Units.getInstance().getUnit(Dimensionless::class.java)
    )
}
fun dimensionlessProp(name: String? = null, value: Number): ObjectProperty<Quantity<Dimensionless>> =
    SimpleObjectProperty(null, name, dimensionlessOf(value))
@JvmName("asTypeDimensionless")
fun Unit<*>.asDimensionless():Unit<Dimensionless> = this.asType(Dimensionless::class.java)


fun electricCapacitanceOf(value: Number, unit: Unit<ElectricCapacitance>): Quantity<ElectricCapacitance> = criarQuantity(value, unit)
fun electricCapacitanceProp(name: String?): ObjectProperty<Quantity<ElectricCapacitance>> =
    SimpleObjectProperty<Quantity<ElectricCapacitance>>(null, name)
fun electricCapacitanceProp(name: String? = null, value: Number, unit: Unit<ElectricCapacitance>): ObjectProperty<Quantity<ElectricCapacitance>> {
    return electricCapacitanceProp(name = name).apply { this.value = electricCapacitanceOf(value = value, unit = unit) }
}
fun electricCapacitanceProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<ElectricCapacitance>>): ObjectProperty<Quantity<ElectricCapacitance>> {
    return electricCapacitanceProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun electricCapacitanceOf(value: Number): Quantity<ElectricCapacitance> {
    return electricCapacitanceOf(
        value = value,
        unit = Units.getInstance().getUnit(ElectricCapacitance::class.java)
    )
}
fun electricCapacitanceProp(name: String? = null, value: Number): ObjectProperty<Quantity<ElectricCapacitance>> =
    SimpleObjectProperty(null, name, electricCapacitanceOf(value))
@JvmName("asTypeElectricCapacitance")
fun Unit<*>.asElectricCapacitance():Unit<ElectricCapacitance> = this.asType(ElectricCapacitance::class.java)


fun electricChargeOf(value: Number, unit: Unit<ElectricCharge>): Quantity<ElectricCharge> = criarQuantity(value, unit)
fun electricChargeProp(name: String?): ObjectProperty<Quantity<ElectricCharge>> =
    SimpleObjectProperty<Quantity<ElectricCharge>>(null, name)
fun electricChargeProp(name: String? = null, value: Number, unit: Unit<ElectricCharge>): ObjectProperty<Quantity<ElectricCharge>> {
    return electricChargeProp(name = name).apply { this.value = electricChargeOf(value = value, unit = unit) }
}
fun electricChargeProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<ElectricCharge>>): ObjectProperty<Quantity<ElectricCharge>> {
    return electricChargeProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun electricChargeOf(value: Number): Quantity<ElectricCharge> {
    return electricChargeOf(
        value = value,
        unit = Units.getInstance().getUnit(ElectricCharge::class.java)
    )
}
fun electricChargeProp(name: String? = null, value: Number): ObjectProperty<Quantity<ElectricCharge>> =
    SimpleObjectProperty(null, name, electricChargeOf(value))
@JvmName("asTypeElectricCharge")
fun Unit<*>.asElectricCharge():Unit<ElectricCharge> = this.asType(ElectricCharge::class.java)


fun electricConductanceOf(value: Number, unit: Unit<ElectricConductance>): Quantity<ElectricConductance> = criarQuantity(value, unit)
fun electricConductanceProp(name: String?): ObjectProperty<Quantity<ElectricConductance>> =
    SimpleObjectProperty<Quantity<ElectricConductance>>(null, name)
fun electricConductanceProp(name: String? = null, value: Number, unit: Unit<ElectricConductance>): ObjectProperty<Quantity<ElectricConductance>> {
    return electricConductanceProp(name = name).apply { this.value = electricConductanceOf(value = value, unit = unit) }
}
fun electricConductanceProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<ElectricConductance>>): ObjectProperty<Quantity<ElectricConductance>> {
    return electricConductanceProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun electricConductanceOf(value: Number): Quantity<ElectricConductance> {
    return electricConductanceOf(
        value = value,
        unit = Units.getInstance().getUnit(ElectricConductance::class.java)
    )
}
fun electricConductanceProp(name: String? = null, value: Number): ObjectProperty<Quantity<ElectricConductance>> =
    SimpleObjectProperty(null, name, electricConductanceOf(value))
@JvmName("asTypeElectricConductance")
fun Unit<*>.asElectricConductance():Unit<ElectricConductance> = this.asType(ElectricConductance::class.java)


fun electricCurrentOf(value: Number, unit: Unit<ElectricCurrent>): Quantity<ElectricCurrent> = criarQuantity(value, unit)
fun electricCurrentProp(name: String?): ObjectProperty<Quantity<ElectricCurrent>> =
    SimpleObjectProperty<Quantity<ElectricCurrent>>(null, name)
fun electricCurrentProp(name: String? = null, value: Number, unit: Unit<ElectricCurrent>): ObjectProperty<Quantity<ElectricCurrent>> {
    return electricCurrentProp(name = name).apply { this.value = electricCurrentOf(value = value, unit = unit) }
}
fun electricCurrentProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<ElectricCurrent>>): ObjectProperty<Quantity<ElectricCurrent>> {
    return electricCurrentProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun electricCurrentOf(value: Number): Quantity<ElectricCurrent> {
    return electricCurrentOf(
        value = value,
        unit = Units.getInstance().getUnit(ElectricCurrent::class.java)
    )
}
fun electricCurrentProp(name: String? = null, value: Number): ObjectProperty<Quantity<ElectricCurrent>> =
    SimpleObjectProperty(null, name, electricCurrentOf(value))
@JvmName("asTypeElectricCurrent")
fun Unit<*>.asElectricCurrent():Unit<ElectricCurrent> = this.asType(ElectricCurrent::class.java)


fun electricInductanceOf(value: Number, unit: Unit<ElectricInductance>): Quantity<ElectricInductance> = criarQuantity(value, unit)
fun electricInductanceProp(name: String?): ObjectProperty<Quantity<ElectricInductance>> =
    SimpleObjectProperty<Quantity<ElectricInductance>>(null, name)
fun electricInductanceProp(name: String? = null, value: Number, unit: Unit<ElectricInductance>): ObjectProperty<Quantity<ElectricInductance>> {
    return electricInductanceProp(name = name).apply { this.value = electricInductanceOf(value = value, unit = unit) }
}
fun electricInductanceProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<ElectricInductance>>): ObjectProperty<Quantity<ElectricInductance>> {
    return electricInductanceProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun electricInductanceOf(value: Number): Quantity<ElectricInductance> {
    return electricInductanceOf(
        value = value,
        unit = Units.getInstance().getUnit(ElectricInductance::class.java)
    )
}
fun electricInductanceProp(name: String? = null, value: Number): ObjectProperty<Quantity<ElectricInductance>> =
    SimpleObjectProperty(null, name, electricInductanceOf(value))
@JvmName("asTypeElectricInductance")
fun Unit<*>.asElectricInductance():Unit<ElectricInductance> = this.asType(ElectricInductance::class.java)


fun electricPotentialOf(value: Number, unit: Unit<ElectricPotential>): Quantity<ElectricPotential> = criarQuantity(value, unit)
fun electricPotentialProp(name: String?): ObjectProperty<Quantity<ElectricPotential>> =
    SimpleObjectProperty<Quantity<ElectricPotential>>(null, name)
fun electricPotentialProp(name: String? = null, value: Number, unit: Unit<ElectricPotential>): ObjectProperty<Quantity<ElectricPotential>> {
    return electricPotentialProp(name = name).apply { this.value = electricPotentialOf(value = value, unit = unit) }
}
fun electricPotentialProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<ElectricPotential>>): ObjectProperty<Quantity<ElectricPotential>> {
    return electricPotentialProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun electricPotentialOf(value: Number): Quantity<ElectricPotential> {
    return electricPotentialOf(
        value = value,
        unit = Units.getInstance().getUnit(ElectricPotential::class.java)
    )
}
fun electricPotentialProp(name: String? = null, value: Number): ObjectProperty<Quantity<ElectricPotential>> =
    SimpleObjectProperty(null, name, electricPotentialOf(value))
@JvmName("asTypeElectricPotential")
fun Unit<*>.asElectricPotential():Unit<ElectricPotential> = this.asType(ElectricPotential::class.java)


fun electricResistanceOf(value: Number, unit: Unit<ElectricResistance>): Quantity<ElectricResistance> = criarQuantity(value, unit)
fun electricResistanceProp(name: String?): ObjectProperty<Quantity<ElectricResistance>> =
    SimpleObjectProperty<Quantity<ElectricResistance>>(null, name)
fun electricResistanceProp(name: String? = null, value: Number, unit: Unit<ElectricResistance>): ObjectProperty<Quantity<ElectricResistance>> {
    return electricResistanceProp(name = name).apply { this.value = electricResistanceOf(value = value, unit = unit) }
}
fun electricResistanceProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<ElectricResistance>>): ObjectProperty<Quantity<ElectricResistance>> {
    return electricResistanceProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun electricResistanceOf(value: Number): Quantity<ElectricResistance> {
    return electricResistanceOf(
        value = value,
        unit = Units.getInstance().getUnit(ElectricResistance::class.java)
    )
}
fun electricResistanceProp(name: String? = null, value: Number): ObjectProperty<Quantity<ElectricResistance>> =
    SimpleObjectProperty(null, name, electricResistanceOf(value))
@JvmName("asTypeElectricResistance")
fun Unit<*>.asElectricResistance():Unit<ElectricResistance> = this.asType(ElectricResistance::class.java)


fun energyOf(value: Number, unit: Unit<Energy>): Quantity<Energy> = criarQuantity(value, unit)
fun energyProp(name: String?): ObjectProperty<Quantity<Energy>> =
    SimpleObjectProperty<Quantity<Energy>>(null, name)
fun energyProp(name: String? = null, value: Number, unit: Unit<Energy>): ObjectProperty<Quantity<Energy>> {
    return energyProp(name = name).apply { this.value = energyOf(value = value, unit = unit) }
}
fun energyProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<Energy>>): ObjectProperty<Quantity<Energy>> {
    return energyProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun energyOf(value: Number): Quantity<Energy> {
    return energyOf(
        value = value,
        unit = Units.getInstance().getUnit(Energy::class.java)
    )
}
fun energyProp(name: String? = null, value: Number): ObjectProperty<Quantity<Energy>> =
    SimpleObjectProperty(null, name, energyOf(value))
@JvmName("asTypeEnergy")
fun Unit<*>.asEnergy():Unit<Energy> = this.asType(Energy::class.java)

@JvmName("toDoubleSUForce")
fun Quantity<Force>.toDoubleSU():Double = this.to(unitForce).value.toDouble()
@JvmName("propToDoubleSuForce")
fun Property<Quantity<Force>>.toDoubleSU():Double = this.value.toDoubleSU()
fun forceSUOf(value:Double):Quantity<Force> = criarQuantity(value,unitForce)
fun forceSUOf(value:Double, toUnit:Unit<Force>):Quantity<Force> = criarQuantity(value,unitForce).to(toUnit)
fun forceOf(value: Number, unit: Unit<Force>): Quantity<Force> = criarQuantity(value, unit)
fun forceProp(name: String?): ObjectProperty<Quantity<Force>> =
    SimpleObjectProperty<Quantity<Force>>(null, name)
fun forceProp(name: String? = null, value: Number, unit: Unit<Force>): ObjectProperty<Quantity<Force>> {
    return forceProp(name = name).apply { this.value = forceOf(value = value, unit = unit) }
}
fun forceProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<Force>>): ObjectProperty<Quantity<Force>> {
    return forceProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun forceOf(value: Number): Quantity<Force> {
    return forceOf(
        value = value,
        unit = Units.getInstance().getUnit(Force::class.java)
    )
}
fun forceProp(name: String? = null, value: Number): ObjectProperty<Quantity<Force>> =
    SimpleObjectProperty(null, name, forceOf(value))
@JvmName("asTypeForce")
fun Unit<*>.asForce():Unit<Force> = this.asType(Force::class.java)


fun frequencyOf(value: Number, unit: Unit<Frequency>): Quantity<Frequency> = criarQuantity(value, unit)
fun frequencyProp(name: String?): ObjectProperty<Quantity<Frequency>> =
    SimpleObjectProperty<Quantity<Frequency>>(null, name)
fun frequencyProp(name: String? = null, value: Number, unit: Unit<Frequency>): ObjectProperty<Quantity<Frequency>> {
    return frequencyProp(name = name).apply { this.value = frequencyOf(value = value, unit = unit) }
}
fun frequencyProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<Frequency>>): ObjectProperty<Quantity<Frequency>> {
    return frequencyProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun frequencyOf(value: Number): Quantity<Frequency> {
    return frequencyOf(
        value = value,
        unit = Units.getInstance().getUnit(Frequency::class.java)
    )
}
fun frequencyProp(name: String? = null, value: Number): ObjectProperty<Quantity<Frequency>> =
    SimpleObjectProperty(null, name, frequencyOf(value))
@JvmName("asTypeFrequency")
fun Unit<*>.asFrequency():Unit<Frequency> = this.asType(Frequency::class.java)


fun illuminanceOf(value: Number, unit: Unit<Illuminance>): Quantity<Illuminance> = criarQuantity(value, unit)
fun illuminanceProp(name: String?): ObjectProperty<Quantity<Illuminance>> =
    SimpleObjectProperty<Quantity<Illuminance>>(null, name)
fun illuminanceProp(name: String? = null, value: Number, unit: Unit<Illuminance>): ObjectProperty<Quantity<Illuminance>> {
    return illuminanceProp(name = name).apply { this.value = illuminanceOf(value = value, unit = unit) }
}
fun illuminanceProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<Illuminance>>): ObjectProperty<Quantity<Illuminance>> {
    return illuminanceProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun illuminanceOf(value: Number): Quantity<Illuminance> {
    return illuminanceOf(
        value = value,
        unit = Units.getInstance().getUnit(Illuminance::class.java)
    )
}
fun illuminanceProp(name: String? = null, value: Number): ObjectProperty<Quantity<Illuminance>> =
    SimpleObjectProperty(null, name, illuminanceOf(value))
@JvmName("asTypeIlluminance")
fun Unit<*>.asIlluminance():Unit<Illuminance> = this.asType(Illuminance::class.java)

@JvmName("toDoubleSULength")
fun Quantity<Length>.toDoubleSU():Double = this.to(unitLength).value.toDouble()
@JvmName("propToDoubleSuLength")
fun Property<Quantity<Length>>.toDoubleSU():Double = this.value.toDoubleSU()
fun lengthSUOf(value:Double):Quantity<Length> = criarQuantity(value,unitLength)
fun lengthSUOf(value:Double, toUnit:Unit<Length>):Quantity<Length> = criarQuantity(value,unitLength).to(toUnit)
fun lengthOf(value: Number, unit: Unit<Length>): Quantity<Length> = criarQuantity(value, unit)
fun lengthProp(name: String?): ObjectProperty<Quantity<Length>> =
    SimpleObjectProperty<Quantity<Length>>(null, name)
fun lengthProp(name: String? = null, value: Number, unit: Unit<Length>): ObjectProperty<Quantity<Length>> {
    return lengthProp(name = name).apply { this.value = lengthOf(value = value, unit = unit) }
}
fun lengthProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<Length>>): ObjectProperty<Quantity<Length>> {
    return lengthProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun lengthOf(value: Number): Quantity<Length> {
    return lengthOf(
        value = value,
        unit = Units.getInstance().getUnit(Length::class.java)
    )
}
fun lengthProp(name: String? = null, value: Number): ObjectProperty<Quantity<Length>> =
    SimpleObjectProperty(null, name, lengthOf(value))
@JvmName("asTypeLength")
fun Unit<*>.asLength():Unit<Length> = this.asType(Length::class.java)


fun luminousFluxOf(value: Number, unit: Unit<LuminousFlux>): Quantity<LuminousFlux> = criarQuantity(value, unit)
fun luminousFluxProp(name: String?): ObjectProperty<Quantity<LuminousFlux>> =
    SimpleObjectProperty<Quantity<LuminousFlux>>(null, name)
fun luminousFluxProp(name: String? = null, value: Number, unit: Unit<LuminousFlux>): ObjectProperty<Quantity<LuminousFlux>> {
    return luminousFluxProp(name = name).apply { this.value = luminousFluxOf(value = value, unit = unit) }
}
fun luminousFluxProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<LuminousFlux>>): ObjectProperty<Quantity<LuminousFlux>> {
    return luminousFluxProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun luminousFluxOf(value: Number): Quantity<LuminousFlux> {
    return luminousFluxOf(
        value = value,
        unit = Units.getInstance().getUnit(LuminousFlux::class.java)
    )
}
fun luminousFluxProp(name: String? = null, value: Number): ObjectProperty<Quantity<LuminousFlux>> =
    SimpleObjectProperty(null, name, luminousFluxOf(value))
@JvmName("asTypeLuminousFlux")
fun Unit<*>.asLuminousFlux():Unit<LuminousFlux> = this.asType(LuminousFlux::class.java)


fun luminousIntensityOf(value: Number, unit: Unit<LuminousIntensity>): Quantity<LuminousIntensity> = criarQuantity(value, unit)
fun luminousIntensityProp(name: String?): ObjectProperty<Quantity<LuminousIntensity>> =
    SimpleObjectProperty<Quantity<LuminousIntensity>>(null, name)
fun luminousIntensityProp(name: String? = null, value: Number, unit: Unit<LuminousIntensity>): ObjectProperty<Quantity<LuminousIntensity>> {
    return luminousIntensityProp(name = name).apply { this.value = luminousIntensityOf(value = value, unit = unit) }
}
fun luminousIntensityProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<LuminousIntensity>>): ObjectProperty<Quantity<LuminousIntensity>> {
    return luminousIntensityProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun luminousIntensityOf(value: Number): Quantity<LuminousIntensity> {
    return luminousIntensityOf(
        value = value,
        unit = Units.getInstance().getUnit(LuminousIntensity::class.java)
    )
}
fun luminousIntensityProp(name: String? = null, value: Number): ObjectProperty<Quantity<LuminousIntensity>> =
    SimpleObjectProperty(null, name, luminousIntensityOf(value))
@JvmName("asTypeLuminousIntensity")
fun Unit<*>.asLuminousIntensity():Unit<LuminousIntensity> = this.asType(LuminousIntensity::class.java)


fun magneticFluxOf(value: Number, unit: Unit<MagneticFlux>): Quantity<MagneticFlux> = criarQuantity(value, unit)
fun magneticFluxProp(name: String?): ObjectProperty<Quantity<MagneticFlux>> =
    SimpleObjectProperty<Quantity<MagneticFlux>>(null, name)
fun magneticFluxProp(name: String? = null, value: Number, unit: Unit<MagneticFlux>): ObjectProperty<Quantity<MagneticFlux>> {
    return magneticFluxProp(name = name).apply { this.value = magneticFluxOf(value = value, unit = unit) }
}
fun magneticFluxProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<MagneticFlux>>): ObjectProperty<Quantity<MagneticFlux>> {
    return magneticFluxProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun magneticFluxOf(value: Number): Quantity<MagneticFlux> {
    return magneticFluxOf(
        value = value,
        unit = Units.getInstance().getUnit(MagneticFlux::class.java)
    )
}
fun magneticFluxProp(name: String? = null, value: Number): ObjectProperty<Quantity<MagneticFlux>> =
    SimpleObjectProperty(null, name, magneticFluxOf(value))
@JvmName("asTypeMagneticFlux")
fun Unit<*>.asMagneticFlux():Unit<MagneticFlux> = this.asType(MagneticFlux::class.java)


fun magneticFluxDensityOf(value: Number, unit: Unit<MagneticFluxDensity>): Quantity<MagneticFluxDensity> = criarQuantity(value, unit)
fun magneticFluxDensityProp(name: String?): ObjectProperty<Quantity<MagneticFluxDensity>> =
    SimpleObjectProperty<Quantity<MagneticFluxDensity>>(null, name)
fun magneticFluxDensityProp(name: String? = null, value: Number, unit: Unit<MagneticFluxDensity>): ObjectProperty<Quantity<MagneticFluxDensity>> {
    return magneticFluxDensityProp(name = name).apply { this.value = magneticFluxDensityOf(value = value, unit = unit) }
}
fun magneticFluxDensityProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<MagneticFluxDensity>>): ObjectProperty<Quantity<MagneticFluxDensity>> {
    return magneticFluxDensityProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun magneticFluxDensityOf(value: Number): Quantity<MagneticFluxDensity> {
    return magneticFluxDensityOf(
        value = value,
        unit = Units.getInstance().getUnit(MagneticFluxDensity::class.java)
    )
}
fun magneticFluxDensityProp(name: String? = null, value: Number): ObjectProperty<Quantity<MagneticFluxDensity>> =
    SimpleObjectProperty(null, name, magneticFluxDensityOf(value))
@JvmName("asTypeMagneticFluxDensity")
fun Unit<*>.asMagneticFluxDensity():Unit<MagneticFluxDensity> = this.asType(MagneticFluxDensity::class.java)

@JvmName("toDoubleSUMass")
fun Quantity<Mass>.toDoubleSU():Double = this.to(unitMass).value.toDouble()
@JvmName("propToDoubleSuMass")
fun Property<Quantity<Mass>>.toDoubleSU():Double = this.value.toDoubleSU()
fun massSUOf(value:Double):Quantity<Mass> = criarQuantity(value,unitMass)
fun massSUOf(value:Double, toUnit:Unit<Mass>):Quantity<Mass> = criarQuantity(value,unitMass).to(toUnit)
fun massOf(value: Number, unit: Unit<Mass>): Quantity<Mass> = criarQuantity(value, unit)
fun massProp(name: String?): ObjectProperty<Quantity<Mass>> =
    SimpleObjectProperty<Quantity<Mass>>(null, name)
fun massProp(name: String? = null, value: Number, unit: Unit<Mass>): ObjectProperty<Quantity<Mass>> {
    return massProp(name = name).apply { this.value = massOf(value = value, unit = unit) }
}
fun massProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<Mass>>): ObjectProperty<Quantity<Mass>> {
    return massProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun massOf(value: Number): Quantity<Mass> {
    return massOf(
        value = value,
        unit = Units.getInstance().getUnit(Mass::class.java)
    )
}
fun massProp(name: String? = null, value: Number): ObjectProperty<Quantity<Mass>> =
    SimpleObjectProperty(null, name, massOf(value))
@JvmName("asTypeMass")
fun Unit<*>.asMass():Unit<Mass> = this.asType(Mass::class.java)


fun powerOf(value: Number, unit: Unit<Power>): Quantity<Power> = criarQuantity(value, unit)
fun powerProp(name: String?): ObjectProperty<Quantity<Power>> =
    SimpleObjectProperty<Quantity<Power>>(null, name)
fun powerProp(name: String? = null, value: Number, unit: Unit<Power>): ObjectProperty<Quantity<Power>> {
    return powerProp(name = name).apply { this.value = powerOf(value = value, unit = unit) }
}
fun powerProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<Power>>): ObjectProperty<Quantity<Power>> {
    return powerProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun powerOf(value: Number): Quantity<Power> {
    return powerOf(
        value = value,
        unit = Units.getInstance().getUnit(Power::class.java)
    )
}
fun powerProp(name: String? = null, value: Number): ObjectProperty<Quantity<Power>> =
    SimpleObjectProperty(null, name, powerOf(value))
@JvmName("asTypePower")
fun Unit<*>.asPower():Unit<Power> = this.asType(Power::class.java)

@JvmName("toDoubleSUPressure")
fun Quantity<Pressure>.toDoubleSU():Double = this.to(unitPressure).value.toDouble()
@JvmName("propToDoubleSuPressure")
fun Property<Quantity<Pressure>>.toDoubleSU():Double = this.value.toDoubleSU()
fun pressureSUOf(value:Double):Quantity<Pressure> = criarQuantity(value,unitPressure)
fun pressureSUOf(value:Double, toUnit:Unit<Pressure>):Quantity<Pressure> = criarQuantity(value,unitPressure).to(toUnit)
fun pressureOf(value: Number, unit: Unit<Pressure>): Quantity<Pressure> = criarQuantity(value, unit)
fun pressureProp(name: String?): ObjectProperty<Quantity<Pressure>> =
    SimpleObjectProperty<Quantity<Pressure>>(null, name)
fun pressureProp(name: String? = null, value: Number, unit: Unit<Pressure>): ObjectProperty<Quantity<Pressure>> {
    return pressureProp(name = name).apply { this.value = pressureOf(value = value, unit = unit) }
}
fun pressureProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<Pressure>>): ObjectProperty<Quantity<Pressure>> {
    return pressureProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun pressureOf(value: Number): Quantity<Pressure> {
    return pressureOf(
        value = value,
        unit = Units.getInstance().getUnit(Pressure::class.java)
    )
}
fun pressureProp(name: String? = null, value: Number): ObjectProperty<Quantity<Pressure>> =
    SimpleObjectProperty(null, name, pressureOf(value))
@JvmName("asTypePressure")
fun Unit<*>.asPressure():Unit<Pressure> = this.asType(Pressure::class.java)


fun radiationDoseAbsorbedOf(value: Number, unit: Unit<RadiationDoseAbsorbed>): Quantity<RadiationDoseAbsorbed> = criarQuantity(value, unit)
fun radiationDoseAbsorbedProp(name: String?): ObjectProperty<Quantity<RadiationDoseAbsorbed>> =
    SimpleObjectProperty<Quantity<RadiationDoseAbsorbed>>(null, name)
fun radiationDoseAbsorbedProp(name: String? = null, value: Number, unit: Unit<RadiationDoseAbsorbed>): ObjectProperty<Quantity<RadiationDoseAbsorbed>> {
    return radiationDoseAbsorbedProp(name = name).apply { this.value = radiationDoseAbsorbedOf(value = value, unit = unit) }
}
fun radiationDoseAbsorbedProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<RadiationDoseAbsorbed>>): ObjectProperty<Quantity<RadiationDoseAbsorbed>> {
    return radiationDoseAbsorbedProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun radiationDoseAbsorbedOf(value: Number): Quantity<RadiationDoseAbsorbed> {
    return radiationDoseAbsorbedOf(
        value = value,
        unit = Units.getInstance().getUnit(RadiationDoseAbsorbed::class.java)
    )
}
fun radiationDoseAbsorbedProp(name: String? = null, value: Number): ObjectProperty<Quantity<RadiationDoseAbsorbed>> =
    SimpleObjectProperty(null, name, radiationDoseAbsorbedOf(value))
@JvmName("asTypeRadiationDoseAbsorbed")
fun Unit<*>.asRadiationDoseAbsorbed():Unit<RadiationDoseAbsorbed> = this.asType(RadiationDoseAbsorbed::class.java)


fun radiationDoseEffectiveOf(value: Number, unit: Unit<RadiationDoseEffective>): Quantity<RadiationDoseEffective> = criarQuantity(value, unit)
fun radiationDoseEffectiveProp(name: String?): ObjectProperty<Quantity<RadiationDoseEffective>> =
    SimpleObjectProperty<Quantity<RadiationDoseEffective>>(null, name)
fun radiationDoseEffectiveProp(name: String? = null, value: Number, unit: Unit<RadiationDoseEffective>): ObjectProperty<Quantity<RadiationDoseEffective>> {
    return radiationDoseEffectiveProp(name = name).apply { this.value = radiationDoseEffectiveOf(value = value, unit = unit) }
}
fun radiationDoseEffectiveProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<RadiationDoseEffective>>): ObjectProperty<Quantity<RadiationDoseEffective>> {
    return radiationDoseEffectiveProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun radiationDoseEffectiveOf(value: Number): Quantity<RadiationDoseEffective> {
    return radiationDoseEffectiveOf(
        value = value,
        unit = Units.getInstance().getUnit(RadiationDoseEffective::class.java)
    )
}
fun radiationDoseEffectiveProp(name: String? = null, value: Number): ObjectProperty<Quantity<RadiationDoseEffective>> =
    SimpleObjectProperty(null, name, radiationDoseEffectiveOf(value))
@JvmName("asTypeRadiationDoseEffective")
fun Unit<*>.asRadiationDoseEffective():Unit<RadiationDoseEffective> = this.asType(RadiationDoseEffective::class.java)


fun radioactivityOf(value: Number, unit: Unit<Radioactivity>): Quantity<Radioactivity> = criarQuantity(value, unit)
fun radioactivityProp(name: String?): ObjectProperty<Quantity<Radioactivity>> =
    SimpleObjectProperty<Quantity<Radioactivity>>(null, name)
fun radioactivityProp(name: String? = null, value: Number, unit: Unit<Radioactivity>): ObjectProperty<Quantity<Radioactivity>> {
    return radioactivityProp(name = name).apply { this.value = radioactivityOf(value = value, unit = unit) }
}
fun radioactivityProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<Radioactivity>>): ObjectProperty<Quantity<Radioactivity>> {
    return radioactivityProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun radioactivityOf(value: Number): Quantity<Radioactivity> {
    return radioactivityOf(
        value = value,
        unit = Units.getInstance().getUnit(Radioactivity::class.java)
    )
}
fun radioactivityProp(name: String? = null, value: Number): ObjectProperty<Quantity<Radioactivity>> =
    SimpleObjectProperty(null, name, radioactivityOf(value))
@JvmName("asTypeRadioactivity")
fun Unit<*>.asRadioactivity():Unit<Radioactivity> = this.asType(Radioactivity::class.java)


fun solidAngleOf(value: Number, unit: Unit<SolidAngle>): Quantity<SolidAngle> = criarQuantity(value, unit)
fun solidAngleProp(name: String?): ObjectProperty<Quantity<SolidAngle>> =
    SimpleObjectProperty<Quantity<SolidAngle>>(null, name)
fun solidAngleProp(name: String? = null, value: Number, unit: Unit<SolidAngle>): ObjectProperty<Quantity<SolidAngle>> {
    return solidAngleProp(name = name).apply { this.value = solidAngleOf(value = value, unit = unit) }
}
fun solidAngleProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<SolidAngle>>): ObjectProperty<Quantity<SolidAngle>> {
    return solidAngleProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun solidAngleOf(value: Number): Quantity<SolidAngle> {
    return solidAngleOf(
        value = value,
        unit = Units.getInstance().getUnit(SolidAngle::class.java)
    )
}
fun solidAngleProp(name: String? = null, value: Number): ObjectProperty<Quantity<SolidAngle>> =
    SimpleObjectProperty(null, name, solidAngleOf(value))
@JvmName("asTypeSolidAngle")
fun Unit<*>.asSolidAngle():Unit<SolidAngle> = this.asType(SolidAngle::class.java)


fun speedOf(value: Number, unit: Unit<Speed>): Quantity<Speed> = criarQuantity(value, unit)
fun speedProp(name: String?): ObjectProperty<Quantity<Speed>> =
    SimpleObjectProperty<Quantity<Speed>>(null, name)
fun speedProp(name: String? = null, value: Number, unit: Unit<Speed>): ObjectProperty<Quantity<Speed>> {
    return speedProp(name = name).apply { this.value = speedOf(value = value, unit = unit) }
}
fun speedProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<Speed>>): ObjectProperty<Quantity<Speed>> {
    return speedProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun speedOf(value: Number): Quantity<Speed> {
    return speedOf(
        value = value,
        unit = Units.getInstance().getUnit(Speed::class.java)
    )
}
fun speedProp(name: String? = null, value: Number): ObjectProperty<Quantity<Speed>> =
    SimpleObjectProperty(null, name, speedOf(value))
@JvmName("asTypeSpeed")
fun Unit<*>.asSpeed():Unit<Speed> = this.asType(Speed::class.java)


fun temperatureOf(value: Number, unit: Unit<Temperature>): Quantity<Temperature> = criarQuantity(value, unit)
fun temperatureProp(name: String?): ObjectProperty<Quantity<Temperature>> =
    SimpleObjectProperty<Quantity<Temperature>>(null, name)
fun temperatureProp(name: String? = null, value: Number, unit: Unit<Temperature>): ObjectProperty<Quantity<Temperature>> {
    return temperatureProp(name = name).apply { this.value = temperatureOf(value = value, unit = unit) }
}
fun temperatureProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<Temperature>>): ObjectProperty<Quantity<Temperature>> {
    return temperatureProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun temperatureOf(value: Number): Quantity<Temperature> {
    return temperatureOf(
        value = value,
        unit = Units.getInstance().getUnit(Temperature::class.java)
    )
}
fun temperatureProp(name: String? = null, value: Number): ObjectProperty<Quantity<Temperature>> =
    SimpleObjectProperty(null, name, temperatureOf(value))
@JvmName("asTypeTemperature")
fun Unit<*>.asTemperature():Unit<Temperature> = this.asType(Temperature::class.java)


fun timeOf(value: Number, unit: Unit<Time>): Quantity<Time> = criarQuantity(value, unit)
fun timeProp(name: String?): ObjectProperty<Quantity<Time>> =
    SimpleObjectProperty<Quantity<Time>>(null, name)
fun timeProp(name: String? = null, value: Number, unit: Unit<Time>): ObjectProperty<Quantity<Time>> {
    return timeProp(name = name).apply { this.value = timeOf(value = value, unit = unit) }
}
fun timeProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<Time>>): ObjectProperty<Quantity<Time>> {
    return timeProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun timeOf(value: Number): Quantity<Time> {
    return timeOf(
        value = value,
        unit = Units.getInstance().getUnit(Time::class.java)
    )
}
fun timeProp(name: String? = null, value: Number): ObjectProperty<Quantity<Time>> =
    SimpleObjectProperty(null, name, timeOf(value))
@JvmName("asTypeTime")
fun Unit<*>.asTime():Unit<Time> = this.asType(Time::class.java)

@JvmName("toDoubleSUVolume")
fun Quantity<Volume>.toDoubleSU():Double = this.to(unitVolume).value.toDouble()
@JvmName("propToDoubleSuVolume")
fun Property<Quantity<Volume>>.toDoubleSU():Double = this.value.toDoubleSU()
fun volumeSUOf(value:Double):Quantity<Volume> = criarQuantity(value,unitVolume)
fun volumeSUOf(value:Double, toUnit:Unit<Volume>):Quantity<Volume> = criarQuantity(value,unitVolume).to(toUnit)
fun volumeOf(value: Number, unit: Unit<Volume>): Quantity<Volume> = criarQuantity(value, unit)
fun volumeProp(name: String?): ObjectProperty<Quantity<Volume>> =
    SimpleObjectProperty<Quantity<Volume>>(null, name)
fun volumeProp(name: String? = null, value: Number, unit: Unit<Volume>): ObjectProperty<Quantity<Volume>> {
    return volumeProp(name = name).apply { this.value = volumeOf(value = value, unit = unit) }
}
fun volumeProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<Volume>>): ObjectProperty<Quantity<Volume>> {
    return volumeProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun volumeOf(value: Number): Quantity<Volume> {
    return volumeOf(
        value = value,
        unit = Units.getInstance().getUnit(Volume::class.java)
    )
}
fun volumeProp(name: String? = null, value: Number): ObjectProperty<Quantity<Volume>> =
    SimpleObjectProperty(null, name, volumeOf(value))
@JvmName("asTypeVolume")
fun Unit<*>.asVolume():Unit<Volume> = this.asType(Volume::class.java)

@JvmName("toDoubleSUMoment")
fun Quantity<Moment>.toDoubleSU():Double = this.to(unitMoment).value.toDouble()
@JvmName("propToDoubleSuMoment")
fun Property<Quantity<Moment>>.toDoubleSU():Double = this.value.toDoubleSU()
fun momentSUOf(value:Double):Quantity<Moment> = criarQuantity(value,unitMoment)
fun momentSUOf(value:Double, toUnit:Unit<Moment>):Quantity<Moment> = criarQuantity(value,unitMoment).to(toUnit)
fun momentOf(value: Number, unit: Unit<Moment>): Quantity<Moment> = criarQuantity(value, unit)
fun momentProp(name: String?): ObjectProperty<Quantity<Moment>> =
    SimpleObjectProperty<Quantity<Moment>>(null, name)
fun momentProp(name: String? = null, value: Number, unit: Unit<Moment>): ObjectProperty<Quantity<Moment>> {
    return momentProp(name = name).apply { this.value = momentOf(value = value, unit = unit) }
}
fun momentProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<Moment>>): ObjectProperty<Quantity<Moment>> {
    return momentProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun momentOf(value: Number): Quantity<Moment> {
    return momentOf(
        value = value,
        unit = Units.getInstance().getUnit(Moment::class.java)
    )
}
fun momentProp(name: String? = null, value: Number): ObjectProperty<Quantity<Moment>> =
    SimpleObjectProperty(null, name, momentOf(value))
@JvmName("asTypeMoment")
fun Unit<*>.asMoment():Unit<Moment> = this.asType(Moment::class.java)

@JvmName("toDoubleSUSpringStiffness")
fun Quantity<SpringStiffness>.toDoubleSU():Double = this.to(unitSpringStiffness).value.toDouble()
@JvmName("propToDoubleSuSpringStiffness")
fun Property<Quantity<SpringStiffness>>.toDoubleSU():Double = this.value.toDoubleSU()
fun springStiffnessSUOf(value:Double):Quantity<SpringStiffness> = criarQuantity(value,unitSpringStiffness)
fun springStiffnessSUOf(value:Double, toUnit:Unit<SpringStiffness>):Quantity<SpringStiffness> = criarQuantity(value,unitSpringStiffness).to(toUnit)
fun springStiffnessOf(value: Number, unit: Unit<SpringStiffness>): Quantity<SpringStiffness> = criarQuantity(value, unit)
fun springStiffnessProp(name: String?): ObjectProperty<Quantity<SpringStiffness>> =
    SimpleObjectProperty<Quantity<SpringStiffness>>(null, name)
fun springStiffnessProp(name: String? = null, value: Number, unit: Unit<SpringStiffness>): ObjectProperty<Quantity<SpringStiffness>> {
    return springStiffnessProp(name = name).apply { this.value = springStiffnessOf(value = value, unit = unit) }
}
fun springStiffnessProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<SpringStiffness>>): ObjectProperty<Quantity<SpringStiffness>> {
    return springStiffnessProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun springStiffnessOf(value: Number): Quantity<SpringStiffness> {
    return springStiffnessOf(
        value = value,
        unit = Units.getInstance().getUnit(SpringStiffness::class.java)
    )
}
fun springStiffnessProp(name: String? = null, value: Number): ObjectProperty<Quantity<SpringStiffness>> =
    SimpleObjectProperty(null, name, springStiffnessOf(value))
@JvmName("asTypeSpringStiffness")
fun Unit<*>.asSpringStiffness():Unit<SpringStiffness> = this.asType(SpringStiffness::class.java)

@JvmName("toDoubleSUSpringStiffnessPerUnitLength")
fun Quantity<SpringStiffnessPerUnitLength>.toDoubleSU():Double = this.to(unitSpringStiffnessPerUnitLength).value.toDouble()
@JvmName("propToDoubleSuSpringStiffnessPerUnitLength")
fun Property<Quantity<SpringStiffnessPerUnitLength>>.toDoubleSU():Double = this.value.toDoubleSU()
fun springStiffnessPerUnitLengthSUOf(value:Double):Quantity<SpringStiffnessPerUnitLength> = criarQuantity(value,unitSpringStiffnessPerUnitLength)
fun springStiffnessPerUnitLengthSUOf(value:Double, toUnit:Unit<SpringStiffnessPerUnitLength>):Quantity<SpringStiffnessPerUnitLength> = criarQuantity(value,unitSpringStiffnessPerUnitLength).to(toUnit)
fun springStiffnessPerUnitLengthOf(value: Number, unit: Unit<SpringStiffnessPerUnitLength>): Quantity<SpringStiffnessPerUnitLength> = criarQuantity(value, unit)
fun springStiffnessPerUnitLengthProp(name: String?): ObjectProperty<Quantity<SpringStiffnessPerUnitLength>> =
    SimpleObjectProperty<Quantity<SpringStiffnessPerUnitLength>>(null, name)
fun springStiffnessPerUnitLengthProp(name: String? = null, value: Number, unit: Unit<SpringStiffnessPerUnitLength>): ObjectProperty<Quantity<SpringStiffnessPerUnitLength>> {
    return springStiffnessPerUnitLengthProp(name = name).apply { this.value = springStiffnessPerUnitLengthOf(value = value, unit = unit) }
}
fun springStiffnessPerUnitLengthProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<SpringStiffnessPerUnitLength>>): ObjectProperty<Quantity<SpringStiffnessPerUnitLength>> {
    return springStiffnessPerUnitLengthProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun springStiffnessPerUnitLengthOf(value: Number): Quantity<SpringStiffnessPerUnitLength> {
    return springStiffnessPerUnitLengthOf(
        value = value,
        unit = Units.getInstance().getUnit(SpringStiffnessPerUnitLength::class.java)
    )
}
fun springStiffnessPerUnitLengthProp(name: String? = null, value: Number): ObjectProperty<Quantity<SpringStiffnessPerUnitLength>> =
    SimpleObjectProperty(null, name, springStiffnessPerUnitLengthOf(value))
@JvmName("asTypeSpringStiffnessPerUnitLength")
fun Unit<*>.asSpringStiffnessPerUnitLength():Unit<SpringStiffnessPerUnitLength> = this.asType(SpringStiffnessPerUnitLength::class.java)

@JvmName("toDoubleSUSpringStiffnessPerUnitArea")
fun Quantity<SpringStiffnessPerUnitArea>.toDoubleSU():Double = this.to(unitSpringStiffnessPerUnitArea).value.toDouble()
@JvmName("propToDoubleSuSpringStiffnessPerUnitArea")
fun Property<Quantity<SpringStiffnessPerUnitArea>>.toDoubleSU():Double = this.value.toDoubleSU()
fun springStiffnessPerUnitAreaSUOf(value:Double):Quantity<SpringStiffnessPerUnitArea> = criarQuantity(value,unitSpringStiffnessPerUnitArea)
fun springStiffnessPerUnitAreaSUOf(value:Double, toUnit:Unit<SpringStiffnessPerUnitArea>):Quantity<SpringStiffnessPerUnitArea> = criarQuantity(value,unitSpringStiffnessPerUnitArea).to(toUnit)
fun springStiffnessPerUnitAreaOf(value: Number, unit: Unit<SpringStiffnessPerUnitArea>): Quantity<SpringStiffnessPerUnitArea> = criarQuantity(value, unit)
fun springStiffnessPerUnitAreaProp(name: String?): ObjectProperty<Quantity<SpringStiffnessPerUnitArea>> =
    SimpleObjectProperty<Quantity<SpringStiffnessPerUnitArea>>(null, name)
fun springStiffnessPerUnitAreaProp(name: String? = null, value: Number, unit: Unit<SpringStiffnessPerUnitArea>): ObjectProperty<Quantity<SpringStiffnessPerUnitArea>> {
    return springStiffnessPerUnitAreaProp(name = name).apply { this.value = springStiffnessPerUnitAreaOf(value = value, unit = unit) }
}
fun springStiffnessPerUnitAreaProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<SpringStiffnessPerUnitArea>>): ObjectProperty<Quantity<SpringStiffnessPerUnitArea>> {
    return springStiffnessPerUnitAreaProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun springStiffnessPerUnitAreaOf(value: Number): Quantity<SpringStiffnessPerUnitArea> {
    return springStiffnessPerUnitAreaOf(
        value = value,
        unit = Units.getInstance().getUnit(SpringStiffnessPerUnitArea::class.java)
    )
}
fun springStiffnessPerUnitAreaProp(name: String? = null, value: Number): ObjectProperty<Quantity<SpringStiffnessPerUnitArea>> =
    SimpleObjectProperty(null, name, springStiffnessPerUnitAreaOf(value))
@JvmName("asTypeSpringStiffnessPerUnitArea")
fun Unit<*>.asSpringStiffnessPerUnitArea():Unit<SpringStiffnessPerUnitArea> = this.asType(SpringStiffnessPerUnitArea::class.java)

@JvmName("toDoubleSUForcePerUnitLength")
fun Quantity<ForcePerUnitLength>.toDoubleSU():Double = this.to(unitForcePerUnitLength).value.toDouble()
@JvmName("propToDoubleSuForcePerUnitLength")
fun Property<Quantity<ForcePerUnitLength>>.toDoubleSU():Double = this.value.toDoubleSU()
fun forcePerUnitLengthSUOf(value:Double):Quantity<ForcePerUnitLength> = criarQuantity(value,unitForcePerUnitLength)
fun forcePerUnitLengthSUOf(value:Double, toUnit:Unit<ForcePerUnitLength>):Quantity<ForcePerUnitLength> = criarQuantity(value,unitForcePerUnitLength).to(toUnit)
fun forcePerUnitLengthOf(value: Number, unit: Unit<ForcePerUnitLength>): Quantity<ForcePerUnitLength> = criarQuantity(value, unit)
fun forcePerUnitLengthProp(name: String?): ObjectProperty<Quantity<ForcePerUnitLength>> =
    SimpleObjectProperty<Quantity<ForcePerUnitLength>>(null, name)
fun forcePerUnitLengthProp(name: String? = null, value: Number, unit: Unit<ForcePerUnitLength>): ObjectProperty<Quantity<ForcePerUnitLength>> {
    return forcePerUnitLengthProp(name = name).apply { this.value = forcePerUnitLengthOf(value = value, unit = unit) }
}
fun forcePerUnitLengthProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<ForcePerUnitLength>>): ObjectProperty<Quantity<ForcePerUnitLength>> {
    return forcePerUnitLengthProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun forcePerUnitLengthOf(value: Number): Quantity<ForcePerUnitLength> {
    return forcePerUnitLengthOf(
        value = value,
        unit = Units.getInstance().getUnit(ForcePerUnitLength::class.java)
    )
}
fun forcePerUnitLengthProp(name: String? = null, value: Number): ObjectProperty<Quantity<ForcePerUnitLength>> =
    SimpleObjectProperty(null, name, forcePerUnitLengthOf(value))
@JvmName("asTypeForcePerUnitLength")
fun Unit<*>.asForcePerUnitLength():Unit<ForcePerUnitLength> = this.asType(ForcePerUnitLength::class.java)

@JvmName("toDoubleSUSpecificWeight")
fun Quantity<SpecificWeight>.toDoubleSU():Double = this.to(unitSpecificWeight).value.toDouble()
@JvmName("propToDoubleSuSpecificWeight")
fun Property<Quantity<SpecificWeight>>.toDoubleSU():Double = this.value.toDoubleSU()
fun specificWeightSUOf(value:Double):Quantity<SpecificWeight> = criarQuantity(value,unitSpecificWeight)
fun specificWeightSUOf(value:Double, toUnit:Unit<SpecificWeight>):Quantity<SpecificWeight> = criarQuantity(value,unitSpecificWeight).to(toUnit)
fun specificWeightOf(value: Number, unit: Unit<SpecificWeight>): Quantity<SpecificWeight> = criarQuantity(value, unit)
fun specificWeightProp(name: String?): ObjectProperty<Quantity<SpecificWeight>> =
    SimpleObjectProperty<Quantity<SpecificWeight>>(null, name)
fun specificWeightProp(name: String? = null, value: Number, unit: Unit<SpecificWeight>): ObjectProperty<Quantity<SpecificWeight>> {
    return specificWeightProp(name = name).apply { this.value = specificWeightOf(value = value, unit = unit) }
}
fun specificWeightProp(name: String? = null, value: Number, unit: ObjectProperty<Unit<SpecificWeight>>): ObjectProperty<Quantity<SpecificWeight>> {
    return specificWeightProp(name = name, value = value, unit = unit.value).apply {
        conectar(unitProperty = unit)
    }
}
fun specificWeightOf(value: Number): Quantity<SpecificWeight> {
    return specificWeightOf(
        value = value,
        unit = Units.getInstance().getUnit(SpecificWeight::class.java)
    )
}
fun specificWeightProp(name: String? = null, value: Number): ObjectProperty<Quantity<SpecificWeight>> =
    SimpleObjectProperty(null, name, specificWeightOf(value))
@JvmName("asTypeSpecificWeight")
fun Unit<*>.asSpecificWeight():Unit<SpecificWeight> = this.asType(SpecificWeight::class.java)