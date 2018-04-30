package rs.emulate.editor.workspace.components.widgets.content

import rs.emulate.editor.workspace.components.opengl.GLFragment
import rs.emulate.editor.workspace.components.opengl.render.RendererEventListener
import rs.emulate.editor.workspace.resource.Resource
import rs.emulate.editor.workspace.resource.bundles.legacy.ModelResource
import rs.emulate.editor.workspace.resource.extensions.annotations.SupportedResources


@SupportedResources(types = [ModelResource::class])
class ModelViewerExtension : ResourceViewerExtension {
    override fun createView(resource: Resource): ResourceViewer {
        return ModelResourceViewer(resource as ModelResource)
    }
}

class ModelResourceViewer(val model: ModelResource) : ResourceViewer() {
    override val root = GLFragment(
        object : RendererEventListener {
            override fun onStart() {
            }

            override fun onResize(width: Int, height: Int) {
            }

            override fun onRender(delta: Float) {
            }

            override fun onStop() {
            }
        }
    )

    override fun onClose() {
        root.onUndock()
    }

    override fun onOpen() {
        root.onDock()
    }
}
