package rs.emulate.editor.workspace.resource

import javafx.collections.FXCollections
import rs.emulate.legacy.IndexedFileSystem
import rs.emulate.legacy.config.ConfigDecoder
import rs.emulate.legacy.config.DefinitionSupplier
import rs.emulate.legacy.config.SerializableProperty
import rs.emulate.legacy.config.`object`.DefaultObjectDefinition
import rs.emulate.legacy.config.`object`.ObjectDefinition
import rs.emulate.legacy.config.`object`.ObjectProperty

data class ObjectResourceId(val id: Int, override val name: String?) : ResourceId

class ObjectResource(definition: ObjectDefinition) : Resource<ObjectProperty> {

    @Suppress("UNCHECKED_CAST")
    override val properties = FXCollections.observableMap(definition.serializableProperties()
        .map { it.value as SerializableProperty<ObjectProperty> }
        .associateBy({ it }, { it.value })
    )!!

    override val id = ObjectResourceId(definition.id, definition.name().value)

}

class ObjectResourceBundle(fs: IndexedFileSystem) : ResourceBundle {

    private val definitions: Map<ResourceId, ObjectDefinition>

    init {
        val archive = fs.getArchive(CONFIG_INDEX, CONFIG_ARCHIVE_ID)
        definitions = ConfigDecoder(archive, supplier).decode()
            .associateBy { ObjectResourceId(it.id, it.name().value) }
    }

    override fun load(id: ResourceId): Resource<*> = ObjectResource(definitions[id]!!)

    override fun list(): Sequence<ResourceId> = definitions.keys.asSequence()

    private companion object {
        const val CONFIG_INDEX = 0
        const val CONFIG_ARCHIVE_ID = 2

        private val supplier: DefinitionSupplier<ObjectDefinition> = DefinitionSupplier.create(
            ObjectDefinition.ENTRY_NAME, ObjectDefinition::class.java, DefaultObjectDefinition::class.java
        )
    }

}
