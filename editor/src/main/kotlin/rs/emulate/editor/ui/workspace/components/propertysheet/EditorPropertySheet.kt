package rs.emulate.editor.ui.workspace.components.propertysheet

import javafx.beans.binding.Bindings
import org.controlsfx.control.PropertySheet
import rs.emulate.editor.ui.workspace.components.EditorTopView
import tornadofx.*

class EditorPropertySheet : EditorTopView<EditorPropertySheetController, EditorPropertySheetModel>() {
    override val controller by inject<EditorPropertySheetController>()
    override val model by inject<EditorPropertySheetModel>()

    override val root = PropertySheet().apply {
        title = messages["title"]

        Bindings.bindContent(items, model.resourceProperties)
        setPropertyEditorFactory(model.editorFactory)

        // FIXME must _not_ be a method reference because of a kotlinc bug
        model.addEditorFactoryCallback { setPropertyEditorFactory(it) }
    }
}
