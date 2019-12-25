package rs.emulate.editor.core.project

import javafx.collections.ObservableList
import rs.emulate.editor.vfs.ResourceType
import rs.emulate.editor.vfs.VirtualFileId

class ProjectIndexCategory<V : VirtualFileId>(val type: ResourceType, val entries: ObservableList<ProjectIndexEntry<V>>)
