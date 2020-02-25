package vitorscoelho.ecfx.novagui

import javafx.application.Application
import tornadofx.App
import tornadofx.FX
import tornadofx.reloadStylesheetsOnFocus
import vitorscoelho.ecfx.gui.estilo.EstiloPrincipal
import vitorscoelho.ecfx.novagui.view.ViewInicial
import java.util.*

fun main(args: Array<String>) {
    Locale.setDefault(Locale("pt", "BR"))
    FX.locale = Locale("pt", "BR")
    Application.launch(Aplicacao::class.java, *args)
}

class Aplicacao : App(ViewInicial::class, EstiloPrincipal::class) {
    init {
        reloadStylesheetsOnFocus()
        val versaoJava = System.getProperty("java.version")
        val versaoJavaFX = System.getProperty("javafx.version")
        println("Versão Java: $versaoJava // Versão JavaFX: $versaoJavaFX")
    }
}