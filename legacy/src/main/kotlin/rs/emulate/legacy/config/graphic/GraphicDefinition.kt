package rs.emulate.legacy.config.graphic

import com.google.common.collect.ImmutableMap
import rs.emulate.legacy.config.ConfigPropertyMap
import rs.emulate.legacy.config.ConfigUtils
import rs.emulate.legacy.config.MutableConfigDefinition
import rs.emulate.legacy.config.SerializableProperty

/**
 * A definition for a graphic (a 'spotanim').
 *
 * @param id The id of the definition.
 * @param properties The [ConfigPropertyMap].
 */
open class GraphicDefinition(id: Int, properties: ConfigPropertyMap) : MutableConfigDefinition(id, properties) {

    /**
     * Gets the [SerializableProperty] containing the animation id of this graphic.
     */
    fun animation(): SerializableProperty<Int> {
        return getProperty(GraphicProperty.ANIMATION)
    }

    /**
     * Gets the [SerializableProperty] containing the breadth scale of this graphic.
     */
    fun breadthScale(): SerializableProperty<Int> {
        return getProperty(GraphicProperty.BREADTH_SCALE)
    }

    /**
     * Gets the [SerializableProperty] containing the model brightness of this graphic.
     */
    fun brightness(): SerializableProperty<Int> {
        return getProperty(GraphicProperty.BRIGHTNESS)
    }

    /**
     * Gets an [ImmutableMap] containing the original and replacement colour values.
     */
    fun colours(): ImmutableMap<Int, Int> {
        val builder = ImmutableMap.builder<Int, Int>()

        for (slot in 1..COLOUR_COUNT) {
            val original = originalColour(slot)
            val replacement = replacementColour(slot)
            builder.put(original.value!!, replacement.value!!)
        }

        return builder.build()
    }

    /**
     * Gets the [SerializableProperty] containing the depth scale of this graphic.
     */
    fun depthScale(): SerializableProperty<Int> {
        return getProperty(GraphicProperty.DEPTH_SCALE)
    }

    /**
     * Gets the [SerializableProperty] containing the model id of this graphic.
     */
    fun modelId(): SerializableProperty<Int> {
        return getProperty(GraphicProperty.MODEL)
    }

    /**
     * Gets the [SerializableProperty] containing the original colour for the specified slot.
     */
    fun originalColour(slot: Int): SerializableProperty<Int> {
        require(slot in 1..COLOUR_COUNT) { "Slot must be greater than 0 and less than $COLOUR_COUNT." }
        return getProperty(ConfigUtils.getOriginalColourPropertyName(slot))
    }

    /**
     * Gets the [SerializableProperty] containing the replacement colour for the specified slot.
     */
    fun replacementColour(slot: Int): SerializableProperty<Int> {
        require(slot in 1..COLOUR_COUNT) { "Slot must be greater than 0 and less than $COLOUR_COUNT." }
        return getProperty(ConfigUtils.getReplacementColourPropertyName(slot))
    }

    /**
     * Gets the [SerializableProperty] containing the rotation of this graphic.
     */
    fun rotation(): SerializableProperty<Int> {
        return getProperty(GraphicProperty.ROTATION)
    }

    /**
     * Gets the [SerializableProperty] containing the model shadow of this graphic.
     */
    fun shadow(): SerializableProperty<Int> {
        return getProperty(GraphicProperty.SHADOW)
    }

    companion object {

        /**
         * The name of the archive entry containing the graphic definitions, without the extension.
         */
        const val ENTRY_NAME = "spotanim"

        /**
         * The amount of original and replacement colours.
         */
        const val COLOUR_COUNT = 10

    }

}
