package rs.emulate.editor.workspace.resource.index

import rs.emulate.editor.workspace.resource.ResourceId

data class ResourceIndexEntry(val id: ResourceId, val label: String, val type: String? = null)
