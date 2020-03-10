package vitorscoelho.ecfx.novagui.view.resultadosfuste

import javafx.geometry.Orientation
import javafx.scene.input.KeyCode
import javafx.scene.layout.Region.USE_COMPUTED_SIZE
import tornadofx.*
import vitorscoelho.ecfx.novagui.utils.fieldTextField
import vitorscoelho.ecfx.novagui.utils.rb

internal class ViewGraficosFuste : View(title = rb["tituloJanelaGraficosFuste"]) {
    private val controller: ControllerGraficosFuste by inject(overrideScope = scope)

    override val root = hbox {
        prefWidth = USE_COMPUTED_SIZE
        minWidth = USE_COMPUTED_SIZE
        maxWidth = USE_COMPUTED_SIZE
        this += controller.nodeGraficos
        this += form {
            fieldset(rb["fieldsetProfundidade"]) {
                labelPosition = Orientation.VERTICAL
                fieldTextField(property = controller.profundidadeProp) { _, tf ->
                    tf.setOnKeyPressed { event -> if (event.code == KeyCode.ENTER) controller.acaoIrProfundidade() }
                    tf.focusedProperty().onChange { noFoco -> if (!noFoco) controller.acaoIrProfundidade() }
                }
                button(rb["botao.irProfundidade"]) { action { controller.acaoIrProfundidade() } }
            }
        }
    }
}