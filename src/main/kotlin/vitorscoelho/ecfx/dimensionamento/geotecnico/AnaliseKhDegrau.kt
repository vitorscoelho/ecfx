package vitorscoelho.ecfx.dimensionamento.geotecnico

import vitorscoelho.ecfx.dimensionamento.Esforco
import kotlin.math.pow

class AnaliseKhDegrau(
    val tubulao: Tubulao, val kh1: Double, val kh2: Double, val l1: Double = 0.0, val kv: Double
) : AnaliseTubulao {
    constructor(tubulao: Tubulao, kh2: Double, kv: Double, moduloElasticidade: Double) : this(
        tubulao = tubulao,
        kh1 = kh1Padrao(kh2 = kh2),
        kh2 = kh2,
        l1 = l1Padrao(
            moduloElasticidade = moduloElasticidade,
            inerciaFuste = tubulao.fuste.momentoDeInercia,
            kh2 = kh2
        ),
        kv = kv
    )

    override fun dimensionar(esforco: Esforco): ResultadosAnaliseTubulao = ResultadosKhDegrau(esforco = esforco)

    private inner class ResultadosKhDegrau(override val esforco: Esforco) : ResultadosAnaliseTubulao {
        override val tubulao: Tubulao
            get() = this@AnaliseKhDegrau.tubulao
        private val fuste: FusteTubulao
            get() = tubulao.fuste
        private val base: BaseTubulao
            get() = tubulao.base
        private val comprimento: Double
            get() = tubulao.comprimento

        override val deltaV = esforco.normal / (kv * base.area)
        override val deltaH: Double = run {
            val parcial1 = 0.5 * rotacao * fuste.dimensao
            val parcial2 = kh1 * l1.pow(2) + kh2 * (comprimento.pow(2) - l1.pow(2))
            val parcial3 = fuste.dimensao * (kh1 * l1 + kh2 * (comprimento - l1))
            (parcial1 * parcial2 + esforco.horizontal) / parcial3
        }
        override val rotacao: Double = run {
            val parcial1 = kv * base.dimensao * base.moduloFlexao / 2.0
            val parcial2 = (kh1 * l1.pow(3) + kh2 * (comprimento.pow(3) - l1.pow(3))) * fuste.dimensao / 3.0
            val parcial3 = deltaH * fuste.dimensao / 2.0
            val parcial4 = kh1 * l1.pow(2) + kh2 * (comprimento.pow(2) - l1.pow(2))
            (esforco.momento + (parcial3 * parcial4)) / (parcial1 * parcial2)
        }

        override val tensaoMediaBase = esforco.normal / base.area
        override val tensaoMinimaBase = tensaoMediaBase - kv * rotacao * base.dimensao / 2.0
        override val tensaoMaximaBase = tensaoMediaBase + kv * rotacao * base.dimensao / 2.0

        override fun coeficienteReacaoHorizontal(z: Double) = if (z <= l1) kh1 else kh2

        override fun tensaoHorizontal(z: Double): Double {
            if (z <= l1) return kh1 * (deltaH - rotacao * z)
            return kh2 * (deltaH - rotacao * z)
        }

        override fun reacaoHorizontal(z: Double): Double {
            if (z <= l1) return kh1 * fuste.dimensao * (deltaH - rotacao * z)
            return kh2 * fuste.dimensao * (deltaH - rotacao * z)
        }

        override fun cortante(z: Double): Double {
            if (z <= l1) {
                return esforco.horizontal - kh1 * deltaH * fuste.dimensao * z + kh1 * rotacao * fuste.dimensao * z.pow(2) / 2.0
            }
            val parcial1 = deltaH * fuste.dimensao * (kh1 * l1 + kh2 * (z - l1))
            val parcial2 = rotacao * fuste.dimensao / 2.0
            val parcial3 = kh1 * l1.pow(2) + kh2 * (z.pow(2) - l1.pow(2))
            return esforco.horizontal - parcial1 + parcial2 * parcial3
        }

        override fun momento(z: Double): Double {
            if (z <= l1) {
                val parcial1 = kh1 * deltaH * fuste.dimensao * z.pow(2) / 2.0
                val parcial2 = kh1 * rotacao * fuste.dimensao * z.pow(3) / 6.0
                return esforco.momento + esforco.horizontal * z - parcial1 + parcial2
            }
            val parcial1 = esforco.momento + esforco.horizontal * z
            val parcial2 = deltaH * fuste.dimensao / 2.0
            val parcial3 = kh1 * l1.pow(2) + kh2 * (z.pow(2) - l1.pow(2))
            val parcial4 = rotacao * fuste.dimensao / 6.0
            val parcial5 =
                kh1 * rotacao * fuste.dimensao * l1.pow(3) + kh2 * rotacao * fuste.dimensao * (z.pow(3) - l1.pow(3))
            return parcial1 - parcial2 * parcial3 + parcial4 * parcial5
        }
    }

    companion object {
        fun l1Padrao(moduloElasticidade: Double, inerciaFuste: Double, kh2: Double): Double {
            val r = (moduloElasticidade * inerciaFuste / kh2).pow(1.0 / 4.0)
            return 0.4 * r
        }

        fun kh1Padrao(kh2: Double): Double = 0.5 * kh2
    }
}