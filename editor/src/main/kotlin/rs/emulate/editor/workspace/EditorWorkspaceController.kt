package rs.emulate.editor.workspace

import rs.emulate.editor.workspace.components.widgets.content.ResourceViewerExtension
import rs.emulate.editor.workspace.resource.ResourceId
import rs.emulate.editor.workspace.resource.extensions.ResourceExtensionPoint
import tornadofx.*

class EditorWorkspaceController : Controller() {
    val model: EditorWorkspaceModel by inject()
    val viewerExtensions = ResourceExtensionPoint(ResourceViewerExtension::class)

    init {
        runAsync {
            viewerExtensions.load()
            model.cache.index()
        } success {
            model.onResourceIndexUpdate.onNext(it)
        }
    }

    fun open(resourceId: ResourceId) {
        runAsync {
            requireNotNull(model.cache.load(resourceId)) { "Unable to decode resource" }
        } success {
            model.onResourceSelected.onNext(it)
        } fail {
            model.onResourceSelectionError.onNext(it)
        }
    }
}
