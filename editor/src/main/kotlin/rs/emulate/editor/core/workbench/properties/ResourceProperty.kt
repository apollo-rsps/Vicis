package rs.emulate.editor.core.workbench.properties

import org.controlsfx.control.PropertySheet

interface ResourceProperty {
    val name: String
    val category: String
    val description: String
}

abstract class ResourcePropertySheetItem<T : ResourceProperty>(val property: T) : PropertySheet.Item {
    override fun getCategory(): String = property.category
    override fun getDescription(): String = property.description
    override fun getName(): String = property.name
}
