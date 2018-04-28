package rs.emulate.editor.workspace.components

import com.github.thomasnield.rxkotlinfx.changes
import com.github.thomasnield.rxkotlinfx.toBinding
import io.reactivex.Observable
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableMap
import org.controlsfx.control.PropertySheet
import rs.emulate.editor.workspace.resource.bundles.legacy.config.ConfigResource
import rs.emulate.legacy.config.ConfigPropertyType
import rs.emulate.legacy.config.SerializableProperty
import tornadofx.get
import java.util.Optional
import kotlin.collections.component1
import kotlin.collections.set

class EditorPropertySheet : EditorComponent() {
    override val root: PropertySheet = PropertySheet()

    init {
        title = messages["title"]

        model.onResourceSelected.map { resource ->
            when (resource) {
                is ConfigResource<*> -> resource.properties.map { (property) ->
                    val propertyValueChanges = resource.properties.changes()
                        .distinctUntilChanged()
                        .filter(property::equals)
                        .map { it.value }
                        .startWith(property)

                    StringPropertyItem(resource.properties, property, propertyValueChanges)
                }
                else -> emptyList()
            }
        }.subscribe {
            root.items.setAll(it)
        }
    }

}

class StringPropertyItem(
    val properties: ObservableMap<ConfigPropertyType<*>, Any?>,
    val type: ConfigPropertyType<*>,
    propertyChanges: Observable<*>
) : PropertySheet.Item {

    val valueProperty = ReadOnlyObjectWrapper<Any>()

    init {
        valueProperty.bind(propertyChanges.toBinding())
    }

    override fun setValue(value: Any?) {
        properties[type] = value as Any
    }

    override fun getName(): String = type.name

    override fun getDescription(): String = "Filler description"

    override fun getType(): Class<*> = SerializableProperty::class.java

    override fun getValue(): Any = properties[type] ?: ""

    override fun getObservableValue(): Optional<ObservableValue<out Any>>? = Optional.of(valueProperty)

    override fun getCategory(): String {
        return "Property"
    }

}
