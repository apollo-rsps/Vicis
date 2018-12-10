package rs.emulate.editor.core.workbench

import com.panemu.tiwulfx.control.DetachableTabPane
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.MenuBar
import javafx.scene.control.Tab
import javafx.scene.layout.BorderPane
import rs.emulate.editor.core.action.Action
import rs.emulate.editor.core.workbench.docking.DockingArea
import rs.emulate.editor.core.workbench.docking.DockingComponent
import rs.emulate.editor.core.workbench.docking.WorkbenchDockNodeFactory
import rs.emulate.editor.core.workbench.menu.WorkbenchMenuFactory
import rs.emulate.editor.javafx.loader.FxmlLoader
import tornadofx.add
import javax.inject.Inject

/**
 * The workbench is a container for
 */
class Workbench @Inject constructor(
    val ctx: WorkbenchContext,
    val fxmlLoader: FxmlLoader,
    val dockedNodeFactory: WorkbenchDockNodeFactory,
    val menuFactory: WorkbenchMenuFactory
) {

    @FXML
    lateinit var borderPane: DetachableTabPane

    @FXML
    lateinit var bottomDock: DetachableTabPane

    @FXML
    lateinit var leftDock: DetachableTabPane

    @FXML
    lateinit var rightDock: DetachableTabPane

    @FXML
    lateinit var centerDock: DetachableTabPane

    @FXML
    lateinit var menuBar: MenuBar

    @FXML
    fun initialize() {
        dockedNodeFactory.createDockNodes().forEach {
            when (it.area) {
                DockingArea.LEFT -> leftDock.tabs.add(Tab(it.title, it.node))
                DockingArea.RIGHT -> rightDock.tabs.add(Tab(it.title, it.node))
                DockingArea.BOTTOM -> bottomDock.tabs.add(Tab(it.title, it.node))
            }
        }

        menuFactory.createMenu().entries.forEach {
            menuBar.menus.add(it)
        }
    }
}
