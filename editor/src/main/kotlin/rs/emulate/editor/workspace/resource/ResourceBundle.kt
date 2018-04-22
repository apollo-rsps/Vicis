package rs.emulate.editor.workspace.resource

import rs.emulate.editor.workspace.resource.index.ResourceIndexBuilder

interface ResourceBundleLoader {
    fun load(out: MutableList<ResourceBundle>)
}

interface ResourceBundle {

    fun load(id: ResourceId): Resource

    fun index(index: ResourceIndexBuilder)

}
