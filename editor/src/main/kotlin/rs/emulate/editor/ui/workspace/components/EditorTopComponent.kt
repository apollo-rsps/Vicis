package rs.emulate.editor.ui.workspace.components

import tornadofx.*

interface EditorTopComponent {
    val scope: EditorTopComponentScope
}

abstract class EditorTopController<M : ViewModel> : Controller(), EditorTopComponent {
    override val scope: EditorTopComponentScope = super.scope as EditorTopComponentScope

    abstract fun bind(model: M)
}

abstract class EditorTopView<out C : EditorTopController<M>, M : ViewModel> : View(), EditorTopComponent {
    override val scope: EditorTopComponentScope = super.scope as EditorTopComponentScope

    abstract val controller: C
    abstract val model: M

    override fun onDock() {
        controller.bind(model)
    }
}
