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
     *
     * @param slot The colour slot.
     * @return The DynamicPropertyType.
     */
    fun getOriginalColourPropertyName(slot: Int): DynamicConfigPropertyType {
        return newOptionProperty(ORIGINAL_COLOUR_PREFIX, slot)
    }

    /**
     * Creates or retrieves a a [DynamicConfigPropertyType] for replacement colours, with a name of the form
     * "replacement-colour-`[slot]`".
     *
     * The returned DynamicPropertyType may be the same object as a previously created one, as only one
     * DynamicPropertyType may exist per string (see [DynamicConfigPropertyType.valueOf]).
     *
     * @param slot The colour slot.
     * @return The DynamicPropertyType.
     */
    fun getReplacementColourPropertyName(slot: Int): DynamicConfigPropertyType {
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
    fun <T : ConfigPropertyType> newColourProperty(type: T): SerializableProperty<HashMap<Int, Int>> {
        val encoder: (DataBuffer, HashMap<Int, Int>) -> DataBuffer = { buffer, colours ->
            buffer.putByte(colours.size)
            colours.entries.forEach { colour -> buffer.putShort(colour.key).putShort(colour.value) }
            buffer
        }

        val decoder: (DataBuffer) -> HashMap<Int, Int> = { buffer ->
            val size = buffer.getUnsignedByte()
            val colours = HashMap<Int, Int>(size)

            for (colour in 0 until size) {
                val index = buffer.getUnsignedShort()
                colours[index] = buffer.getUnsignedShort()
            }

            colours
        }

        return SerializableProperty(type, HashMap(1), encoder, decoder, { colours: HashMap<Int, Int> ->
            (colours.size * java.lang.Short.BYTES * 2) + java.lang.Byte.BYTES
        })
    }

    /**
     * Returns a [DynamicConfigPropertyType] with the name of the form "`[prefix]`-`[slot]`".
     *
     * The returned DynamicPropertyType may be the same object as a previously-returned one, as only one
     * DynamicPropertyType may exist per string (see [DynamicConfigPropertyType.valueOf]).
     *
     * @param prefix The prefix.
     * @param option The option.
     * @return The DynamicPropertyType.
     */
    fun newOptionProperty(prefix: String, option: Int): DynamicConfigPropertyType {
        return DynamicConfigPropertyType.valueOf("$prefix-$option", option)
    }

}
