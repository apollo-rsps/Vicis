package rs.emulate.editor.vfs.index

import rs.emulate.editor.vfs.VirtualFileId

interface IndexMetadataStore<T : VirtualFileId> {
    fun nameOf(id: T): String?
}
