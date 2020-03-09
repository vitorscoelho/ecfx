package vitorscoelho.ecfx.novagui.view.resultadosfuste

import tornadofx.*
import vitorscoelho.ecfx.novagui.utils.rb
import vitorscoelho.ecfx.novagui.view.resultadosfuste.ControllerGraficosFuste

internal class ViewGraficosFuste : View(title = rb["tituloJanelaGraficosFuste"]) {
    private val controller: ControllerGraficosFuste by inject(overrideScope = scope)

    override val root = controller.nodeGraficos
//        hbox {
//        setPrefSize(1210.0, 640.0)
//        usePrefSize = true
//        this += controller.nodeGraficos
//    }
}