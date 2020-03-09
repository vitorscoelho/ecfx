package vitorscoelho.ecfx.novagui.view.resultadosfuste

import javafx.scene.Node
import javafx.scene.layout.Region
import tornadofx.Controller
import tornadofx.Scope
import vitorscoelho.ecfx.dimensionamento.geotecnico.ResultadosAnaliseTubulao

internal class ScopeGraficosFuste(val resultados: ResultadosAnaliseTubulao) : Scope()

internal class ControllerGraficosFuste : Controller() {
    override val scope = super.scope as ScopeGraficosFuste
    private val graficos = Graficos(scope.resultados)
    val nodeGraficos: Region = graficos.node

    companion object {
        val LARGURA_INICIAL_JANELA = 600.0
        val ALTURA_INICIAL_JANELA = 600.0
    }
}