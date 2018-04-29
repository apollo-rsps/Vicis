package rs.emulate.editor.workspace

import javafx.geometry.Side
import javafx.scene.layout.Priority
import rs.emulate.editor.workspace.components.EditorPropertySheet
import rs.emulate.editor.workspace.components.EditorStatusBar
import rs.emulate.editor.workspace.components.EditorTabPane
import rs.emulate.editor.workspace.components.menu.EditorMenu
import rs.emulate.editor.workspace.components.widgets.tree.ResourceIndexTree
import tornadofx.*

class EditorWorkspaceView : View() {
    override val root = borderpane {
        top<EditorMenu>()

        left = hbox {
            drawer(Side.LEFT) {
                item(ResourceIndexTree::class, showHeader = true)
            }
        }

        center = vbox {
            add(EditorTabPane::class)
        }

        right = hbox {
            drawer(Side.RIGHT) {
                item(EditorPropertySheet::class, showHeader = true)
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
