package rs.emulate.shared.util

import java.nio.ByteBuffer
import java.util.Arrays

/**
 * Contains [ByteBuffer]-related utility methods.
 */
object ByteBufferUtils {

    /**
     * Copies the contents in the specified [ByteBuffer], but **not** any of its attributes (e.g.
     * mark, read-only). The capacity and limit of the new buffer will be the limit of the specified one, the position
     * of the new buffer will be 0, and the mark will be undefined. The specified buffer will be flipped after writing.
     *
     * This method uses [ByteBuffer.put] and so will write from the specified byte buffers current
     * position.
     */
    fun copy(buffer: ByteBuffer): ByteBuffer {
        val copy = ByteBuffer.allocate(buffer.remaining())
        buffer.mark()
        copy.put(buffer).flip()
        buffer.reset()
        return copy
    }

    /**
     * Converts the contents of the specified [ByteBuffer] to a string, which is formatted similarly to the
     * output of the [Arrays.toString] method.
     */
    fun toString(buffer: ByteBuffer): String {
        val builder = StringBuilder("[")

        val limit = buffer.limit()
        for (index in 0 until limit) {
            val byte = buffer.get().toInt() and 0xFF
            var hex = Integer.toHexString(byte).toUpperCase()

            if (hex.length == 1) {
                hex = "0$hex"
            }

            builder.append("0x").append(hex)
            if (index != limit - 1) {
                builder.append(", ")
            }
        }

        builder.append("]")
        return builder.toString()
    }

}
