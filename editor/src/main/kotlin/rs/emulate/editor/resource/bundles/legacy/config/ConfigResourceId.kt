package rs.emulate.editor.resource.bundles.legacy.config

import rs.emulate.editor.resource.ResourceId

/**
 * A [ResourceId] for entries in the config archive.
 */
sealed class ConfigResourceId : ResourceId {
    abstract val id: Int
}

data class NpcResourceId(override val id: Int) : ConfigResourceId()
