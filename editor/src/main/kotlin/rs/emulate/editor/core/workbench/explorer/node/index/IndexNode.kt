package rs.emulate.editor.core.workbench.explorer.node.index

import javafx.collections.ObservableList
import rs.emulate.editor.core.workbench.explorer.WorkbenchExplorerNode
import rs.emulate.editor.vfs.index.VirtualFileIndex

class IndexNode(val index: VirtualFileIndex<*, *>) : WorkbenchExplorerNode {

    override val isLeaf = false

    override val title: String
        get() = index.category.name

    override suspend fun bindChildrenTo(dest: ObservableList<WorkbenchExplorerNode>) {
        for (entry in index.entries) {
            dest.add(IndexEntryNode(entry))
        }
    }
}
