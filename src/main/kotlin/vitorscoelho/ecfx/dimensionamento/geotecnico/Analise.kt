package vitorscoelho.ecfx.dimensionamento.geotecnico

import vitorscoelho.ecfx.dimensionamento.Esforco

interface AnaliseTubulao {
    fun dimensionar(esforco: Esforco): ResultadosAnaliseTubulao
}

interface ResultadosAnaliseTubulao {
    val tubulao: Tubulao
    val esforco: Esforco
    val deltaV: Double
    val deltaH: Double
    val rotacao: Double
    val tensaoMinimaBase: Double
    val tensaoMaximaBase: Double
    val tensaoMediaBase: Double
    fun coeficienteReacaoHorizontal(z: Double): Double
    fun tensaoHorizontal(z: Double): Double
    fun reacaoHorizontal(z: Double): Double
    fun cortante(z: Double): Double
    fun momento(z: Double): Double
}