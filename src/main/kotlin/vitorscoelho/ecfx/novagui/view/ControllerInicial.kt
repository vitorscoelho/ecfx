package vitorscoelho.ecfx.novagui.view

import javafx.scene.control.ButtonType
import javafx.stage.Window
import tornadofx.Controller
import tornadofx.confirm
import tornadofx.get
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

    fun acaoDimensionar() {
        model.commit()
        val moduloConcreto = model.ecs.toDoubleSU()
        val fuste: FusteTubulao = FusteCircular(diametro = model.diametroFuste.toDoubleSU())
        val base: BaseTubulao = BaseCircular(
            diametroInferior = model.diametroFuste.toDoubleSU(),
            diametroSuperior = model.diametroBase.toDoubleSU(),
            altura = 0.0,
            rodape = 0.0
        )
        val tubulao: Tubulao = Tubulao(
            fuste = fuste, base = base, comprimento = model.profundidadeEstaca.toDoubleSU()
        )
        val kv: Double = model.kv.toDoubleSU()
        val resultados: AnaliseTubulao = when (model.tipoSolo.value) {
            TipoSolo.AREIA_OU_ARGILA_MOLE -> AnaliseKhLinearmenteVariavel(
                tubulao = tubulao, kv = kv,
                nh = model.khAreiaOuArgilaMole.toDoubleSU()
            )
            TipoSolo.ARGILA_RIJA_A_DURA -> AnaliseKhDegrau(
                tubulao = tubulao, kv = kv,
                kh2 = model.khArgilaRijaADura.toDoubleSU(), moduloElasticidade = moduloConcreto
            )
        }
    }
}