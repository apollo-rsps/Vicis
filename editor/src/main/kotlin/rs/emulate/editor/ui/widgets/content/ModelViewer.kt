package rs.emulate.editor.ui.widgets.content

import rs.emulate.editor.ui.widgets.scene3d.SceneComponent
import rs.emulate.editor.ui.widgets.scene3d.meshFromModels
import rs.emulate.editor.resource.Resource
import rs.emulate.editor.resource.ResourceCache
import rs.emulate.editor.resource.bundles.legacy.ModelResource
import rs.emulate.editor.resource.extensions.annotations.SupportedResources


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
