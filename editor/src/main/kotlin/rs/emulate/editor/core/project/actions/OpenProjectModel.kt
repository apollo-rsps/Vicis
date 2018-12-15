package rs.emulate.editor.core.project.actions

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import rs.emulate.editor.core.project.ProjectType

class OpenProjectModel {
    val projectName = SimpleStringProperty()
    val vfsRoot = SimpleStringProperty()
    val vfsType = SimpleObjectProperty<ProjectType>()
}
