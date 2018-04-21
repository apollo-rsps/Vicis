package rs.emulate.editor.workspace

import rs.emulate.editor.workspace.components.EditorMenu
import rs.emulate.editor.workspace.resource.ResourceCache
import rs.emulate.legacy.IndexedFileSystem
import tornadofx.*

class EditorWorkspaceView() : View() {
    override val root = borderpane {
        top<EditorMenu>()
    }

    init {
        title = messages["title"]
    }
}
