package rs.emulate.editor.core.workbench.explorer

import rs.emulate.editor.core.workbench.docking.DockingArea
import rs.emulate.editor.core.workbench.docking.DockingComponent

class WorkbenchExplorerDockingComponent : DockingComponent {
    override val title = "explorer.title"
    override val fxml = "/rs/emulate/editor/core/workbench/explorer/WorkbenchExplorerView.fxml"
    override val area = DockingArea.LEFT
}
