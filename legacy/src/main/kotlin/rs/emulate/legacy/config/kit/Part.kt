package rs.emulate.legacy.config.kit

import rs.emulate.shared.util.getUnsignedByte
import rs.emulate.shared.util.putByte
import java.nio.ByteBuffer

/**
 * The body part an [IdentikitDefinition] is for.
 */
enum class Part(val maleId: Int) {

    /**
     * The null Part, used as the default.
     */
    NULL(-1),

    /**
     * The head Part.
     */
    HEAD(0),

    /**
     * The chin Part.
     */
    CHIN(1),

    /**
     * The chest Part.
     */
    CHEST(2),

    /**
     * The arms Part.
     */
    ARMS(3),

    /**
     * The hands Part.
     */
    HANDS(4),

    /**
     * The legs Part.
     */
    LEGS(5),

    /**
     * The feet Part.
     */
    FEET(6);

    /**
     * Gets the id of this Part for female characters.
     */
    val femaleId: Int = maleId + FEMALE_ID_OFFSET

    companion object {

        /**
         * Decodes a Part from the specified [ByteBuffer].
         */
        fun decode(buffer: ByteBuffer): Part {
            return Part.valueOf(buffer.getUnsignedByte())
        }

        /**
         * Encodes the specified Part into the specified [ByteBuffer].
         */
        fun encode(buffer: ByteBuffer, part: Part): ByteBuffer {
            return buffer.putByte(part.maleId)
        }

        /**
         * Gets the Part with the specified id.
         */
        fun valueOf(value: Int): Part {
            require(value in 0..13) { "Part value must be [0, 13] (received $value)." }
            val values = values()
            val id = value % (values.size - 1) // 0-6 are male, 7-13 are female.

            return values.find { part -> part.maleId == id }!!
        }
    }

}

/**
 * The id offset for female characters.
 */
private const val FEMALE_ID_OFFSET = 7
