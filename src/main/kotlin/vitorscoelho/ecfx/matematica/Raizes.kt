package vitorscoelho.ecfx.matematica

import kotlin.math.abs

/**
 * Encontra a raíz da função fornecida.
 * Os valores de [xEsquerda] e [xDireita] devem ser adotados de forma que
 * funcao(xEsquerda) e função(xDireita) retornem valores com sinais opostos.
 * Caso esta medida não seja adotada, ainda pode-se chegar à raiz, porém a possibilidade
 * fica menos garantida
 * @param xEsquerda abcissa à esquerda da raiz
 * @param fxEsquerda resultado para funcao(xEsquerda)
 * @param xDireita abcissa à direita da raiz
 * @param fxDireita resultado para funcao(xDireita)
 * @param acuraciaAbsoluta tolerância aceitável para considerar [funcao] igual a zero
 * @param limiteDeIteracoes quantidade máxima de iterações permitidas. Deve ser maior ou igual a 1
 * @return uma abscissa onde [funcao] retorna zero
 * @throws IllegalArgumentException se [xEsquerda] não for menor do que [xDireita]
 * @throws IllegalArgumentException se [limiteDeIteracoes] for menor do que 1
 * @throws IllegalStateException se o número de iterações exceder [limiteDeIteracoes]
 */
fun raiz(
    xEsquerda: Double,
    fxEsquerda: Double,
    xDireita: Double,
    fxDireita: Double,
    acuraciaAbsoluta: Double,
    limiteDeIteracoes: Int,
    funcao: (x: Double) -> Double
): Double {
    require(limiteDeIteracoes >= 1) { "|limiteDeIteracoes| não pode ser menor do que 1. limiteDeIteracoes=$limiteDeIteracoes" }
    require(xEsquerda < xDireita) { "|xEsquerda| deve ser menor do que |xDireita|. Porém: xEsquerda=$xEsquerda e xDireita=$xDireita" }
    var xEsquerdaVariavel = xEsquerda
    var fxEsquerdaVariavel = fxEsquerda
    var xDireitaVariavel = xDireita
    var fxDireitaVariavel = fxDireita
    var xAproximacaoVariavel: Double
    var fxAproximacaoVariavel: Double
    var nIteracao = 0

    do {
        check(nIteracao <= limiteDeIteracoes) {
            "Não foi possível encontrar a raiz dentro do número de iterações informados. limiteDeIteracoes=$limiteDeIteracoes"
        }

        val delta1 = xDireitaVariavel - xEsquerdaVariavel
        val delta2 = fxDireitaVariavel - fxEsquerdaVariavel
        xAproximacaoVariavel = xDireitaVariavel - fxDireitaVariavel * delta1 / delta2
        fxAproximacaoVariavel = funcao.invoke(xAproximacaoVariavel)
        if (fxAproximacaoVariavel * fxDireitaVariavel < 0.0) {
            xEsquerdaVariavel = xDireitaVariavel
            fxEsquerdaVariavel = fxDireitaVariavel
        } else {
            fxEsquerdaVariavel = fxEsquerdaVariavel * fxDireitaVariavel / (fxDireitaVariavel + fxAproximacaoVariavel)
        }
        xDireitaVariavel = xAproximacaoVariavel
        fxDireitaVariavel = fxAproximacaoVariavel

        nIteracao++
    } while (abs(fxAproximacaoVariavel) >= acuraciaAbsoluta)
    return xAproximacaoVariavel
}

/**
 * Encontra a raíz da função fornecida.
 * Os valores de [xEsquerda] e [xDireita] devem ser adotados de forma que
 * funcao(xEsquerda) e função(xDireita) retornem valores com sinais opostos.
 * Caso esta medida não seja adotada, ainda pode-se chegar à raiz, porém a possibilidade
 * fica menos garantida
 * @param xEsquerda abcissa à esquerda da raiz
 * @param xDireita abcissa à direita da raiz
 * @param acuraciaAbsoluta tolerância aceitável para considerar [funcao] igual a zero
 * @param limiteDeIteracoes quantidade máxima de iterações permitidas. Deve ser maior ou igual a 1
 * @return uma abscissa onde [funcao] retorna zero
 * @throws IllegalArgumentException se [xEsquerda] não for menor do que [xDireita]
 * @throws IllegalArgumentException se [limiteDeIteracoes] for menor do que 1
 * @throws IllegalStateException se o número de iterações exceder [limiteDeIteracoes]
 */
fun raiz(
    xEsquerda: Double,
    xDireita: Double,
    acuraciaAbsoluta: Double,
    limiteDeIteracoes: Int,
    funcao: (x: Double) -> Double
): Double =
    raiz(
        xEsquerda = xEsquerda, fxEsquerda = funcao.invoke(xEsquerda),
        xDireita = xDireita, fxDireita = funcao.invoke(xDireita),
        acuraciaAbsoluta = acuraciaAbsoluta,
        limiteDeIteracoes = limiteDeIteracoes,
        funcao = funcao
    )