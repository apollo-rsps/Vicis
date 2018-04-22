package rs.emulate.editor.workspace.resource.index

class ResourceIndex(val entries: List<ResourceIndexEntry>) {
    fun byType() = entries.groupBy { it.type }
}

