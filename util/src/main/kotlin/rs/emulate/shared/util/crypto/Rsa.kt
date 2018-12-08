package rs.emulate.shared.util.crypto

import java.math.BigInteger
import java.nio.ByteBuffer

/**
 * An implementation of the RSA algorithm.
 */
object Rsa {

    /**
     * Encrypts/decrypts the specified [ByteBuffer] with the key and modulus.
     *
     * @param buffer The input buffer.
     * @param modulus The modulus.
     * @param key The key.
     */
    fun crypt(buffer: ByteBuffer, modulus: BigInteger, key: BigInteger): ByteBuffer {
        val bytes = ByteArray(buffer.limit())
        buffer.get(bytes)

        val input = BigInteger(bytes)
        val output = input.modPow(key, modulus)

        return ByteBuffer.wrap(output.toByteArray())
    }

}
