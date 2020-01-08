package rs.emulate.editor.core.workbench.properties.editors

import javafx.beans.property.BooleanProperty
import javafx.scene.control.CheckBox
import org.controlsfx.control.PropertySheet
import org.controlsfx.property.editor.AbstractPropertyEditor

class CheckEditor(item: PropertySheet.Item) : AbstractPropertyEditor<Boolean, CheckBox>(item, CheckBox()) {

    override fun getObservableValue(): BooleanProperty {
        return editor.selectedProperty()
    }

    override fun setValue(value: Boolean) {
        editor.isSelected = value
    }

}
