package vitorscoelho.ecfx.gui.model

import vitorscoelho.utils.measure.*
import vitorscoelho.utils.tfx.doubleProp
import vitorscoelho.utils.tfx.objectProp

//class BeanViewInicial {
//    //Concreto
//    val fckProperty = pressureProp(name = "fck", value = 0.0)
//    val gamaCProperty = doubleProp(name = "gamaC", value = 0.0)
//    val ecsProperty = pressureProp(name = "ecs", value = 0.0)
//    //Estribos
//    val estriboFykProperty = pressureProp(name = "estribo.fyk", value = 0.0)
//    val estriboGamaSProperty = doubleProp(name = "estribo.gamaS", value = 0.0)
//    val estriboBitolaProperty = lengthProp(name = "estribo.bitola", value = 0.0)
//    //Longitudinal
//    val longitudinalFykProperty = pressureProp(name = "longitudinal.fyk", value = 0.0)
//    val longitudinalGamaSProperty = doubleProp(name = "longitudinal.gamaS", value = 0.0)
//    val longitudinalModuloElasticidadeProperty = pressureProp(name = "longitudinal.moduloElasticidade", value = 0.0)
//    val longitudinalBitolaProperty = lengthProp(name = "longitudinal.bitola", value = 0.0)
//    //Tipo de fundação
//    val tipoFundacaoProperty = objectProp(name = "tipoestaca.tipo", value = ESTACA_PERSONALIZADA)
//    val comprimentoMinimoArmaduraProperty = lengthProp(name = "tipoestaca.comprimentoMinimoArmadura", value = 0.0)
//    val tensaoMediaMaximaProperty = pressureProp(name = "tipoestaca.tensaoMediaMaxima", value = 0.0)
//    //Dados do solo
//    val tipoSoloProperty = objectProp(name = "solo.tipo", value = TipoSolo.AREIA_OU_ARGILA_MOLE)
//    //    val kh = springStiffnessPerUnitAreaProp(name = "solo.${tipo.value.idCoeficienteReacaoHorizontal}", value = 0.0)
//    val kvProperty = springStiffnessPerUnitAreaProp(name = "solo.kv", value = 0.0)
//    val coesaoProperty = pressureProp(name = "solo.coesao", value = 0.0)
//    val anguloDeAtritoProperty = angleProp(name = "solo.anguloDeAtrito", value = 0.0)
//    val pesoEspecificoProperty = specificWeightProp(name = "solo.pesoEspecifico", value = 0.0)
//    val tensaoAdmissivelProperty = pressureProp(name = "solo.tensaoAdmissivel", value = 0.0)
//    //Dados da fundação
//    val cobrimentoProperty = lengthProp(name = "estaca.cobrimento", value = 0.0)
//    val diametroFusteProperty = lengthProp(name = "estaca.diametroFuste", value = 0.0)
//    val diametroBaseProperty = lengthProp(name = "estaca.diametroBase", value = 0.0)
//    val alturaBaseProperty = lengthProp(name = "estaca.alturaBase", value = 0.0)
//    val rodapeProperty = lengthProp(name = "estaca.rodape", value = 0.0)
//    val profundidadeProperty = lengthProp(name = "estaca.profundidade", value = 0.0)
//    //Cargas no topo
//    val normalProperty = forceProp(name = "esforco.normal", value = 0.0)
//    val forcaHorizontalProperty = forceProp(name = "esforco.horizontal", value = 0.0)
//    val momentoProperty = momentProp(name = "esforco.momento", value = 0.0)
//    val gamaNProperty = doubleProp(name = "esforco.gamaN", value = 0.0)
//}