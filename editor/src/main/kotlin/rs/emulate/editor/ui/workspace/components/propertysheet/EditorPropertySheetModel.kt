package rs.emulate.editor.ui.workspace.components.propertysheet

import javafx.collections.FXCollections
import org.controlsfx.control.PropertySheet
import tornadofx.*

class EditorPropertySheetModel : ViewModel() {
    val resourceProperties = FXCollections.observableArrayList<PropertySheet.Item>()
}
