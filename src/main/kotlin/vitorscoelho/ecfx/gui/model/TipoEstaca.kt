package vitorscoelho.ecfx.gui.model

import tech.units.indriya.unit.Units.METRE
import tech.units.indriya.unit.Units.PASCAL
import tornadofx.toObservable
import vitorscoelho.utils.measure.lengthOf
import vitorscoelho.utils.measure.pressureOf
import javax.measure.MetricPrefix.*
import javax.measure.Quantity
import javax.measure.quantity.Length
import javax.measure.quantity.Pressure

abstract class TipoEstaca() {
    abstract val nome: String
    abstract val fck: Quantity<Pressure>
    abstract val gamaC: Double
    abstract val gamaS: Double
    abstract val comprimentoMinimoArmadura: Quantity<Length>
    abstract val tensaoMediaMaxima: Quantity<Pressure>
    abstract val personalizada: Boolean
    abstract val tubulao: Boolean

    override fun toString() = nome
}

val ESTACA_PERSONALIZADA = object : TipoEstaca() {
    override val nome: String = "Estaca personalizada"
    override val fck: Quantity<Pressure> = pressureOf(value = 20, unit = MEGA(PASCAL))
    override val gamaC: Double = 1.8
    override val gamaS: Double = 1.15
    override val comprimentoMinimoArmadura: Quantity<Length> = lengthOf(value = 4.0, unit = METRE)
    override val tensaoMediaMaxima: Quantity<Pressure> = pressureOf(value = 6.0, unit = MEGA(PASCAL))
    override val personalizada: Boolean = true
    override val tubulao: Boolean = false
}

val HELICE = object : TipoEstaca() {
    override val nome: String = "Estaca hélice"
    override val fck: Quantity<Pressure> = pressureOf(value = 20, unit = MEGA(PASCAL))
    override val gamaC: Double = 1.8
    override val gamaS: Double = 1.15
    override val comprimentoMinimoArmadura: Quantity<Length> = lengthOf(value = 4.0, unit = METRE)
    override val tensaoMediaMaxima: Quantity<Pressure> = pressureOf(value = 6.0, unit = MEGA(PASCAL))
    override val personalizada: Boolean = false
    override val tubulao: Boolean = false
}

val listaEstacas = listOf(
    ESTACA_PERSONALIZADA,
    HELICE
).toObservable()


/*
EstacaPersonalizada("Estaca personalizada", 2, 1.8, 1.15, 400, 0.6, true, false),
        TubulaoPersonalizado("Tubulão personalizado", 2, 1.8, 1.15, 300, 0.5, true, true),
        Helice("Estaca hélice", 2, 1.8, 1.15, 400, 0.6, false, false),
        EscavadaSemFluido("Estaca escavada sem fluido", 1.5, 1.9, 1.15, 200, 0.5, false, false),
        EscavadaComFluido("Estaca escavada com fluido", 2, 1.8, 1.15, 400, 0.6, false, false),
        Strauss("Estaca Strauss", 1.5, 1.9, 1.15, 200, 0.5, false, false),
        Franki("Estaca Franki", 2, 1.8, 1.15, -100, 0, false, false),
        Tubulao("Tubulão não encamisado", 2, 1.8, 1.15, 300, 0.5, false, true),
        Raiz("Estaca raiz", 2, 1.6, 1.15, -100, 0, false, false),
        MicroEstaca("Microestaca", 2, 1.8, 1.15, -100, 0, false, false),
        EstacaTradoVazado("Estaca trado vazado segmetado", 2, 1.8, 1.15, -100, 0, false, false);
 */