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
import rs.emulate.legacy.config.ConfigPropertyType
import rs.emulate.legacy.config.ConfigUtils
import rs.emulate.legacy.config.DefinitionSupplier
import rs.emulate.legacy.config.kit.DefaultIdentikitDefinition
import rs.emulate.legacy.config.kit.IdentikitDefinition
import rs.emulate.legacy.config.kit.IdentikitProperty


/**
 * A [Resource] for an [IdentikitDefinition].
 */
class IdentikitResource(
    id: IdentikitResourceId,
    definition: IdentikitDefinition
) : ConfigResource<IdentikitDefinition>(id, definition) {

    operator fun <T> get(property: ConfigPropertyType<T>): T {
        @Suppress("UNCHECKED_CAST")
        return properties[property] as T
    }

    override fun toString(): String = "IdentikitResource($id) { $properties }"

}

class IdentikitResourceBundle(config: Archive) : ConfigResourceBundle<IdentikitResourceId, IdentikitDefinition> {

    override val idType = IdentikitResourceId::class

    override val definitions = ConfigDecoder(config, DEFINITION_SUPPLIER).decode()
        .associateBy { IdentikitResourceId(it.id) }

    override fun index(index: ResourceIndexBuilder) {
        definitions.forEach { (resourceId, def) ->
            index.category(ConfigResource.INDEX_CATEGORY_NAME) {
                category("Identikit Definitions") {
                    item {
                        id = resourceId
                        label = "${def.id}"
                    }
                }
            }
        }
    }

    override fun load(id: IdentikitResourceId) = IdentikitResource(id, definitions[id]!!)

    private companion object {
        private val DEFINITION_SUPPLIER = DefinitionSupplier.create(
            name = IdentikitDefinition.ENTRY_NAME,
            definition = IdentikitDefinition::class.java,
            default = DefaultIdentikitDefinition::class.java
        )
    }

}

@SupportedResources(types = [IdentikitResource::class])
class IdentikitModelViewer : ResourceViewerExtension {

    @Suppress("UNCHECKED_CAST")
    override fun createView(resource: Resource, cache: ResourceCache): ResourceViewer {
        val identikit = resource as IdentikitResource

        val recolours = mutableMapOf<Int, Int>()
        for (slot in 0 until IdentikitDefinition.COLOUR_COUNT) {
            val original = ConfigUtils.getOriginalColourPropertyName<Int>(slot, 40)
            val new = ConfigUtils.getReplacementColourPropertyName<Int>(slot, 50)

            recolours[identikit[original]] = identikit[new]
        }

        val widgetModels = mutableListOf<Int>()
        for (index in 0 until IdentikitDefinition.WIDGET_MODEL_COUNT) {
            val model = identikit[ConfigUtils.newOptionProperty<Int>(IdentikitDefinition.WIDGET_MODEL_PREFIX, index, 60)]
            if (model != -1) {
                widgetModels += model
            }
        }

        val modelIds = if (widgetModels.isNotEmpty()) {
            widgetModels.toSet()
        } else {
            identikit[IdentikitProperty.Models].toSet()
        }

        val models = modelIds
            .map { cache.load(ModelResourceId(it))!! as ModelResource }
            .map { model -> model.recoloured(recolours) }
            .toList()

        return ModelResourceViewer(models)
    }

}
