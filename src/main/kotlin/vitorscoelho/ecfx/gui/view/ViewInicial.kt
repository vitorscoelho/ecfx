package vitorscoelho.ecfx.gui.view

import javafx.geometry.Orientation
import javafx.scene.Parent
import javafx.scene.control.ToggleGroup
import javafx.scene.image.Image
import tornadofx.*
import vitorscoelho.ecfx.dimensionamento.geotecnico.AnaliseKhDegrau
import vitorscoelho.ecfx.dimensionamento.geotecnico.AnaliseKhLinearmenteVariavel
import vitorscoelho.ecfx.gui.controller.ControllerInicial
import vitorscoelho.ecfx.gui.estilo.DELAY_TOOLTIP
import vitorscoelho.ecfx.gui.model.AGREGADOS_DISPONIVEIS
import vitorscoelho.ecfx.gui.model.AGREGADO_QUALQUER
import vitorscoelho.ecfx.gui.model.Dados
import vitorscoelho.ecfx.gui.model.TipoSoloModel
import vitorscoelho.ecfx.utils.*


class ViewInicial : View(title = TITULO_VIEW_INICIAL) {
    private val controller: ControllerInicial by inject()

    init {
        setStageIcon(icon = Image("/vitorscoelho/ecfx/gui/icones/icone.png"))//vitorscoelho/ecfx/gui/icones
        primaryStage.setOnCloseRequest { windowEvent ->
            windowEvent.consume()
            controller.acaoFecharPrograma(currentWindow!!)
        }
    }

    val dados = Dados()
    private val conteudo = vbox {
        form {
            hbox {
                vbox {
                    fieldset("Concreto") {
                        labelPosition = Orientation.VERTICAL
                        fieldQuantity(property = dados.fckProperty)
                        fieldQuantity(property = dados.gamaCProperty)
                        fieldQuantity(property = dados.moduloDeformacaoConcretoProperty) { tf ->
                            tf.editableWhen(dados.agregadoProperty.isEqualTo(AGREGADO_QUALQUER))
                        }
                        field("Agregado graúdo") {
                            combobox(values = AGREGADOS_DISPONIVEIS, property = dados.agregadoProperty) {
                                tooltip(
                                    """
                                Selecione o agregado graúdo usado no concreto.
                                O módulo de deformação será calculado baseado no agregado selecionado.
                                Para informar manualmente o módulo de deformação, selecione "Outro".
                            """.trimIndent()
                                ).apply { showDelay = DELAY_TOOLTIP }
                            }
                        }
                    }
                    fieldset("Armadura transversal\r\n(Estribos)") {
                        labelPosition = Orientation.VERTICAL
                        fieldQuantity(property = dados.estriboFywkProperty)
                        fieldQuantity(property = dados.estriboGamaSProperty)
                        fieldQuantity(property = dados.estriboBitolaProperty)
                    }
                    fieldset("Armadura longitudinal") {
                        labelPosition = Orientation.VERTICAL
                        fieldQuantity(property = dados.longitudinalFykProperty)
                        fieldQuantity(property = dados.longitudinalGamaSProperty)
                        fieldQuantity(property = dados.longitudinalModuloDeformacaoProperty)
                        fieldQuantity(property = dados.longitudinalBitolaProperty)
                    }
                }
                vbox {
                    fieldset("Características geométricas") {
                        labelPosition = Orientation.VERTICAL
                        fieldQuantity(property = dados.cobrimentoProperty)
                        fieldQuantity(property = dados.diametroFusteProperty)
                        fieldQuantity(property = dados.diametroBaseProperty)
                        fieldQuantity(property = dados.alturaBaseProperty)
                        fieldQuantity(property = dados.rodapeBaseProperty)
                        fieldQuantity(property = dados.profundidadeProperty)
                    }
                    fieldset("Cargas no topo") {
                        labelPosition = Orientation.VERTICAL
                        fieldQuantity(property = dados.normalProperty)
                        fieldQuantity(property = dados.forcaHorizontalProperty)
                        fieldQuantity(property = dados.momentoProperty)
                        fieldQuantity(property = dados.gamaNProperty)
                    }
                }
                vbox {
                    fieldset("Características do elemento de fundação") {
                        labelPosition = Orientation.VERTICAL
                        field("Tipo") {
                            combobox(property = dados.tipoEstaca)
                        }
                    }
                    fieldset("Características do solo") {
                        labelPosition = Orientation.VERTICAL
                        field("Tipo do solo") {
                            combobox(property = dados.tipoSolo, values = TipoSoloModel.values().toList()) { }
                        }
                        fieldQuantity(property = dados.kvProperty)
                        fieldQuantity(property = dados.coesaoProperty)
                        fieldQuantity(property = dados.anguloDeAtritoProperty)
                        fieldQuantity(property = dados.pesoEspecificoProperty)
                        fieldQuantity(property = dados.tensaoAdmissivelProperty)
                    }
                }
            }
        }
    }

    override val root = borderpane {
        top {
            menubar {
                menu("Arquivo") {
                    item("Sobre")
                    item("Abrir")
                    item("Salvar")
                    item("Fechar"){action { controller.acaoFecharPrograma(currentWindow!!) }}
                }
                menu("Opções") {
                    item("Calculadoras de dimensionamento")
                    item("Parâmetros")
                }
            }
        }
        center {
            this += conteudo
        }
    }
}