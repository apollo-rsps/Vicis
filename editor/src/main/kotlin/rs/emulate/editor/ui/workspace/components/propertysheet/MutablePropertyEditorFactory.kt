package rs.emulate.editor.ui.workspace.components.propertysheet

import org.controlsfx.control.PropertySheet
import org.controlsfx.property.editor.PropertyEditor

abstract class MutablePropertyEditorFactory<P : ListedProperty> : PropertyEditorFactory {

    override fun invoke(item: PropertySheet.Item): PropertyEditor<*>? {
        require(item is MutablePropertyItem<*, *>) {
            "PropertyEditorFactory received PropertyItem that was not a MutablePropertyItem."
        }

        return create(item as MutablePropertyItem<*, P>)
    }

    abstract fun create(item: MutablePropertyItem<*, P>): PropertyEditor<*>? // TODO rename

}
