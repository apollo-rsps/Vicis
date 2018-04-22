package rs.emulate.legacy.config.npc

import rs.emulate.legacy.config.ConfigPropertyMap
import rs.emulate.legacy.config.MutableConfigDefinition
import rs.emulate.legacy.config.SerializableProperty

/**
 * A [MutableConfigDefinition] for an npc.
 */
open class NpcDefinition(id: Int, properties: ConfigPropertyMap) : MutableConfigDefinition(id, properties) {

    val animationSet: SerializableProperty<MovementAnimationSet>
        get() = getProperty(NpcProperty.ANIMATION_SET)

    val clickable: SerializableProperty<Boolean>
        get() = getProperty(NpcProperty.CLICKABLE)

    val colours: SerializableProperty<Map<Int, Int>>
        get() = getProperty(NpcProperty.COLOURS)

    val combatLevel: SerializableProperty<Int>
        get() = getProperty(NpcProperty.COMBAT_LEVEL)

    val description: SerializableProperty<String>
        get() = getProperty(NpcProperty.DESCRIPTION)

    val flatScale: SerializableProperty<Int>
        get() = getProperty(NpcProperty.FLAT_SCALE)

    val headIcon: SerializableProperty<Int>
        get() = getProperty(NpcProperty.HEAD_ICON)

    val heightScale: SerializableProperty<Int>
        get() = getProperty(NpcProperty.HEIGHT_SCALE)

    val idleAnimation: SerializableProperty<Int>
        get() = getProperty(NpcProperty.IDLE_ANIMATION)

    val lightModifier: SerializableProperty<Int>
        get() = getProperty(NpcProperty.LIGHT_MODIFIER)

    val minimapVisible: SerializableProperty<Boolean>
        get() = getProperty(NpcProperty.MINIMAP_VISIBLE)

    val models: SerializableProperty<IntArray>
        get() = getProperty(NpcProperty.MODELS)

    val morphismSet: SerializableProperty<MorphismSet>
        get() = getProperty(NpcProperty.MORPHISM_SET)

    val name: SerializableProperty<String>
        get() = getProperty(NpcProperty.NAME)

    val priorityRender: SerializableProperty<Boolean>
        get() = getProperty(NpcProperty.PRIORITY_RENDER)

    val rotation: SerializableProperty<Int>
        get() = getProperty(NpcProperty.ROTATION)

    val secondaryModels: SerializableProperty<IntArray>
        get() = getProperty(NpcProperty.SECONDARY_MODELS)

    val shadowModifier: SerializableProperty<Int>
        get() = getProperty(NpcProperty.SHADOW_MODIFIER)

    val size: SerializableProperty<Int>
        get() = getProperty(NpcProperty.SIZE)

    val walkingAnimation: SerializableProperty<Int>
        get() = getProperty(NpcProperty.WALKING_ANIMATION)

    companion object {

        /**
         * The name of the archive entry containing the NpcDefinitions, without the extension.
         */
        const val ENTRY_NAME = "npc"

        /**
         * The amount of interactions.
         */
        const val INTERACTION_COUNT = 10

        /**
         * The prefix used for interaction definition properties.
         */
        const val INTERACTION_PROPERTY_PREFIX = "interaction"
    }

}
