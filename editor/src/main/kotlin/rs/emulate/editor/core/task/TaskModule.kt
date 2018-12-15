package rs.emulate.editor.core.task

import com.google.inject.AbstractModule
import com.google.inject.assistedinject.FactoryModuleBuilder
import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult
import javafx.concurrent.Task
import rs.emulate.editor.core.task.annotations.TaskFactoryFor

class TaskModule : AbstractModule() {
    override fun configure() {
        val taskFactories: List<Class<*>> = ClassGraph()
            .enableAnnotationInfo()
            .enableClassInfo()
            .whitelistPackages("rs.emulate.editor")
            .scan()
            .use(::loadTaskFactoryClasses)

        for (taskFactory in taskFactories) {
            val taskClass = taskFactory.getAnnotation(TaskFactoryFor::class.java).type.java as Class<Task<*>>

            install(
                FactoryModuleBuilder()
                    .implement(taskClass, taskClass)
                    .build(taskFactory)
            )
        }
    }


    @Suppress("UNCHECKED_CAST")
    private fun loadTaskFactoryClasses(scanResult: ScanResult): List<Class<*>> = scanResult
        .getClassesWithAnnotation(TaskFactoryFor::class.qualifiedName)
        .loadClasses() as List<Class<*>>
}
