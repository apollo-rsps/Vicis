package rs.emulate.editor.core.project

import rs.emulate.editor.vfs.VirtualFileId

class ProjectIndexEntry<V : VirtualFileId>(val vfsId: V, val name: String)
