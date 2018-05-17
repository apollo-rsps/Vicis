package rs.emulate.editor.resource.bundles.legacy.config

import rs.emulate.editor.ui.widgets.content.ModelResourceViewer
import rs.emulate.editor.ui.widgets.content.ResourceViewer
import rs.emulate.editor.ui.widgets.content.ResourceViewerExtension
import rs.emulate.editor.resource.Resource
import rs.emulate.editor.resource.ResourceCache
import rs.emulate.editor.resource.bundles.legacy.ModelResource
import rs.emulate.editor.resource.bundles.legacy.ModelResourceId
import rs.emulate.editor.resource.extensions.annotations.SupportedResources
import rs.emulate.editor.resource.index.ResourceIndexBuilder
import rs.emulate.legacy.archive.Archive
import rs.emulate.legacy.config.ConfigDecoder
import rs.emulate.legacy.config.DefinitionSupplier
import rs.emulate.legacy.config.npc.DefaultNpcDefinition
import rs.emulate.legacy.config.npc.NpcDefinition
import rs.emulate.legacy.config.npc.NpcProperty

/**
 * A [Resource] for an [NpcDefinition].
 */
class NpcResource(id: NpcResourceId, definition: NpcDefinition) : ConfigResource<NpcDefinition>(id, definition) {

    operator fun <T> get(property: NpcProperty<T>): T {
        @Suppress("UNCHECKED_CAST")
        return properties[property] as T
    }

    override fun toString(): String = "NpcResource($id) { $properties }"

}

class NpcResourceBundle(config: Archive) : ConfigResourceBundle<NpcResourceId, NpcDefinition> {

    override val idType = NpcResourceId::class

    override val definitions = ConfigDecoder(config, DEFINITION_SUPPLIER).decode()
        .associateBy { NpcResourceId(it.id) }

    override fun index(index: ResourceIndexBuilder) {
        definitions.forEach { (resourceId, def) ->
            index.category(ConfigResource.INDEX_CATEGORY_NAME) {
                category("Npc Definitions") {
                    item {
                        id = resourceId
                        label = "${def.id} - ${def.name.value}"
                    }
                }
            }
        }
    }

    override fun load(id: NpcResourceId) = NpcResource(id, definitions[id]!!)

    private companion object {
        private val DEFINITION_SUPPLIER = DefinitionSupplier.create(
            name = NpcDefinition.ENTRY_NAME,
            definition = NpcDefinition::class.java,
            default = DefaultNpcDefinition::class.java
        )
    }

}

@SupportedResources(types = [NpcResource::class])
class NpcModelViewer : ResourceViewerExtension {

    @Suppress("UNCHECKED_CAST")
    override fun createView(resource: Resource, cache: ResourceCache): ResourceViewer {
        val npc = resource as NpcResource

        val models = npc[NpcProperty.Models].asSequence()
            .map { cache.load(ModelResourceId(it)) as ModelResource }
            .map { model -> model.recoloured(npc[NpcProperty.Colours]) }
            .toList()

        return ModelResourceViewer(models)
    }

}
