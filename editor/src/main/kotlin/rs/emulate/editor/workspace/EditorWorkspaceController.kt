package rs.emulate.editor.workspace

import rs.emulate.editor.workspace.resource.ResourceId
import tornadofx.*

class EditorWorkspaceController : Controller() {
    val model: EditorWorkspaceModel by inject()

    fun open(resourceId: ResourceId) {
        runAsync {
            requireNotNull(model.cache.load(resourceId)) { "Unable to decode resource" }
        } success {
            model.onResourceSelected.onNext(it)
        } fail {
            model.onResourceSelectionError.onNext(it)
        }
    }

    init {
        model.onResourceIndexUpdate.onNext(model.cache.index())
    }
}
