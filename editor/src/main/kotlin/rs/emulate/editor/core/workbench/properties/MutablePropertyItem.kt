package rs.emulate.editor.core.workbench.properties


import javafx.beans.value.ObservableValue
import org.controlsfx.control.PropertySheet
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty

class MutablePropertyItem<T, P : ListedProperty>(
    val propertyMetadata: P,
    private val property: KMutableProperty<T>
) : PropertySheet.Item {

    override fun getCategory(): String = propertyMetadata.category
    override fun getDescription(): String = propertyMetadata.description
    override fun getName(): String = propertyMetadata.name
    override fun getType(): Class<*> = (property.returnType.classifier as KClass<*>).java

    override fun getValue(): Any? {
        return property.getter.call()
    }

    override fun setValue(value: Any?) {
        try {
            property.setter.call(value as T)
        } catch (e: Exception) {
            println("Incorrect PropertyEditorFactory for $name - could not cast $value to type.")
        }
    }

    override fun getObservableValue(): Optional<ObservableValue<out Any>>? = Optional.empty() // TODO
}
