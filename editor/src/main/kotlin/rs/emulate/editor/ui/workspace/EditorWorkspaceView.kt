package rs.emulate.editor.ui.workspace

import javafx.event.EventTarget
import javafx.geometry.Side
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
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

            left = workspaceDrawer(Side.LEFT) {
                hboxConstraints {
                    hGrow = Priority.ALWAYS
                }

                workspaceDrawerItem<EditorExplorer>(index = 1)
            }

            val tabPane = findTopView<EditorTabPane>()
            center = tabPane.root
            right = workspaceDrawer(Side.RIGHT) {
                hboxConstraints {
                    hGrow = Priority.ALWAYS
                }

                workspaceDrawerItem<EditorPropertySheet>(index = 2)
            }

            bottom = vbox {
                workspaceDrawer(Side.BOTTOM) {
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

    private fun EventTarget.workspaceDrawer(side: Side, builder: Drawer.() -> Unit = {}): Drawer {
        val drawer = drawer(side, op = builder)
        val drawerSide = when (side) {
            Side.LEFT -> root.leftSide
            Side.RIGHT -> root.rightSide
            Side.TOP -> root.topSide
            Side.BOTTOM -> root.bottomSide
        }

        drawerSide.resizeTarget = drawer.contentArea
        return drawer
    }

    private inline fun <reified T : EditorTopView<*, *>> Drawer.workspaceDrawerItem(
        index: Int? = null,
        crossinline builder: DrawerItem.() -> Unit = {}
    ) {
        val view = findTopView<T>()
        val key = if (index == 10) 0 else index
        val title = view.titleProperty.stringBinding { if (index != null) "$index: $it" else it }

        val item = item(title) {
            children.add(view.root)
            builder(this)
        }

        if (key != null && key < 9) {
            val keyCode = KeyCode.getKeyCode(key.toString())
            val keyCombination = KeyCodeCombination(keyCode, KeyCombination.SHORTCUT_DOWN)

            sceneProperty().onChangeOnce {
                it?.accelerators?.put(keyCombination, Runnable {
                    item.expanded = !item.expanded
                })
            }
        }
    }

    companion object {
        const val WIDTH_KEY = "width"
        const val HEIGHT_KEY = "height"
    }
}
