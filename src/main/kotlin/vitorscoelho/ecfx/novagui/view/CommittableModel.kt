package vitorscoelho.ecfx.novagui.view

import javafx.beans.binding.Bindings
import javafx.beans.binding.BooleanBinding
import vitorscoelho.ecfx.novagui.utils.Committable
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

abstract class CommittableModel {
    private val commitableProps: List<Committable<*, *>> by lazy {
        val mutableList = mutableListOf<Committable<*, *>>()
        this.javaClass.kotlin.memberProperties.forEach { member ->
            val visibility: KVisibility? = member.getter.visibility
            if (visibility != null) {
                member.getter.isAccessible = true
                val retornoDoGet: Any? = member.invoke(this)
                if (retornoDoGet != null && retornoDoGet is Committable<*, *>) {
                    mutableList.add(retornoDoGet)
                }
            }
        }
        mutableList
    }

    fun createDirtyBinding(): BooleanBinding {
        val propertiesParaObservar: Array<BooleanBinding> = commitableProps.map { it.dirty }.toTypedArray()
        return Bindings.createBooleanBinding(
            { propertiesParaObservar.any { it.value } },
            propertiesParaObservar
        )
    }

    fun commit() {
        commitableProps.forEach { it.commit() }
    }

    fun rollback() {
        commitableProps.forEach { it.rollback() }
    }
}