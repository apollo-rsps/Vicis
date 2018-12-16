package rs.emulate.editor.core.project

import kotlinx.serialization.Serializable
import rs.emulate.editor.core.project.storage.ProjectStorageComponent

@Serializable
@ProjectStorageComponent(name = "metadata")
class ProjectMetadata {
    var name: String = "<empty>"
    var type: ProjectType = ProjectType.Legacy
}
