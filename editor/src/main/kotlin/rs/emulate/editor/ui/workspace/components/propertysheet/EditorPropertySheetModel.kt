package rs.emulate.editor.ui.workspace.components.propertysheet

import javafx.collections.FXCollections
import org.controlsfx.control.PropertySheet
import org.controlsfx.property.editor.Editors
import org.controlsfx.property.editor.PropertyEditor
import tornadofx.*

typealias PropertyEditorFactory = (PropertySheet.Item) -> PropertyEditor<*>?

class EditorPropertySheetModel : ViewModel() {
    val resourceProperties = FXCollections.observableArrayList<PropertySheet.Item>()!!

    private val editorFactoryCallbacks = mutableListOf<(PropertyEditorFactory) -> Unit>()

    var editorFactory: PropertyEditorFactory = Editors::createTextEditor
        set(factory) {
            field = factory
            editorFactoryCallbacks.forEach { it(factory) }
        }

    fun addEditorFactoryCallback(callback: (PropertyEditorFactory) -> Unit) {
        editorFactoryCallbacks += callback
    }

}
