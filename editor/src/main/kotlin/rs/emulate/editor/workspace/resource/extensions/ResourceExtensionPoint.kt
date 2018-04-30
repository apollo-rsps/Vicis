package rs.emulate.editor.workspace.resource.extensions

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner
import rs.emulate.editor.workspace.resource.Resource
import rs.emulate.editor.workspace.resource.extensions.annotations.SupportedResources
import kotlin.collections.forEach
import kotlin.collections.mutableMapOf
import kotlin.collections.set
import kotlin.reflect.KClass

class ResourceExtensionPoint<T : Any>(val type: KClass<T>) {
    private val extensions = mutableMapOf<KClass<out Resource>, T>()

    fun extensionFor(type: KClass<out Resource>): T? = extensions[type]

    fun load() {
        val scanner = FastClasspathScanner()

        scanner.matchClassesImplementing(type.java) {
            val extension = it.newInstance()
            val annotation = it.getAnnotation(SupportedResources::class.java)
                ?: throw IllegalArgumentException("SupportedResources annotation not present on extension")

            annotation.types.forEach { type ->
                extensions[type] = extension
            }
        }

        scanner.scan()
    }
}
