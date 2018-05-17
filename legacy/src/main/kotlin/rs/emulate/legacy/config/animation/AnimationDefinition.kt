package rs.emulate.legacy.config.animation

import rs.emulate.legacy.config.ConfigPropertyMap
import rs.emulate.legacy.config.MutableConfigDefinition
import rs.emulate.legacy.config.SerializableProperty

/**
 * A [MutableConfigDefinition] for an animation.
 */
open class AnimationDefinition(id: Int, properties: ConfigPropertyMap) : MutableConfigDefinition(id, properties) {

    val interleaveOrder: SerializableProperty<ByteArray>
        get() = getProperty(AnimationProperty.InterleaveOrder)

    val animatingPrecedence: SerializableProperty<Int>
        get() = getProperty(AnimationProperty.AnimatingPrecedence)

    val frameCollection: SerializableProperty<FrameCollection>
        get() = getProperty(AnimationProperty.Frames)

    val loopOffset: SerializableProperty<Int>
        get() = getProperty(AnimationProperty.LoopOffset)

    val maximumLoops: SerializableProperty<Int>
        get() = getProperty(AnimationProperty.MaximumLoops)

    val characterMainhand: SerializableProperty<Int>
        get() = getProperty(AnimationProperty.CharacterMainhand)

    val characterOffhand: SerializableProperty<Int>
        get() = getProperty(AnimationProperty.CharacterOffhand)

    val priority: SerializableProperty<Int>
        get() = getProperty(AnimationProperty.Priority)

    val replayMode: SerializableProperty<Int>
        get() = getProperty(AnimationProperty.ReplayMode)

    val stretches: SerializableProperty<Boolean>
        get() = getProperty(AnimationProperty.Stretches)

    val walkingPrecedence: SerializableProperty<Int>
        get() = getProperty(AnimationProperty.WalkingPrecedence)

    companion object {

        /**
         * The name of the ArchiveEntry containing the AnimationDefinitions, without the extension.
         */
        const val ENTRY_NAME = "seq"
    }

}
