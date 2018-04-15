package rs.emulate.editor.resource

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ObservableValue
import org.controlsfx.control.PropertySheet
import java.util.*

sealed class ResourceIdentifier() {
    data class FileDescriptor(val index: Int, val file: Int) : ResourceIdentifier()
    data class ArchiveEntry(val index: Int, val archive: Int) : ResourceIdentifier()
    data class ConfigEntry(val config: String, val id: Int) : ResourceIdentifier()
}

open class ResourceProperty<T : Any>(
    private val name: String,
    private var value: T,
    private val category: String? = null,
    private val description: String? = null
) : PropertySheet.Item {

    private val property by lazy { SimpleObjectProperty<T>(value) }
    var propertyValue: T
        get() = property.get()
        set(value) = property.set(value)

    override fun getName(): String {
        return name
    }

    override fun getValue(): T {
        return propertyValue
    }

    override fun setValue(value: Any?) {
        propertyValue = value as T
    }

    override fun getDescription(): String {
        return description ?: ""
    }

    override fun getType(): Class<T> {
        return value.javaClass
    }

    override fun getObservableValue(): Optional<ObservableValue<out Any>> {
        return Optional.of(property)
    }

    override fun getCategory(): String {
        return category ?: ""
    }
}

abstract class Resource(
    private val displayName: String,
    private val identifier: ResourceIdentifier,
    val properties: List<ResourceProperty<*>> = emptyList()
) {
    fun name(): String {
        return displayName
    }

    fun id() : ResourceIdentifier {
        return identifier
    }

    abstract fun createContentModel(store: ResourceStore): ResourceContentModel
}