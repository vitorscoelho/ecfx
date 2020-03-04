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
//        model.commit()
//        val fuste: FusteTubulao = FusteCircular(diametro = model.diametroFuste)
//        val base: BaseTubulao = BaseCircular(
//            diametroInferior = model.diametroFuste,
//            diametroSuperior = model.diametroBase,
//            altura = 0.0,
//            rodape = 0.0
//        )
//        val tubulao: Tubulao = Tubulao(
//            fuste = fuste, base = base, comprimento = model.profundidadeEstaca
//        )
//        val kv: Double = model.kv
////        val resultados = if (model.tipoSolo == TipoSolo.AREIA_OU_ARGILA_MOLE) {
////            AnaliseKhLinearmenteVariavel()
////        }
//        val resultados: ResultadosAnaliseTubulao = when (model.tipoSolo) {
//            TipoSolo.AREIA_OU_ARGILA_MOLE -> AnaliseKhLinearmenteVariavel(
//
//            )
//            TipoSolo.ARGILA_RIJA_A_DURA -> AnaliseKhDegrau(
//
//            )
//            else -> IllegalArgumentException()
//        }
    }
}