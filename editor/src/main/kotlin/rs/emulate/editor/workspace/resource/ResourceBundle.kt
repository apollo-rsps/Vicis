package rs.emulate.editor.workspace.resource

import rs.emulate.editor.workspace.resource.index.ResourceIndexBuilder
import kotlin.reflect.KClass

interface ResourceBundleLoader {
    fun load(out: MutableList<ResourceBundle<*>>)
}

interface ResourceBundle<T: ResourceId> {
    val idType: KClass<T>

    fun load(id: T): Resource

    fun index(index: ResourceIndexBuilder)

}
