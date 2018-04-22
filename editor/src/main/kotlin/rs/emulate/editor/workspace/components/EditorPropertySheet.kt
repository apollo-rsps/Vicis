package rs.emulate.editor.workspace.components

import javafx.beans.value.ObservableValue
import javafx.collections.ObservableMap
import org.controlsfx.control.PropertySheet
import rs.emulate.editor.workspace.resource.bundles.legacy.config.ConfigResource
import rs.emulate.legacy.config.SerializableProperty
import java.util.Optional

class EditorPropertySheet : EditorComponent() {
    override val root: PropertySheet = PropertySheet()

    init {
        model.onResourceSelected.map {
            if (it is ConfigResource<*>) {
                it.properties.map { (property) ->
                    StringPropertyItem(property, it.properties as ObservableMap<SerializableProperty<*>, Any?>)
                }
            } else {
                emptyList()
            }
        }.subscribe {
            root.items.setAll(it)
        }
    }

}

class StringPropertyItem(
    val property: SerializableProperty<*>,
    val properties: ObservableMap<SerializableProperty<*>, Any?>
) : PropertySheet.Item {

    override fun setValue(value: Any?) {
        properties[property] = value
    }

    override fun getName(): String = property.name

    override fun getDescription(): String = "Filler description"

    override fun getType(): Class<*> = SerializableProperty::class.java

    override fun getValue(): Any = property.value ?: ""

    override fun getObservableValue(): Optional<ObservableValue<out Any>> {
        // TODO
        return Optional.empty()
    }

    override fun getCategory(): String {
        return "Property"
    }

}
