package vitorscoelho.ecfx.gui.controller

import javafx.scene.control.ButtonType
import javafx.stage.StageStyle
import javafx.stage.Window
import tornadofx.Controller
import tornadofx.confirm
import tornadofx.get
import vitorscoelho.ecfx.dimensionamento.Esforco
import vitorscoelho.ecfx.dimensionamento.geotecnico.*
import vitorscoelho.ecfx.gui.descricoes
import vitorscoelho.ecfx.gui.model.*
import vitorscoelho.ecfx.gui.view.ViewSobre
import vitorscoelho.utils.measure.toDoubleSU

internal class ControllerInicial : Controller() {
    val unidades = BeanUnidades()
    val concreto = BeanConcretoModel(unidades = unidades)
    val armaduraTransversal = BeanArmaduraModel(tipo = TipoArmadura.ESTRIBO, unidades = unidades)
    val armaduraLongitudinal = BeanArmaduraModel(tipo = TipoArmadura.LONGITUDINAL, unidades = unidades)
    val dadosDaFundacao = BeanDadosDaFundacaoModel(unidades = unidades)
    val cargasNoTopo = BeanCargasNoTopoModel(unidades = unidades)
    val solo = BeanSoloModel(unidades = unidades)

    fun acaoMenuItemSobre() {
        find(ViewSobre::class.java).openModal(
            stageStyle = StageStyle.DECORATED,
            resizable = false
        )
    }

    fun acaoMenuItemAbrir() {

    }

    fun acaoMenuItemSalvar() {

    }

    fun acaoFecharPrograma(currentWindow: Window) {
        confirm(
            title = descricoes.rb["confirmacao"],
            header = "",
            content = descricoes.rb["confirmacaoFecharPrograma"],
            owner = currentWindow,
            confirmButton = ButtonType.YES,
            cancelButton = ButtonType.NO,
            actionFn = {
                primaryStage.close()
            }
        )
    }

    fun acaoBtnValoresSugeridos() {

    }

    fun acaoBtnVisualizarResultados() {
        val fuste = FusteCircular(diametro = dadosDaFundacao.diametroFuste.value.toDoubleSU())
        val base = with(dadosDaFundacao) {
            BaseCircular(
                diametroInferior = diametroBase.toDoubleSU(),
                diametroSuperior = diametroFuste.toDoubleSU(),
                altura = alturaBase.toDoubleSU(),
                rodape = rodape.toDoubleSU()
            )
        }
        val tubulao = Tubulao(
            fuste = fuste, base = base, comprimento = dadosDaFundacao.profundidade.toDoubleSU()
        )
        val esforco = with(cargasNoTopo) {
            Esforco(
                normal = normal.toDoubleSU(),
                horizontal = forcaHorizontal.toDoubleSU(),
                momento = momento.toDoubleSU()
            )
        }
        val analise = when (solo.tipo.value) {
            TipoSolo.AREIA_OU_ARGILA_MOLE -> AnaliseKhLinearmenteVariavel(
                tubulao = tubulao,
                nh = solo.kh.toDoubleSU(),
                kv = solo.kv.toDoubleSU()
            )
            TipoSolo.ARGILA_RIJA_A_DURA -> AnaliseKhDegrau(
                tubulao = tubulao,
                kh2 = solo.kh.toDoubleSU(),
                kv = solo.kv.toDoubleSU(),
                moduloElasticidade = concreto.ecs.toDoubleSU()
            )
            else -> throw IllegalArgumentException("Tipo de solo não suportado")
        }
        val resultados = analise.dimensionar(esforco = esforco)
        println(tubulao.fuste.area)
    }
}