package rs.emulate.editor.core.workbench.explorer.node.index

import rs.emulate.editor.core.project.Project
import rs.emulate.editor.core.project.ProjectIndexEntry
import rs.emulate.editor.core.workbench.explorer.WorkbenchExplorerLeaf
import rs.emulate.editor.vfs.ResourceType

class IndexEntryNode(val project: Project, val type: ResourceType, val entry: ProjectIndexEntry) : WorkbenchExplorerLeaf() {
    override val title: String
        get() = entry.name
}
