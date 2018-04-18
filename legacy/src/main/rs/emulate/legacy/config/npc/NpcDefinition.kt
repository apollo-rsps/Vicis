package rs.emulate.legacy.config.npc

import rs.emulate.legacy.config.ConfigPropertyMap
import rs.emulate.legacy.config.MutableConfigDefinition
import rs.emulate.legacy.config.SerializableProperty

/**
 * A [MutableConfigDefinition] for an npc.
 *
 * @param id The id of the npc.
 * @param properties The [ConfigPropertyMap].
 */
open class NpcDefinition(id: Int, properties: ConfigPropertyMap) : MutableConfigDefinition(id, properties) {

    /**
     * Gets the [SerializableProperty] containing the animation set.
     */
    fun animationSet(): SerializableProperty<MovementAnimationSet> {
        return getProperty(NpcProperty.ANIMATION_SET)
    }

    /**
     * Gets the [SerializableProperty] containing the clickable flag.
     */
    fun clickable(): SerializableProperty<Boolean> {
        return getProperty(NpcProperty.CLICKABLE)
    }

    /**
     * Gets the [SerializableProperty] containing the colours.
     */
    fun colours(): SerializableProperty<Map<Int, Int>> {
        return getProperty(NpcProperty.COLOURS)
    }

    /**
     * Gets the [SerializableProperty] containing the combat level.
     */
    fun combatLevel(): SerializableProperty<Int> {
        return getProperty(NpcProperty.COMBAT_LEVEL)
    }

    /**
     * Gets the [SerializableProperty] containing the description.
     */
    fun description(): SerializableProperty<String> {
        return getProperty(NpcProperty.DESCRIPTION)
    }

    /**
     * Gets the [SerializableProperty] containing the flat scale.
     */
    fun flatScale(): SerializableProperty<Int> {
        return getProperty(NpcProperty.FLAT_SCALE)
    }

    /**
     * Gets the [SerializableProperty] containing the head icon.
     */
    fun headIcon(): SerializableProperty<Int> {
        return getProperty(NpcProperty.HEAD_ICON)
    }

    /**
     * Gets the [SerializableProperty] containing the height scale.
     */
    fun heightScale(): SerializableProperty<Int> {
        return getProperty(NpcProperty.HEIGHT_SCALE)
    }

    /**
     * Gets the [SerializableProperty] containing the idle animation.
     */
    fun idleAnimation(): SerializableProperty<Int> {
        return getProperty(NpcProperty.IDLE_ANIMATION)
    }

    /**
     * Gets the [SerializableProperty] containing the light modifier.
     */
    fun lightModifier(): SerializableProperty<Int> {
        return getProperty(NpcProperty.LIGHT_MODIFIER)
    }

    /**
     * Gets the [SerializableProperty] containing the minimap visible flag.
     */
    fun minimapVisible(): SerializableProperty<Boolean> {
        return getProperty(NpcProperty.MINIMAP_VISIBLE)
    }

    /**
     * Gets the [SerializableProperty] containing the models.
     *
     * @return The property.
     */
    fun models(): SerializableProperty<IntArray> {
        return getProperty(NpcProperty.MODELS)
    }

    /**
     * Gets the [SerializableProperty] containing the [MorphismSet].
     */
    fun morphismSet(): SerializableProperty<MorphismSet> {
        return getProperty(NpcProperty.MORPHISM_SET)
    }

    /**
     * Gets the [SerializableProperty] containing the name.
     */
    fun name(): SerializableProperty<String> {
        return getProperty(NpcProperty.NAME)
    }

    /**
     * Gets the [SerializableProperty] containing the priority render flag.
     */
    fun priorityRender(): SerializableProperty<Boolean> {
        return getProperty(NpcProperty.PRIORITY_RENDER)
    }

    /**
     * Gets the [SerializableProperty] containing the rotation.
     */
    fun rotation(): SerializableProperty<Int> {
        return getProperty(NpcProperty.ROTATION)
    }

    /**
     * Gets the [SerializableProperty] containing the secondary models.
     */
    fun secondaryModels(): SerializableProperty<IntArray> {
        return getProperty(NpcProperty.SECONDARY_MODELS)
    }

    /**
     * Gets the [SerializableProperty] containing the shadow modifier.
     */
    fun shadowModifier(): SerializableProperty<Int> {
        return getProperty(NpcProperty.SHADOW_MODIFIER)
    }

    /**
     * Gets the [SerializableProperty] containing the size.
     */
    fun size(): SerializableProperty<Int> {
        return getProperty(NpcProperty.SIZE)
    }

    /**
     * Gets the [SerializableProperty] containing the walking animation.
     */
    fun walkingAnimation(): SerializableProperty<Int> {
        return getProperty(NpcProperty.WALKING_ANIMATION)
    }

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
