package rs.emulate.editor.workspace.components

import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import rs.emulate.editor.workspace.resource.ResourceId
import tornadofx.tab

class EditorTabPane : EditorComponent() {
    override val root: TabPane = TabPane()

    private val tabs = mutableMapOf<ResourceId, Tab>()

    init {
        model.onResourceSelected.map { resource ->
            val id = resource.id
            // TODO fire onResourceSelected event when selected, to update the other components

            val tab = if (id in tabs) {
                tabs[id]
            } else {
                root.tab(id.toString()) {
                    setOnClosed { tabs.remove(id) }
                }.also {
                    tabs[id] = it
                }
            }

            root.selectionModel.select(tab)
        }.subscribe()
    }
}
