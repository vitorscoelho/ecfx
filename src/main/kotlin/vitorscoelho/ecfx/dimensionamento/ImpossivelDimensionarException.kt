package vitorscoelho.ecfx.dimensionamento

import java.lang.Exception

class ImpossivelDimensionarException(message: String) : Exception(message)

/*
/**
 * Representa um concreto.
 */
interface Concreto{
    /**
     * Resistência característica à compressão, em kN/cm².
     */
    val fck:Double

    /**
     * Resistência média à tração, em kN/cm².
     */
    val fctm:Double

    /**
     * Resistência característica inferior à tração, em kN/cm².
     */
    val fctkInf:Double

    /**
     * Resistência característica superior à tração, em kN/cm².
     */
    val fctkSup:Double

    /**
     * Peso específico para concreto simples (sem armaduras), em kN/cm³.
     */
    val pesoEspecificoSimples:Double

    /**
     * Peso específico para concreto armado, em kN/cm³.
     */
    val pesoEspecificoArmado:Double

    /**
     * Módulo de elasticidade inicial (ou tangente), em kN/cm².
     */
    val moduloDeElasticidadeInicial:Double

    /**
     * Módulo de elasticidade secante, em kN/cm².
     */
    val moduloDeElasticidadeSecante:Double

    /**
     * Resistência característica à compressão, em kN/cm², na idade de [t] dias
     */
    fun fck(t:Double):Double

    /**
     * Resistência média à tração, em kN/cm², na idade de [t] dias
     */
    fun fctm(t:Double):Double

    /**
     * Resistência característica inferior à tração, em kN/cm², na idade de [t] dias
     */
    fun fctkInf(t:Double):Double

    /**
     * Resistência característica superior à tração, em kN/cm², na idade de [t] dias
     */
    fun fctkSup(t:Double):Double

    /**
     * Módulo de elasticidade inicial (ou tangente), em kN/cm², na idade de [t] dias
     */
    fun moduloDeElasticidadeInicial(t:Double):Double

    /**
     * Módulo de elasticidade secante, em kN/cm², na idade de [t] dias
     */
    fun moduloDeElasticidadeSecante(t:Double):Double
}
 */