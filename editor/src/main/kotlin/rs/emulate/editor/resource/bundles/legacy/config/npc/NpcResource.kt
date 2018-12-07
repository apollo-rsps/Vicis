package rs.emulate.editor.resource.bundles.legacy.config.npc

import rs.emulate.editor.resource.Resource
import rs.emulate.editor.resource.ResourceCache
import rs.emulate.editor.resource.bundles.legacy.ModelResource
import rs.emulate.editor.resource.bundles.legacy.ModelResourceId
import rs.emulate.editor.resource.bundles.legacy.config.ConfigResource
import rs.emulate.editor.resource.bundles.legacy.config.NpcResourceId
import rs.emulate.editor.resource.extensions.annotations.SupportedResources
import rs.emulate.editor.ui.widgets.content.ModelResourceViewer
import rs.emulate.editor.ui.widgets.content.ResourceViewer
import rs.emulate.editor.ui.widgets.content.ResourceViewerExtension
import rs.emulate.editor.ui.workspace.components.propertysheet.PropertyEditorFactory
import rs.emulate.legacy.config.npc.NpcDefinition

/**
 * A [Resource] for an [NpcDefinition].
 */
class NpcResource(
    override val id: NpcResourceId,
    override val definition: NpcDefinition
) : ConfigResource<NpcDefinition>() {

    override val editorFactory: PropertyEditorFactory = NpcPropertyEditorFactory // TODO move elsewhere
    override val generator = NpcPropertyItemFactory

}

@SupportedResources(types = [NpcResource::class])
class NpcModelViewer : ResourceViewerExtension {

    @Suppress("UNCHECKED_CAST")
    override fun createView(resource: Resource, cache: ResourceCache): ResourceViewer? {
        val npc = resource as NpcResource

        val modelIds = npc.definition.models
        if (modelIds?.isEmpty() != false) {
            return null
        }

        val models = modelIds.asSequence()
            .map { cache.load(ModelResourceId(it)) as ModelResource }
            .map { model -> model.recoloured(npc.definition.replacementColours) }
            .toList()

        return ModelResourceViewer(models)
    }

}
