package vitorscoelho.ecfx.utils

import org.kohsuke.github.GitHub

const val VERSAO_DO_PROGRAMA = "v0.3"

private val NOME_REPOSITORIO: String by lazy { "vitorscoelho/ecfx" }

/**@return o nome da tag do último release do repositório*/
fun tagUltimoRelease(): String {
    val github = GitHub.connectAnonymously()
    val repositorio = github.getRepository(NOME_REPOSITORIO)
    return repositorio.latestRelease.tagName
}

/**@return **true** se a versão do programa é a mais recente, **false** caso contrário*/
fun versaoDoProgramaEhAMaisRecente(): Boolean = (VERSAO_DO_PROGRAMA == tagUltimoRelease())

val GAMA_MINUSCULO: String by lazy { "\u03B3" }
val PHI_MAIUSCULO: String by lazy { "\u03D5" }
val ETA_MINUSCULO: String by lazy { "\u03B7" }
val SIGMA_MINUSCULO: String by lazy { "\u03C3" }