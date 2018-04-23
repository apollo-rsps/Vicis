package rs.emulate.legacy.config.npc

import rs.emulate.legacy.config.ConfigPropertyMap
import rs.emulate.legacy.config.MutableConfigDefinition
import rs.emulate.legacy.config.SerializableProperty
import rs.emulate.legacy.config.npc.NpcProperty.*

/**
 * A [MutableConfigDefinition] for an npc.
 */
open class NpcDefinition(id: Int, properties: ConfigPropertyMap) : MutableConfigDefinition(id, properties) {

    val animationSet: SerializableProperty<MovementAnimationSet>
        get() = getProperty(AnimationSet)

    val clickable: SerializableProperty<Boolean>
        get() = getProperty(Clickable)

    val colours: SerializableProperty<Map<Int, Int>>
        get() = getProperty(Colours)

    val combatLevel: SerializableProperty<Int>
        get() = getProperty(CombatLevel)

    val description: SerializableProperty<String>
        get() = getProperty(Description)

    val flatScale: SerializableProperty<Int>
        get() = getProperty(FlatScale)

    val headIcon: SerializableProperty<Int>
        get() = getProperty(HeadIcon)

    val heightScale: SerializableProperty<Int>
        get() = getProperty(HeightScale)

    val idleAnimation: SerializableProperty<Int>
        get() = getProperty(IdleAnimation)

    val lightModifier: SerializableProperty<Int>
        get() = getProperty(LightModifier)

    val minimapVisible: SerializableProperty<Boolean>
        get() = getProperty(MinimapVisible)

    val models: SerializableProperty<IntArray>
        get() = getProperty(Models)

    val morphismSet: SerializableProperty<MorphismSet>
        get() = getProperty(Morphisms)

    val name: SerializableProperty<String>
        get() = getProperty(Name)

    val priorityRender: SerializableProperty<Boolean>
        get() = getProperty(PriorityRender)

    val rotation: SerializableProperty<Int>
        get() = getProperty(Rotation)

    val secondaryModels: SerializableProperty<IntArray>
        get() = getProperty(SecondaryModels)

    val shadowModifier: SerializableProperty<Int>
        get() = getProperty(ShadowModifier)

    val size: SerializableProperty<Int>
        get() = getProperty(Size)

    val walkingAnimation: SerializableProperty<Int>
        get() = getProperty(WalkingAnimation)

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
