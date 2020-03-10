package vitorscoelho.ecfx.novagui.view.resultadosfuste

import javafx.scene.chart.XYChart
import tornadofx.toObservable
import vitorscoelho.ecfx.dimensionamento.geotecnico.ResultadosAnaliseTubulao
import vitorscoelho.ecfx.dimensionamento.geotecnico.Tubulao
import vitorscoelho.ecfx.novagui.configuracoes.Formato
import vitorscoelho.ecfx.novagui.configuracoes.Formatos
import vitorscoelho.ecfx.novagui.utils.description
import vitorscoelho.ecfx.novagui.utils.label
import vitorscoelho.ecfx.novagui.utils.rb
import vitorscoelho.utils.measure.CENTIMETRE
import vitorscoelho.utils.measure.lengthSUOf
import vitorscoelho.utils.measure.toSU
import javax.measure.Quantity
import javax.measure.Unit
import javax.measure.quantity.*

internal sealed class DadosDoGrafico<Q : Quantity<Q>>(nome: String, tubulao: Tubulao) {
    abstract val formato: Formato<Q>
    val unit: Unit<Q>
        get() = formato.unit.value

    abstract fun valor(profundidade: Double): Double
    val label: String = rb.label(nome)
    val descricao: String = rb.description(nome)
    private val converterParaValor by lazy { unit.toSU().getConverterToAny(unit) }
    private val listaValores: List<XYChart.Data<Number, Number>> by lazy {
        val profundidadeMaxima = lengthSUOf(tubulao.comprimento).to(unidadeProfundidade).value.toDouble()
        val qtdPontos = 50
        val distPontos = profundidadeMaxima / qtdPontos
        (0..qtdPontos).map { nPonto ->
            val y = nPonto * distPontos
            val yEmCentimetro = converterParaCentimetro.convert(y)
            val xSU = valor(profundidade = yEmCentimetro)
            val x = converterParaValor.convert(xSU)
            XYChart.Data(x as Number, -y as Number)
        }
    }

    /**
     * Cria uma nova [XYChart.Series] cada vez que é invocado.
     * Isso foi necessário por conta de um bug no gráfico.
     * Quando selecionava uma mesma série em mais de um gráfico, ela ficava fixada, mesmo que mudasse na comboBox.
     */
    val serie: XYChart.Series<Number, Number>
        get() = XYChart.Series(listaValores.toObservable())

    fun valorNasUnidadesCorrentes(profundidadeNaUnidadeCorrente: Double): Double {
        val profundidadeEmCentimetro = converterParaCentimetro.convert(profundidadeNaUnidadeCorrente)
        val valorEmSU = valor(profundidadeEmCentimetro)
        return converterParaValor.convert(valorEmSU)
    }

    override fun toString(): String = "$label ($unit)"

    companion object {
        private val unidadeProfundidade = Formatos.profundidade.unit.value
        private val converterParaCentimetro = unidadeProfundidade.getConverterTo(CENTIMETRE)
    }
}

internal class CoeficienteDeReacao(val resultados: ResultadosAnaliseTubulao) :
    DadosDoGrafico<SpringStiffnessPerUnitArea>(nome = "grafico.coeficienteDeReacao", tubulao = resultados.tubulao) {
    override val formato: Formato<SpringStiffnessPerUnitArea>
        get() = Formatos.coeficienteReacao


    override fun valor(profundidade: Double): Double {
        return resultados.coeficienteReacaoHorizontal(z = profundidade)
    }
}

internal class TensaoHorizontalAtuante(val resultados: ResultadosAnaliseTubulao) :
    DadosDoGrafico<Pressure>(nome = "grafico.tensaoHorizontalAtuante", tubulao = resultados.tubulao) {
    override val formato: Formato<Pressure>
        get() = Formatos.tensaoSolo


    override fun valor(profundidade: Double): Double {
        return resultados.tensaoHorizontal(z = profundidade)
    }
}

internal class Reacao(val resultados: ResultadosAnaliseTubulao) :
    DadosDoGrafico<ForcePerUnitLength>(nome = "grafico.reacao", tubulao = resultados.tubulao) {
    override val formato: Formato<ForcePerUnitLength>
        get() = Formatos.reacaoLateralNaEstaca

    override fun valor(profundidade: Double): Double {
        return resultados.reacaoHorizontal(z = profundidade)
    }
}

//internal class TensaoHorizontalResistente(val resultados: ResultadosAnaliseTubulao) :
//    DadosDoGrafico<Pressure>(nome = "grafico.tensaoHorizontalResistente", tubulao = resultados.tubulao) {
//    override val formato: Formato<Pressure>
//        get() = Formatos.tensaoSolo
//
//    override fun x(profundidade: Double, resultados: ResultadosAnaliseTubulao): Double {
//        return resultados.
//    }
//}

//    COEFICIENTE_SEGURANCA_ESTABILIDADE {
//        override val nome: String = "grafico.segurancaEstabilidade"
//        override val formato: Formato<*>
//            get() = Formatos.
//
//        override fun x(profundidade: Double, resultados: ResultadosAnaliseTubulao): Double {
//            return resultados.
//        }
//    },
internal class Cortante(val resultados: ResultadosAnaliseTubulao) :
    DadosDoGrafico<Force>(nome = "grafico.cortante", tubulao = resultados.tubulao) {
    override val formato: Formato<Force>
        get() = Formatos.forca

    override fun valor(profundidade: Double): Double {
        return resultados.cortante(z = profundidade)
    }
}

internal class Momento(val resultados: ResultadosAnaliseTubulao) :
    DadosDoGrafico<Moment>(nome = "grafico.momento", tubulao = resultados.tubulao) {
    override val formato: Formato<Moment>
        get() = Formatos.momento

    override fun valor(profundidade: Double): Double {
        return resultados.momento(z = profundidade)
    }
}

//    ARMADURA_LONGITUDINAL {
//        override val nome: String = "grafico.armaduraLongitudinal"
//        override val formato: Formato<*>
//            get() = Formatos.
//
//        override fun x(profundidade: Double, resultados: ResultadosAnaliseTubulao): Double {
//            return resultados.
//        }
//    },
//    ARMADURA_TRANSVERSAL {
//        override val nome: String = "grafico.armaduraTransversal"
//        override val formato: Formato<*>
//            get() = Formatos.
//
//        override fun x(profundidade: Double, resultados: ResultadosAnaliseTubulao): Double {
//            return resultados.
//        }
//    }