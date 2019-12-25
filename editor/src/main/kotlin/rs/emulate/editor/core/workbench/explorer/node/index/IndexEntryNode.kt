package rs.emulate.editor.core.workbench.explorer.node.index

import rs.emulate.editor.core.project.Project
import rs.emulate.editor.core.project.ProjectIndexEntry
import rs.emulate.editor.core.workbench.explorer.WorkbenchExplorerLeaf
import rs.emulate.editor.vfs.ResourceType
import rs.emulate.editor.vfs.VirtualFileId

class IndexEntryNode<V : VirtualFileId>(
    val project: Project<V>,
    val type: ResourceType,
    val entry: ProjectIndexEntry<V>
) : WorkbenchExplorerLeaf() {
    override val title: String
        get() = entry.name
}
