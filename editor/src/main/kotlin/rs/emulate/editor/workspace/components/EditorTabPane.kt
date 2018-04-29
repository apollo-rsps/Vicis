package rs.emulate.editor.workspace.components

import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.Priority
import rs.emulate.editor.workspace.components.opengl.GLFragment
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
                tab.properties["resource"] = resource
                tab.setOnClosed {
                    tabs.remove(id)
                }

                root.tabs.add(tab)
                tabs[id] = tab
                tab
            }

            root.selectionModel.select(tab)
        }.subscribe()

        root.selectionModel.selectedItemProperty().onChange {
            it?.let { model.onResourceSelected.onNext(it.properties["resource"] as Resource) }
        }
    }
}
