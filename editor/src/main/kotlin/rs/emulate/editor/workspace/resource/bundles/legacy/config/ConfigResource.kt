package rs.emulate.editor.workspace.resource.bundles.legacy.config

import javafx.collections.FXCollections
import javafx.collections.ObservableMap
import rs.emulate.editor.workspace.resource.Resource
import rs.emulate.editor.workspace.resource.ResourceBundle
import rs.emulate.editor.workspace.resource.ResourceId
import rs.emulate.legacy.archive.Archive
import rs.emulate.legacy.config.ConfigDecoder
import rs.emulate.legacy.config.ConfigPropertyType
import rs.emulate.legacy.config.DefinitionSupplier
import rs.emulate.legacy.config.MutableConfigDefinition

/**
 * A [Resource] from the config archive.
 */
class ConfigResource<T : MutableConfigDefinition>(override val id: ResourceId, definition: T) : Resource {

    val properties: ObservableMap<ConfigPropertyType<*>, Any?> = FXCollections.observableMap(
        definition.serializableProperties().associateBy({ it.value.type }, { it.value.value })
    )

    override fun equals(other: Any?): Boolean { /* TODO should we only care about matching id? */
        return other is ConfigResource<*> && id == other.id && properties == other.properties
    }

    override fun hashCode(): Int {
        return 31 * properties.hashCode() + id.hashCode()
    }

    override fun toString(): String {
        return "ConfigResource($id) { $properties }"
    }

}

data class ConfigResourceId(val id: Int) : ResourceId

abstract class ConfigResourceBundle<T : MutableConfigDefinition>(
    config: Archive,
    supplier: DefinitionSupplier<T>
) : ResourceBundle<ConfigResourceId> {

    override val idType = ConfigResourceId::class

    protected val definitions = ConfigDecoder(config, supplier).decode()
        .associateBy { ConfigResourceId(it.id) } // TODO load lazily?

    override fun load(id: ConfigResourceId): Resource = ConfigResource(id, definitions[id]!!)

}
