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
    private val ctx: WorkbenchContext,
    private val propertySupportMap: @JvmSuppressWildcards Map<ResourceType, ResourcePropertySupport<*>>
) {

    @FXML
    lateinit var propertySheet: PropertySheet

    @FXML
    fun initialize() {
        ctx.selectionProperty.onChange {
            when (it) {
                is VirtualFileSelection -> {
                    val support = propertySupportMap[it.type]!!

                    propertySheet.items.setAll(support.createProperties(it.project, it.vfsId))
                    propertySheet.propertyEditorFactory = support
                }
                else -> propertySheet.items.clear()
            }
        }
    }
}
