package rs.emulate.editor.core.workbench.docking

import javafx.scene.Node
import rs.emulate.editor.javafx.loader.FxmlLoader
import javax.inject.Inject

class DockingNodeFactory @Inject constructor(
    private val fxmlLoader: FxmlLoader,
    private val dockingComponents: @JvmSuppressWildcards Set<DockingComponent>
) {
    fun createDockNodes() = dockingComponents
        .map {
            val component = fxmlLoader.load<Node>(this.javaClass.getResource(it.fxml))
            val node = DockingNode(component, it.area, it.title)

            node
        }
}
