package rs.emulate.editor.workspace.resource.bundles.legacy.config

import rs.emulate.editor.workspace.resource.ResourceId

/**
 * A [ResourceId] for entries in the config archive.
 */
sealed class ConfigResourceId : ResourceId {
    abstract val id: Int
}

data class AnimationResourceId(override val id: Int) : ConfigResourceId()
data class FloorResourceId(override val id: Int) : ConfigResourceId()
data class GraphicResourceId(override val id: Int) : ConfigResourceId()
data class ItemResourceId(override val id: Int) : ConfigResourceId()
data class IdentikitResourceId(override val id: Int) : ConfigResourceId()
data class NpcResourceId(override val id: Int) : ConfigResourceId()
data class ObjectResourceId(override val id: Int) : ConfigResourceId()
