package rs.emulate.editor.core.workbench.explorer

import javafx.collections.ObservableList

interface WorkbenchExplorerNode {

    val title: String

    val isLeaf: Boolean

    suspend fun bindChildrenTo(dest: ObservableList<WorkbenchExplorerNode>)
}

abstract class WorkbenchExplorerLeaf : WorkbenchExplorerNode {

    override val isLeaf = true

    override suspend fun bindChildrenTo(dest: ObservableList<WorkbenchExplorerNode>) {}
}
