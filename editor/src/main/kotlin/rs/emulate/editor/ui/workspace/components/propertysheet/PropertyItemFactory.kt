package rs.emulate.editor.ui.workspace.components.propertysheet

import org.controlsfx.control.PropertySheet
import rs.emulate.editor.resource.bundles.legacy.config.ConfigResource

interface PropertyItemFactory<T> {

    fun generate(resource: ConfigResource<T>): List<PropertySheet.Item>

}
