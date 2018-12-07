package rs.emulate.legacy.config.animation

import com.google.common.base.MoreObjects
import rs.emulate.shared.util.DataBuffer
import java.util.Arrays

/**
 * A collection of data about frames used in the animation.
 */
class FrameCollection {

    /**
     * The frame durations.
     */
    val durations: IntArray?
        get() = field?.clone()

    /**
     * The primary frame ids.
     */
    val primaries: IntArray?
        get() = field?.clone()

    /**
     * The secondary frame ids.
     */
    val secondaries: IntArray?
        get() = field?.clone()

    /**
     * Gets the size of this collection.
     */
    val size: Int
        get() = primaries?.size ?: 0

    /**
     * Creates the FrameCollection.
     */
    constructor(primaries: IntArray, secondaries: IntArray, durations: IntArray) {
        require(primaries.size == secondaries.size && primaries.size == durations.size) {
            "Frame collection arrays must all have the same size."
        }

        this.primaries = primaries
        this.secondaries = secondaries
        this.durations = durations
    }

    /**
     * Private constructor used to create a default (i.e. empty) FrameCollection.
     */
    private constructor() {
        durations = null
        secondaries = null
        primaries = null
    }

    override fun equals(other: Any?): Boolean {
        if (other is FrameCollection) {
            return Arrays.equals(primaries, other.primaries) &&
                Arrays.equals(secondaries, other.secondaries) &&
                Arrays.equals(durations, other.durations)
        }

        return false
    }

    /**
     * Gets the duration at the specified index.
     */
    fun getDuration(index: Int): Int {
        return durations!![index]
    }

    /**
     * Gets the primary frame id at the specified index.
     */
    fun getPrimary(index: Int): Int {
        return primaries!![index]
    }

    /**
     * Gets the secondary frame id at the specified index.
     */
    fun getSecondary(index: Int): Int {
        return secondaries!![index]
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = prime + Arrays.hashCode(primaries)
        result = prime * result + Arrays.hashCode(durations)
        return prime * result + Arrays.hashCode(secondaries)
    }

    override fun toString(): String {
        return MoreObjects.toStringHelper(this)
            .add("Primary frames", Arrays.toString(primaries))
            .add("Secondary frames", Arrays.toString(secondaries))
            .add("Frame durations", Arrays.toString(durations))
            .toString()
    }

    companion object {

        /**
         * The empty FrameCollection, used as a default value.
         */
        val EMPTY = FrameCollection()

        /**
         * The integer value that indicates a secondary frame id does not exist.
         */
        private const val NULL_SECONDARY_ID = 65535

        /**
         * Gets the size of the specified FrameCollection, in bytes.
         */
        fun bytes(collection: FrameCollection): Int {
            return collection.size * java.lang.Short.BYTES * 3 + java.lang.Byte.BYTES
        }

        /**
         * Decodes a [FrameCollection] from the specified [DataBuffer].
         */
        fun decode(buffer: DataBuffer): FrameCollection {
            val frames = buffer.getUnsignedByte()
            val primaries = IntArray(frames)
            val secondaries = IntArray(frames)
            val durations = IntArray(frames)

            for (frame in 0 until frames) {
                primaries[frame] = buffer.getUnsignedShort()
                val secondary = buffer.getUnsignedShort()
                secondaries[frame] = if (secondary == NULL_SECONDARY_ID) -1 else secondary
                durations[frame] = buffer.getUnsignedShort()
            }

            return FrameCollection(primaries, secondaries, durations)
        }

        /**
         * Encodes the specified [FrameCollection] into the specified [DataBuffer].
         */
        fun encode(buffer: DataBuffer, collection: FrameCollection): DataBuffer {
            val size = collection.size
            buffer.putByte(size)

            for (index in 0 until size) {
                buffer.putShort(collection.getPrimary(index)).putShort(collection.getSecondary(index))
                buffer.putShort(collection.getDuration(index))
            }

            return buffer
        }
    }

}
