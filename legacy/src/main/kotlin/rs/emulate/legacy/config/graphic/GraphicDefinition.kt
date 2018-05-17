package rs.emulate.legacy.config.graphic

import rs.emulate.legacy.config.ConfigPropertyMap
import rs.emulate.legacy.config.ConfigUtils
import rs.emulate.legacy.config.MutableConfigDefinition
import rs.emulate.legacy.config.SerializableProperty

/**
 * A definition for a graphic (a 'spotanim').
 */
open class GraphicDefinition(id: Int, properties: ConfigPropertyMap) : MutableConfigDefinition(id, properties) {

    /**
     * Gets the [SerializableProperty] containing the animation id of this graphic.
     */
    val animation: SerializableProperty<Int>
        get() = getProperty(GraphicProperty.Animation)

    /**
     * Gets the [SerializableProperty] containing the breadth scale of this graphic.
     */
    val breadthScale: SerializableProperty<Int>
        get() = getProperty(GraphicProperty.BreadthScale)

    /**
     * Gets the [SerializableProperty] containing the model brightness of this graphic.
     */
    val brightness: SerializableProperty<Int>
        get() = getProperty(GraphicProperty.Brightness)

    fun colours(): Map<Int, Int> {
        val builder = mutableMapOf<Int, Int>()

        for (slot in 1..COLOUR_COUNT) {
            val original = originalColour(slot)
            val replacement = replacementColour(slot)

            builder[original.value] = replacement.value
        }

        return builder
    }

    /**
     * Gets the [SerializableProperty] containing the depth scale of this graphic.
     */
    val depthScale: SerializableProperty<Int>
        get() = getProperty(GraphicProperty.DepthScale)

    /**
     * Gets the [SerializableProperty] containing the model id of this graphic.
     */
    val modelId: SerializableProperty<Int>
        get() = getProperty(GraphicProperty.Model)

    /**
     * Gets the [SerializableProperty] containing the original colour for the specified slot.
     */
    fun originalColour(slot: Int): SerializableProperty<Int> {
        require(slot in 0 until COLOUR_COUNT) { "Slot must be greater than 0 and less than $COLOUR_COUNT." }
        return getProperty(ConfigUtils.getOriginalColourPropertyName(slot, 40))
    }

    /**
     * Gets the [SerializableProperty] containing the replacement colour for the specified slot.
     */
    fun replacementColour(slot: Int): SerializableProperty<Int> {
        require(slot in 0 until COLOUR_COUNT) { "Slot must be greater than 0 and less than $COLOUR_COUNT." }
        return getProperty(ConfigUtils.getReplacementColourPropertyName(slot, 50))
    }

    /**
     * Gets the [SerializableProperty] containing the rotation of this graphic.
     */
    val rotation: SerializableProperty<Int>
        get() = getProperty(GraphicProperty.Rotation)

    /**
     * Gets the [SerializableProperty] containing the model shadow of this graphic.
     */
    val shadow: SerializableProperty<Int>
        get() = getProperty(GraphicProperty.Shadow)

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
