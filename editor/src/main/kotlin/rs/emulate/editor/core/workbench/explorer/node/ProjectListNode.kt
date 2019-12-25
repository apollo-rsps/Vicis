package rs.emulate.editor.core.workbench.explorer.node

import javafx.collections.ObservableList
import rs.emulate.editor.core.project.Project
import rs.emulate.editor.core.workbench.explorer.WorkbenchExplorerNode
import rs.emulate.editor.utils.javafx.bindWithMapping

/**
 * An explorer node that expands to the list of projects currently loaded into the [Workbench].
 * This is usually the root node.
 */
class ProjectListNode(private val projects: ObservableList<Project<*>>) : WorkbenchExplorerNode {

    override val isLeaf = false

    override val title: String
        get() = "Projects"

    override fun bindChildrenTo(dest: ObservableList<WorkbenchExplorerNode>) {
        bindWithMapping(projects, dest) { ProjectNode(it) }
    }
}
