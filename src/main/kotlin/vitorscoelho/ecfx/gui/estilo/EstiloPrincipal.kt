package vitorscoelho.ecfx.gui.estilo

import javafx.util.Duration
import tornadofx.*

internal val DELAY_TOOLTIP = Duration(200.0)

internal class EstiloPrincipal : Stylesheet() {
    private val ubuntuRegular = loadFont("/vitorscoelho/ecfx/gui/fontes/Ubuntu-Regular.ttf", 15)!!
    private val ubuntuBold = loadFont("/vitorscoelho/ecfx/gui/fontes/Ubuntu-Bold.ttf", 15)!!
    private val ubuntuLight = loadFont("/vitorscoelho/ecfx/gui/fontes/Ubuntu-Light.ttf", 15)!!

    init {
//        root {
//            font = ubuntuRegular
//            fontSize = 13.px
//        }
//        tooltip {
//            font = ubuntuRegular
//            fontSize = 12.px
//        }
//        fundoBranco {
//            backgroundColor = multi(Color.WHITE)
//        }
//        gridpane {
//            hgap = 2.0.px
//            vgap = 6.0.px
//        }
//        textField{
//            alignment= Pos.CENTER_RIGHT
//        }
//        label
    }

    companion object {
        val fundoBranco by cssclass()
        //        val gridpane by cssclass()
        private val gridpane by csselement("GridPane")
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