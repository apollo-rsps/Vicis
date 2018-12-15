package rs.emulate.editor.core.action

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder
import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult
import rs.emulate.editor.core.action.annotation.Action


class WorkbenchActionModule : AbstractModule() {
    override fun configure() {
        val actionBinder = Multibinder.newSetBinder(binder(), WorkbenchAction::class.java)
        val actionClasses: List<Class<out WorkbenchAction>> = ClassGraph()
            .enableAnnotationInfo()
            .enableClassInfo()
            .whitelistPackages("rs.emulate.editor")
            .scan()
            .use(::loadActionClasses)

        actionClasses.forEach { actionBinder.addBinding().to(it) }
    }

    @Suppress("UNCHECKED_CAST")
    private fun loadActionClasses(scanResult: ScanResult): List<Class<out WorkbenchAction>> = scanResult
        .getClassesImplementing(WorkbenchAction::class.qualifiedName)
        .filter { it.hasAnnotation(Action::class.qualifiedName) }
        .loadClasses() as List<Class<out WorkbenchAction>>
}
