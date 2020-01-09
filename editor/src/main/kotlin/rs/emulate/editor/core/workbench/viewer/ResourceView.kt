package rs.emulate.editor.core.workbench.viewer

import javafx.scene.Node

abstract class ResourceView {
    abstract val root: Node

    open fun onFocusGained() {}

    open fun onFocusLost() {}
}
