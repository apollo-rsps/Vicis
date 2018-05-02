package rs.emulate.editor.workspace.components

import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.Priority
import rs.emulate.editor.workspace.components.widgets.content.ResourceViewer
import rs.emulate.editor.workspace.resource.Resource
import rs.emulate.editor.workspace.resource.ResourceId
import tornadofx.*

class EditorTabPane : EditorComponent() {
    override val root: TabPane = with(TabPane()) {
        vboxConstraints { vGrow = Priority.ALWAYS }
    }

    private val tabs = mutableMapOf<ResourceId, Tab>()

    init {
        model.onResourceSelected.distinctUntilChanged().map { resource ->
            val id = resource.id

            val tab = if (id in tabs) {
                tabs[id]
            } else {
                val tab = Tab(id.toString())
                val tabContentExtension = controller.viewerExtensions.extensionFor(resource::class)
                val tabContent = tabContentExtension?.createView(resource)

                tab.content = tabContent?.root
                tab.content.isFocusTraversable = true
                tab.content.setOnMouseClicked {
                    tab.content.requestFocus()
                }

                tab.properties["viewer"] = tabContent
                tab.properties["resource"] = resource
                tab.setOnClosed {
                    tabContent?.onClose()
                    tabs.remove(id)
                }

                root.tabs.add(tab)
                tabs[id] = tab
                tabContent?.onOpen()
                tab
            }


            root.selectionModel.select(tab)
            tab?.content?.requestFocus()
        }.subscribe()

        root.selectionModel.selectedItemProperty().addListener { _, old, new ->
            old?.let {
                val viewer = it.properties["viewer"] as ResourceViewer?
                viewer?.onFocusLost()
            }
            new?.let {
                val viewer = it.properties["viewer"] as ResourceViewer?
                viewer?.onFocus()

                model.onResourceSelected.onNext(it.properties["resource"] as Resource)
            }
        }
    }
}
