package vitorscoelho.ecfx.gui.controller

import tornadofx.Controller
import tornadofx.Scope
import vitorscoelho.ecfx.dimensionamento.geotecnico.ResultadosAnaliseTubulao
import vitorscoelho.ecfx.gui.model.BeanResultados
import vitorscoelho.ecfx.gui.model.BeanUnidades

internal class ScopeResultados(val resultados: ResultadosAnaliseTubulao, val unidades: BeanUnidades) : Scope()

internal class ControllerResultados : Controller() {
    override val scope = super.scope as ScopeResultados
    val resultados = BeanResultados(resultados = scope.resultados, unidades = scope.unidades)
}