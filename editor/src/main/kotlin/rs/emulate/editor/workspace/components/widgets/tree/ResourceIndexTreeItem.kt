package rs.emulate.editor.workspace.components.widgets.tree

import rs.emulate.editor.workspace.resource.ResourceId
import rs.emulate.editor.workspace.resource.index.ResourceIndex

sealed class ResourceTreeItem {
    class Root(val items: List<ResourceTreeItem> = emptyList()) : ResourceTreeItem()
    class Category(val name: String, val items: List<ResourceTreeItem>) : ResourceTreeItem()
    class Item(val id: ResourceId, val name: String) : ResourceTreeItem()
}

fun indexMapper(items: ResourceIndex): ResourceTreeItem {
    val categories = items.byType().flatMap { (type, items) ->
        val treeItems : List<ResourceTreeItem> = items.map { ResourceTreeItem.Item(it.id, it.label) }

            listOf(ResourceTreeItem.Category(type ?: "Other", treeItems))
    }

    return ResourceTreeItem.Root(categories)
}
