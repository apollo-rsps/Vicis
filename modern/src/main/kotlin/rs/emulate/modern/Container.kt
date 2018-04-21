package rs.emulate.modern

import rs.emulate.shared.util.CompressionUtils
import rs.emulate.shared.util.DataBuffer
import rs.emulate.shared.util.crypto.Xtea

import java.io.IOException

/**
 * A [Container] holds an optionally compressed file. This class can be used to decompress and compress containers.
 * A container can also have a two byte trailer that specifies the version of the file within it.
 *
 * @param type The type of compression.
 * @param data The decompressed data.
 * @param version The version of the file within this container.
 */
class Container(var type: Int, data: DataBuffer, version: Int = -1) {

    var version = version
        get() {
            require(versioned) { "Container does not have a version." }
            return field
        }

    val data = data.asReadOnlyBuffer()

    /**
     * Checks if this container is versioned.
     */
    val versioned: Boolean
        get() = version != -1

    /**
     * Encodes and compresses this container.
     */
    fun encode(): DataBuffer {
        val data = data.asReadOnlyBuffer() // read-only makes this method thread-safe
        val bytes = DataBuffer.allocate(data.limit())
        data.mark()
        data.read(bytes)
        data.reset()

        val compressed = when (type) {
            COMPRESSION_GZIP -> CompressionUtils.gzip(bytes)
            COMPRESSION_BZIP2 -> CompressionUtils.bzip2(bytes)
            else -> throw IOException("Invalid compression type.")
        }

        val header = 5 + (if (type == COMPRESSION_NONE) 0 else 4) + if (versioned) 2 else 0
        val buffer = DataBuffer.allocate(header + compressed.limit())

        buffer.putByte(type)
        buffer.putInt(compressed.limit())

        if (type != COMPRESSION_NONE) {
            buffer.putInt(data.limit())
        }

        buffer.put(compressed)

        if (versioned) {
            buffer.putShort(version)
        }

        return buffer.flip()
    }

    /**
     * Removes the version on this container so it becomes unversioned.
     */
    fun removeVersion() {
        version = -1
    }

    companion object {

        /**
         * This type indicates that BZIP2 compression is used.
         */
        const val COMPRESSION_BZIP2 = 1

        /**
         * This type indicates that GZIP compression is used.
         */
        const val COMPRESSION_GZIP = 2

        /**
         * This type indicates that no compression is used.
         */
        const val COMPRESSION_NONE = 0

        /**
         * An empty/'null' XTEA key.
         */
        private val NULL_KEY = IntArray(4)

        /**
         * Decodes and decompresses the container.
         */
        fun decode(buffer: DataBuffer): Container {
            return Container.decode(buffer, NULL_KEY)
        }

        /**
         * Decodes and decompresses the container, applying the XTEA cipher with the specified key..
         */
        fun decode(buffer: DataBuffer, key: IntArray): Container {
            val type = buffer.getUnsignedByte()
            val length = buffer.getInt()

            /* decrypt (TODO what to do about version number trailer?) */
            if (key[0] != 0 || key[1] != 0 || key[2] != 0 || key[3] != 0) {
                Xtea.decipher(buffer, 5, length + if (type == COMPRESSION_NONE) 5 else 9, key)
            }

            if (type == COMPRESSION_NONE) {
                val data = DataBuffer.allocate(length)
                data.fill(buffer)

                // decode the version, if present
                var version = -1
                if (buffer.remaining() >= 2) {
                    version = buffer.getShort()
                }

                return Container(type, data, version)
            }

            val uncompressedLength = buffer.getInt()

            val compressed = DataBuffer.allocate(length)
                .fill(buffer)

            val uncompressed = when (type) {
                COMPRESSION_BZIP2 -> CompressionUtils.bunzip2(compressed)
                COMPRESSION_GZIP -> CompressionUtils.gunzip(compressed)
                else -> throw IllegalArgumentException("Invalid compression type $type.")
            }

            val limit = uncompressed.limit()
            require(limit == uncompressedLength) { "Length mismatch: expected=$uncompressedLength, got $limit." }

            var version = -1
            if (buffer.remaining() >= 2) {
                version = buffer.getShort()
            }

            return Container(type, uncompressed, version)
        }
    }

}
