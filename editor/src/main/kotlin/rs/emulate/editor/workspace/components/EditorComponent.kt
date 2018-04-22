package rs.emulate.editor.workspace.components

import rs.emulate.editor.workspace.EditorWorkspaceController
import rs.emulate.editor.workspace.EditorWorkspaceModel
import tornadofx.*

abstract class EditorComponent : View() {
    val model: EditorWorkspaceModel by inject()
    val controller: EditorWorkspaceController by inject()
}
