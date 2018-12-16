package rs.emulate.editor.core.workbench.properties

import javafx.fxml.FXML
import org.controlsfx.control.PropertySheet
import rs.emulate.editor.core.content.capabilities.ResourcePropertySupport
import rs.emulate.editor.core.workbench.VirtualFileSelection
import rs.emulate.editor.core.workbench.WorkbenchContext
import rs.emulate.editor.utils.javafx.onChange
import rs.emulate.editor.vfs.ResourceType
import javax.inject.Inject

class WorkbenchPropertySheet @Inject constructor(
    val ctx: WorkbenchContext,
    val propertySupportMap: @JvmSuppressWildcards Map<ResourceType, ResourcePropertySupport>
) {

    @FXML
    lateinit var propertySheet: PropertySheet

    @FXML
    fun initialize() {
        ctx.selectionProperty.onChange {
            when (it) {
                is VirtualFileSelection -> {
                    propertySheet.items.setAll(
                        propertySupportMap[it.type]?.createProperties(
                            it.project.loader,
                            it.vfsId
                        )
                    )
                }
                else -> propertySheet.items.clear()
            }
        }
    }
}
