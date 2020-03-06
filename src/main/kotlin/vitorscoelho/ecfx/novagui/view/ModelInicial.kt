package vitorscoelho.ecfx.novagui.view

import tornadofx.get
import vitorscoelho.ecfx.novagui.utils.*
import vitorscoelho.utils.measure.*

class ModelInicial : CommittableModel() {
    private val unitInicialModuloReacao = KILONEWTON.divide(CUBIC_METRE).asSpringStiffnessPerUnitArea()
    private val unitInicialPesoEspecifico = KILONEWTON.divide(CUBIC_METRE).asSpecificWeight()

    //Concreto
    val fck = FckProp(pressureOf(20, MEGAPASCAL))
    val gamaC = GamaCProp(1.8)
    val ecs = EcsProp(pressureOf(25_000, MEGAPASCAL))

    //Armaduras
    val fywk = FywkProp(pressureOf(500, MEGAPASCAL))
    val bitolaEstribo = BitolaEstriboProp(lengthOf(8, MILLIMETRE))
    val fyk = FykProp(pressureOf(500, MEGAPASCAL))
    val bitolaLongitudinal = BitolaLongitudinalProp(lengthOf(10, MILLIMETRE))
    val gamaS = GamaSProp(1.15)
    val es = EsProp(pressureOf(21, GIGAPASCAL))

    //Dados do solo
    val tipoSolo = ObjectGuiProp(initValue = TipoSolo.AREIA_OU_ARGILA_MOLE, name = "solo.tipo", rb = rb)
    val khAreiaOuArgilaMole = KhAreiaOuArgilaMoleProp(springStiffnessPerUnitAreaOf(12_500, unitInicialModuloReacao))
    val khArgilaRijaADura = KhArgilaRijaADuraProp(springStiffnessPerUnitAreaOf(12_500, unitInicialModuloReacao))
    val kv = KvProp(springStiffnessPerUnitAreaOf(120_000, unitInicialModuloReacao))
    val coesao = CoesaoProp(pressureOf(0, MEGAPASCAL))
    val anguloDeAtrito = AnguloDeAtritoProp(angleOf(30, DEGREE))
    val pesoEspecifico = PesoEspecificoProp(specificWeightOf(18, unitInicialPesoEspecifico))
    val tensaoAdmissivel = TensaoAdmissivelProp(pressureOf(200, KILOPASCAL))

    //Dados da fundação
    val armaduraIntegral = BooleanGuiProp(initValue = false, name = "estaca.armaduraIntegral", rb = rb)
    val compMinimoArmadura = ComprimentoMinimoArmaduraProp(lengthOf(3, METRE))
    val tensaoMediaMaximaConcreto = TensaoMediaLimiteConcretoProp(pressureOf(5, MEGAPASCAL))
    val cobrimento = CobrimentoProp(lengthOf(3, CENTIMETRE))
    val diametroFuste = DiametroFusteProp(lengthOf(120, CENTIMETRE))
    val diametroBase = DiametroBaseProp(lengthOf(220, CENTIMETRE))
    val profundidadeEstaca = ProfundidadeEstacaProp(lengthOf(8, METRE))

    //Cargas no topo
    val forcaNormal = ForcaNormalProp(forceOf(200, KILONEWTON))
    val forcaHorizontal = ForcaHorizontalProp(forceOf(30, KILONEWTON))
    val momentoFletor = MomentoFletorProp(momentOf(3, KILONEWTON_METRE))
    val gamaN = GamaNProp(1.4)

    init {
        armaduraIntegral.transitional.doNowAndOnChange { integral ->
            if (integral!!) {
                compMinimoArmadura.transitional.bind(profundidadeEstaca.transitional)
                tensaoMediaMaximaConcreto.transitional.value = "0"
            } else {
                compMinimoArmadura.transitional.unbind()
            }
        }
        khAreiaOuArgilaMole.transitional.bindBidirectional(khArgilaRijaADura.transitional)
    }
}

enum class TipoSolo {
    AREIA_OU_ARGILA_MOLE {
        override val descricao = rb["descricao.areiaOuArgilaMole"]
        override val idCoeficienteReacaoHorizontal = "khAreiaOuArgilaMole"
    },
    ARGILA_RIJA_A_DURA {
        override val descricao = rb["descricao.argilaRijaADura"]
        override val idCoeficienteReacaoHorizontal = "khArgilaRijaADura"
    };

    abstract val descricao: String
    abstract val idCoeficienteReacaoHorizontal: String

    override fun toString() = descricao
}