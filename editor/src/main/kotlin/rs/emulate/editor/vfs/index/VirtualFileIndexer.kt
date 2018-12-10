package rs.emulate.editor.vfs.index

import rs.emulate.editor.vfs.ResourceType
import rs.emulate.editor.vfs.VirtualFileId

interface VirtualFileIndexer<T : VirtualFileId, R : ResourceType> {
    fun index(): List<VirtualFileIndex<T, R>>
}
