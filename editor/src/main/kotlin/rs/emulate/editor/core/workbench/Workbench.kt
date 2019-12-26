package rs.emulate.editor.core.workbench

import javafx.collections.ListChangeListener
import javafx.fxml.FXML
import javafx.geometry.Side
import javafx.scene.control.MenuBar
import javafx.scene.control.TabPane
import javafx.scene.control.TitledPane
import org.controlsfx.control.StatusBar
import rs.emulate.editor.core.task.TaskRunner
import rs.emulate.editor.core.workbench.docking.DockingArea
import rs.emulate.editor.core.workbench.docking.DockingNodeFactory
import rs.emulate.editor.core.workbench.menu.MenuFactory
import rs.emulate.editor.javafx.controls.Dock
import rs.emulate.editor.javafx.controls.ResizableBorderPane
import rs.emulate.editor.javafx.loader.FxmlLoader
import javax.inject.Inject

/**
 * The workbench is a container for
 */
class Workbench @Inject constructor(
    val ctx: WorkbenchContext,
    val fxmlLoader: FxmlLoader,
    val dockedNodeFactory: DockingNodeFactory,
    val menuFactory: MenuFactory,
    val taskRunner: TaskRunner
) {

    @FXML
    lateinit var dockPane: ResizableBorderPane

    @FXML
    lateinit var bottomDock: Dock

    @FXML
    lateinit var leftDock: Dock

    @FXML
    lateinit var rightDock: Dock

    @FXML
    lateinit var viewer: TabPane

    @FXML
    lateinit var menuBar: MenuBar

    @FXML
    lateinit var statusBar: StatusBar

    @FXML
    fun initialize() {
        dockedNodeFactory.createDockNodes().forEach {
            val pane = TitledPane(it.title, it.node)

            val dock = when (it.area) {
                DockingArea.LEFT -> leftDock
                DockingArea.RIGHT -> rightDock
                DockingArea.BOTTOM -> bottomDock
            }

            dock.addItem(pane)
        }

        menuFactory.createMenu().entries.forEach {
            menuBar.menus.add(it)
        }

        taskRunner.tasks.addListener(ListChangeListener {
            statusBar.progressProperty().unbind()

            val activeTask = taskRunner.tasks.first()
            val activeTaskProgress = activeTask.progressProperty()

            statusBar.progressProperty().bind(activeTaskProgress)
        })

        dockPane.bindResizeTarget(Side.LEFT, leftDock.contentAreaProperty)
        dockPane.bindResizeTarget(Side.RIGHT, rightDock.contentAreaProperty)
    }
}
