package vitorscoelho.ecfx.novagui.view

import tornadofx.Controller
import tornadofx.Scope
import vitorscoelho.ecfx.dimensionamento.geotecnico.ResultadosAnaliseTubulao

internal class ScopeGraficosFuste(val resultados: ResultadosAnaliseTubulao) : Scope()

internal class ControllerGraficosFuste : Controller() {
    override val scope = super.scope as ScopeGraficosFuste
}