package rs.emulate.editor.core.workbench.properties

import javafx.fxml.FXML
import org.controlsfx.control.PropertySheet
import rs.emulate.editor.core.workbench.WorkbenchContext
import javax.inject.Inject

class WorkbenchPropertySheet @Inject constructor(val ctx: WorkbenchContext) {

    @FXML
    lateinit var propertySheet: PropertySheet

}
