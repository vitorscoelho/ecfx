package vitorscoelho.ecfx.dimensionamento.estrutural

import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sqrt

class SecaoCircular(val diametro: Double, val concreto: Concreto) {
    val area = PI * diametro * diametro / 4.0
    val momentoDeInercia = PI * diametro.pow(4) / 64.0
    val moduloFlexao = 2.0 * momentoDeInercia / diametro
    val bwCisalhamento = diametro
    val dEquiCisalhamento = 0.75 * diametro

    fun bw(y: Double) = 2.0 * sqrt(0.25 * diametro * diametro - y * y)
}