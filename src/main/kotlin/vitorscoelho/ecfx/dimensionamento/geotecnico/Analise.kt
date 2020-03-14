package vitorscoelho.ecfx.dimensionamento.geotecnico

import vitorscoelho.ecfx.dimensionamento.Esforco
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min
import kotlin.properties.Delegates

interface AnaliseTubulao {
    fun dimensionar(esforco: Esforco): ResultadosAnaliseTubulao
}

interface ResultadosAnaliseTubulao {
    val tubulao: Tubulao
    val esforco: Esforco
    val deltaV: Double
    val deltaH: Double
    val rotacao: Double
    val tensaoMinimaBase: Double
    val tensaoMaximaBase: Double
    val tensaoMediaBase: Double
    fun coeficienteReacaoHorizontal(z: Double): Double
    fun tensaoHorizontal(z: Double): Double
    fun reacaoHorizontal(z: Double): Double
    fun cortante(z: Double): Double
    fun momento(z: Double): Double
    val xCoeficienteReacaoHorizontalMax: Double
    val xTensaoHorizontalMax: Double
    val xReacaoHorizontalMax: Double
    val xCortanteMax: Double
    val xMomentoMax: Double
    val coeficienteReacaoHorizontalMax: Double
    val tensaoHorizontalMax: Double
    val reacaoHorizontalMax: Double
    val cortanteMax: Double
    val momentoMax: Double
}

private val DELTAS = doubleArrayOf(5.0, 1.0, 0.1)
internal fun ResultadosAnaliseTubulao.xMaximoAbsoluto(funcao: (x: Double) -> Double): Double {
    return xValorMaximoAbsoluto(
        limiteInferior = 0.0,
        limiteSuperior = tubulao.comprimento,
        deltas = DELTAS,
        funcao = funcao
    )
}

internal fun xValorMaximoAbsoluto(
    limiteInferior: Double, limiteSuperior: Double, delta: Double, funcao: (x: Double) -> Double
): Double {
    require(limiteSuperior > limiteInferior) { "|limiteSuperior| deve ser maior que |limiteInferior|" }
    val deltaTotal = limiteSuperior - limiteInferior
    val qtdEspacos: Int = (deltaTotal / delta).toInt() + 1
    val deltaAdotado = deltaTotal / qtdEspacos
    return (0..qtdEspacos).map { limiteInferior + it * deltaAdotado }.maxBy {
        val x = min(it, limiteSuperior)
        funcao(x).absoluteValue
    }!!
}

internal fun xValorMaximoAbsoluto(
    limiteInferior: Double, limiteSuperior: Double, deltas: DoubleArray, funcao: (x: Double) -> Double
): Double {
    require(deltas.isNotEmpty()) { "|deltas| não pode ser vazio" }
    val deltasAdotados = deltas.sortedArrayDescending()
    var x by Delegates.notNull<Double>()
    var limiteInferiorAtual = limiteInferior
    var limiteSuperiorAtual = limiteSuperior
    deltasAdotados.forEach { delta ->
        x = xValorMaximoAbsoluto(
            limiteInferior = limiteInferiorAtual, limiteSuperior = limiteSuperiorAtual,
            delta = delta,
            funcao = funcao
        )
        limiteInferiorAtual = max(limiteInferior, x - delta)
        limiteSuperiorAtual = min(limiteSuperior, x + delta)
    }
    return x
}