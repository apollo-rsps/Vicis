package rs.emulate.editor.core.workbench.properties


import org.controlsfx.control.PropertySheet

interface PropertyItemFactory<T> {

    fun generate(resource: Any): List<PropertySheet.Item>

}
