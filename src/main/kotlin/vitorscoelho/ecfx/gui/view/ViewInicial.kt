package vitorscoelho.ecfx.gui.view

import javafx.beans.property.SimpleObjectProperty
import javafx.geometry.Orientation
import javafx.scene.image.Image
import tech.units.indriya.unit.Units.METRE
import tech.units.indriya.unit.Units.PASCAL
import tornadofx.*
import vitorscoelho.ecfx.gui.controller.ControllerInicial
import vitorscoelho.ecfx.gui.descricoes
import vitorscoelho.ecfx.gui.estilo.EstiloPrincipal
import vitorscoelho.ecfx.gui.model.BeanArmaduraModel
import vitorscoelho.ecfx.gui.model.BeanConcretoModel
import vitorscoelho.ecfx.gui.model.TipoArmadura
import vitorscoelho.ecfx.utils.*
import vitorscoelho.utils.tfx.inputTextFieldPositiveDouble


class ViewInicial : View(title = TITULO_VIEW_INICIAL) {
    private val controller: ControllerInicial by inject()

    init {
        setStageIcon(icon = Image("/vitorscoelho/ecfx/gui/icones/icone.png"))//vitorscoelho/ecfx/gui/icones
        primaryStage.setOnCloseRequest { windowEvent ->
            windowEvent.consume()
            controller.acaoFecharPrograma(currentWindow!!)
        }
    }

    //    val dados = Dados()
    val unitResistenciaMaterialProperty = SimpleObjectProperty(PASCAL)
    val unitModuloElasticidadeConcretoProperty = SimpleObjectProperty(PASCAL)
    val unitModuloElasticidadeAcoProperty = SimpleObjectProperty(PASCAL)
    val unitBitolaProperty = SimpleObjectProperty(METRE)
    val concreto = BeanConcretoModel(
        unitResistencia = unitResistenciaMaterialProperty,
        unitModuloElasticidade = unitModuloElasticidadeConcretoProperty
    )
    val armaduraTransversal = BeanArmaduraModel(
        tipo = TipoArmadura.ESTRIBO,
        unitResistencia = unitResistenciaMaterialProperty,
        unitModuloElasticidade = unitModuloElasticidadeAcoProperty,
        unitBitola = unitBitolaProperty
    )
    val armaduraLongitudinal = BeanArmaduraModel(
        tipo = TipoArmadura.LONGITUDINAL,
        unitResistencia = unitResistenciaMaterialProperty,
        unitModuloElasticidade = unitModuloElasticidadeAcoProperty,
        unitBitola = unitBitolaProperty
    )
    private val conteudo = vbox {
        addClass(EstiloPrincipal.vboxDados)
        form {
            fieldset(text = descricoes.rb["fieldsetConcreto"]) {
                labelPosition = Orientation.VERTICAL
                inputTextFieldPositiveDouble(property = concreto.fck)
                inputTextFieldPositiveDouble(property = concreto.gamaC)
                inputTextFieldPositiveDouble(property = concreto.ecs)
            }
            fieldset(text = descricoes.rb["fieldsetArmaduraTransversal"]) {
                labelPosition = Orientation.VERTICAL
                inputTextFieldPositiveDouble(property = armaduraTransversal.fyk)
                inputTextFieldPositiveDouble(property = armaduraTransversal.gamaS)
                inputTextFieldPositiveDouble(property = armaduraTransversal.bitola)
            }
            fieldset(text = descricoes.rb["fieldsetArmaduraLongitudinal"]) {
                labelPosition = Orientation.VERTICAL
                inputTextFieldPositiveDouble(property = armaduraLongitudinal.fyk)
                inputTextFieldPositiveDouble(property = armaduraLongitudinal.gamaS)
                inputTextFieldPositiveDouble(property = armaduraLongitudinal.moduloElasticidade)
                inputTextFieldPositiveDouble(property = armaduraLongitudinal.bitola)
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

    override val root = borderpane {
        top {
            menubar {
                menu("Arquivo") {
                    item("Sobre")
                    item("Abrir")
                    item("Salvar")
                    item("Fechar") { action { controller.acaoFecharPrograma(currentWindow!!) } }
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