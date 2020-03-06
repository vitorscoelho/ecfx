package vitorscoelho.ecfx.novagui.view

import javafx.scene.control.ButtonType
import javafx.stage.StageStyle
import javafx.stage.Window
import tornadofx.Controller
import tornadofx.confirm
import tornadofx.get
import vitorscoelho.ecfx.dimensionamento.Esforco
import vitorscoelho.ecfx.dimensionamento.geotecnico.*
import vitorscoelho.ecfx.gui.descricoes

internal class ControllerInicial : Controller() {
    val model = ModelInicial()

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

    fun acaoMenuItemSobre() {
        find(ViewSobre::class.java).openModal(
            stageStyle = StageStyle.DECORATED,
            resizable = false
        )
    }

    fun acaoDimensionar() {
        model.commit()
        val moduloConcreto = model.ecs.toDoubleSU()
        val fuste: FusteTubulao = FusteCircular(diametro = model.diametroFuste.toDoubleSU())
        val base: BaseTubulao = BaseCircular(
            diametroInferior = model.diametroBase.toDoubleSU(),
            diametroSuperior = model.diametroFuste.toDoubleSU(),
            altura = 0.0,
            rodape = 0.0
        )
        val tubulao: Tubulao = Tubulao(
            fuste = fuste, base = base, comprimento = model.profundidadeEstaca.toDoubleSU()
        )
        val kv: Double = model.kv.toDoubleSU()
        val esforcoTopo = Esforco(
            normal = model.forcaNormal.toDoubleSU(),
            horizontal = model.forcaHorizontal.toDoubleSU(),
            momento = model.momentoFletor.toDoubleSU()
        )
        val analiseTubulao: AnaliseTubulao = when (model.tipoSolo.value) {
            TipoSolo.AREIA_OU_ARGILA_MOLE -> AnaliseKhLinearmenteVariavel(
                tubulao = tubulao, kv = kv,
                nh = model.khAreiaOuArgilaMole.toDoubleSU()
            )
            TipoSolo.ARGILA_RIJA_A_DURA -> AnaliseKhDegrau(
                tubulao = tubulao, kv = kv,
                kh2 = model.khArgilaRijaADura.toDoubleSU(), moduloElasticidade = moduloConcreto
            )
        }
        val resultadosAnaliseTubulao = analiseTubulao.dimensionar(esforco = esforcoTopo)
        abrirJanelaResultados(resultados = resultadosAnaliseTubulao)
    }

    private fun abrirJanelaResultados(resultados: ResultadosAnaliseTubulao) {
        find(componentType = ViewResultados::class.java, scope = ScopeResultados(resultados = resultados)).openModal(
        )
    }
}