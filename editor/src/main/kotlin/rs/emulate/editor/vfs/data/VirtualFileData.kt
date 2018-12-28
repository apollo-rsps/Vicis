package rs.emulate.editor.vfs.data

import javafx.beans.binding.Bindings
import javafx.beans.property.*
import rs.emulate.editor.utils.javafx.mapped
import rs.emulate.editor.utils.javafx.onChange
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty1


abstract class VirtualFileData {
    var isDirty = false

    private fun <P : Property<out T>, T> boundProperty(prop: P, binding: KMutableProperty<out T>): P {
        prop.value = binding.getter.call()
        prop.onChange { binding.setter.call(it) }
        // @TODO - change tracking
        return prop
    }

    protected fun boolProperty(binding: KMutableProperty0<Boolean>) = boundProperty(SimpleBooleanProperty(), binding)
    protected fun intProperty(binding: KMutableProperty0<Int>) = boundProperty(SimpleIntegerProperty(), binding)
    protected fun <T> property(binding: KMutableProperty0<T>) = boundProperty(SimpleObjectProperty(), binding)

    protected fun <T, R> mappedListProperty(
        values: Collection<R>,
        valueMappingProperty: KProperty1<R, T>,
        property: KMutableProperty0<MutableList<T>>
    ): SimpleListProperty<R> {
        val backingList = property.getter.call()
        val list = SimpleListProperty<R>()

        list.setAll(values)
        Bindings.bindContent(backingList, list.mapped { valueMappingProperty.get(it) })

        return list
    }
}
