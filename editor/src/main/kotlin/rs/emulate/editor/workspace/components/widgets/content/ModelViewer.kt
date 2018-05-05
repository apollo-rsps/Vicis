package rs.emulate.editor.workspace.components.widgets.content

import rs.emulate.editor.workspace.components.scene3d.SceneComponent
import rs.emulate.editor.workspace.components.scene3d.meshFromModels
import rs.emulate.editor.workspace.resource.Resource
import rs.emulate.editor.workspace.resource.ResourceCache
import rs.emulate.editor.workspace.resource.bundles.legacy.ModelResource
import rs.emulate.editor.workspace.resource.extensions.annotations.SupportedResources


@SupportedResources(types = [ModelResource::class])
class ModelViewerExtension : ResourceViewerExtension {
    override fun createView(resource: Resource, cache: ResourceCache): ResourceViewer {
        return ModelResourceViewer(resource as ModelResource)
    }
}

class ModelResourceViewer(val models: List<ModelResource>) : ResourceViewer() {
    constructor(vararg resources: ModelResource) : this(resources.toList())

    override val root = SceneComponent()

    override fun onFocusGained() {
        root.active = true
    }

    override fun onFocusLost() {
        root.active = false
    }

    init {
        root.scene3d.addChild(meshFromModels(models.map(ModelResource::model)))
    }
}
