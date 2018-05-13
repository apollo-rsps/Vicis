package rs.emulate.editor.ui.widgets.content

import javafx.scene.Node
import tornadofx.*

abstract class ResourceViewer : Component() {
    abstract val root: Node

    open fun onFocusGained() { }

    open fun onFocusLost() { }
}
