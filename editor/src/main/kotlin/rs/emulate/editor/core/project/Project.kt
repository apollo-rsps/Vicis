package rs.emulate.editor.core.project

import javafx.beans.property.SimpleStringProperty
import rs.emulate.editor.vfs.VirtualFileId
import rs.emulate.editor.vfs.VirtualFileLoader

class Project<V : VirtualFileId>(name: String, val loader: VirtualFileLoader<V>, val index: ProjectIndex<V>) {
    val nameProp = SimpleStringProperty(name)
    var name: String
        get() = nameProp.get()
        set(value) {
            nameProp.set(value)
        }
}
