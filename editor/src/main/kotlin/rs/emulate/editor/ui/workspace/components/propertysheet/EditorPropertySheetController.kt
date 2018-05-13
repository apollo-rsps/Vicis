package rs.emulate.editor.ui.workspace.components.propertysheet

import javafx.beans.value.ObservableValue
import javafx.collections.ObservableMap
import org.controlsfx.control.PropertySheet
import rs.emulate.editor.resource.bundles.legacy.config.ConfigResource
import rs.emulate.editor.ui.workspace.components.EditorTopController
import rs.emulate.editor.utils.javafx.onGuardedChange
import rs.emulate.legacy.config.ConfigPropertyType
import rs.emulate.legacy.config.SerializableProperty
import java.util.*

class EditorPropertySheetController : EditorTopController<EditorPropertySheetModel>() {
    override fun bind(model: EditorPropertySheetModel) {
        scope.resourceSelectionProperty.onGuardedChange { resource ->
            val propertyItems = if (resource is ConfigResource<*>) {
                resource.properties.map { (type, _) ->
                    StringPropertyItem(resource.properties, type)
                }
            } else {
               emptyList()
            }

            model.resourceProperties.setAll(propertyItems)
        }
    }

}

class StringPropertyItem(
    val properties: ObservableMap<ConfigPropertyType<*>, Any?>,
    val type: ConfigPropertyType<*>
) : PropertySheet.Item {

    override fun setValue(value: Any?) {
        properties[type] = value as Any
    }

    override fun getName(): String = type.name

    override fun getDescription(): String = "Filler description"

    override fun getType(): Class<*> = SerializableProperty::class.java

    override fun getValue(): Any {
        val value = properties[type]

        return when (value) {
            is BooleanArray -> value.contentToString()
            is CharArray -> value.contentToString()
            is ByteArray -> value.contentToString()
            is ShortArray -> value.contentToString()
            is IntArray -> value.contentToString()
            is LongArray -> value.contentToString()
            is FloatArray -> value.contentToString()
            is DoubleArray -> value.contentToString()
            is Array<*> -> value.contentDeepToString()
            else -> value.toString()
        }
    }

    override fun getObservableValue(): Optional<ObservableValue<out Any>>? = Optional.empty()

    override fun getCategory(): String {
        return "Property"
    }

}
