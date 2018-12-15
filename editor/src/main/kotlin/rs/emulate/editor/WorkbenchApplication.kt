package rs.emulate.editor

import com.google.inject.Guice
import javafx.application.Application
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import rs.emulate.editor.core.CoreModule
import rs.emulate.editor.core.action.WorkbenchActionModule
import rs.emulate.editor.core.task.TaskModule
import rs.emulate.editor.core.workbench.WorkbenchModule
import rs.emulate.editor.javafx.JavaFxModule
import rs.emulate.editor.javafx.loader.FxmlLoader

class WorkbenchApplication : Application() {

    override fun start(primaryStage: Stage) {
        val injector = Guice.createInjector(
            CoreModule(),
            JavaFxModule(),
            TaskModule(),
            WorkbenchModule(),
            WorkbenchActionModule()
        )

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
