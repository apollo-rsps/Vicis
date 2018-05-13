package rs.emulate.editor.ui.workspace.components.explorer

import rs.emulate.editor.resource.ResourceId
import rs.emulate.editor.resource.index.ResourceIndex
import rs.emulate.editor.resource.index.ResourceIndexCategory
import rs.emulate.editor.resource.index.ResourceIndexEntry
import rs.emulate.editor.resource.index.ResourceIndexItem

sealed class ExplorerTreeItem {
    class Category(val name: String, val items: List<ExplorerTreeItem>) : ExplorerTreeItem()
    class Item(val id: ResourceId, val name: String) : ExplorerTreeItem()
}

fun indexMapper(items: ResourceIndex): ExplorerTreeItem {
    val categories = items.entries.map(::mapIndexEntry)
    return ExplorerTreeItem.Category("root", categories)
}

private fun mapIndexEntry(entry: ResourceIndexEntry): ExplorerTreeItem {
    return when (entry) {
        is ResourceIndexItem -> ExplorerTreeItem.Item(entry.id, entry.label)
        is ResourceIndexCategory -> ExplorerTreeItem.Category(entry.name, entry.children.map(::mapIndexEntry))
    }
}
