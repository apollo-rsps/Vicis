package rs.emulate.legacy.config.kit

import rs.emulate.legacy.config.ConfigPropertyType

/**
 * A [ConfigPropertyType] implementation for [IdentikitDefinition]s.
 *
 * @param opcode The opcode.
 */
internal enum class IdentikitProperty(override val opcode: Int) : ConfigPropertyType {

    /**
     * The IdentikitProperty that specifies which body part the identikit is for.
     */
    PART(1),

    /**
     * The IdentikitProperty that specifies the body model ids of the identikit.
     */
    MODELS(2),

    /**
     * The IdentikitProperty that specifies whether or not the identikit may be used as a player design style.
     */
    PLAYER_DESIGN_STYLE(3);

}
