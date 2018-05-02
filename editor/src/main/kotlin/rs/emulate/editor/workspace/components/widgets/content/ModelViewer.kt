package rs.emulate.editor.workspace.components.widgets.content

import glm_.vec3.Vec3
import glm_.vec3.Vec3i
import rs.emulate.editor.workspace.components.scene3d.SceneComponent
import rs.emulate.editor.workspace.resource.Resource
import rs.emulate.editor.workspace.resource.bundles.legacy.ModelResource
import rs.emulate.editor.workspace.resource.extensions.annotations.SupportedResources
import rs.emulate.legacy.model.Vertex
import rs.emulate.scene3d.Mesh


@SupportedResources(types = [ModelResource::class])
class ModelViewerExtension : ResourceViewerExtension {
    override fun createView(resource: Resource): ResourceViewer {
        return ModelResourceViewer(resource as ModelResource)
    }
}

class ModelResourceViewer(val resource: ModelResource) : ResourceViewer() {
    override val root = SceneComponent()

    override fun onClose() {
        root.onUndock()
    }

    override fun onOpen() {
        root.onDock()
    }

    init {
        val mesh = Mesh().also {
            it.indices = resource.model.faces.map {
                Vec3i(it.a, it.b, it.c)
            }

            it.positions = resource.model.vertices.map {
                Vec3(it.x * 0.01f, -it.y * 0.01f, it.z * 0.01f)
            }
        }

        root.scene3d.addChild(mesh)
    }
}
