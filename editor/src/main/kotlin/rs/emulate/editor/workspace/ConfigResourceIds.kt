package rs.emulate.editor.workspace

import rs.emulate.editor.workspace.resource.ResourceId

data class ObjectResourceId(val id: Int, override val name: String?) : ResourceId

data class ItemResourceId(val id: Int, override val name: String?) : ResourceId

data class NpcResourceId(val id: Int, override val name: String?) : ResourceId
