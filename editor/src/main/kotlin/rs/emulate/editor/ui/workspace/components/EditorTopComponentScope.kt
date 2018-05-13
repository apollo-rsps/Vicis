package rs.emulate.editor.ui.workspace.components

import javafx.beans.property.SimpleObjectProperty
import rs.emulate.editor.resource.Resource
import rs.emulate.editor.resource.ResourceCache
import rs.emulate.editor.ui.workspace.EditorWorkspaceModel
import tornadofx.*

class EditorTopComponentScope : Scope() {
    val resourceSelectionProperty = SimpleObjectProperty<Resource>()
    var resourceSelection: Resource? by resourceSelectionProperty

    var resourceCache = ResourceCache()

    /**
     * Bind the properties of this [Scope] to the workspace model it exists within.
     */
    fun bind(model: EditorWorkspaceModel) {
        resourceCache = model.cache
    }
}
