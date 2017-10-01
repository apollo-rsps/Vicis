package rs.emulate.editor.ui.viewers

import org.springframework.stereotype.Component
import rs.emulate.editor.resource.ResourceContentModel
import rs.emulate.editor.ui.ResourceContentModelViewer
import rs.emulate.editor.ui.ResourceContentViewerFactory
import rs.emulate.editor.ui.scene3d.Scene3dInstance
import tornadofx.*
import kotlin.reflect.KClass

class MeshContentModelViewer(
    model: MeshContentModel
) : ResourceContentModelViewer<MeshContentModel>(model) {

    override val root = vbox {

    }

    override fun onActivate() {
        Scene3dInstance.showNewModel(model.vertices, model.faces)

        replaceChildren {
            add(Scene3dInstance)
        }
    }
}

@Component
class MeshContentModelViewerFactory : ResourceContentViewerFactory<MeshContentModel, MeshContentModelViewer> {
    override fun supports(kClass: KClass<out ResourceContentModel>): Boolean {
        return kClass == MeshContentModel::class
    }

    override fun create(contentModel: ResourceContentModel): MeshContentModelViewer {
        return MeshContentModelViewer(contentModel as MeshContentModel)
    }

}