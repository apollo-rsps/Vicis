package rs.emulate.editor.ui.workspace

import javafx.geometry.Side
import mergeChangesOf
import rs.emulate.editor.ui.workspace.components.EditorTopComponentScope
import rs.emulate.editor.ui.workspace.components.EditorTopView
import rs.emulate.editor.ui.workspace.components.explorer.EditorExplorer
import rs.emulate.editor.ui.workspace.components.menu.EditorMenu
import rs.emulate.editor.ui.workspace.components.propertysheet.EditorPropertySheet
import rs.emulate.editor.ui.workspace.components.status.EditorStatusBar
import rs.emulate.editor.ui.workspace.components.tabpane.EditorTabPane
import tornadofx.*
import java.util.concurrent.TimeUnit

class EditorWorkspaceView : View() {

    private val componentScope: EditorTopComponentScope
    private val model by inject<EditorWorkspaceModel>()

    override val root = borderpane()

    init {
        title = messages["title"]
        componentScope = EditorTopComponentScope()
        componentScope.bind(model)

        with(root) {
            top<EditorMenu>()

            left = hbox {
                drawer(Side.LEFT) {
                    item(findTopView<EditorExplorer>(), showHeader = true)
                }
            }

            center = vbox {
                add(findTopView<EditorTabPane>())
            }

            right = hbox {
                drawer(Side.RIGHT) {
                    item(findTopView<EditorPropertySheet>(), showHeader = true)
                }
            }

            bottom = vbox {
                drawer(Side.BOTTOM) {

                }

                add(findTopView<EditorStatusBar>())
            }
        }
    }

    override fun onDock() {
        primaryStage.width = config.double(WIDTH_KEY, 800.0)!!
        primaryStage.height = config.double(HEIGHT_KEY, 600.0)!!
        primaryStage.centerOnScreen()

        mergeChangesOf(root.heightProperty(), root.widthProperty())
            .throttleLast(200L, TimeUnit.MILLISECONDS)
            .subscribe {
                with(config) {
                    set(WIDTH_KEY to root.width)
                    set(HEIGHT_KEY to root.height)
                    save()
                }
            }
    }

    private inline fun <reified T : EditorTopView<*, *>> findTopView(): T {
        return find(T::class, scope = componentScope)
    }

    companion object {
        const val WIDTH_KEY = "width"
        const val HEIGHT_KEY = "height"
    }
}
