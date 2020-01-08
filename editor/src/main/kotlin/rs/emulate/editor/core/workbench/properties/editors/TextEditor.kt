package rs.emulate.editor.core.workbench.properties.editors

import javafx.beans.property.StringProperty
import javafx.scene.control.TextField
import org.controlsfx.control.PropertySheet
import org.controlsfx.property.editor.AbstractPropertyEditor

class TextEditor(item: PropertySheet.Item) : AbstractPropertyEditor<String, TextField>(item, TextField()) {

    init {
        enableAutoSelectAll(editor)
    }

    override fun getObservableValue(): StringProperty {
        return editor.textProperty()
    }

    override fun setValue(value: String?) {
        editor.text = value
    }

}
