package rs.emulate.editor.workspace.resource.bundles.legacy.config

import rs.emulate.editor.workspace.components.widgets.content.ModelResourceViewer
import rs.emulate.editor.workspace.components.widgets.content.ResourceViewer
import rs.emulate.editor.workspace.components.widgets.content.ResourceViewerExtension
import rs.emulate.editor.workspace.resource.Resource
import rs.emulate.editor.workspace.resource.ResourceCache
import rs.emulate.editor.workspace.resource.bundles.legacy.ModelResource
import rs.emulate.editor.workspace.resource.bundles.legacy.ModelResourceId
import rs.emulate.editor.workspace.resource.extensions.annotations.SupportedResources
import rs.emulate.editor.workspace.resource.index.ResourceIndexBuilder
import rs.emulate.legacy.archive.Archive
import rs.emulate.legacy.config.ConfigDecoder
import rs.emulate.legacy.config.DefinitionSupplier
import rs.emulate.legacy.config.item.DefaultItemDefinition
import rs.emulate.legacy.config.item.ItemDefinition
import rs.emulate.legacy.config.item.ItemProperty

/**
 * A [Resource] for an [ItemDefinition].
 */
class ItemResource(
    id: ItemResourceId,
    definition: ItemDefinition
) : ConfigResource<ItemDefinition>(id, definition) {

    operator fun <T> get(property: ItemProperty<T>): T {
        @Suppress("UNCHECKED_CAST")
        return properties[property] as T
    }

    override fun toString(): String = "ItemResource($id) { $properties }"

}

class ItemResourceBundle(config: Archive) : ConfigResourceBundle<ItemResourceId, ItemDefinition> {

    override val idType = ItemResourceId::class

    override val definitions = ConfigDecoder(config, DEFINITION_SUPPLIER).decode()
        .associateBy { ItemResourceId(it.id) }

    override fun index(index: ResourceIndexBuilder) {
        definitions.forEach { (resourceId, def) ->
            index.category(ConfigResource.INDEX_CATEGORY_NAME) {
                category("Item Definitions") {
                    item {
                        id = resourceId
                        label = "${def.id} - ${def.name.value}"
                    }
                }
            }
        }
    }

    override fun load(id: ItemResourceId) = ItemResource(id, definitions[id]!!)

    private companion object {
        private val DEFINITION_SUPPLIER = DefinitionSupplier.create(
            name = ItemDefinition.ENTRY_NAME,
            definition = ItemDefinition::class.java,
            default = DefaultItemDefinition::class.java
        )
    }

}

@SupportedResources(types = [ItemResource::class])
class ItemModelViewer : ResourceViewerExtension {

    @Suppress("UNCHECKED_CAST")
    override fun createView(resource: Resource, cache: ResourceCache): ResourceViewer {
        val item = resource as ItemResource

        val modelId = item[ItemProperty.Model]
        val model = cache.load(ModelResourceId(modelId)) as ModelResource
        val models = listOf(model.recoloured(item[ItemProperty.Colours]))

        return ModelResourceViewer(models)
    }

}
