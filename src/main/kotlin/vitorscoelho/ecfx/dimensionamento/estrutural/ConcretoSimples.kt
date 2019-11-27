package vitorscoelho.ecfx.dimensionamento.estrutural

import kotlin.math.max
import kotlin.math.min

data class ResultadosConcretoSimples(
    val tensaoNormalMinimaAtuante: Double,
    val tensaoNormalMaximaAtuante: Double,
    val tensaoNormalMediaAtuante: Double,
    val tensaoCisalhanteMaximaAtuante: Double,
    val tensaoNormalMinimaLimite: Double,
    val tensaoNormalMaximaLimite: Double,
    val tensaoCisalhanteLimite: Double,
    val precisaDeArmadura: Boolean
)

class ConcretoSimples(val secao: SecaoCircular) {
    fun dimensionar(nd: Double, vd: Double, md: Double): ResultadosConcretoSimples {
        val tensaoNormalMediaAtuante = nd / secao.area
        val tensaoNormalMinimaAtuante = tensaoNormalMediaAtuante - md / secao.moduloFlexao
        val tensaoNormalMaximaAtuante = tensaoNormalMediaAtuante + md / secao.moduloFlexao

        val tensaoNormalMinimaLimite = -secao.concreto.fctd / 1.2
        val tensaoNormalMaximaLimite = 0.85 * secao.concreto.fcd / 1.2

        val tensaoCisalhanteMaximaAtuante = 4.0 * vd / (3.0 * secao.area)

        /**Já está considerando que, para concreto simples, deve-se multiplicar o gamac por 1,2*/
        val tensaoCisalhanteLimite = max(
            1.0, min(
                0.25 * secao.concreto.fctd * (1.0 + 3.0 * tensaoNormalMediaAtuante / secao.concreto.fck),
                0.5 * secao.concreto.fctd
            )
        )

        val tensaoNormalMaiorQueMinima = (tensaoNormalMinimaAtuante >= tensaoNormalMinimaLimite)
        val tensaoNormalMenorQueMaxima = (tensaoNormalMaximaAtuante <= tensaoNormalMaximaLimite)
        val tensaoCisalhanteMenorQueMinima = (tensaoCisalhanteMaximaAtuante <= tensaoCisalhanteLimite)
        val naoPrecisaDeArmadura =
            tensaoNormalMaiorQueMinima && tensaoNormalMenorQueMaxima && tensaoCisalhanteMenorQueMinima

        return ResultadosConcretoSimples(
            tensaoNormalMinimaAtuante = tensaoNormalMinimaAtuante,
            tensaoNormalMaximaAtuante = tensaoNormalMaximaAtuante,
            tensaoNormalMediaAtuante = tensaoNormalMediaAtuante,
            tensaoCisalhanteMaximaAtuante = tensaoCisalhanteMaximaAtuante,
            tensaoNormalMinimaLimite = tensaoNormalMinimaLimite,
            tensaoNormalMaximaLimite = tensaoNormalMaximaLimite,
            tensaoCisalhanteLimite = tensaoCisalhanteLimite,
            precisaDeArmadura = !naoPrecisaDeArmadura
        )
    }
}