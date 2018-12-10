package rs.emulate.editor.core.action

import com.google.inject.AbstractModule
import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult
import rs.emulate.editor.core.action.annotation.ActionDef
import com.google.inject.multibindings.Multibinder


class ActionModule : AbstractModule() {
    override fun configure() {
        val actionBinder = Multibinder.newSetBinder(binder(), Action::class.java)
        val actionClasses : List<Class<out Action>> = ClassGraph()
            .enableAnnotationInfo()
            .enableClassInfo()
            .whitelistPackages("rs.emulate.editor")
            .scan()
            .use(::loadActionClasses)

        actionClasses.forEach { actionBinder.addBinding().to(it) }
    }

    @Suppress("UNCHECKED_CAST")
    private fun loadActionClasses(scanResult: ScanResult) : List<Class<out Action>> = scanResult
        .getClassesImplementing(Action::class.qualifiedName)
        .filter { it.hasAnnotation(ActionDef::class.qualifiedName) }
        .loadClasses() as List<Class<out Action>>
}
