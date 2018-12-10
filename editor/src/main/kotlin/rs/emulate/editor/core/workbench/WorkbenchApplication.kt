package rs.emulate.editor.core.workbench

import com.google.inject.Guice
import javafx.application.Application
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import org.dockfx.DockPane
import rs.emulate.editor.core.CoreModule
import rs.emulate.editor.core.action.ActionModule
import rs.emulate.editor.javafx.JavaFxModule
import rs.emulate.editor.javafx.loader.FxmlLoader
import java.net.URL
import java.net.URLClassLoader
import java.util.logging.Logger
import kotlin.reflect.KClass

class WorkbenchApplication : Application() {

    override fun start(primaryStage: Stage) {
        val workbenchContext = WorkbenchContext()
        workbenchContext.window = primaryStage.owner

        val injector = Guice.createInjector(CoreModule(), WorkbenchModule(workbenchContext), ActionModule(), JavaFxModule())
        val loader = injector.getInstance(FxmlLoader::class.java)
        val workbench = loader.load<Parent>(this.javaClass.getResource("/rs/emulate/editor/core/workbench/Workbench.fxml"))

        primaryStage.scene = Scene(workbench, 800.0, 500.0)
        primaryStage.sizeToScene()

        primaryStage.show()
    }
}

fun main(args: Array<String>) {
    Application.launch(WorkbenchApplication::class.java, *args)
}
