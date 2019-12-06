package vitorscoelho.ecfx.dimensionamento.estrutural

import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.sign

/**
 * Representa um aço para armadura passiva (CA) com características físicas calculadas conforme NBR6118:2014
 * @property fyk resistência característica ao escoamento da armadura longitudinal
 * @property gamaS coeficiente de ponderação da resistência do aço
 * @property moduloDeDeformacao módulo de deformação do aço, em kN/cm²
 */
class Aco(val fyk: Double, val gamaS: Double, val moduloDeDeformacao: Double) {
    /**Resistência de cálculo ao escoamento da armadura longitudinal, em kN/cm²*/
    val fyd = fyk / gamaS

    /**Deformação específica de cálculo de escoamento do aço*/
    val eyd = fyd / moduloDeDeformacao

    /**
     * Calcula a tensão correspondente a determinada deformação do material
     * @param deformacao deformação específica a qual se deseja ter a tensão correspondente. Positiva quando encurtamento, negativa quando alongamento
     * @return a tensão, em kN/cm², dada a [deformacao]. Positiva quando compressão, negativa quando tração.
     */
    fun tensao(deformacao: Double): Double {
        if (deformacao.absoluteValue < eyd) return deformacao * moduloDeDeformacao
        return fyd * deformacao.sign
    }
}

/**
 * @property bitola diâmetro da barra, em cm
 * @property aco tipo do aço da barra
 */
class BarraAco(val bitola: Double, val aco: Aco) {
    /**Área da seção transversal da barra, em cm²*/
    val area = PI * bitola * bitola / 4.0
}