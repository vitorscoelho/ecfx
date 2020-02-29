package vitorscoelho.ecfx.novagui.utils

val regexVazioPositivoOuNegativo = """[.\-+]""".toRegex()
val regexCientifico = """(.+)[eE](.*)""".toRegex()

fun String.inputIsInt(): Boolean = (this.inputToIntOrNull() != null)
fun String.inputIsDouble(): Boolean = (this.inputToDoubleOrNull() != null)
fun String.inputIsNumber(): Boolean = (this.inputToNumberOrNull() != null)
fun String.inputIsPositiveInt(): Boolean {
    val valor: Int = this.inputToIntOrNull() ?: return false
    return valor >= 0
}

fun String.inputIsPositiveDouble(): Boolean {
    val valor: Double = this.inputToDoubleOrNull() ?: return false
    return valor >= 0.0
}

fun String.inputIsPositiveNumber(): Boolean {
    return (this.inputIsPositiveInt() || this.inputIsPositiveDouble())
}

fun String.inputToIntOrNull(): Int? {
    val retorno: Int? = this.toIntOrNull()
    if (retorno == null) {
        if (this.isBlank() || regexVazioPositivoOuNegativo.matches(this)) return 0
    }
    return retorno
}

fun String.inputToDoubleOrNull(): Double? {
    val retorno: Double? = this.toDoubleOrNull()
    if (retorno == null) {
        if (this.isBlank() || regexVazioPositivoOuNegativo.matches(this)) return 0.0
        if (regexCientifico.matches(this)) {
            val (_, expoente) = regexCientifico.find(this)!!.destructured
            if (expoente.isBlank() || expoente == "+" || expoente == "-") return (this + "0").toDoubleOrNull()
        }
    }
    if (this.last() == 'd' || this.last() == 'D') return null
    return retorno
}

fun String.inputToNumberOrNull(): Number? = this.inputToIntOrNull() ?: this.inputToDoubleOrNull()

fun String.inputToInt(): Int = this.inputToIntOrNull() ?: throw NumberFormatException()
fun String.inputToDouble(): Double = this.inputToDoubleOrNull() ?: throw NumberFormatException()
fun String.inputToNumber(): Number = this.inputToNumberOrNull() ?: throw NumberFormatException()