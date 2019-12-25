package rs.emulate.editor.core.workbench.explorer.node.index

import javafx.collections.ObservableList
import rs.emulate.editor.core.project.Project
import rs.emulate.editor.core.project.ProjectIndexCategory
import rs.emulate.editor.core.workbench.explorer.WorkbenchExplorerNode
import rs.emulate.editor.utils.javafx.bindWithMapping
import rs.emulate.editor.vfs.VirtualFileId

class IndexNode<V : VirtualFileId>(val project: Project<V>, val index: ProjectIndexCategory<V>) : WorkbenchExplorerNode {

    override val isLeaf = false

    override val title: String
        get() = index.type.name

    override fun bindChildrenTo(dest: ObservableList<WorkbenchExplorerNode>) {
        bindWithMapping(index.entries, dest) { IndexEntryNode(project, index.type, it) }
    }
}
