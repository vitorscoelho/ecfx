package vitorscoelho.ecfx.novagui.view.resultadosfuste

import javafx.scene.Node
import javafx.scene.layout.Region
import tornadofx.Controller
import tornadofx.Scope
import tornadofx.onChange
import vitorscoelho.ecfx.dimensionamento.geotecnico.ResultadosAnaliseTubulao
import vitorscoelho.ecfx.novagui.configuracoes.Formatos
import vitorscoelho.ecfx.novagui.utils.ProfundidadeProp
import vitorscoelho.utils.measure.lengthOf

internal class ScopeGraficosFuste(val resultados: ResultadosAnaliseTubulao) : Scope()

internal class ControllerGraficosFuste : Controller() {
    override val scope = super.scope as ScopeGraficosFuste
    val profundidadeProp = ProfundidadeProp(lengthOf(0.0))
    private val graficos = Graficos(scope.resultados)
    val nodeGraficos: Region = graficos.node

    init {
        graficos.yGraficoUnidadeProfundidade.onChange {
            profundidadeProp.setTransitional(lengthOf(it, Formatos.profundidade.unit.value))
        }
    }

    fun acaoIrProfundidade() {
        profundidadeProp.commit()
        graficos.setYGrafico(profundidadeProp.value.value.toDouble())
    }

    companion object {
        val LARGURA_INICIAL_JANELA = 600.0
        val ALTURA_INICIAL_JANELA = 600.0
    }
}