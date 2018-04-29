package rs.emulate.editor.workspace.components.widgets.tree

import rs.emulate.editor.workspace.resource.ResourceId
import rs.emulate.editor.workspace.resource.index.ResourceIndex
import rs.emulate.editor.workspace.resource.index.ResourceIndexCategory
import rs.emulate.editor.workspace.resource.index.ResourceIndexEntry
import rs.emulate.editor.workspace.resource.index.ResourceIndexItem

sealed class ResourceTreeItem {
    class Category(val name: String, val items: List<ResourceTreeItem>) : ResourceTreeItem()
    class Item(val id: ResourceId, val name: String) : ResourceTreeItem()
}

fun indexMapper(items: ResourceIndex): ResourceTreeItem {
    val categories = items.entries.map(::mapIndexEntry)
    return ResourceTreeItem.Category("root", categories)
}

private fun mapIndexEntry(entry: ResourceIndexEntry): ResourceTreeItem {
    return when (entry) {
        is ResourceIndexItem -> ResourceTreeItem.Item(entry.id, entry.label)
        is ResourceIndexCategory -> ResourceTreeItem.Category(entry.name, entry.children.map(::mapIndexEntry))
    }
}
