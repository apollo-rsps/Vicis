package rs.emulate.editor.workspace.resource

import rs.emulate.editor.workspace.resource.index.ResourceIndexBuilder

interface ResourceBundleLoader {
    fun load(out: MutableList<ResourceBundle<*>>)
}

interface ResourceBundle<T: ResourceId> {

    fun load(id: T): Resource

    fun index(index: ResourceIndexBuilder)

}
