package vitorscoelho.ecfx.dimensionamento

class Esforco(val normal: Double, val horizontal: Double, val momento: Double) {
    operator fun plus(outro: Esforco) = Esforco(
        normal = normal + outro.normal,
        horizontal = horizontal + outro.horizontal,
        momento = momento + outro.momento
    )

    operator fun minus(outro: Esforco) = Esforco(
        normal = normal - outro.normal,
        horizontal = horizontal - outro.horizontal,
        momento = momento - outro.momento
    )
}