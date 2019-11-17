package vitorscoelho.ecfx.gui

import javafx.application.Application
import tornadofx.App
import tornadofx.reloadStylesheetsOnFocus
import vitorscoelho.ecfx.gui.estilo.EstiloPrincipal
import vitorscoelho.ecfx.gui.view.ViewInicial
import vitorscoelho.ecfx.utils.inicializarUnidadesDeMedidaExtras
import java.util.*

fun main(args: Array<String>) {
    Locale.setDefault(Locale.US)
    inicializarUnidadesDeMedidaExtras()
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