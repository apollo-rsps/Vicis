package rs.emulate.legacy.config

import rs.emulate.shared.util.DataBuffer
import java.util.HashMap

/**
 * Contains utility methods used in config definitions.
 */
object ConfigUtils {

    /**
     * A function that decodes data from a [DataBuffer] into model indices.
     */
    val MODEL_DECODER: (DataBuffer) -> IntArray = { buffer ->
        val count = buffer.getUnsignedByte()
        IntArray(count) { buffer.getUnsignedShort() }
    }

    /**
     * The String prefixed when creating [DynamicConfigPropertyType]s for original colours.
     */
    private const val ORIGINAL_COLOUR_PREFIX = "original-colour"

    /**
     * The String prefixed when creating [DynamicConfigPropertyType]s for replacement colours.
     */
    private const val REPLACEMENT_COLOUR_PREFIX = "replacement-colour"

    /**
     * Creates or retrieves a [DynamicConfigPropertyType] for original colours, with a name of the form
     * "original-colour-`[slot]`".
     *
     * The returned DynamicPropertyType may be the same object as a previously-returned one, as only one
     * DynamicPropertyType may exist per string (see [DynamicConfigPropertyType.valueOf]).
     */
    fun <T> getOriginalColourPropertyName(slot: Int): DynamicConfigPropertyType<T> {
        return newOptionProperty(ORIGINAL_COLOUR_PREFIX, slot)
    }

    /**
     * Creates or retrieves a a [DynamicConfigPropertyType] for replacement colours, with a name of the form
     * "replacement-colour-`[slot]`".
     *
     * The returned DynamicPropertyType may be the same object as a previously created one, as only one
     * DynamicPropertyType may exist per string (see [DynamicConfigPropertyType.valueOf]).
     */
    fun <T> getReplacementColourPropertyName(slot: Int): DynamicConfigPropertyType<T> {
        return newOptionProperty(REPLACEMENT_COLOUR_PREFIX, slot)
    }

    /**
     * Creates a [SerializableProperty] for a [Map] of original to replacement colour values.
     *
     * FIXME This property is too complicated to be done like this.
     *
     * @param type The [ConfigPropertyType] of the DefinitionProperty.
     * @return The DefinitionProperty.
     */
    fun newColourProperty(type: ConfigPropertyType<Map<Int, Int>>): SerializableProperty<Map<Int, Int>> {
        val encoder: (DataBuffer, Map<Int, Int>) -> DataBuffer = { buffer, colours ->
            buffer.putByte(colours.size)
            colours.entries.forEach { colour -> buffer.putShort(colour.key).putShort(colour.value) }
            buffer
        }

        val decoder: (DataBuffer) -> Map<Int, Int> = { buffer ->
            val size = buffer.getUnsignedByte()
            val colours = HashMap<Int, Int>(size)

            for (colour in 0 until size) {
                val index = buffer.getUnsignedShort()
                colours[index] = buffer.getUnsignedShort()
            }

            colours
        }

        return SerializableProperty(type, mutableMapOf(), encoder, decoder) { colours ->
            (colours.size * java.lang.Short.BYTES * 2) + java.lang.Byte.BYTES
        }
    }

    /**
     * Returns a [DynamicConfigPropertyType] with the name of the form "`[prefix]`-`[option]`".
     *
     * The returned DynamicPropertyType may be the same object as a previously-returned one, as only one
     * DynamicPropertyType may exist per string (see [DynamicConfigPropertyType.valueOf]).
     *
     * @param prefix The prefix.
     * @param option The option.
     */
    fun <T> newOptionProperty(prefix: String, option: Int): DynamicConfigPropertyType<T> {
        return DynamicConfigPropertyType.valueOf("$prefix-$option", option)
    }

}
