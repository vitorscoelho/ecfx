package vitorscoelho.ecfx.novagui.utils

import javafx.beans.InvalidationListener
import javafx.beans.binding.Bindings
import javafx.beans.binding.BooleanBinding
import javafx.beans.property.*
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.util.StringConverter
import tornadofx.*
import vitorscoelho.ecfx.novagui.configuracoes.Formato
import vitorscoelho.ecfx.novagui.configuracoes.Formatos
import vitorscoelho.utils.measure.MEGAPASCAL
import vitorscoelho.utils.measure.createQuantity
import vitorscoelho.utils.measure.toDoubleSU
import vitorscoelho.utils.tfx.ERROR_IF_NEGATIVE_DOUBLE
import vitorscoelho.utils.tfx.ERROR_IF_NOT_DOUBLE
import vitorscoelho.utils.tfx.ERROR_IF_NOT_POSITIVE_DOUBLE
import java.text.DecimalFormat
import java.util.*
import javax.measure.Quantity
import javax.measure.Unit
import javax.measure.quantity.*

val SEM_FILTER_INPUT: (input: String) -> Boolean = { _ -> true }
val SEMPRE_VALIDO: (input: String) -> Boolean = { _ -> true }

/*
val FILTER_INPUT_INT: (formatter: TextFormatter.Change) -> Boolean by lazy {
    { formatter: TextFormatter.Change -> formatter.controlNewText.isInt() || formatter.controlNewText == "-" }
}
 */
val FILTER_INPUT_NOT_NEGATIVE_DOUBLE: (texto: String) -> Boolean by lazy {
    { texto: String ->
        if (texto.contains(" ") || texto.contains("-")) {
            false
        } else {
            inputIsPositiveDouble(texto)
        }
    }
}

val FILTER_INPUT_DOUBLE: (texto: String) -> Boolean by lazy {
    { texto: String ->
        if (texto.contains(" ")) {
            false
        } else {
            inputIsDouble(texto)
        }
    }
}

val rb: ResourceBundle = ResourceBundle.getBundle("vitorscoelho.ecfx.gui.Textos")
fun ResourceBundle.label(key: String): String = this["$key.label"]
fun ResourceBundle.description(key: String): String = this["$key.description"]

private val fckMinimo = createQuantity(value = 20, unit = MEGAPASCAL)
private val fckMaximo = createQuantity(value = 90, unit = MEGAPASCAL)

class FckProp(initialValue: Quantity<Pressure>) :
    GuiTextQuantityPropECFX<Pressure>(
        initialValue = initialValue, name = "fck", formato = Formatos.resistenciaMaterial
    ), WithTextInputFilter, WithTextInputValidator {
    override val filterInput = FILTER_INPUT_NOT_NEGATIVE_DOUBLE
    override val validator: ValidationContext.(String?) -> ValidationMessage? = { texto: String? ->
        if ((texto == null) || valorValido(texto = texto)) {
            null
        } else {
            val fckMinimoString: String = toString(fckMinimo)
            val fckMaximoString: String = toString(fckMaximo)
            val unidadeString: String = unit.value.toString()
            ValidationMessage(
                message = "Deve estar entre $fckMinimoString$unidadeString e $fckMaximoString$unidadeString.",
                severity = ValidationSeverity.Error
            )
        }
    }

    private fun valorValido(texto: String): Boolean {
        val magnitude: Number = inputToNumberOrNull(texto) ?: return false
        val valorAtual = createQuantity(value = magnitude, unit = unit.value)
        return (valorAtual in fckMinimo..fckMaximo)
    }
}

class GamaCProp(initialValue: Double) : DoubleTextGuiProp(
    initialValue = initialValue, name = "gamaC", rb = rb, format = Formatos.coeficienteDeSeguranca.format
), WithTextInputFilterAndValidator by WithTextInputPositiveDouble()

class EcsProp(initialValue: Quantity<Pressure>) : GuiTextQuantityPropECFX<Pressure>(
    initialValue = initialValue, name = "ecs", formato = Formatos.moduloElasticidadeConcreto
), WithTextInputFilterAndValidator by WithTextInputPositiveDouble()

class FywkProp(initialValue: Quantity<Pressure>) : GuiTextQuantityPropECFX<Pressure>(
    initialValue = initialValue, name = "estribo.fyk", formato = Formatos.resistenciaMaterial
), WithTextInputFilterAndValidator by WithTextInputPositiveDouble()

class BitolaEstriboProp(initialValue: Quantity<Length>) : GuiTextQuantityPropECFX<Length>(
    initialValue = initialValue, name = "estribo.bitola", formato = Formatos.bitola
), WithTextInputFilterAndValidator by WithTextInputPositiveDouble()

class FykProp(initialValue: Quantity<Pressure>) : GuiTextQuantityPropECFX<Pressure>(
    initialValue = initialValue, name = "longitudinal.fyk", formato = Formatos.resistenciaMaterial
), WithTextInputFilterAndValidator by WithTextInputPositiveDouble()

class BitolaLongitudinalProp(initialValue: Quantity<Length>) : GuiTextQuantityPropECFX<Length>(
    initialValue = initialValue, name = "longitudinal.bitola", formato = Formatos.bitola
), WithTextInputFilterAndValidator by WithTextInputPositiveDouble()

class GamaSProp(initialValue: Double) : DoubleTextGuiProp(
    initialValue = initialValue, name = "aco.gamaS", rb = rb, format = Formatos.coeficienteDeSeguranca.format
), WithTextInputFilterAndValidator by WithTextInputPositiveDouble()

class EsProp(initialValue: Quantity<Pressure>) : GuiTextQuantityPropECFX<Pressure>(
    initialValue = initialValue, name = "aco.moduloElasticidade", formato = Formatos.moduloElasticidadeAco
), WithTextInputFilterAndValidator by WithTextInputPositiveDouble()

class KhAreiaOuArgilaMoleProp(initialValue: Quantity<SpringStiffnessPerUnitArea>) :
    GuiTextQuantityPropECFX<SpringStiffnessPerUnitArea>(
        initialValue = initialValue, name = "solo.khAreiaOuArgilaMole", formato = Formatos.coeficienteReacao
    ), WithTextInputFilterAndValidator by WithTextInputPositiveDouble()

class KhArgilaRijaADuraProp(initialValue: Quantity<SpringStiffnessPerUnitArea>) :
    GuiTextQuantityPropECFX<SpringStiffnessPerUnitArea>(
        initialValue = initialValue, name = "solo.khArgilaRijaADura", formato = Formatos.coeficienteReacao
    ), WithTextInputFilterAndValidator by WithTextInputPositiveDouble()

class KvProp(initialValue: Quantity<SpringStiffnessPerUnitArea>) : GuiTextQuantityPropECFX<SpringStiffnessPerUnitArea>(
    initialValue = initialValue, name = "solo.kv", formato = Formatos.coeficienteReacao
), WithTextInputFilterAndValidator by WithTextInputPositiveDouble()

class CoesaoProp(initialValue: Quantity<Pressure>) : GuiTextQuantityPropECFX<Pressure>(
    initialValue = initialValue, name = "solo.coesao", formato = Formatos.coesao
), WithTextInputFilterAndValidator by WithTextInputNotNegativeDouble()

class AnguloDeAtritoProp(initialValue: Quantity<Angle>) : GuiTextQuantityPropECFX<Angle>(
    initialValue = initialValue, name = "solo.anguloDeAtrito", formato = Formatos.anguloDeAtrito
), WithTextInputFilterAndValidator by WithTextInputNotNegativeDouble()

class PesoEspecificoProp(initialValue: Quantity<SpecificWeight>) : GuiTextQuantityPropECFX<SpecificWeight>(
    initialValue = initialValue, name = "solo.pesoEspecifico", formato = Formatos.pesoEspecifico
), WithTextInputFilterAndValidator by WithTextInputPositiveDouble()

class TensaoAdmissivelProp(initialValue: Quantity<Pressure>) : GuiTextQuantityPropECFX<Pressure>(
    initialValue = initialValue, name = "solo.tensaoAdmissivel", formato = Formatos.tensaoSolo
), WithTextInputFilterAndValidator by WithTextInputPositiveDouble()

class ComprimentoMinimoArmaduraProp(initialValue: Quantity<Length>) : GuiTextQuantityPropECFX<Length>(
    initialValue = initialValue, name = "estaca.comprimentoMinimoArmadura", formato = Formatos.profundidade
), WithTextInputFilterAndValidator by WithTextInputPositiveDouble()

class TensaoMediaLimiteConcretoProp(initialValue: Quantity<Pressure>) : GuiTextQuantityPropECFX<Pressure>(
    initialValue = initialValue, name = "estaca.tensaoMediaLimite", formato = Formatos.resistenciaMaterial
), WithTextInputFilterAndValidator by WithTextInputNotNegativeDouble()

class CobrimentoProp(initialValue: Quantity<Length>) : GuiTextQuantityPropECFX<Length>(
    initialValue = initialValue, name = "estaca.cobrimento", formato = Formatos.cobrimento
), WithTextInputFilterAndValidator by WithTextInputPositiveDouble()

class DiametroFusteProp(initialValue: Quantity<Length>) : GuiTextQuantityPropECFX<Length>(
    initialValue = initialValue, name = "estaca.diametroFuste", formato = Formatos.dimensoesEstaca
), WithTextInputFilterAndValidator by WithTextInputPositiveDouble()

class DiametroBaseProp(initialValue: Quantity<Length>) : GuiTextQuantityPropECFX<Length>(
    initialValue = initialValue, name = "estaca.diametroBase", formato = Formatos.dimensoesEstaca
), WithTextInputFilterAndValidator by WithTextInputPositiveDouble()

class ProfundidadeEstacaProp(initialValue: Quantity<Length>) : GuiTextQuantityPropECFX<Length>(
    initialValue = initialValue, name = "estaca.profundidade", formato = Formatos.profundidade
), WithTextInputFilterAndValidator by WithTextInputPositiveDouble()

class ForcaNormalProp(initialValue: Quantity<Force>) : GuiTextQuantityPropECFX<Force>(
    initialValue = initialValue, name = "esforco.normal", formato = Formatos.forca
), WithTextInputFilterAndValidator by WithTextInputPositiveDouble()

class ForcaHorizontalProp(initialValue: Quantity<Force>) : GuiTextQuantityPropECFX<Force>(
    initialValue = initialValue, name = "esforco.horizontal", formato = Formatos.forca
), WithTextInputFilterAndValidator by WithTextInputDouble()

class MomentoFletorProp(initialValue: Quantity<Moment>) : GuiTextQuantityPropECFX<Moment>(
    initialValue = initialValue, name = "esforco.momento", formato = Formatos.momento
), WithTextInputFilterAndValidator by WithTextInputDouble()

class GamaNProp(initialValue: Double) : DoubleTextGuiProp(
    initialValue = initialValue, name = "esforco.gamaN", rb = rb, format = Formatos.coeficienteDeSeguranca.format
), WithTextInputFilterAndValidator by WithTextInputPositiveDouble()

class TensaoMediaAtuanteBaseProp(initialValue: Quantity<Pressure>) : GuiTextQuantityPropECFX<Pressure>(
    initialValue = initialValue, name = "tensaoMediaAtuanteBase", formato = Formatos.tensaoSolo
)

class TensaoMaxAtuanteBaseProp(initialValue: Quantity<Pressure>) : GuiTextQuantityPropECFX<Pressure>(
    initialValue = initialValue, name = "tensaoMaximaAtuanteBase", formato = Formatos.tensaoSolo
)

class TensaoMinAtuanteBaseProp(initialValue: Quantity<Pressure>) : GuiTextQuantityPropECFX<Pressure>(
    initialValue = initialValue, name = "tensaoMinimaAtuanteBase", formato = Formatos.tensaoSolo
)

class TensaoMaximaAtuanteNoConcretoFusteProp(initialValue: Quantity<Pressure>) : GuiTextQuantityPropECFX<Pressure>(
    initialValue = initialValue, name = "tensaoMaximaAtuanteConcretoFuste", formato = Formatos.resistenciaMaterial
)

class CortanteProp(initialValue: Quantity<Force>) : GuiTextQuantityPropECFX<Force>(
    initialValue = initialValue, name = "esforco.cortante", formato = Formatos.forca
)

class TensaoHorizontalSoloFusteProp(initialValue: Quantity<Pressure>) : GuiTextQuantityPropECFX<Pressure>(
    initialValue = initialValue, name = "solo.tensaoHorizontalFuste", formato = Formatos.tensaoSolo
)

class RotacaoTubulaoProp(initialValue: Quantity<Angle>) : GuiTextQuantityPropECFX<Angle>(
    initialValue = initialValue, name = "tubulao.rotacao", formato = Formatos.rotacao
)

class DeltaHTopoTubulaoProp(initialValue: Quantity<Length>) : GuiTextQuantityPropECFX<Length>(
    initialValue = initialValue, name = "tubulao.deltaH", formato = Formatos.deslocamento
)

class DeltaVTopoTubulaoProp(initialValue: Quantity<Length>) : GuiTextQuantityPropECFX<Length>(
    initialValue = initialValue, name = "tubulao.deltaV", formato = Formatos.deslocamento
)

class ProfundidadeProp(initialValue: Quantity<Length>) : GuiTextQuantityPropECFX<Length>(
    initialValue = initialValue, name = "resultados.profundidade", formato = Formatos.profundidade
), WithTextInputFilterAndValidator by WithTextInputPositiveDouble()

class WithTextInputPositiveDouble : WithTextInputFilterAndValidator {
    override val validator = ERROR_IF_NOT_POSITIVE_DOUBLE
    override val filterInput = FILTER_INPUT_NOT_NEGATIVE_DOUBLE
}

class WithTextInputNotNegativeDouble : WithTextInputFilterAndValidator {
    override val validator = ERROR_IF_NEGATIVE_DOUBLE
    override val filterInput = FILTER_INPUT_NOT_NEGATIVE_DOUBLE
}

class WithTextInputDouble : WithTextInputFilterAndValidator {
    override val validator = ERROR_IF_NOT_DOUBLE
    override val filterInput = FILTER_INPUT_DOUBLE
}

abstract class GuiTextQuantityPropECFX<Q : Quantity<Q>>(
    initialValue: Quantity<Q>, name: String, formato: Formato<Q>
) : QuantityTextGuiProp<Q>(
    initialValue = initialValue, name = name, rb = rb, format = formato.format, unit = formato.unit
)

interface WithTextInputFilterAndValidator : WithTextInputValidator, WithTextInputFilter

interface WithTextInputValidator {
    val validator: ValidationContext.(String?) -> ValidationMessage?
}

interface WithTextInputFilter {
    val filterInput: (input: String) -> Boolean
}

abstract class QuantityTextGuiProp<T : Quantity<T>>(
    initialValue: Quantity<T>,
    override val label: String? = null,
    override val description: String? = null,
    private val format: ObservableValue<DecimalFormat>,
    val unit: ObservableValue<Unit<T>>
) : TextGuiProp<Quantity<T>>(
    initValue = initialValue,
    toString = { qtd: Quantity<T> -> format.value.format(qtd.to(unit.value).value) },
    fromString = { string ->
        val magnitude = inputToNumber(string)
        createQuantity(value = magnitude, unit = unit.value)
    },
    label = label,
    description = description
) {
    constructor(
        initialValue: Quantity<T>,
        name: String,
        rb: ResourceBundle,
        format: ObservableValue<DecimalFormat>,
        unit: ObservableValue<Unit<T>>
    ) : this(
        initialValue = initialValue,
        label = rb.label(name), description = rb.description(name),
        format = format, unit = unit
    )

    init {
        format.onChange {
            val qtd = fromString(textProp.value)
            val string = toString(qtd)
            textProp.value = string
        }
        unit.addListener { _, oldValue, newValue ->
            val magnitude = inputToNumber(textProp.value)
            val qtdAnterior = createQuantity(value = magnitude, unit = oldValue)
            val qtdNova = qtdAnterior.to(newValue)
            textProp.value = toString(qtdNova)
        }
    }

    fun toDoubleSU(): Double = committed.value.toDoubleSU()
}

abstract class DoubleTextGuiProp(
    initialValue: Double,
    override val label: String? = null,
    override val description: String? = null,
    private val format: ObservableValue<DecimalFormat>
) : TextGuiProp<Double>(
    initValue = initialValue,
    toString = { valor -> format.value.format(valor) ?: "0" },
    fromString = { string ->
        if (string.isBlank() || string == "-") 0.0 else string.toDouble()
    },
    label = label,
    description = description
) {
    constructor(
        initialValue: Double,
        name: String,
        rb: ResourceBundle,
        format: ObservableValue<DecimalFormat>
    ) : this(
        initialValue = initialValue,
        label = rb.label(name), description = rb.description(name),
        format = format
    )

    init {
        format.onChange {
            val qtd = fromString(textProp.value)
            val string = toString(qtd)
            textProp.value = string
        }
    }
}

open class BooleanGuiProp(
    initValue: Boolean, override val label: String?, override val description: String?
) : GuiPropSameType<Boolean>, ImplCommittableSameType<Boolean>(initValue = initValue) {
    constructor(initValue: Boolean, name: String, rb: ResourceBundle) : this(
        initValue = initValue, label = rb.label(name), description = rb.description(name)
    )
}

open class ObjectGuiProp<T>(
    initValue: T, override val label: String?, override val description: String?
) : GuiPropSameType<T>, ImplCommittableSameType<T>(initValue = initValue) {
    constructor(initValue: T, name: String, rb: ResourceBundle) : this(
        initValue = initValue, label = rb.label(name), description = rb.description(name)
    )
}

open class TextGuiProp<T>(
    initValue: T,
    toString: (from: T) -> String, fromString: (from: String) -> T,
    override val label: String?, override val description: String?
) : GuiProp<String, T>, Committable<String, T> by ImplCommittable<String, T>(
    initValue = initValue,
    convertToTransitional = { from -> toString(from) },
    convertToCommit = { from -> fromString(from) }
) {
    constructor(
        initValue: T,
        converter: StringConverter<T>,
        label: String?,
        description: String?
    ) : this(
        initValue = initValue,
        toString = { value -> converter.toString(value) },
        fromString = { value -> converter.fromString(value) },
        label = label, description = description
    )

    val textProp: Property<String>
        get() = transitional

    fun reformatText() {
        val double = fromString(textProp.value)
        val string = toString(double)
        textProp.value = string
    }

    fun toString(value: T): String = convertToTransitionalType(from = value)
    fun fromString(value: String): T = convertToCommitType(from = value)
}

open class ImplCommittableSameType<C>(
    initValue: C
) : CommittableSameType<C>, Committable<C, C> by ImplCommittable<C, C>(
    initValue = initValue,
    convertToTransitional = { from -> from },
    convertToCommit = { from -> from }
)

open class ImplCommittable<T, C>(
    initValue: C,
    private val convertToTransitional: (from: C) -> T,
    private val convertToCommit: (from: T) -> C
) : Committable<T, C> {
    override val transitional: Property<T> = SimpleObjectProperty<T>(convertToTransitional(initValue))
    override val committed: ObservableValue<C>
        get() = _committed
    private val _committed: ObjectProperty<C> = SimpleObjectProperty<C>(initValue)
    private val _committedWithTransitionalType: Property<T> = SimpleObjectProperty<T>(convertToTransitional(initValue))

    override fun convertToTransitionalType(from: C): T = convertToTransitional(from)

    override fun convertToCommitType(from: T): C = convertToCommit(from)

    override val dirty: BooleanBinding by lazy {
        Bindings.createBooleanBinding(
            {
                val commitType = convertToCommitType(from = transitional.value)
                val valorJahFormatado = convertToTransitionalType(from = commitType)
                valorJahFormatado != _committedWithTransitionalType.value
            },
            arrayOf(transitional, _committedWithTransitionalType)
        )
    }

    override fun commit() {
        _committedWithTransitionalType.value = transitional.value
        _committed.value = convertToCommitType(from = transitional.value)
    }

    override fun rollback() {
        transitional.value = _committedWithTransitionalType.value
    }
}

interface GuiPropSameType<C> : GuiProp<C, C>

interface GuiProp<T, C> : Committable<T, C> {
    val label: String?
    val description: String?
}

interface CommittableSameType<C> : Committable<C, C>

interface Committable<T, C> : ObservableValue<C> {
    val transitional: Property<T>
    val committed: ObservableValue<C>
    val dirty: BooleanBinding
    fun commit()
    fun rollback()

    fun setTransitional(value: C) {
        transitional.value = convertToTransitionalType(value)
    }

    fun convertToTransitionalType(from: C): T
    fun convertToCommitType(from: T): C

    override fun removeListener(listener: ChangeListener<in C>?) {
        committed.removeListener(listener)
    }

    override fun removeListener(listener: InvalidationListener?) {
        committed.removeListener(listener)
    }

    override fun addListener(listener: ChangeListener<in C>?) {
        committed.addListener(listener)
    }

    override fun addListener(listener: InvalidationListener?) {
        committed.addListener(listener)
    }

    override fun getValue(): C = committed.value
}