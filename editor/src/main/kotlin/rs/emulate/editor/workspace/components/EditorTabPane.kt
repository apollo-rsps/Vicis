package rs.emulate.editor.workspace.components

import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.Priority
import rs.emulate.editor.workspace.components.widgets.content.ResourceViewer
import rs.emulate.editor.workspace.resource.Resource
import rs.emulate.editor.workspace.resource.ResourceId
import tornadofx.*
import kotlin.collections.set

const val VIEWER_PROP_KEY = "viewer"
const val RESOURCE_PROP_KEY = "resource"

var Tab.resourceViewer: ResourceViewer?
    get() = properties[VIEWER_PROP_KEY] as? ResourceViewer
    set(v) {
        properties[VIEWER_PROP_KEY] = v
    }

var Tab.resource: Resource?
    get() = properties[RESOURCE_PROP_KEY] as? Resource
    set(v) {
        properties[RESOURCE_PROP_KEY] = v
    }

class EditorTabPane : EditorComponent() {
    override val root: TabPane = with(TabPane()) {
        vboxConstraints { vGrow = Priority.ALWAYS }
    }

    private val tabs = mutableMapOf<ResourceId, Tab>()

    init {
        root.selectionModel.selectedItemProperty().addListener { _, old, new ->
            old?.resourceViewer?.onFocusLost()

            new?.let {
                it.content.requestFocus()
                it.resourceViewer?.onFocusGained()
                it.resource?.let(model.onResourceSelected::onNext)
            }
        }

        model.onResourceSelected.distinctUntilChanged().map { resource ->
            val id = resource.id

            val tab = if (id in tabs) {
                tabs[id]
            } else {
                val tab = Tab(id.toString())
                val viewerExtension = controller.viewerExtensions.extensionFor(resource::class)
                val viewer = viewerExtension?.createView(resource, model.cache)

                if (viewer == null) {
                    tab.content = Label(messages["label.no_viewer_available"]).apply {
                        alignment = Pos.CENTER
                    }
                } else {
                    tab.content = viewer.root
                    tab.resourceViewer = viewer
                    tab.resource = resource
                }

                tab.setOnClosed {
                    tabs.remove(id)
                }

                root.tabs.add(tab)
                tabs[id] = tab
                tab
            }


            root.selectionModel.select(tab)
            tab?.content?.requestFocus()
        }.subscribe()
    }
}
