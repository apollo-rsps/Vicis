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
import rs.emulate.legacy.config.`object`.DefaultObjectDefinition
import rs.emulate.legacy.config.`object`.ObjectDefinition
import rs.emulate.legacy.config.`object`.ObjectProperty

/**
 * A [Resource] for an [ObjectDefinition].
 */
class ObjectResource(
    id: ObjectResourceId,
    definition: ObjectDefinition
) : ConfigResource<ObjectDefinition>(id, definition) {

    operator fun <T> get(property: ObjectProperty<T>): T {
        @Suppress("UNCHECKED_CAST")
        return properties[property] as T
    }

    override fun toString(): String = "ObjectResource($id) { $properties }"

}

class ObjectResourceBundle(config: Archive) : ConfigResourceBundle<ObjectResourceId, ObjectDefinition> {

    override val idType = ObjectResourceId::class

    override val definitions = ConfigDecoder(config, DEFINITION_SUPPLIER).decode()
        .associateBy { ObjectResourceId(it.id) }

    override fun index(index: ResourceIndexBuilder) {
        definitions.forEach { (resourceId, def) ->
            index.category(ConfigResource.INDEX_CATEGORY_NAME) {
                category("Object Definitions") {
                    item {
                        id = resourceId
                        label = "${def.id} - ${def.name.value}"
                    }
                }
            }
        }
    }

    override fun load(id: ObjectResourceId) = ObjectResource(id, definitions[id]!!)

    private companion object {
        private val DEFINITION_SUPPLIER = DefinitionSupplier.create(
            name = ObjectDefinition.ENTRY_NAME,
            definition = ObjectDefinition::class.java,
            default = DefaultObjectDefinition::class.java
        )
    }

}

@SupportedResources(types = [ObjectResource::class])
class ObjectModelViewer : ResourceViewerExtension {

    @Suppress("UNCHECKED_CAST")
    override fun createView(resource: Resource, cache: ResourceCache): ResourceViewer {
        val models = objectModels(resource as ObjectResource, cache)

        return ModelResourceViewer(models)
    }

    private fun objectModels(obj: ObjectResource, cache: ResourceCache): List<ModelResource> {
        val modelId = obj[ObjectProperty.PositionedModels].getModels().firstOrNull()
            ?: obj[ObjectProperty.Models].getModels().firstOrNull() ?: return emptyList()

        val model = cache.load(ModelResourceId(modelId)) as ModelResource
        return listOf(model.recoloured(obj[ObjectProperty.Colours]))
    }

}
