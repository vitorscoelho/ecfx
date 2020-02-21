package vitorscoelho.ecfx.gui.view

import tornadofx.*
import vitorscoelho.ecfx.gui.controller.ControllerResultados
import vitorscoelho.ecfx.gui.controller.ScopeResultados
import vitorscoelho.ecfx.gui.descricoes
import vitorscoelho.ecfx.gui.estilo.EstiloPrincipal
import vitorscoelho.utils.tfx.inputTextFieldDouble

internal class ViewResultados : View(title = descricoes.rb["tituloJanelaResultados"]) {
    private val controller: ControllerResultados by inject(overrideScope = scope)

    override val root = hbox {
        addClass(EstiloPrincipal.vboxDados)
        with(controller.resultados) {
            form {
                fieldset(descricoes.rb["fieldsetTensoesNaBase"]) {
                    inputTextFieldDouble(property = tensaoAtuanteMediaProp)
                    inputTextFieldDouble(property = tensaoAtuanteMaximaProp)
                    inputTextFieldDouble(property = tensaoAtuanteMinimaProp)
                }
                fieldset(descricoes.rb["fieldsetTensoesNoFuste"])
            }
            form {
                fieldset(descricoes.rb["fieldsetEsforcosNoFuste"]) {
                    inputTextFieldDouble(property = normalProp)
                    inputTextFieldDouble(property = forcaHorizontalProp)
                    inputTextFieldDouble(property = momentoProp)
                }
                fieldset(descricoes.rb["fieldsetDeslocamentosNoTopo"]) {
                    inputTextFieldDouble(property = rotacaoProp)
                    inputTextFieldDouble(property = deltaHProp)
                    inputTextFieldDouble(property = deltaVProp)
                }
            }
            form {
                fieldset(descricoes.rb["fieldsetDadosAdicionaisDoSolo"])
                fieldset(descricoes.rb["fieldsetArmaduras"])
            }
        }
    }
}