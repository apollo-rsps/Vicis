package rs.emulate.editor.core.project

import javafx.collections.ObservableList
import rs.emulate.editor.vfs.VirtualFileId

class ProjectIndex<V : VirtualFileId>(val categories: ObservableList<ProjectIndexCategory<V>>)
