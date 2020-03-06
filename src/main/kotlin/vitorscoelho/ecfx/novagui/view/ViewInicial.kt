package vitorscoelho.ecfx.novagui.view

import javafx.event.EventTarget
import javafx.geometry.Orientation
import tornadofx.*
import vitorscoelho.ecfx.gui.estilo.EstiloPrincipal
import vitorscoelho.ecfx.novagui.utils.*
import vitorscoelho.ecfx.utils.ICONE_DO_PROGRAMA
import vitorscoelho.ecfx.utils.TITULO_VIEW_INICIAL
import vitorscoelho.utils.tfx.MessageDecorator

internal class ViewInicial : View(title = TITULO_VIEW_INICIAL) {
    private val controller: ControllerInicial by inject()
    private val model: ModelInicial
        get() = controller.model

    private val validationContext = ValidationContext().apply {
        decorationProvider = {
            MessageDecorator(
                message = it.message,
                severity = it.severity,
                tooltipCssRule = vitorscoelho.utils.tfx.exemplo.EstiloPrincipal.tooltipErro
            )
        }
    }

    init {
        setStageIcon(icon = ICONE_DO_PROGRAMA)
        primaryStage.setOnCloseRequest { windowEvent ->
            windowEvent.consume()
            controller.acaoFecharPrograma(currentWindow!!)
        }
    }

    fun <T> EventTarget.fieldTextField(property: TextGuiProp<T>, op: Field.() -> Unit = {}): Field {
        val field = fieldTextField(property = property, validationContext = validationContext)
        op(field)
        return field
    }

//    private val conteudo = form {
//        fieldset("Concreto") {
//            fieldTextField(property = model.fck, validationContext = validationContext)
////            field("fck") {
////                textfield(property = fck.textProp)
////            }
//            labelPosition = Orientation.VERTICAL
////            textfield(property = fck)
//            field("Formato") {
//                textfield(Formatos.resistenciaMaterial.format.transitional)
//            }
//            field("Unidade") {
//                combobox(
//                    property = Formatos.resistenciaMaterial.unit.transitional,
//                    values = listOf(PASCAL, KILOPASCAL, MEGAPASCAL)
//                )
//            }
//            button("Aplica formato") { action { Formatos.resistenciaMaterial.format.commit() } }
//            button("Aplica unidade") { action { Formatos.resistenciaMaterial.unit.commit() } }
//            button("Commit") { action { model.fck.commit() };enableWhen(model.fck.dirty) }
//            button("Rollback") { action { model.fck.rollback() } }
//        }
//    }

    private val conteudo = hbox {
        form {
            fieldset(rb["fieldsetConcreto"]) {
                labelPosition = Orientation.VERTICAL
                fieldTextField(property = model.fck)
                fieldTextField(property = model.gamaC)
                fieldTextField(property = model.ecs)
            }
            fieldset(rb["fieldsetArmaduras"]) {
                labelPosition = Orientation.VERTICAL
                fieldTextField(property = model.fywk)
                fieldTextField(property = model.bitolaEstribo)
                fieldTextField(property = model.fyk)
                fieldTextField(property = model.bitolaLongitudinal)
                fieldTextField(property = model.gamaS)
                fieldTextField(property = model.es)
            }
        }
        form {
            fieldset(rb["fieldsetSolo"]) {
                labelPosition = Orientation.VERTICAL
                fieldCombobox(property = model.tipoSolo, values = TipoSolo.values().toList())
                val fieldKhAreiaOuArgilaMole = fieldTextField(property = model.khAreiaOuArgilaMole)
                val fieldKhArgilaRijaADura = fieldTextField(property = model.khArgilaRijaADura)
                fieldTextField(property = model.kv)
                fieldTextField(property = model.coesao)
                fieldTextField(property = model.anguloDeAtrito)
                fieldTextField(property = model.pesoEspecifico)
                fieldTextField(property = model.tensaoAdmissivel)
                model.tipoSolo.transitional.doNowAndOnChange {
                    children.remove(fieldKhArgilaRijaADura)
                    children.remove(fieldKhAreiaOuArgilaMole)
                    when (it) {
                        TipoSolo.AREIA_OU_ARGILA_MOLE -> children.add(2, fieldKhAreiaOuArgilaMole)
                        TipoSolo.ARGILA_RIJA_A_DURA -> children.add(2, fieldKhArgilaRijaADura)
                    }
                }
            }
        }
        form {
            fieldset(rb["fieldsetDadosDaFundacao"]) {
                labelPosition = Orientation.VERTICAL
                fieldCheckbox(property = model.armaduraIntegral)
                fieldTextField(property = model.compMinimoArmadura) {
                    disableWhen(model.armaduraIntegral.transitional)
                }
                fieldTextField(property = model.tensaoMediaMaximaConcreto) {
                    disableWhen(model.armaduraIntegral.transitional)
                }
                fieldTextField(property = model.cobrimento)
                fieldTextField(property = model.diametroFuste)
                fieldTextField(property = model.diametroBase)
                fieldTextField(property = model.profundidadeEstaca)
            }
            fieldset(rb["fieldsetCargasNoTopo"]) {
                labelPosition = Orientation.VERTICAL
                fieldTextField(property = model.forcaNormal)
                fieldTextField(property = model.forcaHorizontal)
                fieldTextField(property = model.momentoFletor)
                fieldTextField(property = model.gamaN)
            }
        }
        vbox {
            button("Dimensionar") { action { controller.acaoDimensionar() } }
            button("Rollback") { action { controller.model.rollback() } }
            button("Dirty") { enableWhen { controller.model.createDirtyBinding() } }
        }
    }

    private val menu = menubar {
        menu(rb["menu.arquivo"]) {
            item(rb["menu.item.sobre"]) { action { controller.acaoMenuItemSobre() } }
            item(rb["menu.item.abrir"])
            item(rb["menu.item.salvar"])
            item(rb["menu.item.fechar"]) { action { controller.acaoFecharPrograma(currentWindow!!) } }
        }
        menu(rb["menu.configuracoes"]) {
            item(rb["menu.item.parametros"])
            item(rb["menu.item.unidades"])
        }
        menu(rb["menu.calculadoras"]) {
            item(rb["menu.item.calculadoraDimensionamentoEstrutural"])
            item(rb["menu.item.calculadoraVolumeTubulao"])
        }
    }

    override val root = borderpane {
        top {
            this += menu
        }
        center {
            hbox {
                addClass(EstiloPrincipal.vboxDados)
                this += conteudo
            }
        }
    }
}