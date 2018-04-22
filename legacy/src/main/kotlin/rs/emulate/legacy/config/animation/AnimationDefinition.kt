package rs.emulate.legacy.config.animation

import rs.emulate.legacy.config.ConfigPropertyMap
import rs.emulate.legacy.config.MutableConfigDefinition
import rs.emulate.legacy.config.SerializableProperty

/**
 * A [MutableConfigDefinition] for an animation.
 */
open class AnimationDefinition(id: Int, properties: ConfigPropertyMap) : MutableConfigDefinition(id, properties) {

    val interleaveOrder: SerializableProperty<IntArray>
        get() = getProperty(AnimationProperty.INTERLEAVE_ORDER)

    val animatingPrecedence: SerializableProperty<Int>
        get() = getProperty(AnimationProperty.ANIMATING_PRECEDENCE)

    val frameCollection: SerializableProperty<FrameCollection>
        get() = getProperty(AnimationProperty.FRAMES)

    val loopOffset: SerializableProperty<Int>
        get() = getProperty(AnimationProperty.LOOP_OFFSET)

    val maximumLoops: SerializableProperty<Int>
        get() = getProperty(AnimationProperty.MAXIMUM_LOOPS)

    val playerMainhand: SerializableProperty<Int>
        get() = getProperty(AnimationProperty.PLAYER_MAINHAND)

    val playerOffhand: SerializableProperty<Int>
        get() = getProperty(AnimationProperty.PLAYER_OFFHAND)

    val priority: SerializableProperty<Int>
        get() = getProperty(AnimationProperty.PRIORITY)

    val replayMode: SerializableProperty<Int>
        get() = getProperty(AnimationProperty.REPLAY_MODE)

    val stretches: SerializableProperty<Boolean>
        get() = getProperty(AnimationProperty.STRETCHES)

    val walkingPrecedence: SerializableProperty<Int>
        get() = getProperty(AnimationProperty.WALKING_PRECEDENCE)

    companion object {

        /**
         * The name of the ArchiveEntry containing the AnimationDefinitions, without the extension.
         */
        const val ENTRY_NAME = "seq"
    }

}
