package vitorscoelho.ecfx.dimensionamento.estrutural

import tornadofx.ValidationMessage
import vitorscoelho.ecfx.dimensionamento.Esforco
import vitorscoelho.ecfx.matematica.integrar
import vitorscoelho.ecfx.matematica.raiz
import java.lang.Exception
import kotlin.math.*

interface Concreto {
    val fctkInf: Double
    val alphaV2: Double
    val fcd: Double
    val fctd: Double
    val fctm: Double
    val fck: Double
    val ec2: Double
    val ecu: Double

    fun tensao(deformacao: Double): Double
}

interface Aco {
    val fyk: Double
    val fyd: Double
    val eyd: Double
    fun tensao(deformacao: Double): Double
}

interface BarraAco {
    val diametro: Double
    val area: Double
    val aco: Aco
}

class SecaoCircular(val diametro: Double, val concreto: Concreto) {
    val area = PI * diametro * diametro / 4.0
    val momentoDeInercia = PI * diametro.pow(4) / 64.0
    val moduloFlexao = 2.0 * momentoDeInercia / diametro
    val bwCisalhamento = diametro
    val dEquiCisalhamento = 0.75 * diametro

    fun bw(y: Double) = 2.0 * sqrt(0.25 * diametro * diametro - y * y)
}

private const val ALONGAMENTO_LIMITE_ACO = -0.01

class FlexaoComposta(val secao: SecaoCircular, val qtdPontosTrechoCurvo: Int, val qtdPontosTrechoReto: Int) {

    private fun yLN(ecg: Double, curvatura: Double) = -ecg / curvatura

    private fun y1y2y3(ecg: Double, curvatura: Double): Triple<Double, Double, Double> {
        val yLN = yLN(ecg = ecg, curvatura = curvatura)
        val yc2 = (secao.concreto.ec2 - ecg) / curvatura
        val y1 = max(
            -0.5 * secao.diametro,
            min(
                yLN,
                0.5 * secao.diametro
            )
        )
        val y2 = max(
            -0.5 * secao.diametro,
            min(
                yc2,
                0.5 * secao.diametro
            )
        )
        val y3 = 0.5 * secao.diametro
        return Triple(y1, y2, y3)
    }

    private fun funcaoNormalResistenteConcreto(y: Double, ecg: Double, curvatura: Double): Double {
        val deformacao = deformacao(y = y, ecg = ecg, curvatura = curvatura)
        val tensao = secao.concreto.tensao(deformacao = deformacao)
        val bw = secao.bw(y = y)
        return tensao * bw
    }

    private fun funcaoMomentoResistenteConcreto(y: Double, ecg: Double, curvatura: Double): Double {
        return funcaoNormalResistenteConcreto(y = y, ecg = ecg, curvatura = curvatura) * y
    }

    private fun integrarIntervalos(
        limiteInferior: Double, limiteIntermediario: Double, limiteSuperior: Double,
        qtdPontosIntervalo1: Int, qtdPontosIntervalo2: Int,
        funcao: (x: Double) -> Double
    ): Double {
        val integral1 = integrar(
            limiteInferior = limiteInferior, limiteSuperior = limiteIntermediario, qtdPontosGauss = qtdPontosIntervalo1,
            funcao = funcao
        )
        val integral2 = integrar(
            limiteInferior = limiteIntermediario, limiteSuperior = limiteSuperior, qtdPontosGauss = qtdPontosIntervalo2,
            funcao = funcao
        )
        return integral1 + integral2
    }

    private fun normalResistenteConcreto(ecg: Double, curvatura: Double): Double {
        val (y1, y2, y3) = y1y2y3(ecg = ecg, curvatura = curvatura)
        return integrarIntervalos(
            limiteInferior = y1, limiteIntermediario = y2, limiteSuperior = y3,
            qtdPontosIntervalo1 = qtdPontosTrechoCurvo, qtdPontosIntervalo2 = qtdPontosTrechoReto
        ) { y -> funcaoNormalResistenteConcreto(y = y, ecg = ecg, curvatura = curvatura) }
    }

    private fun momentoResistenteConcreto(ecg: Double, curvatura: Double): Double {
        val (y1, y2, y3) = y1y2y3(ecg = ecg, curvatura = curvatura)
        return integrarIntervalos(
            limiteInferior = y1, limiteIntermediario = y2, limiteSuperior = y3,
            qtdPontosIntervalo1 = qtdPontosTrechoCurvo, qtdPontosIntervalo2 = qtdPontosTrechoReto
        ) { y -> funcaoMomentoResistenteConcreto(y = y, ecg = ecg, curvatura = curvatura) }
    }

    private fun normalResistenteBarraAco(barra: BarraAco, y: Double, ecg: Double, curvatura: Double): Double {
        val deformacao = deformacao(y = y, ecg = ecg, curvatura = curvatura)
        val tensao = barra.aco.tensao(deformacao = deformacao) - secao.concreto.tensao(deformacao = deformacao)
        return tensao * barra.area
    }

    private fun momentoResistenteBarraAco(barra: BarraAco, y: Double, ecg: Double, curvatura: Double): Double {
        val normal = normalResistenteBarraAco(barra = barra, y = y, ecg = ecg, curvatura = curvatura)
        return normal * y
    }

    private fun normalResistenteAco(barra: BarraAco, yBarras: DoubleArray, ecg: Double, curvatura: Double): Double {
        return yBarras.sumByDouble { y ->
            normalResistenteBarraAco(barra = barra, y = y, ecg = ecg, curvatura = curvatura)
        }
    }

    private fun momentoResistenteAco(barra: BarraAco, yBarras: DoubleArray, ecg: Double, curvatura: Double): Double {
        return yBarras.sumByDouble { y ->
            momentoResistenteBarraAco(barra = barra, y = y, ecg = ecg, curvatura = curvatura)
        }
    }

    private fun normalResistente(barra: BarraAco, yBarras: DoubleArray, ecg: Double, curvatura: Double): Double {
        val normalConcreto = normalResistenteConcreto(ecg = ecg, curvatura = curvatura)
        val normalAco = normalResistenteAco(barra = barra, yBarras = yBarras, ecg = ecg, curvatura = curvatura)
        return normalConcreto + normalAco
    }

    private fun momentoResistente(barra: BarraAco, yBarras: DoubleArray, ecg: Double, curvatura: Double): Double {
        val momentoConcreto = momentoResistenteConcreto(ecg = ecg, curvatura = curvatura)
        val momentoAco = momentoResistenteAco(barra = barra, yBarras = yBarras, ecg = ecg, curvatura = curvatura)
        return momentoConcreto + momentoAco
    }

    private fun curvaturaELU(ecg: Double, d: Double): Double {
        val x5 = (secao.concreto.ecu - secao.concreto.ec2) * secao.diametro / secao.concreto.ecu
        val curvatura1 = (secao.concreto.ec2 - ecg) / (0.5 * secao.diametro - x5)
        val curvatura2 = (secao.concreto.ecu - ecg) / (0.5 * secao.diametro)
        val curvatura3 = (ecg + ALONGAMENTO_LIMITE_ACO) / (d - 0.5 * secao.diametro)
        return doubleArrayOf(curvatura1, curvatura2, curvatura3).min()!!
    }

    private fun ecgMax(): Double = secao.concreto.ec2

    private fun ecgMin(barra: BarraAco, yMin: Double, yMax: Double): Double {
        val eSmaxAdot: Double = max(barra.aco.eyd, ALONGAMENTO_LIMITE_ACO)
        return yMax * (ALONGAMENTO_LIMITE_ACO + eSmaxAdot) / (yMax + yMin) - eSmaxAdot
    }

    private fun normalResistenteMaxima(barra: BarraAco, yBarras: DoubleArray, ecgMax: Double): Double {
        val areaAco = yBarras.size * barra.area
        val normalAco = areaAco * barra.aco.tensao(deformacao = ecgMax)
        val areaConcreto = secao.area - areaAco
        val normalConcreto = areaConcreto * secao.concreto.tensao(deformacao = ecgMax)
        return normalAco + normalConcreto
    }

    fun normalResistenteMaxima(barra: BarraAco, yBarras: DoubleArray): Double {
        val ecgMax = ecgMax()
        return normalResistenteMaxima(barra = barra, yBarras = yBarras, ecgMax = ecgMax)
    }

    private fun normalResistenteMinima(barra: BarraAco, yBarras: DoubleArray, d: Double, ecgMin: Double): Double {
        val curvatura = curvaturaELU(ecg = ecgMin, d = d)
        return normalResistente(barra = barra, yBarras = yBarras, ecg = ecgMin, curvatura = curvatura)
    }

    fun normalResistenteMinima(barra: BarraAco, yBarras: DoubleArray): Double {
        val yMin = yBarras.min()!!
        val yMax = yBarras.max()!!
        val d = yMin + secao.diametro / 2.0
        val ecgMin = ecgMin(barra = barra, yMin = yMin, yMax = yMax)
        return normalResistenteMinima(barra = barra, yBarras = yBarras, d = d, ecgMin = ecgMin)
    }

    fun momentoResistente(
        normal: Double,
        barra: BarraAco,
        yBarras: DoubleArray,
        deltaNTolerancia: Double,
        limiteIteracoes: Int
    ): Double {
        val yMin = yBarras.min()!!
        val yMax = yBarras.max()!!
        val d = yMin + secao.diametro / 2.0

        val ecgMax = ecgMax()
        val normalResistenteMaxima = normalResistenteMaxima(barra = barra, yBarras = yBarras, ecgMax = ecgMax)
        if (normal > normalResistenteMaxima) throw ImpossivelDimensionarException("Ruptura por compressão")
        val ecgMin = ecgMin(barra = barra, yMin = yMin, yMax = yMax)
        val normalResistenteMinima = normalResistenteMinima(barra = barra, yBarras = yBarras, d = d, ecgMin = ecgMin)
        if (normal < normalResistenteMinima) throw ImpossivelDimensionarException("Ruptura por tração")

        val ecgResposta = raiz(
            xEsquerda = ecgMin, fxEsquerda = normalResistenteMinima,
            xDireita = ecgMax, fxDireita = normalResistenteMaxima,
            acuraciaAbsoluta = deltaNTolerancia, limiteDeIteracoes = limiteIteracoes
        ) { ecg ->
            val curvatura = curvaturaELU(ecg = ecg, d = d)
            val normalResistente = normalResistente(barra = barra, yBarras = yBarras, ecg = ecg, curvatura = curvatura)
            normal - normalResistente
        }
        val curvatura = curvaturaELU(ecg = ecgResposta, d = d)
        return momentoResistente(barra = barra, yBarras = yBarras, ecg = ecgResposta, curvatura = curvatura)
    }

    companion object {
        private fun deformacao(y: Double, ecg: Double, curvatura: Double): Double = ecg + y * curvatura
    }
}

class ImpossivelDimensionarException(message: String) : Exception(message)

//    private fun integralEsforcoResistenteConcreto(
//        limiteInferior: Double,
//        limiteSuperior: Double,
//        qtdPontosGauss: Int,
//        ecg: Double,
//        curvatura: Double
//    ): Esforco {
//        val integral = integrar(
//            limiteInferior = limiteInferior,
//            limiteSuperior = limiteSuperior,
//            qtdPontosGauss = qtdPontosGauss,
//            qtdFuncoes = 2
//        ) { y ->
//            val deformacao = deformacao(y = y, ecg = ecg, curvatura = curvatura)
//            val tensao = secao.concreto.tensao(deformacao = deformacao)
//            val bw = secao.bw(y = y)
//            val normal = tensao * bw
//            val momento = normal * y
//            doubleArrayOf(normal, momento)
//        }
//        return Esforco(normal = integral[0], horizontal = 0.0, momento = integral[1])
//    }

//    private fun esforcoResistenteConcreto(ecg: Double, curvatura: Double): Esforco {
//        val yLN = -ecg / curvatura
//        val yc2 = (secao.concreto.ec2 - ecg) / curvatura
//        val y1 = max(
//            -0.5 * secao.diametro,
//            min(
//                yLN,
//                0.5 * secao.diametro
//            )
//        )
//        val y2 = max(
//            -0.5 * secao.diametro,
//            min(
//                yc2,
//                0.5 * secao.diametro
//            )
//        )
//        val y3 = 0.5 * secao.diametro
//
//        val esforcoResistenteTrechoCurvo = integralEsforcoResistenteConcreto(
//            limiteInferior = y1, limiteSuperior = y2,
//            qtdPontosGauss = qtdPontosTrechoCurvo,
//            ecg = ecg, curvatura = curvatura
//        )
//        val esforcoResistenteTrechoReto = integralEsforcoResistenteConcreto(
//            limiteInferior = y2, limiteSuperior = y3,
//            qtdPontosGauss = qtdPontosTrechoCurvo,
//            ecg = ecg, curvatura = curvatura
//        )
//        return esforcoResistenteTrechoCurvo + esforcoResistenteTrechoReto
//    }
//
//    private fun esforcoResistenteAco(barra: BarraAco, yBarras: DoubleArray, ecg: Double, curvatura: Double): Esforco {
//        var normal = 0.0
//        var momento = 0.0
//        yBarras.forEach { y ->
//            val deformacao = deformacao(y = y, ecg = ecg, curvatura = curvatura)
//            val tensao = barra.aco.tensao(deformacao = deformacao) - secao.concreto.tensao(deformacao = deformacao)
//            val normalParcial = tensao * barra.area
//            normal += normalParcial
//            momento += normalParcial * y
//        }
//        return Esforco(normal = normal, horizontal = 0.0, momento = momento)
//    }
//
//    private fun esforcoResistente(barra: BarraAco, yBarras: DoubleArray, ecg: Double, curvatura: Double): Esforco {
//        val esforcoResistenteConcreto = esforcoResistenteConcreto(ecg = ecg, curvatura = curvatura)
//        val esforcoResistenteAco = esforcoResistenteAco(
//            barra = barra, yBarras = yBarras, ecg = ecg, curvatura = curvatura
//        )
//        return esforcoResistenteConcreto + esforcoResistenteAco
//    }
//    fun momentoResistente(
//        normal: Double,
//        barra: BarraAco,
//        yBarras: DoubleArray,
//        deltaNTolerancia: Double,
//        limiteIteracoes: Int
//    ): Double {
//        val yMin = yBarras.min()!!
//        val yMax = yBarras.max()!!
//        val d = yMin + secao.diametro / 2.0
//
//        val ecgMax = ecgMax()
//        val normalResistenteMaxima = normalResistenteMaxima(barra = barra, yBarras = yBarras, ecgMax = ecgMax)
//        if (normal > normalResistenteMaxima) throw ImpossivelDimensionarException("Ruptura por compressão")
//        val ecgMin = ecgMin(barra = barra, yMin = yMin, yMax = yMax)
//        val normalResistenteMinima = normalResistenteMinima(barra = barra, yBarras = yBarras, d = d, ecgMin = ecgMin)
//        if (normal < normalResistenteMinima) throw ImpossivelDimensionarException("Ruptura por tração")
//
//        val ecgResposta = raiz(
//            xEsquerda = ecgMin, fxEsquerda = normalResistenteMinima,
//            xDireita = ecgMax, fxDireita = normalResistenteMaxima,
//            acuraciaAbsoluta = deltaNTolerancia, limiteDeIteracoes = limiteIteracoes
//        ) { ecg ->
//            val curvatura = curvaturaELU(ecg = ecg, d = d)
//            esforcoResistente(barra = barra, yBarras = yBarras, ecg = ecg, curvatura = curvatura).normal
//        }
//        val curvatura = curvaturaELU(ecg = ecgResposta, d = d)
//        return esforcoResistente(barra = barra, yBarras = yBarras, ecg = ecgResposta, curvatura = curvatura).normal
//    }