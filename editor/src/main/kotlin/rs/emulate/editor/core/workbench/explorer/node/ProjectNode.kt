package rs.emulate.editor.core.workbench.explorer.node

import javafx.collections.ObservableList
import rs.emulate.editor.core.project.Project
import rs.emulate.editor.core.workbench.explorer.WorkbenchExplorerNode
import rs.emulate.editor.core.workbench.explorer.node.index.IndexNode
import rs.emulate.editor.utils.javafx.bindWithMapping
import rs.emulate.editor.vfs.VirtualFileId

class ProjectNode<V : VirtualFileId>(val project: Project<V>) : WorkbenchExplorerNode {

    override val isLeaf = false

    override val title: String
        get() = project.name

    override fun bindChildrenTo(dest: ObservableList<WorkbenchExplorerNode>) {
        bindWithMapping(project.index.categories, dest) { IndexNode(project, it) }
    }
}
