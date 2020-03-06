package vitorscoelho.ecfx.dimensionamento.geotecnico

import org.junit.Test
import org.junit.Assert.*
import vitorscoelho.ecfx.dimensionamento.Esforco
import kotlin.math.pow

class AnaliseKhLinearmenteVariavelKtTest {
    private val DELTA = 0.005

    @Test
    fun teste1() {
        val esforcoTopo = Esforco(normal = 200.0, horizontal = 30.0, momento = 3.0 * 100.0)
        val fusteTubulao = FusteCircular(diametro = 120.0)
        val baseTubulao = BaseCircular(
            diametroSuperior = 120.0, diametroInferior = 220.0,
            altura = 110.0, rodape = 20.0
        )
        val tubulao = Tubulao(
            fuste = fusteTubulao, base = baseTubulao, comprimento = 800.0
        )
        val analiseTubulao = AnaliseKhLinearmenteVariavel(
            tubulao = tubulao, nh = 12_500.0 / 100.0.pow(3), kv = 120_000.0 / 100.0.pow(3)
        )
        val resultados = analiseTubulao.dimensionar(esforco = esforcoTopo)
        //Tensões na base
        assertEquals(52.61, resultados.tensaoMediaBase * 100.0.pow(2), DELTA)
        assertEquals(66.4, resultados.tensaoMaximaBase * 100.0.pow(2), DELTA)
        assertEquals(38.82, resultados.tensaoMinimaBase * 100.0.pow(2), DELTA)
        //Deslocamentos no topo
//        assertEquals(0.63, resultados.deltaH * 10.0, DELTA)
//        assertEquals(0.43, resultados.deltaV * 10.0, DELTA)
    }
}