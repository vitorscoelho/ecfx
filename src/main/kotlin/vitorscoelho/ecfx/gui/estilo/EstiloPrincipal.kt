package vitorscoelho.ecfx.gui.estilo

import javafx.geometry.Pos
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import javafx.util.Duration
import tornadofx.*

internal val DELAY_TOOLTIP = Duration(200.0)

internal class EstiloPrincipal : Stylesheet() {
    private val fonteRegular = loadFont("/vitorscoelho/ecfx/gui/fontes/Ubuntu-Regular.ttf", 15)!!
    private val fonteBold = loadFont("/vitorscoelho/ecfx/gui/fontes/Ubuntu-Bold.ttf", 15)!!
    private val fonteLight = loadFont("/vitorscoelho/ecfx/gui/fontes/Ubuntu-Light.ttf", 15)!!

    init {
        val fontSizePadrao = 13.px
        val fontSizeTooltip = fontSizePadrao
        Stylesheet.root {
            font = fonteRegular
            fontSize = fontSizePadrao
        }
        tooltip {
            backgroundColor = multi(Color.GRAY)
            font = fonteRegular
            fontSize = fontSizeTooltip
        }
        tooltipErro {
            backgroundColor = multi(Color.RED)
            font = fonteRegular
            fontSize = fontSizeTooltip
        }
        val larguraFieldset = 180.px
        val larguraBotaoETextArea = larguraFieldset * 2.0
        val spacingVBox = 15.px
        vboxDados {
            font = fonteRegular
            fontSize = fontSizePadrao
            alignment = Pos.TOP_CENTER
            spacing = spacingVBox
            setMinMaxPrefWidth(Region.USE_COMPUTED_SIZE.px)
            setMinMaxPrefHeight(Region.USE_COMPUTED_SIZE.px)
            Stylesheet.form {
                spacing = 15.px
                font = fonteRegular
                fontSize = fontSizePadrao
                Stylesheet.legend {
                    font = fonteBold
                    fontSize = fontSizePadrao * 1.1
                }
                hbox {
                    spacing = 20.px
                }
                Stylesheet.field {
                    setMinMaxPrefWidth(larguraFieldset)
                    label {
                        minWidth = 200.px
                    }
                    Stylesheet.textField {
                        prefWidth = larguraFieldset
                        maxWidth = larguraFieldset
                        alignment = Pos.CENTER_RIGHT
                    }
                }
            }
            Stylesheet.fieldset {
                backgroundColor = multi(Color.LIGHTGRAY)
                borderColor = multi(box(Color.BLACK))
                padding = box(top = 0.px, bottom = 10.px, right = 15.px, left = 15.px)
            }
            Stylesheet.button {
                setMinMaxPrefWidth(larguraBotaoETextArea)
                font = fonteBold
                fontSize = fontSizePadrao * 1.5
            }
            Stylesheet.textArea {
                setMinMaxPrefWidth(larguraBotaoETextArea)
            }
        }
        vboxCanvas {
            font = fonteRegular
            fontSize = fontSizePadrao * 1.2
        }
    }

    private fun PropertyHolder.setMinMaxPrefWidth(valor: Dimension<Dimension.LinearUnits>) {
        prefWidth = valor; minWidth = valor; maxWidth = valor
    }

    private fun PropertyHolder.setMinMaxPrefHeight(valor: Dimension<Dimension.LinearUnits>) {
        prefHeight = valor; minHeight = valor; maxHeight = valor
    }

    companion object {
        private val gridpane by csselement("GridPane")
        private val hbox by csselement("HBox")
        val tooltipErro by cssclass()
        val vboxDados by cssclass()
        val vboxCanvas by cssclass()
    }
}

/*
CSS DO PROJETO ORIGINAL
.backgroundWhite{
    -fx-background-color: white;
}
.backgroundRed{
    -fx-background-color: red;
}
.backgroundGray{
    -fx-background-color: #cccccc;
}
.backgroundTransparent{
    -fx-background-color: transparent;
}
.borderLightGray{
    -fx-border-color: lightgray;
}
.linhaSelecionada{
    -fx-stroke:red;
}
.linhaNaoSelecionada{
    -fx-stroke:black;
}
 */