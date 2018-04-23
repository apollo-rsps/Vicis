package rs.emulate.editor.workspace.components

import com.github.thomasnield.rxkotlinfx.changes
import com.github.thomasnield.rxkotlinfx.toBinding
import io.reactivex.Observable
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableMap
import org.controlsfx.control.PropertySheet
import rs.emulate.editor.workspace.resource.bundles.legacy.config.ConfigResource
import rs.emulate.legacy.config.SerializableProperty
import tornadofx.*
import java.util.Optional

class EditorPropertySheet : EditorComponent() {
    override val root: PropertySheet = PropertySheet()

    init {
        title = messages["title"]

        model.onResourceSelected.map {
            when (it) {
                is ConfigResource<*> -> it.properties.map { (property) ->
                    val propertyValueChanges = it.properties.changes()
                        .distinctUntilChanged()
                        .filter(property::equals)
                        .map { it.value }
                        .startWith(property.value)

                    StringPropertyItem(it.properties, property, propertyValueChanges)
                }
                else -> emptyList()
            }
        }.subscribe {
            root.items.setAll(it)
        }
    }

}

class StringPropertyItem(
    val properties: ObservableMap<SerializableProperty<*>, out Any?>,
    val property: SerializableProperty<*>,
    val propertyChanges: Observable<*>
) : PropertySheet.Item {

    val valueProperty = ReadOnlyObjectWrapper<Any>()

    init {
        valueProperty.bind(propertyChanges.toBinding())
    }

    override fun setValue(value: Any?) {
        // @todo - using `property` as the key here is weird
        val propertyMap = properties as ObservableMap<SerializableProperty<*>, Any?>
        propertyMap[property] = value as Any
    }

    override fun getName(): String = property.name

    override fun getDescription(): String = "Filler description"

    override fun getType(): Class<*> = SerializableProperty::class.java

    override fun getValue(): Any = property.value ?: ""

    override fun getObservableValue(): Optional<ObservableValue<out Any>>? = Optional.of(valueProperty)

    override fun getCategory(): String {
        return "Property"
    }

}
