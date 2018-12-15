package rs.emulate.editor.core.workbench.explorer.tree

import javafx.scene.control.TreeCell
import javafx.scene.control.TreeView
import javafx.scene.paint.Color
import javafx.util.Callback
import org.kordamp.ikonli.foundation.Foundation
import org.kordamp.ikonli.javafx.FontIcon
import rs.emulate.editor.core.workbench.explorer.WorkbenchExplorerNode


class ExplorerTreeCellFactory : Callback<TreeView<WorkbenchExplorerNode>, TreeCell<WorkbenchExplorerNode>> {
    override fun call(param: TreeView<WorkbenchExplorerNode>?): TreeCell<WorkbenchExplorerNode> {
        return object : TreeCell<WorkbenchExplorerNode>() {
            override fun updateItem(item: WorkbenchExplorerNode?, empty: Boolean) {
                super.updateItem(item, empty)

                if (empty) {
                    text = null
                    graphic = null
                    return
                }

                graphic = item?.isLeaf?.let { leaf -> if (leaf) null else FontIcon(Foundation.FOLDER) }
                text = item?.title
            }
        }
    }

}
