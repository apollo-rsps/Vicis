package rs.emulate.legacy.config.`object`

import com.google.common.base.MoreObjects
import rs.emulate.shared.util.DataBuffer
import java.util.Arrays

/**
 * A set of an object's possible model ids and positions. This class is immutable.
 */
class ModelSet {

    /**
     * The model ids.
     */
    private val models: IntArray

    /**
     * The model types.
     */
    private val types: IntArray
        get() = field.clone()

    /**
     * The amount of models in this ModelSet.
     */
    val count: Int
        get() = models.size

    /**
     * Creates the ModelSet, with an empty array for the types.
     */
    constructor(models: IntArray) {
        this.models = models.clone()
        types = IntArray(0)
    }

    /**
     * Creates the ModelSet. The length of the `models` and `types` arrays must be equal.
     * @throws IllegalArgumentException If the `models` and `types` arrays are not of equal length.
     */
    constructor(models: IntArray, types: IntArray) {
        require(models.size == types.size) { "Models and types arrays must have an equal length." }

        this.models = models.clone()
        this.types = types.clone()
    }

    /**
     * Gets the model id at the specified index.
     */
    fun getModel(index: Int): Int {
        return models[index]
    }

    /**
     * Gets the model ids.
     */
    fun getModels(): IntArray {
        return models.clone()
    }

    /**
     * Gets the type at the specified index.
     */
    fun getType(index: Int): Int {
        return types[index]
    }

    override fun toString(): String {
        return MoreObjects.toStringHelper(this)
            .add("ids", Arrays.toString(models))
            .add("types", Arrays.toString(types))
            .toString()
    }

    companion object {

        /**
         * The empty ModelSet, to be used as a default value.
         */
        val EMPTY = ModelSet(IntArray(0), IntArray(0))

        /**
         * Decodes a ModelSet with no model positions from the specified [DataBuffer].
         */
        fun decode(buffer: DataBuffer): ModelSet {
            val count = buffer.getUnsignedByte()
            val models = IntArray(count)

            for (index in 0 until count) {
                models[index] = buffer.getUnsignedShort()
            }

            return ModelSet(models)
        }

        /**
         * Decodes a ModelSet with model positions from the specified [DataBuffer].
         */
        fun decodePositioned(buffer: DataBuffer): ModelSet {
            val count = buffer.getUnsignedByte()
            val models = IntArray(count)
            val positions = IntArray(count)

            for (index in 0 until count) {
                models[index] = buffer.getUnsignedShort()
                positions[index] = buffer.getUnsignedByte()
            }

            return ModelSet(models, positions)
        }

        /**
         * Encodes the specified [ModelSet] into the specified [DataBuffer].
         */
        fun encode(buffer: DataBuffer, models: ModelSet): DataBuffer {
            val count = models.count
            buffer.putByte(count)

            val positions = models.types
            val positioned = positions.isNotEmpty()

            for (index in 0 until count) {
                buffer.putShort(models.getModel(index))

                if (positioned) {
                    buffer.putByte(positions[index])
                }
            }

            return buffer
        }
    }

}
