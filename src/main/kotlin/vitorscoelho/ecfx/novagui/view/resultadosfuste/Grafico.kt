package vitorscoelho.ecfx.novagui.view.resultadosfuste

import javafx.beans.property.ReadOnlyObjectProperty
import javafx.geometry.Side
import javafx.scene.chart.NumberAxis
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import tornadofx.observableListOf
import tornadofx.onChange
import tornadofx.toObservable
import tornadofx.vgrow

internal class Grafico(private val dados: List<DadosDoGrafico<*>>, val yMaximo: Double) {
    private val textArea = TextArea()
    private val labelMaximo = Label()
    private val combobox = ComboBox<DadosDoGrafico<*>>()
    val chart = CustomAreaChart(NumberAxis(), NumberAxis())
    private val labelValores = Label()
    val vbox = VBox(textArea, labelMaximo, combobox, chart, labelValores)
    private val tipoGrafico: ReadOnlyObjectProperty<DadosDoGrafico<*>>
        get() = combobox.valueProperty()

    init {
        configurarNodes()
        tipoGrafico.onChange { atualizarVisualizacao() }
    }

    private fun configurarNodes() {
        textArea.apply {
            minHeight = HEIGHT_TEXT_AREA; maxHeight = HEIGHT_TEXT_AREA
            isWrapText = true
            isEditable = false
        }
        combobox.apply {
            items = dados.toObservable()
            maxWidth = Double.MAX_VALUE
        }
        chart.apply {
            minHeight = 400.0; maxHeight = Double.MAX_VALUE
            createSymbols = false
            animated = false
            isLegendVisible = false
            (xAxis as NumberAxis).apply {
                side = Side.TOP
                tickUnit = 5.0
            }
            (yAxis as NumberAxis).apply {
                isAutoRanging = false
                tickUnit = 1.0
                lowerBound = -yMaximo
                upperBound = 0.0
            }
            vgrow = Priority.ALWAYS
        }
        vbox.maxHeight = Double.MAX_VALUE
    }

    private fun atualizarVisualizacao() {
        val novoTipo: DadosDoGrafico<*> = tipoGrafico.value
        textArea.text = novoTipo.descricao
        chart.apply {
            data.clear()
            data = observableListOf(novoTipo.serie)
        }
    }

    companion object {
        val HEIGHT_TEXT_AREA = 100.0
    }
}