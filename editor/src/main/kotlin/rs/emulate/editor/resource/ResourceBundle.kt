package rs.emulate.editor.resource

import rs.emulate.editor.resource.index.ResourceIndexBuilder
import kotlin.reflect.KClass

interface ResourceBundleLoader {
    fun load(out: MutableList<ResourceBundle<*>>)
}

interface ResourceBundle<T: ResourceId> {
    val idType: KClass<T>

    fun load(id: T): Resource

    fun index(index: ResourceIndexBuilder)

}
