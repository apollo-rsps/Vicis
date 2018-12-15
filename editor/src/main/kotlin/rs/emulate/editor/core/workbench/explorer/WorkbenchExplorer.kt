package rs.emulate.editor.core.workbench.explorer

import javafx.fxml.FXML
import javafx.scene.control.TreeView
import rs.emulate.editor.core.content.actions.OpenContentAction
import rs.emulate.editor.core.workbench.WorkbenchContext
import rs.emulate.editor.core.workbench.explorer.node.ProjectListNode
import rs.emulate.editor.core.workbench.explorer.tree.ExplorerTreeCellFactory
import rs.emulate.editor.core.workbench.explorer.tree.ExplorerTreeItem
import javax.inject.Inject

class WorkbenchExplorer @Inject constructor(val ctx: WorkbenchContext, val openAction: OpenContentAction) {

    @FXML
    lateinit var explorerTree: TreeView<WorkbenchExplorerNode>

    @FXML
    fun initialize() {
        explorerTree.root = ExplorerTreeItem(ProjectListNode(ctx.projects))
        explorerTree.cellFactory = ExplorerTreeCellFactory()
        explorerTree.isShowRoot = true
    }

}
