package vitorscoelho.ecfx.matematica

import org.junit.Test
import org.junit.Assert.*
import java.lang.IllegalArgumentException
import kotlin.math.*

class IntegracaoKtTest {
    @Test(expected = IllegalArgumentException::class)
    fun limiteInferiorMaiorQueSuperior() {
        integrar(limiteInferior = 6.0, limiteSuperior = 2.0, qtdPontosGauss = 2) { 0.0 }
    }

    @Test(expected = IllegalArgumentException::class)
    fun qtdPontosMenorQue1() {
        integrar(limiteInferior = 0.0, limiteSuperior = 5.0, qtdPontosGauss = 0) { 0.0 }
    }

    @Test(expected = IllegalArgumentException::class)
    fun qtdPontosMaiorQue50() {
        integrar(limiteInferior = 0.0, limiteSuperior = 5.0, qtdPontosGauss = 51) { 0.0 }
    }

    @Test
    fun teste1LivroCampos() {
        fun integrarFuncao(qtdPontos: Int): Double =
            4.0 * integrar(
                limiteInferior = 0.0,
                limiteSuperior = 1.0,
                qtdPontosGauss = qtdPontos
            ) { x -> 1.0 / (1.0 + x * x) }

        val com3Pontos = integrarFuncao(qtdPontos = 3)
        val com4Pontos = integrarFuncao(qtdPontos = 4)
        assertEquals(3.14108, com3Pontos, 0.00005)
        assertEquals(3.14160, com4Pontos, 0.00005)
    }

    @Test
    fun teste2LivroCampos() {
        val resultado = integrar(limiteInferior = 0.0, limiteSuperior = PI, qtdPontosGauss = 5) { x ->
            E.pow(x) + sin(x) + 2.0
        }
        assertEquals(30.42406, resultado, 0.0005)
    }

    @Test
    fun teste3LivroCampos() {
        val resultado = integrar(limiteInferior = 0.0, limiteSuperior = PI, qtdPontosGauss = 2) { x ->
            x.pow(4) / 4.0 + x.pow(2) + sin(x)
        }
        assertEquals(27.14733, resultado, 0.0005)
    }

    /**Exemplo retirado de https://www.ufrgs.br/reamat/CalculoNumerico/livro-sci/in-quadratura_de_gauss-legendre.html*/
    @Test
    fun testeUfrgs1() {
        fun integrarFuncao(qtdPontos: Int) =
            integrar(limiteInferior = -1.0, limiteSuperior = 1.0, qtdPontosGauss = qtdPontos) { x -> sqrt(1.0 + x * x) }

        val com2Pontos = integrarFuncao(qtdPontos = 2)
        val com3Pontos = integrarFuncao(qtdPontos = 3)
        val com4Pontos = integrarFuncao(qtdPontos = 4)
        val com5Pontos = integrarFuncao(qtdPontos = 5)

        val delta = 0.0000001
        assertEquals(2.3094011, com2Pontos, delta)
        assertEquals(2.2943456, com3Pontos, delta)
        assertEquals(2.2957234, com4Pontos, delta)
        assertEquals(2.2955705, com5Pontos, delta)
    }

    /**Exemplo retirado de https://www.ufrgs.br/reamat/CalculoNumerico/livro-sci/in-quadratura_de_gauss-legendre.html*/
    @Test
    fun testeUfrgs2() {
        val resultado = integrar(limiteInferior = 0.0, limiteSuperior = 1.0, qtdPontosGauss = 3) { x ->
            sqrt(1.0 + x * x)
        }
        assertEquals(1.1478011, resultado, 0.0000001)
    }

    @Test
    fun testeUfrgs3() {
        fun integrarFuncao(qtdPontos: Int) =
            integrar(limiteInferior = -1.0, limiteSuperior = 1.0, qtdPontosGauss = qtdPontos) { x ->
                x.pow(4) * E.pow(x.pow(5))
            }

        val com2Pontos = integrarFuncao(qtdPontos = 2)
        val com3Pontos = integrarFuncao(qtdPontos = 3)
        val com4Pontos = integrarFuncao(qtdPontos = 4)
        val com5Pontos = integrarFuncao(qtdPontos = 5)

        val delta = 0.0001
        assertEquals(0.2227, com2Pontos, delta)
        assertEquals(0.4157, com3Pontos, delta)
        assertEquals(0.4437, com4Pontos, delta)
        assertEquals(0.4616, com5Pontos, delta)
    }

    /**Exemplo retirado de https://www.ime.unicamp.br/~valle/Teaching/2015/MS211/Aula24.pdf*/
    @Test
    fun testeUnicamp1() {
        val resultado = integrar(limiteInferior = 0.0, limiteSuperior = 1.0, qtdPontosGauss = 2) { x -> E.pow(x) }
        assertEquals(1.7179, resultado, 0.00001)
    }

    /**Exemplo retirado de http://www.udc.edu.br/v5/resources/producoes/SeminarioCientifico2014/files/CC/18.pdf*/
    @Test
    fun testeUdc1() {
        val resultado = integrar(limiteInferior = -2.0, limiteSuperior = 2.0, qtdPontosGauss = 3) { x ->
            x * x + 3.0 * x
        }
        assertEquals(5.33333333, resultado, 0.00000001)
    }

    /**Exemplo retirado de https://edisciplinas.usp.br/mod/resource/view.php?id=1399121*/
    @Test
    fun testeEdisciplinasUsp() {
        val resultado = integrar(limiteInferior = 0.0, limiteSuperior = 0.8, qtdPontosGauss = 2) { x ->
            0.2 + 25.0 * x - 200.0 * x.pow(2) + 675.0 * x.pow(3) - 900.0 * x.pow(4) + 400.0 * x.pow(5)
        }
        assertEquals(1.82257, resultado, 0.00001)
    }
}