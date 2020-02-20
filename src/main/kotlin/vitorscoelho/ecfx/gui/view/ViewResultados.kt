package vitorscoelho.ecfx.gui.view

import javafx.scene.Parent
import tornadofx.*
import vitorscoelho.ecfx.gui.descricoes
import vitorscoelho.ecfx.gui.estilo.EstiloPrincipal

internal class ViewResultados : View(title = descricoes.rb["tituloJanelaResultados"]) {
    override val root = hbox {
        addClass(EstiloPrincipal.vboxDados)
        form {
            fieldset(descricoes.rb["fieldsetTensoesNaBase"])
            fieldset(descricoes.rb["fieldsetTensoesNoFuste"])
        }
        form {
            fieldset(descricoes.rb["fieldsetEsforcosNoFuste"])
            fieldset(descricoes.rb["fieldsetDeslocamentosNoTopo"])
        }
        form {
            fieldset(descricoes.rb["fieldsetDadosAdicionaisDoSolo"])
            fieldset(descricoes.rb["fieldsetArmaduras"])
            fieldset(descricoes.rb["fieldsetVolumes"])
        }
    }
}