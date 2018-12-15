package rs.emulate.editor.core.project

import rs.emulate.editor.vfs.VirtualFileId
import rs.emulate.editor.vfs.index.IndexMetadataStore

class ProjectMetadataStore<T : VirtualFileId> : IndexMetadataStore<T> {
    override fun nameOf(id: T): String? {
        return null
    }

}
