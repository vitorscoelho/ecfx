package vitorscoelho.ecfx.dimensionamento.estrutural

import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

class AgregadoGraudo(val nome: String, private val moduloDeformacaoSecante: (fck: Double) -> Double) {
    fun moduloDeformacaoSecante(fck: Double) = moduloDeformacaoSecante.invoke(fck)
    override fun toString(): String = nome

    companion object {
        val BASALTO = AgregadoGraudo(nome = "Basalto") { fck -> moduloSecante(fck = fck, alphaE = 1.2) }
        val DIABASIO = AgregadoGraudo(nome = "Diabásio") { fck -> moduloSecante(fck = fck, alphaE = 1.2) }
        val GRANITO = AgregadoGraudo(nome = "Granito") { fck -> moduloSecante(fck = fck, alphaE = 1.0) }
        val GNAISSE = AgregadoGraudo(nome = "Gnaisse") { fck -> moduloSecante(fck = fck, alphaE = 1.0) }
        val CALCARIO = AgregadoGraudo(nome = "Calcário") { fck -> moduloSecante(fck = fck, alphaE = 0.9) }
        val ARENITO = AgregadoGraudo(nome = "Arenito") { fck -> moduloSecante(fck = fck, alphaE = 0.7) }

        private fun moduloSecante(fck: Double, alphaE: Double): Double {
            require(fck > 0.0) { "|fck| deve ser maior que 0" }
            val moduloTangente = if (fck <= 5.0) {
                alphaE * 560.0 * sqrt(fck * 10.0)
            } else {
                alphaE * 2150.0 * (fck + 1.25).pow(1.0 / 3.0)
            }
            val alphaI = min((0.8 + 0.2 * fck / 8.0), 1.0)
            return alphaI * moduloTangente
        }
    }
}