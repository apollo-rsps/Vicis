package rs.emulate.editor.core.workbench

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.stage.Window
import rs.emulate.editor.core.project.Project
import javax.inject.Singleton

@Singleton
class WorkbenchContext {
    val windowProp = SimpleObjectProperty<Window>()
    var window : Window?
        set(value) {
            windowProp.value = value
        }
        get() = windowProp.get()

    val selectionProperty = SimpleObjectProperty<WorkbenchSelection>()
    var selection : WorkbenchSelection?
        get() = selectionProperty.get()
        set(value) {
            selectionProperty.value = value
        }

    val projects = FXCollections.observableArrayList<Project<*>>()

    fun openProject(project: Project<*>) {
        projects.add(project)
    }

    fun closeProject(project: Project<*>) {
        projects.remove(project)
    }


}
