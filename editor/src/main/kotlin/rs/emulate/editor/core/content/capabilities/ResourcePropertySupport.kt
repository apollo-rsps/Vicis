package rs.emulate.editor.core.content.capabilities

import javafx.util.Callback
import org.controlsfx.control.PropertySheet
import org.controlsfx.property.editor.PropertyEditor
import rs.emulate.editor.core.project.Project
import rs.emulate.editor.core.workbench.properties.ResourceProperty
import rs.emulate.editor.core.workbench.properties.ResourcePropertySheetItem
import rs.emulate.editor.vfs.VirtualFileId

interface ResourcePropertySupport<T : ResourceProperty, V : VirtualFileId>
    : Callback<PropertySheet.Item, PropertyEditor<*>> {

    fun createEditor(item: ResourcePropertySheetItem<T>): PropertyEditor<*>?

    fun createProperties(project: Project<V>, id: V): List<PropertySheet.Item>

    override fun call(param: PropertySheet.Item?): PropertyEditor<*>? {
        return if (param is ResourcePropertySheetItem<*>) {
            @Suppress("UNCHECKED_CAST")
            createEditor(param as ResourcePropertySheetItem<T>)
        } else {
            null
        }
    }

}
