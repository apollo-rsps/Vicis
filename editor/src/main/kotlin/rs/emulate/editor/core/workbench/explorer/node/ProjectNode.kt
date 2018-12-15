package rs.emulate.editor.core.workbench.explorer.node

import javafx.collections.ObservableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.emulate.editor.core.project.Project
import rs.emulate.editor.core.workbench.explorer.WorkbenchExplorerNode
import rs.emulate.editor.core.workbench.explorer.node.index.IndexNode

class ProjectNode(val project: Project<*, *>) : WorkbenchExplorerNode {

    override val isLeaf = false

    override val title: String
        get() = project.name

    override suspend fun bindChildrenTo(dest: ObservableList<WorkbenchExplorerNode>) {
        GlobalScope.launch(Dispatchers.IO) {
            val indexer = project.createIndexer()
            val index = indexer.index()

            // @TODO - Change tracking.
            withContext(Dispatchers.Main) {
                index.forEach { dest.add(IndexNode(it)) }
            }
        }
    }
}
