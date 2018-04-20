package rs.emulate.legacy.config.animation

import rs.emulate.legacy.config.ConfigPropertyMap
import rs.emulate.legacy.config.MutableConfigDefinition
import rs.emulate.legacy.config.SerializableProperty

/**
 * A [MutableConfigDefinition] for an animation.
 *
 * @param id The id of the definition.
 * @param properties The [ConfigPropertyMap].
 */
open class AnimationDefinition(id: Int, properties: ConfigPropertyMap) : MutableConfigDefinition(id, properties) {

    /**
     * Gets the [SerializableProperty] containing the interleave order of this animation.
     */
    val interleaveOrder: SerializableProperty<IntArray>
        get() = getProperty(AnimationProperty.INTERLEAVE_ORDER)

    /**
     * Gets the [SerializableProperty] containing the animating precedence of this animation.
     */
    fun animatingPrecedence(): SerializableProperty<Int> {
        return getProperty(AnimationProperty.ANIMATING_PRECEDENCE)
    }

    /**
     * Gets the [SerializableProperty] containing the [FrameCollection] of this animation.
     */
    fun frameCollection(): SerializableProperty<FrameCollection> {
        return getProperty(AnimationProperty.FRAMES)
    }

    /**
     * Gets the [SerializableProperty] containing the loop offset of this animation.
     */
    fun loopOffset(): SerializableProperty<Int> {
        return getProperty(AnimationProperty.LOOP_OFFSET)
    }

    /**
     * Gets the [SerializableProperty] containing the maximum loop count of this animation.
     */
    fun maximumLoops(): SerializableProperty<Int> {
        return getProperty(AnimationProperty.MAXIMUM_LOOPS)
    }

    /**
     * Gets the [SerializableProperty] containing the player mainhand id of this animation.
     */
    fun playerMainhand(): SerializableProperty<Int> {
        return getProperty(AnimationProperty.PLAYER_MAINHAND)
    }

    /**
     * Gets the [SerializableProperty] containing the player offhand id of this animation.
     */
    fun playerOffhand(): SerializableProperty<Int> {
        return getProperty(AnimationProperty.PLAYER_OFFHAND)
    }

    /**
     * Gets the [SerializableProperty] containing the priority of this animation.
     */
    fun priority(): SerializableProperty<Int> {
        return getProperty(AnimationProperty.PRIORITY)
    }

    /**
     * Gets the [SerializableProperty] containing the replay mode of this animation.
     */
    fun replayMode(): SerializableProperty<Int> {
        return getProperty(AnimationProperty.REPLAY_MODE)
    }

    /**
     * Gets the [SerializableProperty] containing the stretches property of this animation.
     */
    fun stretches(): SerializableProperty<Boolean> {
        return getProperty(AnimationProperty.STRETCHES)
    }

    /**
     * Gets the [SerializableProperty] containing the walking precedence of this animation.
     */
    fun walkingPrecedence(): SerializableProperty<Int> {
        return getProperty(AnimationProperty.WALKING_PRECEDENCE)
    }

    companion object {

        /**
         * The name of the ArchiveEntry containing the AnimationDefinitions, without the extension.
         */
        const val ENTRY_NAME = "seq"
    }

}
