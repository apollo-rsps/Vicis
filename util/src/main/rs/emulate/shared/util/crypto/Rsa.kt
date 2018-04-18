package rs.emulate.shared.util.crypto

import rs.emulate.shared.util.DataBuffer

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
    fun crypt(buffer: DataBuffer, modulus: BigInteger, key: BigInteger): DataBuffer {
        val bytes = ByteArray(buffer.limit())
        buffer.read(bytes)

        val input = BigInteger(bytes)
        val output = input.modPow(key, modulus)

        return DataBuffer.wrap(output.toByteArray())
    }

}
