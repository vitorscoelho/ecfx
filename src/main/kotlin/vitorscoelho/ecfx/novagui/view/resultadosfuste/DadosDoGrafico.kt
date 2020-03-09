package vitorscoelho.ecfx.novagui.view.resultadosfuste

import javafx.scene.chart.XYChart
import tornadofx.toObservable
import vitorscoelho.ecfx.dimensionamento.geotecnico.ResultadosAnaliseTubulao
import vitorscoelho.ecfx.dimensionamento.geotecnico.Tubulao
import vitorscoelho.ecfx.novagui.configuracoes.Formatos
import vitorscoelho.ecfx.novagui.utils.description
import vitorscoelho.ecfx.novagui.utils.label
import vitorscoelho.ecfx.novagui.utils.rb
import vitorscoelho.utils.measure.CENTIMETRE
import vitorscoelho.utils.measure.lengthSUOf
import vitorscoelho.utils.measure.toSU
import javax.measure.Quantity
import javax.measure.Unit
import javax.measure.quantity.Pressure
import javax.measure.quantity.SpringStiffnessPerUnitArea

internal sealed class DadosDoGrafico<Q : Quantity<Q>>(nome: String, tubulao: Tubulao) {
    abstract val unit: Unit<Q>
    abstract fun valor(profundidade: Double): Double
    val label: String = rb.label(nome)
    val descricao: String = rb.description(nome)
    private val listaValores: List<XYChart.Data<Number, Number>> by lazy {
        val unidadeProfundidade = Formatos.profundidade.unit.value
        val profundidadeMaxima = lengthSUOf(tubulao.comprimento).to(unidadeProfundidade).value.toDouble()
        val converterParaCentimetro = unidadeProfundidade.getConverterTo(CENTIMETRE)
        val converterParaValor = unit.toSU().getConverterToAny(unit)
        val qtdPontos = 100
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

    override fun toString(): String = "$label ($unit)"
}

internal class CoeficienteDeReacao(val resultados: ResultadosAnaliseTubulao) :
    DadosDoGrafico<SpringStiffnessPerUnitArea>(nome = "grafico.coeficienteDeReacao", tubulao = resultados.tubulao) {
    override val unit: Unit<SpringStiffnessPerUnitArea>
        get() = Formatos.coeficienteReacao.unit.value


    override fun valor(profundidade: Double): Double {
        return resultados.coeficienteReacaoHorizontal(z = profundidade)
    }
}

internal class TensaoHorizontalAtuante(val resultados: ResultadosAnaliseTubulao) :
    DadosDoGrafico<Pressure>(nome = "grafico.tensaoHorizontalAtuante", tubulao = resultados.tubulao) {
    override val unit: Unit<Pressure>
        get() = Formatos.tensaoSolo.unit.value


    override fun valor(profundidade: Double): Double {
        return resultados.tensaoHorizontal(z = profundidade)
    }
}

//    REACAO {
//        override val nome: String = "grafico.reacao"
//        override val unit: Unit<*>
//            get() = Formatos..unit.value
//
//        override fun x(profundidade: Double, resultados: ResultadosAnaliseTubulao): Double {
//            return resultados.reacaoHorizontal(z = profundidade)
//        }
//    },
//    TENSAO_HORIZONTAL_RESISTENTE {
//        override val nome: String = "grafico.tensaoHorizontalResistente"
//        override val unit: Unit<*>
//            get() = Formatos.tensaoSolo.unit.value
//
//        override fun x(profundidade: Double, resultados: ResultadosAnaliseTubulao): Double {
//            return resultados.
//        }
//    },

//    COEFICIENTE_SEGURANCA_ESTABILIDADE {
//        override val nome: String = "grafico.segurancaEstabilidade"
//        override val unit: Unit<*>
//            get() = Formatos..unit.value
//
//        override fun x(profundidade: Double, resultados: ResultadosAnaliseTubulao): Double {
//            return resultados.
//        }
//    },
//    CORTANTE {
//        override val nome: String = "grafico.cortante"
//        override val unit: Unit<*>
//            get() = Formatos.forca.unit.value
//
//        override fun x(profundidade: Double, resultados: ResultadosAnaliseTubulao): Double {
//            return resultados.cortante(z = profundidade)
//        }
//    },
//    MOMENTO {
//        override val nome: String = "grafico.momento"
//        override val unit: Unit<*>
//            get() = Formatos.momento.unit.value
//
//        override fun x(profundidade: Double, resultados: ResultadosAnaliseTubulao): Double {
//            return resultados.momento(z = profundidade)
//        }
//    },
//    ARMADURA_LONGITUDINAL {
//        override val nome: String = "grafico.armaduraLongitudinal"
//        override val unit: Unit<*>
//            get() = Formatos..unit.value
//
//        override fun x(profundidade: Double, resultados: ResultadosAnaliseTubulao): Double {
//            return resultados.
//        }
//    },
//    ARMADURA_TRANSVERSAL {
//        override val nome: String = "grafico.armaduraTransversal"
//        override val unit: Unit<*>
//            get() = Formatos..unit.value
//
//        override fun x(profundidade: Double, resultados: ResultadosAnaliseTubulao): Double {
//            return resultados.
//        }
//    }