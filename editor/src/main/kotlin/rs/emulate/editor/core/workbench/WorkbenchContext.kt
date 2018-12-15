package rs.emulate.editor.core.workbench

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.stage.Window
import rs.emulate.editor.core.project.Project
import rs.emulate.editor.vfs.ResourceType
import rs.emulate.editor.vfs.VirtualFileId
import javax.inject.Singleton

@Singleton
class WorkbenchContext {
    val windowProp = SimpleObjectProperty<Window>()
    var window : Window?
        set(value) {
            windowProp.value = value
        }
        get() = windowProp.get()

    val projects = FXCollections.observableArrayList<Project<out VirtualFileId, out ResourceType>>()

    fun openProject(project: Project<out VirtualFileId, out ResourceType>) {
        projects.add(project)
    }

    fun closeProject(project: Project<out VirtualFileId, out ResourceType>) {
        projects.remove(project)
    }

    init {

    }
}
