package vitorscoelho.ecfx.gui.controller

import javafx.scene.control.ButtonType
import javafx.stage.Window
import tornadofx.Controller
import tornadofx.confirm
import tornadofx.get
import vitorscoelho.ecfx.gui.descricoes
import vitorscoelho.ecfx.gui.model.*

class ControllerInicial : Controller() {
    //private val controller: ControllerInicial by inject()
    val unidades = BeanUnidades()
    val concreto = BeanConcretoModel(unidades = unidades)
    val armaduraTransversal = BeanArmaduraModel(tipo = TipoArmadura.ESTRIBO, unidades = unidades)
    val armaduraLongitudinal = BeanArmaduraModel(tipo = TipoArmadura.LONGITUDINAL, unidades = unidades)
    val caracteristicasGeometricas = BeanCaracteristicasGeometricasModel(unidades = unidades)
    val cargasNoTopo = BeanCargasNoTopoModel(unidades = unidades)
    val tipoEstaca = BeanTipoEstacaModel(unidades = unidades)
    val solo = BeanSoloModel(unidades = unidades)

    fun acaoMenuItemSobre() {

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

    }
}