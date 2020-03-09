package vitorscoelho.ecfx.novagui.view.resultadosfuste

import javafx.beans.property.DoubleProperty
import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.geometry.Point2D
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.layout.VBox
import javafx.scene.shape.Line
import tornadofx.add
import tornadofx.onChange
import vitorscoelho.ecfx.dimensionamento.geotecnico.ResultadosAnaliseTubulao
import vitorscoelho.ecfx.novagui.configuracoes.Formatos
import vitorscoelho.utils.measure.CENTIMETRE

internal class Graficos(resultados: ResultadosAnaliseTubulao) {
    val yGraficoUnidadeProfundidade: DoubleProperty = SimpleDoubleProperty(0.0)
    private val _yGraficoEmPixel: DoubleProperty = SimpleDoubleProperty(0.0)
    val yGraficoEmPixel: ReadOnlyDoubleProperty
        get() = _yGraficoEmPixel
    private val yMaximoEmPixel: Double
        get() = graficos.first().chart.yAxis.height
    private val yMaximoEmCentimetro = resultados.tubulao.comprimento
    private val converterParaCentimetro = Formatos.profundidade.unit.value.getConverterTo(CENTIMETRE)
    private val converterAPartirDeCentimetro = CENTIMETRE.getConverterTo(Formatos.profundidade.unit.value)
    private val yMaximoUnidadeDeProfundidade = converterAPartirDeCentimetro.convert(yMaximoEmCentimetro)
    val dadosCoeficienteDeReacao = CoeficienteDeReacao(resultados)
    val dadosTensaoHorizontalAtuante = TensaoHorizontalAtuante(resultados)
    val listaDeDados = listOf(
        dadosCoeficienteDeReacao,
        dadosTensaoHorizontalAtuante
    )
    private val graficos: List<Grafico> = listaDeDados.map { Grafico(listaDeDados, yMaximoUnidadeDeProfundidade) }
    private val linhaMarcacaoProfundidade = Line()
    val node: Region = AnchorPane().apply {
        minHeight = 600.0; maxHeight = Double.MAX_VALUE
        val hbox = HBox()
        graficos.forEach { hbox.add(it.vbox) }
        add(hbox)
        add(linhaMarcacaoProfundidade)
        AnchorPane.setBottomAnchor(hbox, 0.0)
        AnchorPane.setTopAnchor(hbox, 0.0)
    }

    init {
        linhaMarcacaoProfundidade.startX = 0.0
        node.widthProperty().onChange { linhaMarcacaoProfundidade.endX = it - 1.0;println(it) }
        val eixoY = graficos.first().chart.yAxis
        node.setOnMouseMoved { event ->
            val mouseNodeCoords = Point2D(event.sceneX, event.sceneY)
            val mouseEixoYCoords = eixoY.sceneToLocal(mouseNodeCoords).y
            if (mouseEixoYCoords in 0.0..yMaximoEmPixel) {
                yGraficoUnidadeProfundidade.value = pixelParaUnidadeProfundidade(mouseEixoYCoords)
                _yGraficoEmPixel.value = mouseEixoYCoords
                linhaMarcacaoProfundidade.startY = mouseNodeCoords.y
                linhaMarcacaoProfundidade.endY = mouseNodeCoords.y
            }
        }
    }

    private fun pixelParaCentimetro(valorEmPixel: Double): Double = yMaximoEmCentimetro * valorEmPixel / yMaximoEmPixel

    private fun pixelParaUnidadeProfundidade(valorEmPixel: Double): Double {
        val emCentimetro = pixelParaCentimetro(valorEmPixel)
        return converterAPartirDeCentimetro.convert(emCentimetro)
    }
}