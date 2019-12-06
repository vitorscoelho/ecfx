package vitorscoelho.ecfx.dimensionamento.estrutural

import kotlin.math.ln
import kotlin.math.pow

/**
 * Representa um concreto com características físicas calculadas conforme NBR6118:2014
 * @property fck Resistência característica à compressão, em kN/cm²
 * @property gamaC Coeficiente de ponderação da resistência do concreto
 */
class Concreto(val fck: Double, val gamaC: Double) {
    /**Parâmetro que influencia no diagrama tensão-deformação do concreto*/
    private val n: Double

    /**Resistência de cálculo à compressão do concreto, em kN/cm²*/
    val fcd: Double = fck / gamaC

    /**Parâmetro que influencia na resistência ao cisalhamento de uma seção transversal de concreto. Adimensional*/
    val alphaV2: Double

    /**Resistência de cálculo à tração do concreto, em kN/cm²*/
    val fctd: Double

    /**Resistência média à tração do concreto, em kN/cm²*/
    val fctm: Double

    /**Resistência característica inferior à tração do concreto, em kN/cm²*/
    val fctkInf: Double

    /**Resistência característica superior à tração do concreto, em kN/cm²*/
    val fctkSup: Double

    /**Deformação específica de encurtamento do concreto no início do patamar plástico. Adimensional*/
    val ec2: Double

    /**Deformação específica no centro de gravidade da seção transversal. Adimensional*/
    val ecu: Double

    init {
        if (fck <= 5.0) {
            this.n = 2.0
            this.ec2 = 2.0
            this.ecu = 3.5
            this.fctm = 0.03 * (fck * 10.0).pow(2.0 / 3.0)
        } else {
            this.n = 1.4 + 23.4 * ((9.0 - fck) / 10.0).pow(4)
            this.fctm = 0.212 * ln(1.0 + 0.11 * fck * 10.0)
            this.ec2 = 2.0 + 0.085 * (fck * 10.0 - 50.0).pow(0.53)
            this.ecu = 2.6 + 35.0 * ((9.0 - fck) / 10.0).pow(4)
        }
        this.fctkInf = 0.7 * fctm
        this.fctkSup = 1.3 * fctm
        this.alphaV2 = (1.0 - (fck * 10.0) / 250.0)
        this.fctd = fctkInf / gamaC
    }

    /**
     * Calcula a tensão correspondente a determinada deformação do material
     * @param deformacao deformação específica a qual se deseja ter a tensão correspondente. Positiva quando encurtamento, negativa quando alongamento
     * @return a tensão, em kN/cm², dada a [deformacao]. Positiva quando compressão, negativa quando tração.
     */
    fun tensao(deformacao: Double): Double {
        if (deformacao <= 0.0) return 0.0
        if (deformacao < ec2) return K_MOD * fcd * (1 - (1 - deformacao / ec2).pow(n))
        return K_MOD * fcd
    }

    companion object {
        /**Fator de redução para resistência a longa duração do concreto. kMod=kMod1*kMod2*kMod3=0.95*1.20*0.75=0.855*/
        private const val K_MOD = 0.85
    }
}