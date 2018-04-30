package rs.emulate.editor.workspace.components.widgets.content

import javafx.scene.Node
import tornadofx.*

abstract class ResourceViewer : Component() {
    abstract val root: Node

    open fun onFocus() {

    }

    open fun onFocusLost() {

    }

    open fun onClose() {

    }

    open fun onOpen() {

    }
}
