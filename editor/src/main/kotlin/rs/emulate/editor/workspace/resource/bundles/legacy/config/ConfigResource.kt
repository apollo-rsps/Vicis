package rs.emulate.editor.workspace.resource.bundles.legacy.config

import javafx.collections.FXCollections
import javafx.collections.ObservableMap
import rs.emulate.editor.workspace.resource.Resource
import rs.emulate.editor.workspace.resource.ResourceBundle
import rs.emulate.editor.workspace.resource.ResourceId
import rs.emulate.legacy.config.ConfigPropertyType
import rs.emulate.legacy.config.MutableConfigDefinition

/**
 * A [Resource] from the config archive.
 */
class ConfigResource<T : MutableConfigDefinition>(override val id: ResourceId, definition: T) : Resource {

    val properties: ObservableMap<ConfigPropertyType<*>, Any?> = FXCollections.observableMap(
        definition.serializableProperties().associateBy({ it.value.type }, { it.value.value })
    )

    override fun equals(other: Any?): Boolean {
        return other is ConfigResource<*> && id == other.id
    }

    override fun hashCode(): Int {
        return 31 * properties.hashCode() + id.hashCode()
    }

    override fun toString(): String {
        return "ConfigResource($id) { $properties }"
    }

    companion object {
        const val INDEX_CATEGORY = "Config"
    }

}

interface ConfigResourceBundle<I : ConfigResourceId, T : MutableConfigDefinition> : ResourceBundle<I> {

    val definitions: Map<I, T>

    override fun load(id: I): Resource = ConfigResource(id, definitions[id]!!)

}
