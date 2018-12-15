package rs.emulate.editor.core.workbench.explorer.tree

import javafx.collections.FXCollections
import javafx.scene.control.TreeItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import rs.emulate.editor.core.workbench.explorer.WorkbenchExplorerNode
import rs.emulate.editor.utils.javafx.bindWithMapping

class ExplorerTreeItem(val node: WorkbenchExplorerNode) : TreeItem<WorkbenchExplorerNode>(node) {

    private val childNodes = FXCollections.observableArrayList<WorkbenchExplorerNode>()

    init {
        bindWithMapping(childNodes, children) { ExplorerTreeItem(it) }

        GlobalScope.launch(Dispatchers.Main) {
            node.bindChildrenTo(childNodes)
        }
    }

    override fun isLeaf(): Boolean {
        return node.isLeaf
    }

}
