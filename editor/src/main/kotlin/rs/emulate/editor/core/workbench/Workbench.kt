package rs.emulate.editor.core.workbench

import javafx.fxml.FXML
import javafx.scene.control.MenuBar
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import rs.emulate.editor.core.workbench.docking.DockingArea
import rs.emulate.editor.core.workbench.docking.DockingNodeFactory
import rs.emulate.editor.core.workbench.menu.MenuFactory
import rs.emulate.editor.javafx.loader.FxmlLoader
import javax.inject.Inject

/**
 * The workbench is a container for
 */
class Workbench @Inject constructor(
    val ctx: WorkbenchContext,
    val fxmlLoader: FxmlLoader,
    val dockedNodeFactory: DockingNodeFactory,
    val menuFactory: MenuFactory
) {

    @FXML
    lateinit var borderPane: TabPane

    @FXML
    lateinit var bottomDock: TabPane

    @FXML
    lateinit var leftDock: TabPane

    @FXML
    lateinit var rightDock: TabPane

    @FXML
    lateinit var centerDock: TabPane

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
