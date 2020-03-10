package vitorscoelho.ecfx.novagui.configuracoes

import vitorscoelho.ecfx.novagui.utils.Committable
import vitorscoelho.ecfx.novagui.utils.ImplCommittable
import vitorscoelho.ecfx.novagui.utils.ImplCommittableSameType
import vitorscoelho.utils.measure.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import javax.measure.Quantity
import javax.measure.Unit

private val DFS = DecimalFormatSymbols(Locale("en", "US"))

object Formatos {
    private val DUAS_CASAS = DecimalFormat("0.##", DFS)
    private val TRES_CASAS = DecimalFormat("0.###", DFS)
    private val CIENTIFICA_DUAS_CASAS = DecimalFormat("0.0#E0", DFS)

    val resistenciaMaterial = Formato(initUnit = MEGAPASCAL, initFormat = DUAS_CASAS)
    val moduloElasticidadeConcreto = Formato(initUnit = MEGAPASCAL, initFormat = DUAS_CASAS)
    val moduloElasticidadeAco = Formato(initUnit = GIGAPASCAL, initFormat = DUAS_CASAS)
    val bitola = Formato(initUnit = MILLIMETRE, initFormat = DUAS_CASAS)
    val cobrimento = Formato(initUnit = CENTIMETRE, initFormat = DUAS_CASAS)
    val dimensoesEstaca = Formato(initUnit = CENTIMETRE, initFormat = DUAS_CASAS)
    val profundidade = Formato(initUnit = METRE, initFormat = DUAS_CASAS)
    val forca = Formato(initUnit = KILONEWTON, initFormat = DUAS_CASAS)
    val momento = Formato(initUnit = KILONEWTON.multiply(METRE).asMoment(), initFormat = DUAS_CASAS)
    val coeficienteReacao = Formato(
        initUnit = KILONEWTON.divide(CUBIC_METRE).asSpringStiffnessPerUnitArea(), initFormat = DUAS_CASAS
    )
    val coesao = Formato(initUnit = KILOPASCAL, initFormat = DUAS_CASAS)
    val anguloDeAtrito = Formato(initUnit = DEGREE, initFormat = DUAS_CASAS)
    val pesoEspecifico = Formato(initUnit = KILONEWTON.divide(CUBIC_METRE).asSpecificWeight(), initFormat = DUAS_CASAS)
    val tensaoSolo = Formato(initUnit = KILOPASCAL, initFormat = DUAS_CASAS)
    val deslocamento = Formato(initUnit = MILLIMETRE, initFormat = DUAS_CASAS)
    val rotacao = Formato(initUnit = RADIAN, initFormat = CIENTIFICA_DUAS_CASAS)
    val coeficienteDeSeguranca = Formato(initUnit = ONE, initFormat = TRES_CASAS)
    val reacaoLateralNaEstaca = Formato(
        initUnit = KILONEWTON.divide(METRE).asForcePerUnitLength(), initFormat = DUAS_CASAS
    )
}

class Formato<Q : Quantity<Q>>(initUnit: Unit<Q>, initFormat: DecimalFormat) {
    val unit = ImplCommittableSameType(initValue = initUnit)
    val format = FormatProp(initValue = initFormat)

    fun toString(qtd: Quantity<Q>): String = format.value.format(qtd.to(unit.value).value)
    fun toStringComUnidade(qtd: Quantity<Q>): String = "${toString(qtd)}${unit.value}"
}

private val decimalFormatToString = { from: DecimalFormat -> from.toPattern() }
private val stringToDecimalFormat = { from: String -> DecimalFormat(from, DFS) }

class FormatProp(
    initValue: DecimalFormat
) : Committable<String, DecimalFormat> by ImplCommittable(
    initValue = initValue,
    convertToTransitional = decimalFormatToString,
    convertToCommit = stringToDecimalFormat
)