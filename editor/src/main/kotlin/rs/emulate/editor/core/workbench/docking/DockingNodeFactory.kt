package rs.emulate.editor.core.workbench.docking

import javafx.scene.Node
import rs.emulate.editor.javafx.loader.FxmlLoader
import java.util.*
import javax.inject.Inject

class DockingNodeFactory @Inject constructor(
    private val fxmlLoader: FxmlLoader,
    private val dockingComponents: @JvmSuppressWildcards Set<DockingComponent>
) {
    fun createDockNodes() = dockingComponents
        .map {
            val resources = ResourceBundle.getBundle(it.resourceBundle)
            val component = fxmlLoader.load<Node>(it.fxml, resources)
            val node = DockingNode(component, it.area, resources.getString(it.title))

            node
        }
}
