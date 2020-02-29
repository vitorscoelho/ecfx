package vitorscoelho.ecfx.novagui.utils

private val regexVazioPositivoOuNegativo = """[.\-+]""".toRegex()
private val regexCientifico = """(.+)[eE](.*)""".toRegex()

fun inputIsInt(string: String): Boolean = (inputToIntOrNull(string) != null)
fun inputIsDouble(string: String): Boolean = (inputToDoubleOrNull(string) != null)
fun inputIsNumber(string: String): Boolean = (inputToNumberOrNull(string) != null)
fun inputIsPositiveInt(string: String): Boolean {
    val valor: Int = inputToIntOrNull(string) ?: return false
    return valor >= 0
}

fun inputIsPositiveDouble(string: String): Boolean {
    val valor: Double = inputToDoubleOrNull(string) ?: return false
    return valor >= 0.0
}

fun inputIsPositiveNumber(string: String): Boolean {
    return (inputIsPositiveInt(string) || inputIsPositiveDouble(string))
}

fun inputToIntOrNull(string: String): Int? {
    val retorno: Int? = string.toIntOrNull()
    if (retorno == null) {
        if (string.isBlank() || regexVazioPositivoOuNegativo.matches(string)) return 0
    }
    return retorno
}

fun inputToDoubleOrNull(string: String): Double? {
    val retorno: Double? = string.toDoubleOrNull()
    if (retorno == null) {
        if (string.isBlank() || regexVazioPositivoOuNegativo.matches(string)) return 0.0
        if (regexCientifico.matches(string)) {
            val (_, expoente) = regexCientifico.find(string)!!.destructured
            if (expoente.isBlank() || expoente == "+" || expoente == "-") return (string + "0").toDoubleOrNull()
        }
    }
    if (string.last() == 'd' || string.last() == 'D') return null
    return retorno
}

fun inputToNumberOrNull(string: String): Number? = inputToIntOrNull(string) ?: inputToDoubleOrNull(string)

fun inputToInt(string: String): Int = inputToIntOrNull(string) ?: throw NumberFormatException()
fun inputToDouble(string: String): Double = inputToDoubleOrNull(string) ?: throw NumberFormatException()
fun inputToNumber(string: String): Number = inputToNumberOrNull(string) ?: throw NumberFormatException()