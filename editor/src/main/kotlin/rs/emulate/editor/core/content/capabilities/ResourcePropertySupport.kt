package rs.emulate.editor.core.content.capabilities

import javafx.util.Callback
import org.controlsfx.control.PropertySheet
import org.controlsfx.property.editor.PropertyEditor
import rs.emulate.editor.core.project.Project
import rs.emulate.editor.core.workbench.properties.ResourceProperty
import rs.emulate.editor.core.workbench.properties.ResourcePropertySheetItem
import rs.emulate.editor.vfs.VirtualFileId

interface ResourcePropertySupport<T : ResourceProperty, V : VirtualFileId> : Callback<PropertySheet.Item, PropertyEditor<*>> {
    fun createProperties(project: Project<V>, id: V): List<PropertySheet.Item>

    override fun call(param: PropertySheet.Item): PropertyEditor<*> {
        if (param is ResourcePropertySheetItem<*>) {
            val editor = param.propertyEditorClass

            val constructor = checkNotNull(editor.constructors.firstOrNull()) {
                "PropertyEditor type $editor must have a constructor that takes a PropertySheet.Item"
            }

            return constructor.call(param)
        } else {
            throw UnsupportedOperationException("Unrecognised PropertySheet.Item type ${param::class.simpleName}.")
        }
    }
}
