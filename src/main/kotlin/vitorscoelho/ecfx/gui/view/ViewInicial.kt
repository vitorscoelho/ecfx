package vitorscoelho.ecfx.gui.view

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.geometry.Orientation
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import tech.units.indriya.unit.Units.*
import tornadofx.*
import vitorscoelho.ecfx.gui.controller.ControllerInicial
import vitorscoelho.ecfx.gui.descricoes
import vitorscoelho.ecfx.gui.estilo.EstiloPrincipal
import vitorscoelho.ecfx.gui.model.*
import vitorscoelho.ecfx.utils.*
import vitorscoelho.utils.tfx.comboboxField
import vitorscoelho.utils.tfx.inputTextFieldPositiveDouble
import javax.measure.quantity.Moment
import javax.measure.quantity.SpecificWeight
import javax.measure.quantity.SpringStiffnessPerUnitArea


class ViewInicial : View(title = TITULO_VIEW_INICIAL) {
    private val controller: ControllerInicial by inject()

    init {
        setStageIcon(icon = ICONE_DO_PROGRAMA)
        primaryStage.setOnCloseRequest { windowEvent ->
            windowEvent.consume()
            controller.acaoFecharPrograma(currentWindow!!)
        }
    }

    val unitResistenciaMaterialProperty = SimpleObjectProperty(PASCAL)
    val unitModuloElasticidadeConcretoProperty = SimpleObjectProperty(PASCAL)
    val unitModuloElasticidadeAcoProperty = SimpleObjectProperty(PASCAL)
    val unitBitolaProperty = SimpleObjectProperty(METRE)
    val unitCobrimento = SimpleObjectProperty(METRE)
    val unitDimensoesEstaca = SimpleObjectProperty(METRE)
    val unitProfundidade = SimpleObjectProperty(METRE)
    val unitForca = SimpleObjectProperty(NEWTON)
    val unitMomento = SimpleObjectProperty(NEWTON.multiply(METRE).asType(Moment::class.java))
    val unitComprimentoArmadura = SimpleObjectProperty(METRE)
    val unitTensaoConcreto = SimpleObjectProperty(PASCAL)
    val unitCoeficienteReacao = SimpleObjectProperty(
        NEWTON.divide(CUBIC_METRE).asType(SpringStiffnessPerUnitArea::class.java)
    )
    val unitCoesao = SimpleObjectProperty(PASCAL)
    val unitAnguloDeAtrito = SimpleObjectProperty(RADIAN)
    val unitPesoEspecifico = SimpleObjectProperty(NEWTON.divide(CUBIC_METRE).asType(SpecificWeight::class.java))
    val unitTensaoSolo = SimpleObjectProperty(PASCAL)

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
    val caracteristicasGeometricas = BeanCaracteristicasGeometricasModel(
        unitCobrimento = unitCobrimento,
        unitDimensoesEstaca = unitDimensoesEstaca,
        unitProfundidade = unitProfundidade
    )
    val cargasNoTopo = BeanCargasNoTopoModel(
        unitForca = unitForca,
        unitMomento = unitMomento
    )
    val tipoEstaca = BeanTipoEstacaModel(
        unitComprimentoArmadura = unitComprimentoArmadura,
        unitTensaoConcreto = unitTensaoConcreto
    )
    val solo = BeanSoloModel(
        unitCoeficienteReacao = unitCoeficienteReacao,
        unitCoesao = unitCoesao,
        unitAnguloDeAtrito = unitAnguloDeAtrito,
        unitPesoEspecifico = unitPesoEspecifico,
        unitTensaoSolo = unitTensaoSolo
    )
    private val conteudo = hbox {
        addClass(EstiloPrincipal.vboxDados)
        form {
            fieldset(text = descricoes.rb["fieldsetConcreto"]) {
                labelPosition = Orientation.VERTICAL
                with(concreto) {
                    inputTextFieldPositiveDouble(property = fck)
                    inputTextFieldPositiveDouble(property = gamaC)
                    inputTextFieldPositiveDouble(property = ecs)
                }
            }
            fieldset(text = descricoes.rb["fieldsetArmaduraTransversal"]) {
                labelPosition = Orientation.VERTICAL
                with(armaduraTransversal) {
                    inputTextFieldPositiveDouble(property = fyk)
                    inputTextFieldPositiveDouble(property = gamaS)
                    inputTextFieldPositiveDouble(property = bitola)
                }
            }
            fieldset(text = descricoes.rb["fieldsetArmaduraLongitudinal"]) {
                labelPosition = Orientation.VERTICAL
                with(armaduraLongitudinal) {
                    inputTextFieldPositiveDouble(property = fyk)
                    inputTextFieldPositiveDouble(property = gamaS)
                    inputTextFieldPositiveDouble(property = moduloElasticidade)
                    inputTextFieldPositiveDouble(property = bitola)
                }
            }
        }
        form {
            fieldset(text = descricoes.rb["fieldsetTipoEstaca"]) {
                labelPosition = Orientation.VERTICAL
                with(tipoEstaca) {
                    comboboxField(property = tipo, values = listaEstacas)
                    inputTextFieldPositiveDouble(property = comprimentoMinimoArmadura)
                    inputTextFieldPositiveDouble(property = tensaoMediaMaxima)
                }
            }
            fieldset(text = descricoes.rb["fieldsetSolo"]) {
                labelPosition = Orientation.VERTICAL
                with(solo) {
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
            fieldset(text = descricoes.rb["fieldsetCaracteristicasGeometricas"]) {
                labelPosition = Orientation.VERTICAL
                with(caracteristicasGeometricas) {
                    inputTextFieldPositiveDouble(property = cobrimento)
                    inputTextFieldPositiveDouble(property = diametroFuste)
                    inputTextFieldPositiveDouble(property = diametroBase)
                    inputTextFieldPositiveDouble(property = alturaBase)
                    inputTextFieldPositiveDouble(property = rodape)
                    inputTextFieldPositiveDouble(property = profundidade)
                }
            }
            fieldset(text = descricoes.rb["fieldsetCargasNoTopo"]) {
                labelPosition = Orientation.VERTICAL
                with(cargasNoTopo) {
                    inputTextFieldPositiveDouble(property = normal)
                    inputTextFieldPositiveDouble(property = forcaHorizontal)//TODO Na verdade, deveria poder ser negativo. Checar os resultados
                    inputTextFieldPositiveDouble(property = momento)//TODO Na verdade, deveria poder ser negativo. Checar os resultados
                    inputTextFieldPositiveDouble(property = gamaN)
                }
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
                menu(descricoes.rb["menu.arquivo"]) {
                    item(descricoes.rb["menu.item.sobre"])
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
        }
        center {
            this += conteudo
        }
    }
}