package rs.emulate.util.crypto

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import rs.emulate.shared.util.DataBuffer
import rs.emulate.shared.util.crypto.Xtea

/**
 * A unit test to validate the XTEA code.
 */
class XteaTest {

    @Test
    fun testDecipher() {
        for (vector in TEST_VECTORS) {
            val key = IntArray(4)
            for (index in key.indices) {
                val hex = vector[0].substring(index * 8, (index + 1) * 8)
                key[index] = Integer.parseInt(hex, 16)
            }

            val plaintext = ByteArray(8)
            for (index in plaintext.indices) {
                val hex = vector[1].substring(index * 2, (index + 1) * 2)
                plaintext[index] = Integer.parseInt(hex, 16).toByte()
            }

            val ciphertext = ByteArray(8)
            for (index in plaintext.indices) {
                val hex = vector[2].substring(index * 2, (index + 1) * 2)
                ciphertext[index] = Integer.parseInt(hex, 16).toByte()
            }

            val buffer = DataBuffer.wrap(ciphertext)
            Xtea.decipher(buffer, 0, 8, key)
            assertArrayEquals(plaintext, buffer.array())
        }
    }

    @Test
    fun testEncipher() {
        for (vector in TEST_VECTORS) {
            val key = IntArray(4)
            for (index in key.indices) {
                val hex = vector[0].substring(index * 8, (index + 1) * 8)
                key[index] = Integer.parseInt(hex, 16)
            }

            val plaintext = ByteArray(8)
            for (index in plaintext.indices) {
                val hex = vector[1].substring(index * 2, (index + 1) * 2)
                plaintext[index] = Integer.parseInt(hex, 16).toByte()
            }

            val ciphertext = ByteArray(8)
            for (index in plaintext.indices) {
                val hex = vector[2].substring(index * 2, (index + 1) * 2)
                ciphertext[index] = Integer.parseInt(hex, 16).toByte()
            }

            val buffer = DataBuffer.wrap(plaintext)
            Xtea.encipher(buffer, 0, 8, key)
            assertArrayEquals(ciphertext, buffer.array())
        }
    }

    companion object {

        /**
         * The vectors used in the XTEA enciphering/deciphering tests.
         */
        private val TEST_VECTORS = arrayOf(
            arrayOf("000102030405060708090a0b0c0d0e0f", "4142434445464748", "497df3d072612cb5"),
            arrayOf("000102030405060708090a0b0c0d0e0f", "4141414141414141", "e78f2d13744341d8"),
            arrayOf("000102030405060708090a0b0c0d0e0f", "5a5b6e278948d77f", "4141414141414141"),
            arrayOf("00000000000000000000000000000000", "4142434445464748", "a0390589f8b8efa5"),
            arrayOf("00000000000000000000000000000000", "4141414141414141", "ed23375a821a8c2d"),
            arrayOf("00000000000000000000000000000000", "70e1225d6e4e7655", "4141414141414141"))
    }

}
