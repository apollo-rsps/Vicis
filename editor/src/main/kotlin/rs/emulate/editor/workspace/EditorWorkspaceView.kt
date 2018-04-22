package rs.emulate.editor.workspace

import javafx.geometry.Side
import rs.emulate.editor.workspace.components.EditorPropertySheet
import rs.emulate.editor.workspace.components.EditorStatusBar
import rs.emulate.editor.workspace.components.menu.EditorMenu
import rs.emulate.editor.workspace.components.widgets.tree.ResourceIndexTree
import tornadofx.View
import tornadofx.borderpane
import tornadofx.drawer
import tornadofx.get
import tornadofx.hbox
import tornadofx.vbox

class EditorWorkspaceView : View() {
    override val root = borderpane {
        top<EditorMenu>()

        left = hbox {
            drawer(Side.LEFT) {
                item(ResourceIndexTree::class, showHeader = true)
            }
        }

        right = hbox {
            drawer(Side.RIGHT) {
                add(EditorPropertySheet::class)
            }
        }

        bottom = vbox {
            drawer(Side.BOTTOM) {

            }

            add<EditorStatusBar>()
        }
    }

    init {
        title = messages["title"]
    }
}
