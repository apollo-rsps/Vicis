package rs.emulate.editor.workspace.resource.index

import rs.emulate.editor.workspace.resource.ResourceId

class ResourceIndexBuilder {
    val entries = mutableListOf<ResourceIndexEntry>()

    fun entry(callback: ResourceIndexEntryBuilder.() -> Unit) {
        val builder = ResourceIndexEntryBuilder()
        builder.callback()

        entries.add(builder.build())
    }

    fun build() = ResourceIndex(entries)
}

class ResourceIndexEntryBuilder {
    var type: String? = null
    var id: ResourceId? = null
    var label: String? = null

    fun build() = ResourceIndexEntry(
        requireNotNull(id) { "Must provide a ResourceId." },
        requireNotNull(label) { "Must provide a label" },
        type
    )
}
