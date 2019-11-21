package vitorscoelho.ecfx.gui.controller

import javafx.scene.control.ButtonType
import javafx.stage.Window
import tornadofx.Controller
import tornadofx.confirm

class ControllerInicial : Controller() {
    fun acaoFecharPrograma(currentWindow: Window) {
        confirm(
            title = "Confirmação",
            header = "",
            content = "Deseja fechar o programa?",
            owner = currentWindow,
            confirmButton = ButtonType.YES,
            cancelButton = ButtonType.NO,
            actionFn = {
                primaryStage.close()
            }
        )
    }
}