package rs.emulate.shared.util.crypto

import java.nio.ByteBuffer

/**
 * An implementation of the XTEA block cipher.
 */
object Xtea {

    /**
     * The golden ratio.
     */
    private const val GOLDEN_RATIO = 0x9E3779B9.toInt()

    /**
     * The number of rounds.
     */
    private const val ROUNDS = 32

    /**
     * Deciphers the data in the specified [ByteBuffer].
     * @throws IllegalArgumentException if the key is not exactly 4 elements long.
     */
    fun decipher(buffer: ByteBuffer, start: Int, end: Int, key: IntArray) {
        require(key.size == 4) { "Key length must be four." }

        val quads = (end - start) / 8
        for (index in 0 until quads) {
            var sum = GOLDEN_RATIO * ROUNDS
            var v0 = buffer.getInt(start + index * 8)
            var v1 = buffer.getInt(start + index * 8 + 4)

            for (j in 0 until ROUNDS) {
                v1 -= (v0 shl 4 xor v0.ushr(5)) + v0 xor sum + key[sum ushr 11 and 3]
                sum -= GOLDEN_RATIO
                v0 -= (v1 shl 4 xor v1.ushr(5)) + v1 xor sum + key[sum and 3]
            }

            buffer.putInt(start + index * 8, v0)
            buffer.putInt(start + index * 8 + 4, v1)
        }
    }

    /**
     * Enciphers the specified [ByteBuffer] with the given key.
     * @throws IllegalArgumentException if the key is not exactly 4 elements long.
     */
    fun encipher(buffer: ByteBuffer, start: Int, end: Int, key: IntArray) {
        require(key.size == 4) { "Key length must be four." }

        val quads = (end - start) / 8
        for (index in 0 until quads) {
            var sum = 0
            var v0 = buffer.getInt(start + index * 8)
            var v1 = buffer.getInt(start + index * 8 + 4)

            for (j in 0 until ROUNDS) {
                v0 += (v1 shl 4 xor v1.ushr(5)) + v1 xor sum + key[sum and 3]
                sum += GOLDEN_RATIO
                v1 += (v0 shl 4 xor v0.ushr(5)) + v0 xor sum + key[sum ushr 11 and 3]
            }

            buffer.putInt(start + index * 8, v0)
            buffer.putInt(start + index * 8 + 4, v1)
        }
    }

}
