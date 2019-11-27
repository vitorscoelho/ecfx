package vitorscoelho.ecfx.dimensionamento.estrutural

import kotlin.math.max
import kotlin.math.min

data class ResultadosCisalhamento(
    val secao: SecaoCircular,
    val fywk: Double,
    val fywd: Double,
    val vrd2: Double,
    val vc0: Double,
    val vc: Double,
    val vsw: Double,
    val vrd3: Double,
    val vu: Double,
    val momentoDeDescompressao: Double,
    val vswMin: Double,
    val aswCalculada: Double,
    val aswMinima: Double,
    val aswAdotada: Double
) {
    fun espacamentoMaximo(
        vd: Double,
        bitolaEstribo: Double,
        bitolaLongitudinal: Double,
        fykLongitudinal: Double
    ): Double {
        //Espaçamento maximo para pilar
        var espacamentoMaximo = max(
            0.01,
            doubleArrayOf(
                20.0,
                secao.diametro,
                (24.0 - 12.0 * (fykLongitudinal - 25.0) / 25.0) * bitolaLongitudinal
            ).min()!!
        )

        //Espaçamento máximo para viga
        if (aswMinima == 0.0) {
            val espacamentoMaximoParaViga = if (vd <= 0.67 * vrd2) {
                min(0.6 * secao.dEquiCisalhamento, 30.0)
            } else {
                min(0.3 * secao.dEquiCisalhamento, 20.0)
            }
            espacamentoMaximo = min(espacamentoMaximo, espacamentoMaximoParaViga)
        }

        //Espaçamento máximo para bitolas mais finas que 1/4 da bitola da armadura longitudinal
        if (bitolaEstribo < bitolaLongitudinal / 4.0) {
            val espacamentoMaximoParaEstribosMaisFinos =
                9_000.0 * (bitolaEstribo * bitolaEstribo / bitolaLongitudinal) / max(fywk, fykLongitudinal)
            espacamentoMaximo = min(espacamentoMaximo, espacamentoMaximoParaEstribosMaisFinos)
        }

        return espacamentoMaximo
    }
}

class CisalhamentoConcretoArmado(val secao: SecaoCircular, val fywk: Double, val fywd: Double) {
    val vrd2 = 0.27 * secao.concreto.alphaV2 * secao.concreto.fcd * secao.bwCisalhamento * secao.dEquiCisalhamento
    val vc0 = 0.6 * secao.concreto.fctd * secao.bwCisalhamento * secao.dEquiCisalhamento
    val aswMinimaViga = 0.2 * secao.bwCisalhamento * secao.concreto.fctm / fywk

    private fun m0(nk: Double): Double = max(0.0, nk * secao.moduloFlexao / secao.area)

    private fun vc(nd: Double, md: Double, linhaNeutraCortaSecao: Boolean, momentoDeDescompressao: Double): Double {
        if (nd < 0.0 && !linhaNeutraCortaSecao) return 0.0
        if (nd <= 0.0) return vc0
        val vc = vc0 * (1.0 + momentoDeDescompressao) / md
        val vcLimite = 2.0 * vc0
        return min(vc, vcLimite)
    }

    private fun aswCalculada(vd: Double, linhaNeutraCortaSecao: Boolean, vc: Double): Double {
        val asw = (vd - vc) / (0.9 * secao.dEquiCisalhamento * fywd)
        return max(0.0, asw)
    }

    private fun aswMinima(nd: Double, vd: Double, md: Double, vc: Double): Double {
        val momentoFissuracao = secao.moduloFlexao * (nd / secao.area + secao.concreto.fctkInf)
        val comprimido = (nd > 0.0)
        val naoFissura = (md <= momentoFissuracao)
        val cortanteMenorQueVc = (vd <= vc)
        val naoUsarArmaduraMinimaViga = comprimido && naoFissura && cortanteMenorQueVc
        return if (naoUsarArmaduraMinimaViga) 0.0 else aswMinimaViga
    }

    private fun vsw(asw: Double): Double = asw * (0.9 * secao.dEquiCisalhamento * fywd)

    fun dimensionar(
        nk: Double,
        nd: Double, vd: Double, md: Double,
        linhaNeutraCortaSecao: Boolean
    ): ResultadosCisalhamento {
        val momentoDeDescompressao = m0(nk = nk)
        val vc = vc(
            nd = nd, md = md,
            linhaNeutraCortaSecao = linhaNeutraCortaSecao,
            momentoDeDescompressao = momentoDeDescompressao
        )
        val vsw = max(0.0, vd - vc)
        val vrd3 = vc + vsw
        val vu = min(vrd2, vrd3)
        val aswMinima = aswMinima(nd = nd, vd = vd, md = md, vc = vc)
        val aswCalculada = aswCalculada(vd = vd, linhaNeutraCortaSecao = linhaNeutraCortaSecao, vc = vc)
        val aswAdotada = max(aswCalculada, aswMinima)
        val vswMin = vsw(asw = aswMinima)

        return ResultadosCisalhamento(
            secao = secao, fywk = fywk, fywd = fywd,
            vrd2 = vrd2,
            vc0 = vc0,
            vc = vc,
            vsw = vsw,
            vrd3 = vrd3,
            vu = vu,
            momentoDeDescompressao = momentoDeDescompressao,
            vswMin = vswMin,
            aswCalculada = aswCalculada,
            aswMinima = aswMinima,
            aswAdotada = aswAdotada
        )
    }

    fun verificar(
        nk: Double,
        nd: Double,
        md: Double,
        asw: Double,
        linhaNeutraCortaSecao: Boolean
    ): ResultadosCisalhamento {
        val momentoDeDescompressao = m0(nk = nk)
        val vc = vc(
            nd = nd, md = md,
            linhaNeutraCortaSecao = linhaNeutraCortaSecao,
            momentoDeDescompressao = momentoDeDescompressao
        )
        val vsw = vsw(asw = asw)
        val vrd3 = vc + vsw
        val vu = min(vrd2, vrd3)
        val aswMinima = aswMinima(nd = nd, vd = vu, md = md, vc = vc)
        val vswMin = vsw(asw = aswMinima)
        return ResultadosCisalhamento(
            secao = secao, fywk = fywk, fywd = fywd,
            vrd2 = vrd2,
            vc0 = vc0,
            vc = vc,
            vsw = vsw,
            vrd3 = vrd3,
            vu = vu,
            momentoDeDescompressao = momentoDeDescompressao,
            vswMin = vswMin,
            aswCalculada = asw,
            aswMinima = aswMinima,
            aswAdotada = asw
        )
    }
}