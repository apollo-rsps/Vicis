package rs.emulate.editor.ui.workspace

import javafx.geometry.Side
import javafx.scene.layout.Priority
import mergeChangesOf
import rs.emulate.editor.ui.widgets.layout.ResizableBorderPane
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

    override val root = ResizableBorderPane()

    init {
        title = messages["title"]
        componentScope = EditorTopComponentScope()
        componentScope.bind(model)

        with(root) {
            top<EditorMenu>()
            topSide.resizable = false

            left = drawer(Side.LEFT) {
                hboxConstraints {
                    hGrow = Priority.ALWAYS
                }

                leftSide.resizeTarget = contentArea
                item(findTopView<EditorExplorer>(), showHeader = true)
            }

            val tabPane = findTopView<EditorTabPane>()
            center = tabPane.root
            right = drawer(Side.RIGHT) {
                hboxConstraints {
                    hGrow = Priority.ALWAYS
                }

                rightSide.resizeTarget = contentArea
                item(findTopView<EditorPropertySheet>(), showHeader = true)
            }

            bottom = vbox {
                drawer(Side.BOTTOM) {
                    bottomSide.resizeTarget = contentArea
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
