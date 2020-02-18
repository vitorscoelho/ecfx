package vitorscoelho.ecfx.utils

import javafx.scene.image.Image
import org.kohsuke.github.GitHub
import tornadofx.get
import vitorscoelho.ecfx.gui.descricoes

const val VERSAO_DO_PROGRAMA = "v1.0"
private val DESCRICAO_DO_PROGRAMA = descricoes.rb["descricaoDoPrograma"]
private val TEXTO_VERSAO = descricoes.rb["textoVersao"]
val TITULO_VIEW_INICIAL = "ECFX: $TEXTO_VERSAO ${VERSAO_DO_PROGRAMA.removePrefix("v")} - $DESCRICAO_DO_PROGRAMA"

private val NOME_REPOSITORIO: String by lazy { "vitorscoelho/ecfx" }

/**@return o nome da tag do último release do repositório*/
fun tagUltimoRelease(): String {
    //TODO Tem que verificar se existe conexão antes de checar a release
    //TODO Dar a opção ao usuário de não ser mais avisado que existe um novo release
    val github = GitHub.connectAnonymously()
    val repositorio = github.getRepository(NOME_REPOSITORIO)
    return repositorio.latestRelease.tagName
}

/**@return **true** se a versão do programa é a mais recente, **false** caso contrário*/
fun versaoDoProgramaEhAMaisRecente(): Boolean = (VERSAO_DO_PROGRAMA == tagUltimoRelease())

val ICONE_DO_PROGRAMA = Image("/vitorscoelho/ecfx/gui/icones/icone.png")
