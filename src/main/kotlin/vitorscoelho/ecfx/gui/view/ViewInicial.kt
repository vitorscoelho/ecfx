package vitorscoelho.ecfx.gui.view

import javafx.beans.binding.Bindings
import javafx.beans.property.*
import javafx.geometry.Orientation
import javafx.util.StringConverter
import tech.units.indriya.quantity.Quantities
import tech.units.indriya.unit.Units.NEWTON
import tornadofx.*
import vitorscoelho.ecfx.gui.estilo.DELAY_TOOLTIP
import vitorscoelho.ecfx.gui.model.AGREGADOS_DISPONIVEIS
import vitorscoelho.ecfx.gui.model.AGREGADO_QUALQUER
import vitorscoelho.ecfx.gui.model.Dados
import vitorscoelho.ecfx.utils.*
import javax.measure.Quantity
import javax.measure.Unit
import javax.measure.quantity.Force


class ViewInicial : View() {
    val dados = Dados()
    override val root = vbox {
        form {
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
                fieldQuantity(property = dados.estriboGamaS)
                fieldQuantity(property = dados.estriboBitola)
            }
            fieldset("Armadura longitudinal") {
                labelPosition = Orientation.VERTICAL
                fieldQuantity(property = dados.longitudinalFykProperty)
                fieldQuantity(property = dados.longitudinalGamaS)
                fieldQuantity(property = dados.longitudinalModuloDeformacao)
                fieldQuantity(property = dados.longitudinalBitola)
            }
        }
//        spacing = 10.0
//        label {
//            textProperty().bind(Bindings.concat("Força (", forca.unidadeTextProperty, ")"))
//        }
//        textfield(property = forca.valorProperty) {
//
//        }
//        combobox<Unit<Force>>(
//            values = listOf(QUILOGRAMA_FORCA, TONELADA_FORCA, NEWTON, QUILONEWTON, MEGANEWTON),
//            property = unidadeForca
//        ) {
//            converter = UnitStringConverter()
//        }
//        form {
//            fieldset("Concreto") {
//                field("fck") {
//                    textfield { }
//                }
//                field("gamaC") {
//                    textfield { }
//                }
//                field("Ecs") {
//                    textfield { }
//                }
//                field("Agregado") {
//                    combobox<String> { }
//                }
//            }
//            fieldset("Armadura transversal (Estribos)") {
//                field("fywk") {
//                    textfield { }
//                }
//                field("gamaS") {
//                    textfield { }
//                }
//                field("Bitola") {
//                    textfield { }
//                }
//            }
//            fieldset("Armadura longitudinal"){
//                field("fyk"){
//                    textfield {  }
//                }
//                field("gamaS"){
//                    textfield {  }
//                }
//                field("Es"){
//                    textfield {  }
//                }
//                field("Bitola"){
//                    textfield {  }
//                }
//            }
//        }
    }
}