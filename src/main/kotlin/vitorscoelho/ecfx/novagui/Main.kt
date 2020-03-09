package vitorscoelho.ecfx.novagui

import javafx.application.Application
import tornadofx.App
import tornadofx.FX
import tornadofx.reloadStylesheetsOnFocus
import vitorscoelho.ecfx.dimensionamento.Esforco
import vitorscoelho.ecfx.dimensionamento.geotecnico.*
import vitorscoelho.ecfx.gui.estilo.EstiloPrincipal
import vitorscoelho.ecfx.novagui.view.inicial.ModelInicial
import vitorscoelho.ecfx.novagui.view.resultadosfuste.ScopeGraficosFuste
import vitorscoelho.ecfx.novagui.view.resultadosfuste.ViewGraficosFuste
import java.util.*

fun main(args: Array<String>) {
    Locale.setDefault(Locale("pt", "BR"))
    FX.locale = Locale("pt", "BR")
    Application.launch(Aplicacao::class.java, *args)
}

class Aplicacao : App(
    ViewGraficosFuste::class, EstiloPrincipal::class, scope = ScopeGraficosFuste(
    criarDados()
)
) {
    init {
        reloadStylesheetsOnFocus()
        val versaoJava = System.getProperty("java.version")
        val versaoJavaFX = System.getProperty("javafx.version")
        println("Versão Java: $versaoJava // Versão JavaFX: $versaoJavaFX")
    }
}

private fun criarDados(): ResultadosAnaliseTubulao {
    val model = ModelInicial()
    model.commit()
    val moduloConcreto = model.ecs.toDoubleSU()
    val fuste: FusteTubulao = FusteCircular(diametro = model.diametroFuste.toDoubleSU())
    val base: BaseTubulao = BaseCircular(
        diametroInferior = model.diametroBase.toDoubleSU(),
        diametroSuperior = model.diametroFuste.toDoubleSU(),
        altura = 0.0,
        rodape = 0.0
    )
    val tubulao = Tubulao(
        fuste = fuste, base = base, comprimento = model.profundidadeEstaca.toDoubleSU()
    )
    val kv: Double = model.kv.toDoubleSU()
    val esforcoTopo = Esforco(
        normal = model.forcaNormal.toDoubleSU(),
        horizontal = model.forcaHorizontal.toDoubleSU(),
        momento = model.momentoFletor.toDoubleSU()
    )
    val analiseTubulao = AnaliseKhLinearmenteVariavel(
        tubulao = tubulao, kv = kv,
        nh = model.khAreiaOuArgilaMole.toDoubleSU()
    )
    val resultadosAnaliseTubulao = analiseTubulao.dimensionar(esforco = esforcoTopo)
    return resultadosAnaliseTubulao
}