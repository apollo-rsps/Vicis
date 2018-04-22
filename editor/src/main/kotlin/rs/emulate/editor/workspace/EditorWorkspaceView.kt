package rs.emulate.editor.workspace

import javafx.geometry.Side
import org.controlsfx.control.StatusBar
import rs.emulate.editor.workspace.components.EditorStatusBar
import rs.emulate.editor.workspace.components.menu.EditorMenu
import rs.emulate.editor.workspace.components.widgets.tree.ResourceIndexTree
import tornadofx.*

class EditorWorkspaceView() : View() {
    override val root = borderpane {
        top<EditorMenu>()

        left = hbox {
            drawer(Side.LEFT) {
                item(ResourceIndexTree::class, showHeader = true)
            }
        }

        right = hbox {
            drawer(Side.RIGHT) {

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
