package rs.emulate.editor.core.workbench.properties

import rs.emulate.editor.core.workbench.docking.DockingArea
import rs.emulate.editor.core.workbench.docking.DockingComponent

class WorkbenchPropertySheetComponent : DockingComponent {
    override val title = "propertysheet.title"
    override val fxml = "/rs/emulate/editor/core/workbench/properties/WorkbenchPropertySheet.fxml"
    override val resourceBundle = "rs.emulate.editor.core.workbench.properties.WorkbenchPropertySheet"
    override val area = DockingArea.RIGHT
}
