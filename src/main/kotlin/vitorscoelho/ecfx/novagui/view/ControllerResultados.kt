package vitorscoelho.ecfx.novagui.view

import tornadofx.Controller
import tornadofx.Scope
import vitorscoelho.ecfx.dimensionamento.geotecnico.ResultadosAnaliseTubulao

internal class ScopeResultados(val resultados: ResultadosAnaliseTubulao) : Scope()

internal class ControllerResultados : Controller() {
    override val scope = super.scope as ScopeResultados
    val model = ModelResultados(resultados = scope.resultados)

    fun acaoVisualizarGraficosFuste() {
        find(
            componentType = ViewGraficosFuste::class.java,
            scope = ScopeGraficosFuste(resultados = scope.resultados)
        ).openModal()
    }
}

/*
internal class ScopeEditarFolha(val idFolha: Int, val dao: DAO) : Scope()

internal class ControllerEditarFolha : Controller() {
    override val scope = super.scope as ScopeEditarFolha
 */