package rs.emulate.editor.core.workbench.explorer.node.index

import rs.emulate.editor.core.project.ProjectIndexEntry
import rs.emulate.editor.core.workbench.explorer.WorkbenchExplorerLeaf

class IndexEntryNode(private val entry: ProjectIndexEntry) : WorkbenchExplorerLeaf() {
    override val title: String
        get() = entry.name
}
