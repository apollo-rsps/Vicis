package rs.emulate.editor.core.project

import rs.emulate.editor.vfs.ResourceType
import rs.emulate.editor.vfs.VirtualFileId
import rs.emulate.editor.vfs.index.VirtualFileIndexer

interface Project<T : VirtualFileId, R : ResourceType> {
    val name: String

    fun createIndexer(): VirtualFileIndexer<out VirtualFileId, out ResourceType>
}
