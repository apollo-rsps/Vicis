package rs.emulate.editor.core.workbench.explorer.node.index

import javafx.collections.ObservableList
import rs.emulate.editor.core.workbench.explorer.WorkbenchExplorerLeaf
import rs.emulate.editor.core.workbench.explorer.WorkbenchExplorerNode
import rs.emulate.editor.vfs.index.VirtualFileIndexEntry

class IndexEntryNode(private val entry: VirtualFileIndexEntry<*>) : WorkbenchExplorerLeaf() {
    override val title: String
        get() = entry.name

    override suspend fun bindChildrenTo(dest: ObservableList<WorkbenchExplorerNode>) {}
}
