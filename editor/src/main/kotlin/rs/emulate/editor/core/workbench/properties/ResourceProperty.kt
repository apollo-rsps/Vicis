package rs.emulate.editor.core.workbench.properties

import javafx.beans.value.ObservableValue
import org.controlsfx.control.PropertySheet
import org.controlsfx.property.editor.PropertyEditor
import java.util.*
import kotlin.reflect.KClass

interface ResourceProperty {
    val name: String
    val category: String
    val description: String
}

abstract class ResourcePropertySheetItem<T : ResourceProperty>(
    private val observable: ObservableProperty<out Any?>,
    val propertyEditorClass: KClass<out PropertyEditor<*>>
) : PropertySheet.Item {

    final override fun getCategory(): String = observable.property.category
    final override fun getDescription(): String = observable.property.description
    final override fun getName(): String = observable.property.name

    final override fun getValue(): Any? = observable.value
    final override fun getObservableValue(): Optional<ObservableValue<out Any?>> = Optional.of(observable)
    final override fun getPropertyEditorClass() = Optional.of(propertyEditorClass.java)
    final override fun getType(): Class<*> = throw UnsupportedOperationException("")

    final override fun setValue(value: Any?) {
        observable.value = value
    }

    override fun isEditable(): Boolean {
        return true
    }

}
