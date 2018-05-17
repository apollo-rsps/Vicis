package rs.emulate.editor.resource.bundles.legacy.config

import rs.emulate.editor.resource.Resource
import rs.emulate.editor.resource.index.ResourceIndexBuilder
import rs.emulate.legacy.archive.Archive
import rs.emulate.legacy.config.ConfigDecoder
import rs.emulate.legacy.config.DefinitionSupplier
import rs.emulate.legacy.config.graphic.DefaultGraphicDefinition
import rs.emulate.legacy.config.graphic.GraphicDefinition
import rs.emulate.legacy.config.graphic.GraphicProperty

/**
 * A [Resource] for an [GraphicDefinition].
 */
class GraphicResource(
    id: GraphicResourceId,
    definition: GraphicDefinition
) : ConfigResource<GraphicDefinition>(id, definition) {

    operator fun <T> get(property: GraphicProperty<T>): T {
        @Suppress("UNCHECKED_CAST")
        return properties[property] as T
    }

    override fun toString(): String = "GraphicResource($id) { $properties }"

}

class GraphicResourceBundle(config: Archive) : ConfigResourceBundle<GraphicResourceId, GraphicDefinition> {

    override val idType = GraphicResourceId::class

    override val definitions = ConfigDecoder(config, DEFINITION_SUPPLIER).decode()
        .associateBy { GraphicResourceId(it.id) }

    override fun index(index: ResourceIndexBuilder) {
        definitions.forEach { (resourceId, def) ->
            index.category(ConfigResource.INDEX_CATEGORY_NAME) {
                category("Graphic Definitions") {
                    item {
                        id = resourceId
                        label = "${def.id}"
                    }
                }
            }
        }
    }

    override fun load(id: GraphicResourceId) = GraphicResource(id, definitions[id]!!)

    private companion object {
        private val DEFINITION_SUPPLIER = DefinitionSupplier.create(
            name = GraphicDefinition.ENTRY_NAME,
            definition = GraphicDefinition::class.java,
            default = DefaultGraphicDefinition::class.java
        )
    }

}
