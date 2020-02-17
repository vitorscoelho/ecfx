package vitorscoelho.ecfx.gui.model

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import tech.units.indriya.AbstractUnit.ONE
import tech.units.indriya.unit.Units.*
import tornadofx.onChange
import vitorscoelho.ecfx.dimensionamento.estrutural.AgregadoGraudo
import vitorscoelho.ecfx.dimensionamento.geotecnico.AnaliseKhDegrau
import vitorscoelho.ecfx.dimensionamento.geotecnico.AnaliseKhLinearmenteVariavel
import vitorscoelho.ecfx.dimensionamento.geotecnico.AnaliseTubulao
import vitorscoelho.ecfx.dimensionamento.geotecnico.TipoEstaca
import vitorscoelho.ecfx.utils.*
import javax.measure.MetricPrefix.*
import javax.measure.Unit
import javax.measure.quantity.*

//val AGREGADO_QUALQUER = AgregadoGraudo(nome = "Outro") {
//    throw IllegalAccessException("Impossível calcular o módulo de deformação automaticamente com o agregado selecionado")
//}
//val AGREGADOS_DISPONIVEIS = listOf(
//    AgregadoGraudo.ARENITO, AgregadoGraudo.BASALTO, AgregadoGraudo.CALCARIO,
//    AgregadoGraudo.DIABASIO, AgregadoGraudo.GNAISSE, AgregadoGraudo.GRANITO,
//    AGREGADO_QUALQUER
//)
//val TIPOS_ESTACAS_DISPONIVEIS = emptyList<TipoEstaca>()
//val TIPOS_SOLO_DISPONIVEIS: List<Class<out AnaliseTubulao>> =
//    listOf(AnaliseKhDegrau::class.java, AnaliseKhLinearmenteVariavel::class.java)
//
//enum class TipoSoloModel(val descricao: String, val classe: Class<out AnaliseTubulao>) {
//    KH_DEGRAU(descricao = "Argila rija a dura", classe = AnaliseKhDegrau::class.java),
//    KH_LINEARMENTE_VARIAVEL(descricao = "Areia ou argila mole", classe = AnaliseKhLinearmenteVariavel::class.java);
//
//    override fun toString(): String = descricao
//}
//
//class Dados {
//    //Unidades
//    val unitTensaoMaterialProperty: ObjectProperty<Unit<Pressure>> = SimpleObjectProperty(MEGA(PASCAL))
//    val unitModuloElasticidadeProperty: ObjectProperty<Unit<Pressure>> = SimpleObjectProperty(MEGA(PASCAL))
//    val unitAdimensionalProperty: ObjectProperty<Unit<Dimensionless>> = SimpleObjectProperty(ONE)
//    val unitBitolaProperty: ObjectProperty<Unit<Length>> = SimpleObjectProperty(MILLI(METRE))
//    val unitCobrimentoProperty: ObjectProperty<Unit<Length>> = SimpleObjectProperty(CENTI(METRE))
//    val unitDimensoesEstacaProperty: ObjectProperty<Unit<Length>> = SimpleObjectProperty(CENTI(METRE))
//    val unitProfundidadeEstacaProperty: ObjectProperty<Unit<Length>> = SimpleObjectProperty(METRE)
//    val unitForcaProperty: ObjectProperty<Unit<Force>> = SimpleObjectProperty(KILO(NEWTON))
//    val unitMomentoProperty: ObjectProperty<Unit<Moment>> =
//        SimpleObjectProperty((KILO(NEWTON)).multiply(METRE).asType(Moment::class.java))
//
//    //Concreto
//    val fckProperty = QuantityDoubleProperty(
//        nome = "fck",
//        descricao = "Resistência característica à compressão do concreto",
//        unitProperty = unitTensaoMaterialProperty
//    )
//    val gamaCProperty = QuantityDoubleProperty(
//        nome = "${GAMA_MINUSCULO}c",
//        descricao = "Coeficiente de ponderação da resistência do concreto",
//        unitProperty = unitAdimensionalProperty
//    )
//    val agregadoProperty: ObjectProperty<AgregadoGraudo> = SimpleObjectProperty(AGREGADO_QUALQUER)
//    val moduloDeformacaoConcretoProperty = QuantityDoubleProperty(
//        nome = "Ecs",
//        descricao = "Módulo de deformação secante do concreto",
//        unitProperty = unitModuloElasticidadeProperty
//    ).apply {
//        agregadoProperty.onChange {
//            try {
//                magnitude = moduloDeformacaoSecante(agregado = it, fckProperty = fckProperty)
//            } catch (e: Exception) {
//            }
//        }
//        fckProperty.magnitudeProperty.onChange {
//            try {
//                magnitude = moduloDeformacaoSecante(agregado = agregadoProperty.value, fckProperty = fckProperty)
//            } catch (e: Exception) {
//            }
//        }
//    }
//
//    //Armadura transversal (Estribos)
//    val estriboFywkProperty = QuantityDoubleProperty(
//        nome = "fywk",
//        descricao = "Resistência característica ao escoamento do aço da armadura transversal",
//        unitProperty = unitTensaoMaterialProperty
//    )
//    val estriboGamaSProperty = QuantityDoubleProperty(
//        nome = "${GAMA_MINUSCULO}s",
//        descricao = "Coeficiente de ponderação da resistência do aço da armadura transversal",
//        unitProperty = unitAdimensionalProperty
//    )
//    val estriboBitolaProperty = QuantityDoubleProperty(
//        nome = "${PHI_MAIUSCULO}t",
//        descricao = "Bitola da barra utilizada na armadura transversal",
//        unitProperty = unitBitolaProperty
//    )
//
//    //Armadura longitudinal
//    val longitudinalFykProperty = QuantityDoubleProperty(
//        nome = "fyk",
//        descricao = "Resistência característica ao escoamento do aço da armadura longitudinal",
//        unitProperty = unitTensaoMaterialProperty
//    )
//    val longitudinalGamaSProperty = QuantityDoubleProperty(
//        nome = "${GAMA_MINUSCULO}s",
//        descricao = "Coeficiente de ponderação da resistência do aço da armadura longitudinal",
//        unitProperty = unitAdimensionalProperty
//    )
//    val longitudinalModuloDeformacaoProperty = QuantityDoubleProperty(
//        nome = "Es",
//        descricao = "Módulo de deformação do aço da armadura longitudinal",
//        unitProperty = unitModuloElasticidadeProperty
//    )
//    val longitudinalBitolaProperty = QuantityDoubleProperty(
//        nome = "${PHI_MAIUSCULO}l",
//        descricao = "Bitola da barra utilizada na armadura longitudinal",
//        unitProperty = unitBitolaProperty
//    )
//
//    //Características geométricas
//    val cobrimentoProperty = QuantityDoubleProperty(
//        nome = "Cobrimento",
//        descricao = "Cobrimento do estribo do fuste",
//        unitProperty = unitCobrimentoProperty
//    )
//    val diametroFusteProperty = QuantityDoubleProperty(
//        nome = "${PHI_MAIUSCULO}fuste",
//        descricao = "Diâmetro do fuste",
//        unitProperty = unitDimensoesEstacaProperty
//    )
//    val diametroBaseProperty = QuantityDoubleProperty(
//        nome = "${PHI_MAIUSCULO}base",
//        descricao = "Diâmetro da base",
//        unitProperty = unitDimensoesEstacaProperty
//    )
//    val alturaBaseProperty = QuantityDoubleProperty(
//        nome = "Hbase",
//        descricao = "Altura da base",
//        unitProperty = unitDimensoesEstacaProperty
//    )
//    val rodapeBaseProperty = QuantityIntProperty(
//        nome = "Rodapé",
//        descricao = "Altura do rodapé da base",
//        unitProperty = unitDimensoesEstacaProperty
//    )
//    val profundidadeProperty = QuantityDoubleProperty(
//        nome = "Profundidade",
//        descricao = "Profundidade total da estaca",
//        unitProperty = unitProfundidadeEstacaProperty
//    )
//
//    //Cargas no topo
//    val normalProperty = QuantityDoubleProperty(
//        nome = "Nk",
//        descricao = "Esforço normal característico atuante no topo da estaca",
//        unitProperty = unitForcaProperty
//    )
//    val forcaHorizontalProperty = QuantityDoubleProperty(
//        nome = "Hk",
//        descricao = "Esforço horizontal característico atuante no topo da estaca",
//        unitProperty = unitForcaProperty
//    )
//    val momentoProperty = QuantityDoubleProperty(
//        nome = "Mk",
//        descricao = "Momento fletor característico atuante no topo da estaca",
//        unitProperty = unitMomentoProperty
//    )
//    val gamaNProperty = QuantityDoubleProperty(
//        nome = "${GAMA_MINUSCULO}n",
//        descricao = "Majorador de esforços para ELU.\r\nUtilizado para o dimensionamento das armaduras.",
//        unitProperty = unitAdimensionalProperty
//    )
//
//    //Características do elemento de fundação
//    val tipoEstaca: ObjectProperty<TipoEstaca> = SimpleObjectProperty()
//
//    //Tipo do solo
//    val tipoSolo: ObjectProperty<TipoSoloModel> = SimpleObjectProperty()
//
//    //Características do solo
//    val etaHProperty = QuantityDoubleProperty(
//        nome = "${ETA_MINUSCULO}h",
//        descricao = "Taxa de crescimento do coeficiente de reação horizontal",
//        unitProperty = unitAdimensionalProperty //TODO colocar a unidade correspondente correta
//    )
//    val khMaximoProperty = QuantityDoubleProperty(
//        nome = "Kh,máx",
//        descricao = "Coeficiente de reação horizontal máximo",
//        unitProperty = unitAdimensionalProperty //TODO colocar a unidade correspondente correta
//    )
//    val kvProperty = QuantityDoubleProperty(
//        nome = "Kv",
//        descricao = "Coeficiente de reação vertical na base do elemento de fundação",
//        unitProperty = unitAdimensionalProperty //TODO colocar a unidade correspondente correta
//    )
//    val coesaoProperty = QuantityDoubleProperty(
//        nome = "c",
//        descricao = "Coesão do solo",
//        unitProperty = unitAdimensionalProperty //TODO colocar a unidade correspondente correta
//    )
//    val anguloDeAtritoProperty = QuantityDoubleProperty(
//        nome = PHI_MAIUSCULO,
//        descricao = "Ângulo de atrito do solo",
//        unitProperty = unitAdimensionalProperty //TODO colocar a unidade correspondente correta
//    )
//    val pesoEspecificoProperty = QuantityDoubleProperty(
//        nome = GAMA_MINUSCULO,
//        descricao = "Peso específico do solo",
//        unitProperty = unitAdimensionalProperty //TODO colocar a unidade correspondente correta
//    )
//    val tensaoAdmissivelProperty = QuantityDoubleProperty(
//        nome = "${SIGMA_MINUSCULO}adm",
//        descricao = "Tensão vertical admissível do solo na base do elemento de fundação",
//        unitProperty = unitAdimensionalProperty //TODO colocar a unidade correspondente correta
//    )
//}
//
//private fun moduloDeformacaoSecante(agregado: AgregadoGraudo?, fckProperty: QuantityProperty<Pressure>): Double {
//    val QUILONEWTON_POR_CENTIMETRO_QUADRADO: Unit<Pressure> = (KILO(NEWTON).divide(CENTI(METRE).pow(2))).asType(Pressure::class.java)
//    val fckConvertido = fckProperty.toQuantity().to(QUILONEWTON_POR_CENTIMETRO_QUADRADO).value.toDouble()
//    return agregado!!.moduloDeformacaoSecante(fck = fckConvertido)
//}