package vitorscoelho.ecfx.dimensionamento.estrutural

import vitorscoelho.ecfx.dimensionamento.Esforco
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

class SecaoCircular(val diametro: Double) {
    fun bw(y: Double) = 2.0 * sqrt(0.25 * diametro * diametro - y * y)
}

class FlexaoComposta(val secao: SecaoCircular) {
//    fun esforcoResistente(deformada: Deformada): Esforco {
//        val yLn = 0.0 //TODO
//        val y1 = max(
//            -0.5 * secao.diametro,
//            min(
//            )
//        )
//    }
}

fun main() {

}