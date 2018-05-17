package rs.emulate.editor.ui.workspace.components.propertysheet

import javafx.beans.binding.Bindings
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableMap
import org.controlsfx.control.PropertySheet
import rs.emulate.editor.ui.workspace.components.EditorTopView
import rs.emulate.legacy.config.ConfigPropertyType
import rs.emulate.legacy.config.SerializableProperty
import tornadofx.*
import java.util.*
import kotlin.collections.set

class EditorPropertySheet : EditorTopView<EditorPropertySheetController, EditorPropertySheetModel>() {
    override val controller by inject<EditorPropertySheetController>()
    override val model by inject<EditorPropertySheetModel>()

    override val root = PropertySheet().apply {
        title = messages["title"]

        Bindings.bindContent(items, model.resourceProperties)
    }
}
