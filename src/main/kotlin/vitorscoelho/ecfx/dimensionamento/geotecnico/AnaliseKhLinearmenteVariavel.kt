package vitorscoelho.ecfx.dimensionamento.geotecnico

import vitorscoelho.ecfx.dimensionamento.Esforco
import kotlin.math.pow

class AnaliseKhLinearmenteVariavel(val tubulao: Tubulao, val nh: Double, val kv: Double) : AnaliseTubulao {
    private val khMax = nh * tubulao.comprimento / tubulao.fuste.dimensao
    override fun dimensionar(esforco: Esforco): ResultadosAnaliseTubulao =
        ResultadosKhLinearmenteVariavel(esforco = esforco)

    private inner class ResultadosKhLinearmenteVariavel(override val esforco: Esforco) : ResultadosAnaliseTubulao {
        override val tubulao: Tubulao
            get() = this@AnaliseKhLinearmenteVariavel.tubulao
        private val fuste: FusteTubulao
            get() = tubulao.fuste
        private val base: BaseTubulao
            get() = tubulao.base
        private val comprimento: Double
            get() = tubulao.comprimento

        override val deltaV = esforco.normal / (kv * base.area)
        override val rotacao = run {
            val parcial1 = 3.0 * esforco.momento + 2.0 * esforco.horizontal * comprimento
            val parcial2 = khMax * fuste.dimensao * comprimento.pow(3) / 36.0
            val parcial3 = kv * base.dimensao * base.moduloFlexao / 2.0
            parcial1 / (3.0 * (parcial2 + parcial3))
        }
        override val deltaH =
            2.0 * esforco.horizontal / (khMax * fuste.dimensao * comprimento) + 2.0 * rotacao * comprimento / 3.0
        override val tensaoMediaBase = esforco.normal / base.area
        override val tensaoMinimaBase = tensaoMediaBase - kv * rotacao * base.dimensao / 2.0
        override val tensaoMaximaBase = tensaoMediaBase + kv * rotacao * base.dimensao / 2.0

        override fun coeficienteReacaoHorizontal(z: Double) = khMax * z / comprimento
        override fun tensaoHorizontal(z: Double) = (khMax * z / comprimento) * (deltaH - rotacao * z)
        override fun reacaoHorizontal(z: Double) = tensaoHorizontal(z = z) * fuste.dimensao
        override fun cortante(z: Double): Double {
            val parcial1 = esforco.horizontal
            val parcial2 = khMax * fuste.dimensao * z * z / comprimento
            val parcial3 = rotacao * z / 3.0 - deltaH / 2.0
            return parcial1 + parcial2 * parcial3
        }

        override fun momento(z: Double): Double {
            val parcial1 = esforco.momento + esforco.horizontal * z
            val parcial2 = khMax * fuste.dimensao * z.pow(3) / (6.0 * comprimento)
            val parcial3 = rotacao * z / 2.0 - deltaH
            return parcial1 + parcial2 * parcial3
        }
    }
}
