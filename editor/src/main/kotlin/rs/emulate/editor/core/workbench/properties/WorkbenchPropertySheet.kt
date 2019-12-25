package rs.emulate.editor.core.workbench.properties

import javafx.fxml.FXML
import org.controlsfx.control.PropertySheet
import rs.emulate.editor.core.content.capabilities.ResourcePropertySupport
import rs.emulate.editor.core.workbench.VirtualFileSelection
import rs.emulate.editor.core.workbench.WorkbenchContext
import rs.emulate.editor.utils.javafx.onChange
import rs.emulate.editor.vfs.ResourceType
import rs.emulate.editor.vfs.VirtualFileId
import javax.inject.Inject

class WorkbenchPropertySheet @Inject constructor(
    private val ctx: WorkbenchContext,
    private val propertySupportMap: @JvmSuppressWildcards Map<ResourceType, ResourcePropertySupport<*, *>>
) {

    @FXML
    lateinit var propertySheet: PropertySheet

    @FXML
    fun initialize() {
        ctx.selectionProperty.onChange {
            when (it) {
                is VirtualFileSelection<*> -> handleFileSelection(it)
                else -> propertySheet.items.clear()
            }
        }
    }

    private fun <V : VirtualFileId> handleFileSelection(selection: VirtualFileSelection<V>) {
        @Suppress("UNCHECKED_CAST")
        val support = propertySupportMap[selection.type] as ResourcePropertySupport<*, V>?

        if (support == null) {
            // TODO unsupported dialog
        } else {
            val properties = support.createProperties(selection.project, selection.vfsId)

            propertySheet.items.setAll(properties)
            propertySheet.propertyEditorFactory = support
        }
    }

}
