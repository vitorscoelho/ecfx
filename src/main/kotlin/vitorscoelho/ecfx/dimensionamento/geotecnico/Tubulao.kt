package vitorscoelho.ecfx.dimensionamento.geotecnico

import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sqrt


interface FusteTubulao {
    val dimensao: Double
    val area: Double
    val momentoDeInercia: Double
}

interface BaseTubulao {
    val dimensao: Double
    val area: Double
    val moduloFlexao: Double
    val volume: Double
}

class Tubulao(val fuste: FusteTubulao, val base: BaseTubulao, val comprimento: Double)

class FusteCircular(val diametro: Double) : FusteTubulao {
    override val dimensao: Double
        get() = diametro
    override val area = PI * diametro * diametro / 4.0
    override val momentoDeInercia = PI * diametro.pow(4) / 64.0
}

class BaseCircular(val diametroInferior: Double, val diametroSuperior: Double, val altura: Double, val rodape: Double) :
    BaseTubulao {
    override val dimensao: Double
        get() = diametroInferior
    override val area = PI * diametroInferior * diametroInferior / 4.0
    override val moduloFlexao = PI * diametroInferior.pow(3) / 32.0
    override val volume = run {
        val areaFuste = PI * diametroSuperior * diametroSuperior / 4.0
        area * rodape + (altura - rodape) * ((area + areaFuste + sqrt(area * areaFuste)) / 3)
    }
}