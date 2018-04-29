package rs.emulate.editor.workspace.resource.index

import rs.emulate.editor.workspace.resource.ResourceId

class ResourceIndexBuilder {
    private val entries = mutableListOf<ResourceIndexEntry>()

    fun category(name: String, builder: ResourceIndexEntryBuilder.() -> Unit) {
        val nested = ResourceIndexEntryBuilder().apply(builder).build()
        entries += ResourceIndexCategory(name, nested)
    }

    fun build(): ResourceIndex {
        val entries = merge(entries)
        return ResourceIndex(entries)
    }

    private fun merge(entries: List<ResourceIndexEntry>): List<ResourceIndexEntry> {
        val items = entries.filterIsInstance<ResourceIndexItem>()

        return items + entries.asSequence()
            .filterIsInstance<ResourceIndexCategory>()
            .groupBy(ResourceIndexCategory::name)
            .map { (name, categories) ->
                val flattened = categories.flatMap(ResourceIndexCategory::children).let(::merge)
                ResourceIndexCategory(name, flattened)
            }
    }
}

class ResourceIndexEntryBuilder {

    private val children = mutableListOf<ResourceIndexEntry>()

    fun category(name: String, builder: ResourceIndexEntryBuilder.() -> Unit) {
        val nested = ResourceIndexEntryBuilder().apply(builder).build()
        children += ResourceIndexCategory(name, nested)
    }

    fun item(builder: ResourceIndexItemBuilder.() -> Unit) {
        children += ResourceIndexItemBuilder().apply(builder).build()
    }

    fun build(): List<ResourceIndexEntry> = children
}

class ResourceIndexItemBuilder {
    lateinit var id: ResourceId
    lateinit var label: String

    fun build(): ResourceIndexItem = ResourceIndexItem(id, label)
}
