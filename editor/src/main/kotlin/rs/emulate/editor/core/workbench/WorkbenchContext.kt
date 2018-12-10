package rs.emulate.editor.core.workbench

import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.stage.Window
import rs.emulate.editor.core.project.Project

class WorkbenchContext {
    val activeProjectsProp = SimpleListProperty<Project>()
    val activeProjects : MutableList<Project>
        get() = activeProjectsProp.get()

    val currentProjectProp = SimpleObjectProperty<Project>()
    val currentProject : Project?
        get() = currentProjectProp.get()

    val windowProp = SimpleObjectProperty<Window>()
    var window : Window?
        set(value) {
            windowProp.value = value
        }
        get() = windowProp.get()
}
