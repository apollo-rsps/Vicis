package rs.emulate.editor.ui.workspace.components.status

import rs.emulate.editor.ui.workspace.components.EditorTopController
import tornadofx.*

class EditorStatusBarController : EditorTopController<EditorStatusBarModel>() {
    override fun bind(model: EditorStatusBarModel) {
        model.footerTextProperty.bind(scope.resourceSelectionProperty.stringBinding { it?.id.toString() })
    }
}
