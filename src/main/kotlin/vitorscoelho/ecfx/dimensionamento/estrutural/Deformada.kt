package vitorscoelho.ecfx.dimensionamento.estrutural

/**
 * Representa uma regra que é capaz de determinar a deformação em qualquer ponto do plano cartesiano
 * @property deformacaoY0 deformação específica na origem do plano cartesiano
 * @property curvatura vetor que representa a curvatura da seção, em 1/cm
 */
class Deformada(val deformacaoY0: Double, val curvatura: Double) {
    /**
     * Retorna a deformação específica em um determinado ponto. Positiva para encurtamento e negativa para alongamento.
     * @param y a ordenada do ponto que se deseja saber a deformação específica
     */
    fun deformacao(y: Double): Double = deformacaoY0 + curvatura * y
}