package vitorscoelho.ecfx.gui.model

import javafx.beans.property.SimpleObjectProperty
import tornadofx.ViewModel
import vitorscoelho.ecfx.dimensionamento.geotecnico.ResultadosAnaliseTubulao
import vitorscoelho.ecfx.gui.WithDescriptionsEcfx
import vitorscoelho.utils.measure.*
import vitorscoelho.utils.tfx.toProperty
import javax.measure.Quantity
import javax.measure.quantity.Pressure

internal class BeanResultados(resultados: ResultadosAnaliseTubulao, private val unidades: BeanUnidades) :
    ViewModel(), WithDescriptionsEcfx {
    val tensaoAtuanteMediaProp = resultados.tensaoMediaBase.propTensaoSolo("resultados.tensaoMediaBase")
    val tensaoAtuanteMaximaProp = resultados.tensaoMaximaBase.propTensaoSolo("resultados.tensaoMaximaBase")
    val tensaoAtuanteMinimaProp = resultados.tensaoMinimaBase.propTensaoSolo("resultados.tensaoMinimaBase")
    val normalProp = resultados.esforco.normal.propForca("esforco.normal")
    val forcaHorizontalProp = resultados.esforco.horizontal.propForca("esforco.horizontal")
    val momentoProp = resultados.esforco.momento.propMomento("esforco.momento")
    val rotacaoProp = resultados.rotacao.propRotacao("resultados.rotacao")
    val deltaHProp = resultados.deltaH.propDeslocamento("resultados.deltaH")
    val deltaVProp = resultados.deltaV.propDeslocamento("resultados.deltaV")

    private fun Number.propTensaoSolo(name: String?) = this.toPressureSU().to(unidades.unitTensaoSolo).toProperty(name)
    private fun Number.propForca(name: String?) = this.toForceSU().to(unidades.unitForca).toProperty(name)
    private fun Number.propMomento(name: String?) = this.toMomentSU().to(unidades.unitMomento).toProperty(name)
    private fun Number.propRotacao(name: String?) = this.toAngleSU().to(unidades.unitRotacao).toProperty(name)
    private fun Number.propDeslocamento(name: String?) =
        this.toLengthSU().to(unidades.unitDeslocamento).toProperty(name)
}