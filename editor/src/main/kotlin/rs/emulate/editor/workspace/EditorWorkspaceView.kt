package rs.emulate.editor.workspace

import rs.emulate.editor.workspace.components.EditorMenu
import rs.emulate.legacy.IndexedFileSystem
import tornadofx.*

class EditorWorkspaceView(fs: IndexedFileSystem) : View() {
    override val root = borderpane {
        top<EditorMenu>()
    }

    init {
        title = messages["title"]
    }
}
