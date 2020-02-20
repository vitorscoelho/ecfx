package vitorscoelho.ecfx.gui.view

import javafx.geometry.Orientation
import tornadofx.*
import vitorscoelho.ecfx.gui.controller.ControllerInicial
import vitorscoelho.ecfx.gui.descricoes
import vitorscoelho.ecfx.gui.estilo.EstiloPrincipal
import vitorscoelho.ecfx.gui.model.TipoSolo
import vitorscoelho.ecfx.utils.ICONE_DO_PROGRAMA
import vitorscoelho.ecfx.utils.TITULO_VIEW_INICIAL
import vitorscoelho.utils.tfx.checkboxField
import vitorscoelho.utils.tfx.comboboxField
import vitorscoelho.utils.tfx.inputTextFieldPositiveDouble

internal class ViewInicial : View(title = TITULO_VIEW_INICIAL) {
    private val controller: ControllerInicial by inject()

    init {
        setStageIcon(icon = ICONE_DO_PROGRAMA)
        primaryStage.setOnCloseRequest { windowEvent ->
            windowEvent.consume()
            controller.acaoFecharPrograma(currentWindow!!)
        }
    }

    private val conteudo = hbox {
        addClass(EstiloPrincipal.vboxDados)
        form {
            fieldset(text = descricoes.rb["fieldsetConcreto"]) {
                labelPosition = Orientation.VERTICAL
                with(controller.concreto) {
                    inputTextFieldPositiveDouble(property = fck)
                    inputTextFieldPositiveDouble(property = gamaC)
                    inputTextFieldPositiveDouble(property = ecs)
                }
            }
            fieldset(text = descricoes.rb["fieldsetArmaduras"]) {
                labelPosition = Orientation.VERTICAL
                with(controller.armaduras) {
                    inputTextFieldPositiveDouble(property = fykEstribo)
                    inputTextFieldPositiveDouble(property = bitolaEstribo)
                    inputTextFieldPositiveDouble(property = fykLongitudinal)
                    inputTextFieldPositiveDouble(property = bitolaLongitudinal)
                    inputTextFieldPositiveDouble(property = gamaS)
                    inputTextFieldPositiveDouble(property = moduloElasticidade)
                }
            }
        }
        form {
            fieldset(text = descricoes.rb["fieldsetSolo"]) {
                labelPosition = Orientation.VERTICAL
                with(controller.solo) {
                    comboboxField(property = tipo, values = TipoSolo.values().toList().toObservable())
                    inputTextFieldPositiveDouble(property = kh)
                    inputTextFieldPositiveDouble(property = kv)
                    inputTextFieldPositiveDouble(property = coesao)
                    inputTextFieldPositiveDouble(property = anguloDeAtrito)
                    inputTextFieldPositiveDouble(property = pesoEspecifico)
                    inputTextFieldPositiveDouble(property = tensaoAdmissivel)
                }
            }
        }
        form {
            fieldset(text = descricoes.rb["fieldsetDadosDaFundacao"]) {
                labelPosition = Orientation.VERTICAL
                with(controller.dadosDaFundacao) {
                    checkboxField(property = armaduraIntegral)
                    inputTextFieldPositiveDouble(property = comprimentoMinimoArmadura) {
                        disableWhen { armaduraIntegral }
                    }
                    inputTextFieldPositiveDouble(property = tensaoMediaMaxima) {
                        disableWhen { armaduraIntegral }
                    }
                    inputTextFieldPositiveDouble(property = cobrimento)
                    inputTextFieldPositiveDouble(property = diametroFuste)
                    inputTextFieldPositiveDouble(property = diametroBase)
                    inputTextFieldPositiveDouble(property = profundidade)
                }
            }
            fieldset(text = descricoes.rb["fieldsetCargasNoTopo"]) {
                labelPosition = Orientation.VERTICAL
                with(controller.cargasNoTopo) {
                    inputTextFieldPositiveDouble(property = normal)
                    inputTextFieldPositiveDouble(property = forcaHorizontal)//TODO Na verdade, deveria poder ser negativo. Checar os resultados
                    inputTextFieldPositiveDouble(property = momento)//TODO Na verdade, deveria poder ser negativo. Checar os resultados
                    inputTextFieldPositiveDouble(property = gamaN)
                }
            }
        }
        vbox {
            button(descricoes.rb["botao.valoresSugeridos"]) {
                action { controller.acaoBtnValoresSugeridos() }
            }
            button(descricoes.rb["botao.visualizarResultados"]) {
                isDisable = true
                enableWhen { controller.concreto.dirty }
                action { controller.concreto.commit() }
                //action { controller.acaoBtnVisualizarResultados() }
            }
        }
        //        form {
//            hbox {
//                vbox {
//                    fieldset("Concreto") {
//                        labelPosition = Orientation.VERTICAL
//                        fieldQuantity(property = dados.fckProperty)
//                        fieldQuantity(property = dados.gamaCProperty)
//                        fieldQuantity(property = dados.moduloDeformacaoConcretoProperty) { tf ->
//                            tf.editableWhen(dados.agregadoProperty.isEqualTo(AGREGADO_QUALQUER))
//                        }
//                        field("Agregado graúdo") {
//                            combobox(values = AGREGADOS_DISPONIVEIS, property = dados.agregadoProperty) {
//                                tooltip(
//                                    """
//                                Selecione o agregado graúdo usado no concreto.
//                                O módulo de deformação será calculado baseado no agregado selecionado.
//                                Para informar manualmente o módulo de deformação, selecione "Outro".
//                            """.trimIndent()
//                                ).apply { showDelay = DELAY_TOOLTIP }
//                            }
//                        }
//                    }
//                    fieldset("Armadura transversal\r\n(Estribos)") {
//                        labelPosition = Orientation.VERTICAL
//                        fieldQuantity(property = dados.estriboFywkProperty)
//                        fieldQuantity(property = dados.estriboGamaSProperty)
//                        fieldQuantity(property = dados.estriboBitolaProperty)
//                    }
//                    fieldset("Armadura longitudinal") {
//                        labelPosition = Orientation.VERTICAL
//                        fieldQuantity(property = dados.longitudinalFykProperty)
//                        fieldQuantity(property = dados.longitudinalGamaSProperty)
//                        fieldQuantity(property = dados.longitudinalModuloDeformacaoProperty)
//                        fieldQuantity(property = dados.longitudinalBitolaProperty)
//                    }
//                }
//                vbox {
//                    fieldset("Características geométricas") {
//                        labelPosition = Orientation.VERTICAL
//                        fieldQuantity(property = dados.cobrimentoProperty)
//                        fieldQuantity(property = dados.diametroFusteProperty)
//                        fieldQuantity(property = dados.diametroBaseProperty)
//                        fieldQuantity(property = dados.alturaBaseProperty)
//                        fieldQuantity(property = dados.rodapeBaseProperty)
//                        fieldQuantity(property = dados.profundidadeProperty)
//                    }
//                    fieldset("Cargas no topo") {
//                        labelPosition = Orientation.VERTICAL
//                        fieldQuantity(property = dados.normalProperty)
//                        fieldQuantity(property = dados.forcaHorizontalProperty)
//                        fieldQuantity(property = dados.momentoProperty)
//                        fieldQuantity(property = dados.gamaNProperty)
//                    }
//                }
//                vbox {
//                    fieldset("Características do elemento de fundação") {
//                        labelPosition = Orientation.VERTICAL
//                        field("Tipo") {
//                            combobox(property = dados.tipoEstaca)
//                        }
//                    }
//                    fieldset("Características do solo") {
//                        labelPosition = Orientation.VERTICAL
//                        field("Tipo do solo") {
//                            combobox(property = dados.tipoSolo, values = TipoSoloModel.values().toList()) { }
//                        }
//                        fieldQuantity(property = dados.kvProperty)
//                        fieldQuantity(property = dados.coesaoProperty)
//                        fieldQuantity(property = dados.anguloDeAtritoProperty)
//                        fieldQuantity(property = dados.pesoEspecificoProperty)
//                        fieldQuantity(property = dados.tensaoAdmissivelProperty)
//                    }
//                }
//            }
//        }
    }

    private val menu = menubar {
        menu(descricoes.rb["menu.arquivo"]) {
            item(descricoes.rb["menu.item.sobre"]) { action { controller.acaoMenuItemSobre() } }
            item(descricoes.rb["menu.item.abrir"])
            item(descricoes.rb["menu.item.salvar"])
            item(descricoes.rb["menu.item.fechar"]) { action { controller.acaoFecharPrograma(currentWindow!!) } }
        }
        menu(descricoes.rb["menu.opcoes"]) {
            item(descricoes.rb["menu.item.calculadorasDeDimensionamento"])
            item(descricoes.rb["menu.item.parametros"])
            item(descricoes.rb["menu.item.unidades"])
        }
    }

    override val root = borderpane {
        top {
            this += menu
        }
        center {
            this += conteudo
        }
    }
}