package vitorscoelho.ecfx.novagui.view.resultadosfuste

import javafx.beans.property.DoubleProperty
import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.beans.property.ReadOnlyObjectProperty
import javafx.geometry.Side
import javafx.scene.chart.NumberAxis
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import tornadofx.*
import vitorscoelho.ecfx.novagui.configuracoes.Formatos
import vitorscoelho.ecfx.novagui.utils.rb

internal class Grafico(
    private val dados: List<DadosDoGrafico<*>>,
    val yMaximo: Double,
    val yUnidadeCorrente: ReadOnlyDoubleProperty
) {
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
        labelValores.apply {
            textProperty().bind(stringBinding(tipoGrafico, yUnidadeCorrente) {
                if (tipoGrafico.value == null || yUnidadeCorrente.value == null) return@stringBinding ""
                val tipo = tipoGrafico.value
                val valor = tipo.valorNasUnidadesCorrentes(yUnidadeCorrente.value)
                val valorFormatado = tipo.formato.format.value.format(valor)
                val profundidadeFormatada = Formatos.profundidade.format.value.format(yUnidadeCorrente.value)
                val unidadeValor = tipo.formato.unit.value
                val unidadeProfundidade = Formatos.profundidade.unit.value
                val emString = rb["em"]
                "$valorFormatado$unidadeValor $emString $profundidadeFormatada$unidadeProfundidade"
            })
        }
        vbox.apply {
            maxHeight = Double.MAX_VALUE
        }
    }

    private fun atualizarVisualizacao() {
        val novoTipo: DadosDoGrafico<*> = tipoGrafico.value
        textArea.text = "Descrição:\r\n${novoTipo.descricao}"
        chart.apply {
            data.clear()
            data = observableListOf(novoTipo.serie)
        }
    }

    companion object {
        private const val HEIGHT_TEXT_AREA = 100.0
        private const val PREF_WIDTH = 250.0
    }
}