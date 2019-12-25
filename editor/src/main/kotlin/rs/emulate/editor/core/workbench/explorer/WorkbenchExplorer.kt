package rs.emulate.editor.core.workbench.explorer

import javafx.fxml.FXML
import javafx.scene.control.TreeView
import javafx.scene.input.MouseEvent
import rs.emulate.editor.core.content.actions.OpenContentAction
import rs.emulate.editor.core.workbench.VirtualFileSelection
import rs.emulate.editor.core.workbench.WorkbenchContext
import rs.emulate.editor.core.workbench.explorer.node.ProjectListNode
import rs.emulate.editor.core.workbench.explorer.node.index.IndexEntryNode
import rs.emulate.editor.core.workbench.explorer.tree.ExplorerTreeCellFactory
import rs.emulate.editor.core.workbench.explorer.tree.ExplorerTreeItem
import rs.emulate.editor.utils.javafx.createAsyncEventHandler
import rs.emulate.editor.vfs.VirtualFileId
import javax.inject.Inject

class WorkbenchExplorer @Inject constructor(val ctx: WorkbenchContext, val openAction: OpenContentAction) {

    @FXML
    lateinit var explorerTree: TreeView<WorkbenchExplorerNode>

    @FXML
    fun initialize() {
        explorerTree.root = ExplorerTreeItem(ProjectListNode(ctx.projects))
        explorerTree.cellFactory = ExplorerTreeCellFactory()
        explorerTree.isShowRoot = true
        explorerTree.onMouseClicked = createAsyncEventHandler(this::onMouseClicked)
    }

    suspend fun onMouseClicked(event: MouseEvent) {
        when (event.clickCount) {
            1 -> onItemSelected(event)
            2 -> onItemOpened(event)
        }
    }

    suspend fun onItemSelected(event: MouseEvent) {

    }

    suspend fun onItemOpened(event: MouseEvent) {
        val selection = explorerTree.selectionModel.selectedItems.firstOrNull() ?: return

        when (val node = selection.value) {
            is IndexEntryNode<*> -> ctx.selection = createIndexSelection(node)
        }
    }

    private fun <V : VirtualFileId> createIndexSelection(node: IndexEntryNode<V>): VirtualFileSelection<V> {
        return VirtualFileSelection(node.project, node.entry.vfsId, node.type)
    }

}
