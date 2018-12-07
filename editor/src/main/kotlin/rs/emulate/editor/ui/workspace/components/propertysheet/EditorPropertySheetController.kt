package rs.emulate.editor.ui.workspace.components.propertysheet

import rs.emulate.editor.resource.bundles.legacy.config.ConfigResource
import rs.emulate.editor.ui.workspace.components.EditorTopController
import rs.emulate.editor.utils.javafx.onGuardedChange

class EditorPropertySheetController : EditorTopController<EditorPropertySheetModel>() {

    override fun bind(model: EditorPropertySheetModel) {
        scope.resourceSelectionProperty.onGuardedChange { resource ->
            if (resource is ConfigResource<*>) { // TODO can we do this better
                model.editorFactory = resource.editorFactory
                model.resourceProperties.setAll(resource.items())
            }
        }
    }

}

