package rs.emulate.editor.workspace.resource.bundles.legacy.config

import rs.emulate.editor.workspace.resource.Resource
import rs.emulate.editor.workspace.resource.index.ResourceIndexBuilder
import rs.emulate.legacy.archive.Archive
import rs.emulate.legacy.config.ConfigDecoder
import rs.emulate.legacy.config.DefinitionSupplier
import rs.emulate.legacy.config.floor.DefaultFloorDefinition
import rs.emulate.legacy.config.floor.FloorDefinition
import rs.emulate.legacy.config.floor.FloorProperty


/**
 * A [Resource] for an [FloorDefinition].
 */
class FloorResource(
    id: FloorResourceId,
    definition: FloorDefinition
) : ConfigResource<FloorDefinition>(id, definition) {

    operator fun <T> get(property: FloorProperty<T>): T {
        @Suppress("UNCHECKED_CAST")
        return properties[property] as T
    }

    override fun toString(): String = "FloorResource($id) { $properties }"

}

class FloorResourceBundle(config: Archive) : ConfigResourceBundle<FloorResourceId, FloorDefinition> {

    override val idType = FloorResourceId::class

    override val definitions = ConfigDecoder(config, DEFINITION_SUPPLIER).decode()
        .associateBy { FloorResourceId(it.id) }

    override fun index(index: ResourceIndexBuilder) {
        definitions.forEach { (resourceId, def) ->
            index.category(ConfigResource.INDEX_CATEGORY_NAME) {
                category("Floor Definitions") {
                    item {
                        id = resourceId
                        label = "${def.id} - ${def.name}"
                    }
                }
            }
        }
    }

    override fun load(id: FloorResourceId) = FloorResource(id, definitions[id]!!)

    private companion object {
        private val DEFINITION_SUPPLIER = DefinitionSupplier.create(
            name = FloorDefinition.ENTRY_NAME,
            definition = FloorDefinition::class.java,
            default = DefaultFloorDefinition::class.java
        )
    }

}
