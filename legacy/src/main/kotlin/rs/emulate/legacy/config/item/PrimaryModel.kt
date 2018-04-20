package rs.emulate.legacy.config.item

import rs.emulate.shared.util.DataBuffer

/**
 * A utility class containing a model-id/translation pair.
 */
class PrimaryModel {

    /**
     * The id of the model.
     */
    val id: Int

    /**
     * The vertical translation applied to the model.
     */
    val translation: Int

    /**
     * Creates the PrimaryModel with the specified id and translation.
     */
    constructor(id: Int, translation: Int) {
        require(id.toLong() >= 0) { "Id cannot be negative." }
        require(translation >= Short.MIN_VALUE && translation <= Short.MAX_VALUE) { "Translation must fit in 16 bits." }

        this.id = id
        this.translation = translation
    }

    /**
     * Creates the PrimaryModel with default values.
     */
    private constructor() {
        id = -1
        translation = 0
    }

    companion object {

        /**
         * The empty PrimaryModel.
         */
        val EMPTY = PrimaryModel()

        /**
         * Decodes a PrimaryModel from the specified [DataBuffer].
         */
        internal fun decode(buffer: DataBuffer): PrimaryModel {
            val id = buffer.getUnsignedShort()
            val translation = buffer.getByte()
            return PrimaryModel(id, translation)
        }

        /**
         * Encodes the specified PrimaryModel into the specified [DataBuffer].
         */
        internal fun encode(buffer: DataBuffer, model: PrimaryModel): DataBuffer {
            return buffer.putShort(model.id)
                .putByte(model.translation)
        }
    }

}
