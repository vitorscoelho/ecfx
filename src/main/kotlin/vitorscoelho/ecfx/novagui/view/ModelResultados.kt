package vitorscoelho.ecfx.novagui.view

import vitorscoelho.ecfx.dimensionamento.geotecnico.ResultadosAnaliseTubulao
import vitorscoelho.ecfx.novagui.utils.*
import vitorscoelho.utils.measure.angleSUOf
import vitorscoelho.utils.measure.lengthSUOf
import vitorscoelho.utils.measure.pressureSUOf

class ModelResultados(val resultados: ResultadosAnaliseTubulao) {
    //Tensões na base
    val tensaoMaxAtuanteBase = TensaoMaxAtuanteBaseProp(pressureSUOf(resultados.tensaoMaximaBase))
    val tensaoMediaAtuanteBase = TensaoMediaAtuanteBaseProp(pressureSUOf(resultados.tensaoMediaBase))
    val tensaoMinimaAtuanteBase = TensaoMinAtuanteBaseProp(pressureSUOf(resultados.tensaoMinimaBase))

    //Tensões no fuste
//    val tensaoMaximaAtuanteNoConcretoFuste= TensaoMaximaAtuanteNoConcretoFusteProp(pressureSUOf(resultados.tens))
    //Deslocamentos no topo
    val rotacaoTubulao = RotacaoTubulaoProp(angleSUOf(resultados.rotacao))
    val deltaHTopoTubulao = DeltaHTopoTubulaoProp(lengthSUOf(resultados.deltaH))
    val deltaVTopoTubulao = DeltaVTopoTubulaoProp(lengthSUOf(resultados.deltaV))
}
