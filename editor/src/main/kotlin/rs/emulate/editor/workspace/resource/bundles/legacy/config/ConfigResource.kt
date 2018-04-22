package rs.emulate.editor.workspace.resource.bundles.legacy.config

import javafx.collections.FXCollections
import javafx.collections.ObservableMap
import rs.emulate.editor.workspace.resource.Resource
import rs.emulate.editor.workspace.resource.ResourceBundle
import rs.emulate.editor.workspace.resource.ResourceId
import rs.emulate.editor.workspace.resource.index.ResourceIndexBuilder
import rs.emulate.legacy.config.MutableConfigDefinition
import rs.emulate.legacy.config.SerializableProperty

/**
 * A [Resource] from the config archive.
 */
class ConfigResource<T : MutableConfigDefinition>(definition: T, toResourceId: T.() -> ResourceId) : Resource {

    override val properties: ObservableMap<SerializableProperty<*>, *> = FXCollections.observableMap(
        definition.serializableProperties().associateBy({ it.value }, { it.value.value })
    )

    override val id = definition.toResourceId()

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

class ConfigResourceBundle<T : MutableConfigDefinition>(
    private val definitions: Map<ResourceId, T>,
    private val toResourceId: T.() -> ResourceId
) : ResourceBundle {

    override fun load(id: ResourceId): Resource = ConfigResource(definitions[id]!!, toResourceId)

    override fun index(index: ResourceIndexBuilder) {
        definitions.forEach { (resourceId, def) ->
            index.entry {
                id = resourceId
                label = resourceId.name
                type = def.javaClass.simpleName
            }
        }
    }

}
