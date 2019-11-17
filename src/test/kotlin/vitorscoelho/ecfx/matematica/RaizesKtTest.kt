package vitorscoelho.ecfx.matematica

import org.junit.Test
import org.junit.Assert.*
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sqrt

class RaizesKtTest {
    private fun resultadoDasDuasFuncoesDeRaiz(
        xEsquerda: Double,
        xDireita: Double,
        acuraciaAbsoluta: Double,
        limiteDeIteracoes: Int,
        funcao: (x: Double) -> Double
    ): Pair<Double, Double> {
        val resultadoFuncao1 = raiz(
            xEsquerda = xEsquerda,
            xDireita = xDireita,
            acuraciaAbsoluta = acuraciaAbsoluta,
            limiteDeIteracoes = limiteDeIteracoes
        ) { x -> funcao(x) }

        val resultadoFuncao2 = raiz(
            xEsquerda = xEsquerda,
            xDireita = xDireita,
            fxEsquerda = funcao(xEsquerda),
            fxDireita = funcao(xDireita),
            acuraciaAbsoluta = acuraciaAbsoluta,
            limiteDeIteracoes = limiteDeIteracoes
        ) { x -> funcao(x) }

        return Pair(resultadoFuncao1, resultadoFuncao2)
    }

    @Test
    fun testeLivroCampos1() {
        val (resultadoFuncao1, resultadoFuncao2) = resultadoDasDuasFuncoesDeRaiz(
            xEsquerda = -1.0,
            xDireita = 2.0,
            acuraciaAbsoluta = 0.01,
            limiteDeIteracoes = 100
        ) { x -> 2.0 * x.pow(3) - cos(x + 1.0) - 3.0 }

        assertEquals(1.07889, resultadoFuncao1, 0.00001)
        assert(resultadoFuncao1 == resultadoFuncao2)
    }

    @Test
    fun testeLivroCampos2() {
        val (resultadoFuncao1, resultadoFuncao2) = resultadoDasDuasFuncoesDeRaiz(
            xEsquerda = -1.0,
            xDireita = 1.0,
            acuraciaAbsoluta = 1.0e-5,
            limiteDeIteracoes = 100
        ) { x -> 4.0 * x.pow(3) + 6.0 * x.pow(2) - 26.0 * x - 14.0 }
        assertEquals(-0.5, resultadoFuncao1, 0.00001)
        assert(resultadoFuncao1 == resultadoFuncao2)
    }

    /**Exemplo retirado de http://www.ufjf.br/flavia_bastos/files/2009/06/aula_raizes.pdf*/
    @Test
    fun testeUfjf1() {
        val (resultadoFuncao1, resultadoFuncao2) = resultadoDasDuasFuncoesDeRaiz(
            xEsquerda = 0.0,
            xDireita = 1.0,
            acuraciaAbsoluta = 1.0e-5,
            limiteDeIteracoes = 100
        ) { x -> 3.0 * x.pow(2) + sqrt(x + 1.0) * cos(x).pow(3) - 2.0 }
        assertEquals(0.6812, resultadoFuncao1, 0.0001)
        assert(resultadoFuncao1 == resultadoFuncao2)
    }
}