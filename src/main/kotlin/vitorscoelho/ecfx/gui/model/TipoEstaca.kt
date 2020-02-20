package vitorscoelho.ecfx.gui.model

import tech.units.indriya.ComparableQuantity
import vitorscoelho.utils.measure.MEGAPASCAL
import vitorscoelho.utils.measure.METRE
import vitorscoelho.utils.measure.lengthOf
import vitorscoelho.utils.measure.pressureOf
import javax.measure.Quantity
import javax.measure.quantity.Length
import javax.measure.quantity.Pressure

class TipoEstaca(
    val nome: String,
    val fck: Quantity<Pressure>,
    val gamaC: Double,
    val gamaS: Double,
    val comprimentoMinimoArmadura: Quantity<Length>,
    val tensaoMediaMaxima: Quantity<Pressure>,
    val tubulao: Boolean
) {
    constructor(
        nome: String,
        fck: Quantity<Pressure>,
        gamaC: Double,
        gamaS: Double,
        tubulao: Boolean
    ) : this(
        nome = nome, fck = fck, gamaC = gamaC, gamaS = gamaS,
        comprimentoMinimoArmadura = comprimentoParaConsiderarArmaduraIntegral,
        tensaoMediaMaxima = pressureOf(value = 0, unit = MEGAPASCAL),
        tubulao = tubulao
    )

    val armaduraIntegral: Boolean =
        ((comprimentoMinimoArmadura as ComparableQuantity<Length>) >= comprimentoParaConsiderarArmaduraIntegral)

    override fun toString() = nome

    companion object {
        private val comprimentoParaConsiderarArmaduraIntegral: ComparableQuantity<Length> =
            lengthOf(value = 1_000_000, unit = METRE) as ComparableQuantity<Length>
    }
}

val TUBULAO_NAO_ENCAMISADO = TipoEstaca(
    nome = "Tubulão não encamisado",
    fck = pressureOf(value = 20, unit = MEGAPASCAL),
    gamaC = 1.8,
    gamaS = 1.15,
    comprimentoMinimoArmadura = lengthOf(value = 3, unit = METRE),
    tensaoMediaMaxima = pressureOf(value = 5, unit = MEGAPASCAL),
    tubulao = true
)

val HELICE = TipoEstaca(
    nome = "Estaca hélice",
    fck = pressureOf(value = 20, unit = MEGAPASCAL),
    gamaC = 1.8,
    gamaS = 1.15,
    comprimentoMinimoArmadura = lengthOf(value = 4, unit = METRE),
    tensaoMediaMaxima = pressureOf(value = 6, unit = MEGAPASCAL),
    tubulao = false
)

val ESCAVADA_SEM_FLUIDO = TipoEstaca(
    nome = "Estaca escavada sem fluido",
    fck = pressureOf(value = 15, unit = MEGAPASCAL),
    gamaC = 1.9,
    gamaS = 1.15,
    comprimentoMinimoArmadura = lengthOf(value = 2, unit = METRE),
    tensaoMediaMaxima = pressureOf(value = 5, unit = MEGAPASCAL),
    tubulao = false
)

val ESCAVADA_COM_FLUIDO = TipoEstaca(
    nome = "Estaca escavada com fluido",
    fck = pressureOf(value = 20, unit = MEGAPASCAL),
    gamaC = 1.8,
    gamaS = 1.15,
    comprimentoMinimoArmadura = lengthOf(value = 4, unit = METRE),
    tensaoMediaMaxima = pressureOf(value = 6, unit = MEGAPASCAL),
    tubulao = false
)

val STRAUSS = TipoEstaca(
    nome = "Estaca Strauss",
    fck = pressureOf(value = 15, unit = MEGAPASCAL),
    gamaC = 1.9,
    gamaS = 1.15,
    comprimentoMinimoArmadura = lengthOf(value = 2, unit = METRE),
    tensaoMediaMaxima = pressureOf(value = 5, unit = MEGAPASCAL),
    tubulao = false
)

val FRANKI = TipoEstaca(
    nome = "Estaca Franki",
    fck = pressureOf(value = 20, unit = MEGAPASCAL),
    gamaC = 1.8,
    gamaS = 1.15,
    tubulao = false
)

val RAIZ = TipoEstaca(
    nome = "Estaca raiz",
    fck = pressureOf(value = 20, unit = MEGAPASCAL),
    gamaC = 1.6,
    gamaS = 1.15,
    tubulao = false
)

val MICRO_ESTACA = TipoEstaca(
    nome = "Microestaca",
    fck = pressureOf(value = 20, unit = MEGAPASCAL),
    gamaC = 1.8,
    gamaS = 1.15,
    tubulao = false
)

val ESTACA_TRADO_VAZADO = TipoEstaca(
    nome = "Estaca trado vazado segmentado",
    fck = pressureOf(value = 20, unit = MEGAPASCAL),
    gamaC = 1.8,
    gamaS = 1.15,
    tubulao = false
)