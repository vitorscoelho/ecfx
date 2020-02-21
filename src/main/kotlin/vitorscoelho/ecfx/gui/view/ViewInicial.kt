package vitorscoelho.ecfx.gui.view

import javafx.geometry.Orientation
import tornadofx.*
import vitorscoelho.ecfx.gui.controller.ControllerInicial
import vitorscoelho.ecfx.gui.descricoes
import vitorscoelho.ecfx.gui.estilo.EstiloPrincipal
import vitorscoelho.ecfx.gui.model.TipoSolo
import vitorscoelho.ecfx.utils.ICONE_DO_PROGRAMA
import vitorscoelho.ecfx.utils.TITULO_VIEW_INICIAL
import vitorscoelho.utils.tfx.*
import javax.measure.Quantity

internal class ViewInicial : View(title = TITULO_VIEW_INICIAL) {
    private val controller: ControllerInicial by inject()

    private val validationContext = ValidationContext().apply {
        decorationProvider = {
            MessageDecorator(
                message = it.message,
                severity = it.severity,
                tooltipCssRule = EstiloPrincipal.tooltipErro
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

    private val conteudo = hbox {
        addClass(EstiloPrincipal.vboxDados)
        form {
            fieldset(text = descricoes.rb["fieldsetConcreto"]) {
                labelPosition = Orientation.VERTICAL
                with(controller.concreto) {
                    inputTextFieldPositiveDouble(property = fck).maiorQueZero()
                    inputTextFieldPositiveDouble(property = gamaC).maiorQueZero()
                    inputTextFieldPositiveDouble(property = ecs).maiorQueZero()
                }
            }
            fieldset(text = descricoes.rb["fieldsetArmaduras"]) {
                labelPosition = Orientation.VERTICAL
                with(controller.armaduras) {
                    inputTextFieldPositiveDouble(property = fykEstribo).maiorQueZero()
                    inputTextFieldPositiveDouble(property = bitolaEstribo).maiorQueZero()
                    inputTextFieldPositiveDouble(property = fykLongitudinal).maiorQueZero()
                    inputTextFieldPositiveDouble(property = bitolaLongitudinal).maiorQueZero()
                    inputTextFieldPositiveDouble(property = gamaS).maiorQueZero()
                    inputTextFieldPositiveDouble(property = moduloElasticidade).maiorQueZero()
                }
            }
        }
        form {
            fieldset(text = descricoes.rb["fieldsetSolo"]) {
                labelPosition = Orientation.VERTICAL
                with(controller.solo) {
                    comboboxField(property = tipo, values = TipoSolo.values().toList().toObservable())
                    inputTextFieldPositiveDouble(property = kh).maiorQueZero()
                    inputTextFieldPositiveDouble(property = kv).maiorQueZero()
                    inputTextFieldPositiveDouble(property = coesao)
                    inputTextFieldPositiveDouble(property = anguloDeAtrito)
                    inputTextFieldPositiveDouble(property = pesoEspecifico).maiorQueZero()
                    inputTextFieldPositiveDouble(property = tensaoAdmissivel).maiorQueZero()
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
                    inputTextFieldPositiveDouble(property = cobrimento).maiorQueZero()
                    inputTextFieldPositiveDouble(property = diametroFuste).maiorQueZero()
                    inputTextFieldPositiveDouble(property = diametroBase).maiorQueZero()
                    inputTextFieldPositiveDouble(property = profundidade).maiorQueZero()
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
                //                enableWhen { controller.concreto.dirty }
                action { controller.concreto.commit() }
                action { controller.acaoBtnVisualizarResultados() }
            }
        }
        validationContext.validate()
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

    private fun InputTextField<*>.maiorQueZero(): InputTextField<*> = this.apply {
        addValidator(validationContext, ERROR_IF_NOT_POSITIVE_DOUBLE)
    }
}