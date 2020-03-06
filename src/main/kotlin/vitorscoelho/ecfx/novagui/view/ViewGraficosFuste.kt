package vitorscoelho.ecfx.novagui.view

import javafx.scene.Parent
import javafx.scene.chart.NumberAxis
import tornadofx.View
import tornadofx.areachart
import tornadofx.get
import tornadofx.hbox
import vitorscoelho.ecfx.novagui.utils.rb

internal class ViewGraficosFuste : View(title = rb["tituloJanelaGraficosFuste"]) {
    private val controller: ControllerGraficosFuste by inject(overrideScope = scope)

    override val root = hbox {
        areachart(title = "Título", x = NumberAxis(), y = NumberAxis())
    }
}