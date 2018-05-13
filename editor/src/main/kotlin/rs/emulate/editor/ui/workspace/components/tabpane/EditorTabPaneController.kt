package rs.emulate.editor.ui.workspace.components.tabpane

import javafx.scene.control.Label
import javafx.scene.control.Tab
import javafx.scene.text.TextAlignment
import rs.emulate.editor.resource.extensions.ResourceExtensionPoint
import rs.emulate.editor.ui.widgets.content.ResourceViewerExtension
import rs.emulate.editor.ui.workspace.components.EditorTopController
import rs.emulate.editor.utils.javafx.onGuardedChange
import tornadofx.*
import tornadofx.Stylesheet.Companion.tab

class EditorTabPaneController : EditorTopController<EditorTabPaneModel>() {
    val resourceViewerExtensionPoint = ResourceExtensionPoint(ResourceViewerExtension::class)

    override fun bind(model: EditorTabPaneModel) {
        resourceViewerExtensionPoint.load()

        model.selectedTabProperty.onGuardedChange {
            scope.resourceSelection = it?.resource
        }

        scope.resourceSelectionProperty.onGuardedChange { resource ->
            if (resource == null) {
                model.selectedTab = null
                return@onGuardedChange
            }

            val existingTab = model.openTabs.find { it.resource?.id == resource.id }
            val tab = existingTab ?: Tab(resource.id.toString()).also {
                val viewerExtension = resourceViewerExtensionPoint.extensionFor(resource::class)
                val viewer = viewerExtension?.createView(resource, scope.resourceCache)

                if (viewer != null) {
                    it.content = viewer.root
                    it.resourceViewer = viewer
                } else {
                    it.content = Label().apply {
                        text = messages["label.no_viewer_available"]
                        textAlignment = TextAlignment.CENTER
                    }
                }

                it.resource = resource
                model.openTabs += it
            }

            model.selectedTab = tab
        }
    }
}
