package rs.emulate.editor.ui

import rs.emulate.editor.resource.NullContentModel
import rs.emulate.editor.resource.ResourceContentModel
import rs.emulate.editor.ui.fragments.EmptyView
import rs.emulate.editor.ui.fragments.Reason
import rs.emulate.editor.ui.scene3d.Scene3dInstance
import tornadofx.*

abstract class ResourceContentModelViewer<out T : ResourceContentModel>(val model: T) : View("ContentModelViewer") {
    open fun onActivate() {}
}

class NullContentModelViewer(model: NullContentModel) : ResourceContentModelViewer<NullContentModel>(model) {
    override val root = vbox {
        add(EmptyView(Reason.NoResourceEditorAvailable))
    }

    override fun onActivate() {
        replaceChildren { add(Scene3dInstance) }
    }
}