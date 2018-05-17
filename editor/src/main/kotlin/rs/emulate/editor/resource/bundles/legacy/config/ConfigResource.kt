package rs.emulate.editor.resource.bundles.legacy.config

import javafx.collections.FXCollections
import javafx.collections.ObservableMap
import rs.emulate.editor.resource.Resource
import rs.emulate.editor.resource.ResourceBundle
import rs.emulate.editor.resource.ResourceId
import rs.emulate.legacy.IndexedFileSystem
import rs.emulate.legacy.config.ConfigPropertyType
import rs.emulate.legacy.config.MutableConfigDefinition

/**
 * A [Resource] from the config archive.
 */
abstract class ConfigResource<T : MutableConfigDefinition>(override val id: ResourceId, definition: T) : Resource {

    val properties: ObservableMap<ConfigPropertyType<*>, Any?> = FXCollections.observableMap(
        definition.serializableProperties().associateBy({ it.value.type }, { it.value.value })
    )

    override fun equals(other: Any?): Boolean {
        return other is ConfigResource<*> && id == other.id && properties == other.properties
    }

    override fun hashCode(): Int {
        return 31 * properties.hashCode() + id.hashCode()
    }

    companion object {
        const val INDEX_CATEGORY_NAME = "Config"
    }

}

interface ConfigResourceBundle<I : ConfigResourceId, T : MutableConfigDefinition> : ResourceBundle<I> {
    val definitions: Map<I, T>
}

class ConfigResourceBundleFactory(fs: IndexedFileSystem) {

    private val config = fs.getArchive(CONFIG_INDEX, CONFIG_ARCHIVE_ID)

    val animations = AnimationResourceBundle(config)
    val floors = FloorResourceBundle(config)
    val graphics = GraphicResourceBundle(config)
    val identikits = IdentikitResourceBundle(config)
    val items = ItemResourceBundle(config)
    val npcs = NpcResourceBundle(config)
    val objects = ObjectResourceBundle(config)

    fun bundles(): List<ResourceBundle<*>> = listOf(animations, floors, graphics, identikits, items, npcs, objects)

    private companion object {
        const val CONFIG_INDEX = 0
        const val CONFIG_ARCHIVE_ID = 2
    }
}
