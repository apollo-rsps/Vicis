package rs.emulate.editor.core.workbench

import rs.emulate.editor.core.project.Project
import rs.emulate.editor.vfs.ResourceType
import rs.emulate.editor.vfs.VirtualFileId

sealed class WorkbenchSelection

data class VirtualFileSelection<T : VirtualFileId>(
    val project: Project<T>,
    val vfsId: T,
    val type: ResourceType
) : WorkbenchSelection()
