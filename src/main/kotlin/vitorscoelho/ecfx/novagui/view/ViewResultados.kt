package vitorscoelho.ecfx.novagui.view

import javafx.event.EventTarget
import javafx.geometry.Orientation
import tornadofx.*
import vitorscoelho.ecfx.gui.estilo.EstiloPrincipal
import vitorscoelho.ecfx.novagui.utils.TextGuiProp
import vitorscoelho.ecfx.novagui.utils.fieldTextField
import vitorscoelho.ecfx.novagui.utils.rb
import vitorscoelho.ecfx.novagui.utils.uneditable

internal class ViewResultados : View(title = rb["tituloJanelaResultados"]) {
    private val controller: ControllerResultados by inject(overrideScope = scope)
    private val model: ModelResultados
        get() = controller.model

    override val root = hbox {
        addClass(EstiloPrincipal.vboxDados)
        form {
            fieldset(rb["fieldsetTensoesNaBase"]) {
                labelPosition = Orientation.VERTICAL
                fieldTextField(property = model.tensaoMaxAtuanteBase)
                fieldTextField(property = model.tensaoMediaAtuanteBase)
                fieldTextField(property = model.tensaoMinimaAtuanteBase)
            }
            fieldset(rb["fieldsetTensoesNoFuste"]) {
                labelPosition = Orientation.VERTICAL
            }
            fieldset(rb["fieldsetDeslocamentosNoTopo"]) {
                labelPosition = Orientation.VERTICAL
                fieldTextField(property = model.rotacaoTubulao)
                fieldTextField(property = model.deltaHTopoTubulao)
                fieldTextField(property = model.deltaVTopoTubulao)
            }
        }
        button(rb["visualizarGraficosFuste"]){action { controller.acaoVisualizarGraficosFuste() }}
    }

    private fun <T> EventTarget.fieldTextField(property: TextGuiProp<T>, op: Field.() -> Unit = {}): Field {
        val field = fieldTextField(property = property, validationContext = null).uneditable()
        op(field)
        return field
    }
}