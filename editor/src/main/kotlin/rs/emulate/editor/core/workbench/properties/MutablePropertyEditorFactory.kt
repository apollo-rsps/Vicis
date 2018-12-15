package rs.emulate.editor.core.workbench.properties

import javafx.util.Callback
import org.controlsfx.control.PropertySheet
import org.controlsfx.property.editor.PropertyEditor

typealias PropertyEditorFactory = Callback<PropertySheet.Item, PropertyEditor<*>>

abstract class MutablePropertyEditorFactory<P : ListedProperty> : PropertyEditorFactory {
    override fun call(item: PropertySheet.Item): PropertyEditor<*> {

        require(item is MutablePropertyItem<*, *>) {
            "PropertyEditorFactory received PropertyItem that was not a MutablePropertyItem."
        }

        return create(item as MutablePropertyItem<*, P>)
    }

    abstract fun create(item: MutablePropertyItem<*, P>): PropertyEditor<*> // TODO rename

}
