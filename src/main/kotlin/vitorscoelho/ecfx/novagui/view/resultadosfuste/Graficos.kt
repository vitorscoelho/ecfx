package vitorscoelho.ecfx.novagui.view.resultadosfuste

import javafx.beans.property.DoubleProperty
import javafx.beans.property.ReadOnlyDoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.geometry.Point2D
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Region
import javafx.scene.shape.Line
import tornadofx.add
import tornadofx.onChange
import vitorscoelho.ecfx.dimensionamento.geotecnico.ResultadosAnaliseTubulao
import vitorscoelho.ecfx.novagui.configuracoes.Formatos
import vitorscoelho.utils.measure.CENTIMETRE
import kotlin.math.max
import kotlin.math.min

internal class Graficos(resultados: ResultadosAnaliseTubulao) {
    private val _yGraficoUnidadeProfundidade: DoubleProperty = SimpleDoubleProperty(0.0)
    val yGraficoUnidadeProfundidade: ReadOnlyDoubleProperty
        get() = _yGraficoUnidadeProfundidade
    private val yMaximoEmPixel: Double
        get() = graficos.first().chart.yAxis.height
    private val yMaximoEmCentimetro = resultados.tubulao.comprimento
    private val converterParaCentimetro = Formatos.profundidade.unit.value.getConverterTo(CENTIMETRE)
    private val converterAPartirDeCentimetro = CENTIMETRE.getConverterTo(Formatos.profundidade.unit.value)
    private val yMaximoUnidadeDeProfundidade = converterAPartirDeCentimetro.convert(yMaximoEmCentimetro)
    val dadosCoeficienteDeReacao = CoeficienteDeReacao(resultados)
    val dadosTensaoHorizontalAtuante = TensaoHorizontalAtuante(resultados)
    val dadosReacao = Reacao(resultados)

    //val dadosTensaoHorizontalResistente = TensaoHorizontalResistente(resultados)
    //val dadosCoeficienteSegurancaEstabilidade = CoeficienteSegurancaEstabilidade(resultados)
    //val dadosArmaduraLongitudinal = ArmaduraLongitudinal()
    //val dadosArmaduraTransversal = ArmaduraTransversal()
    val dadosCortante = Cortante(resultados)
    val dadosMomento = Momento(resultados)
    val listaDeDados = listOf(
        dadosCoeficienteDeReacao,
        dadosTensaoHorizontalAtuante,
        dadosReacao,
//        dadosTensaoHorizontalResistente,
//        dadosCoeficienteSegurancaEstabilidade,
        dadosCortante,
        dadosMomento/*,
        dadosArmaduraLongitudinal,
        dadosArmaduraTransversal*/
    )
    private val graficos: List<Grafico> = Array(size = 4) {
        Grafico(listaDeDados, yMaximoUnidadeDeProfundidade, yGraficoUnidadeProfundidade)
    }.toList()
    private val linhaMarcacaoProfundidade = Line()
    val node: Region = AnchorPane().apply {
        prefWidth = 1000.0
        println(maxWidth)
        minHeight = 600.0; maxHeight = Double.MAX_VALUE
        val hbox = HBox()
        graficos.forEach { hbox.add(it.vbox) }
        add(hbox)
        add(linhaMarcacaoProfundidade)
        AnchorPane.setBottomAnchor(hbox, 0.0)
        AnchorPane.setTopAnchor(hbox, 0.0)
        AnchorPane.setLeftAnchor(hbox, 0.0)
        AnchorPane.setRightAnchor(hbox, 0.0)
    }

    init {
        linhaMarcacaoProfundidade.startX = 0.0
        node.widthProperty().onChange { linhaMarcacaoProfundidade.endX = it - 1.0;println(it) }
        val eixoY = graficos.first().chart.yAxis
        node.setOnMouseMoved { event ->
            val mouseNodeCoords = Point2D(event.sceneX, event.sceneY)
            val mouseEixoYCoords = eixoY.sceneToLocal(mouseNodeCoords).y
            if (mouseEixoYCoords in 0.0..yMaximoEmPixel) {
                setYGrafico(pixelParaUnidadeProfundidade(mouseEixoYCoords))
            }
        }
    }

    fun setYGrafico(yEmUnidadeDeProfundidade: Double) {
        _yGraficoUnidadeProfundidade.value = min(max(0.0, yEmUnidadeDeProfundidade), yMaximoUnidadeDeProfundidade)
        val yEmPixelEixoY = unidadeDeProfundidadeParaPixel(_yGraficoUnidadeProfundidade.value)
        val eixoY = graficos.first().chart.yAxis
        val yEmPixelTela = eixoY.localToScreen(0.0, yEmPixelEixoY).y
        val yEmPixelNode = node.screenToLocal(0.0, yEmPixelTela).y
        linhaMarcacaoProfundidade.startY = yEmPixelNode
        linhaMarcacaoProfundidade.endY = yEmPixelNode
    }

    private fun pixelParaCentimetro(valorEmPixel: Double): Double = yMaximoEmCentimetro * valorEmPixel / yMaximoEmPixel

    private fun pixelParaUnidadeProfundidade(valorEmPixel: Double): Double {
        val emCentimetro = pixelParaCentimetro(valorEmPixel)
        return converterAPartirDeCentimetro.convert(emCentimetro)
    }

    private fun centimetroParaPixel(valorEmCentimetro: Double): Double {
        return valorEmCentimetro * yMaximoEmPixel / yMaximoEmCentimetro
    }

    private fun unidadeDeProfundidadeParaPixel(valorEmUnidadeDeProfundidade: Double): Double {
        val emCentimetro = converterParaCentimetro.convert(valorEmUnidadeDeProfundidade)
        return centimetroParaPixel(emCentimetro)
    }
}