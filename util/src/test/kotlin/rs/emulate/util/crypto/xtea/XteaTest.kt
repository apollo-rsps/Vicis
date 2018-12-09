package rs.emulate.util.crypto.xtea

import io.netty.buffer.Unpooled
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import rs.emulate.util.crypto.xtea.XteaKey
import rs.emulate.util.crypto.xtea.xteaDecrypt
import rs.emulate.util.crypto.xtea.xteaEncrypt
import java.util.stream.Stream

class XteaTest {

    @ArgumentsSource(TestVectorProvider::class)
    @ParameterizedTest(name = "encrypting {2} with key {0} returns {1}")
    fun encrypt(hexKey: String, plaintext: String, ciphertext: String) {
        val key = XteaKey.fromString(hexKey)

        val plaintextBytes = ByteArray(8)
        for (i in plaintextBytes.indices) {
            val hex = plaintext.substring(i * 2, (i + 1) * 2)
            plaintextBytes[i] = hex.toInt(16).toByte()
        }

        val ciphertextBytes = ByteArray(8)
        for (i in plaintextBytes.indices) {
            val hex = ciphertext.substring(i * 2, (i + 1) * 2)
            ciphertextBytes[i] = hex.toInt(16).toByte()
        }

        val buf = Unpooled.wrappedBuffer(plaintextBytes)
        buf.xteaEncrypt(0, ciphertextBytes.size, key)
        assertArrayEquals(ciphertextBytes, buf.array())
    }

    @ArgumentsSource(TestVectorProvider::class)
    @ParameterizedTest(name = "decrypting {2} with key {0} returns {1}")
    fun decrypt(hexKey: String, plaintext: String, ciphertext: String) {
        val key = XteaKey.fromString(hexKey)

        val plaintextBytes = ByteArray(8)
        for (i in plaintextBytes.indices) {
            val hex = plaintext.substring(i * 2, (i + 1) * 2)
            plaintextBytes[i] = hex.toInt(16).toByte()
        }

        val ciphertextBytes = ByteArray(8)
        for (i in plaintextBytes.indices) {
            val hex = ciphertext.substring(i * 2, (i + 1) * 2)
            ciphertextBytes[i] = hex.toInt(16).toByte()
        }

        val buf = Unpooled.wrappedBuffer(ciphertextBytes)
        buf.xteaDecrypt(0, ciphertextBytes.size, key)
        assertArrayEquals(plaintextBytes, buf.array())
    }

    private class TestVectorProvider : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
            return Stream.of(
                Arguments.of("000102030405060708090a0b0c0d0e0f", "4142434445464748", "497df3d072612cb5"),
                Arguments.of("000102030405060708090a0b0c0d0e0f", "4141414141414141", "e78f2d13744341d8"),
                Arguments.of("000102030405060708090a0b0c0d0e0f", "5a5b6e278948d77f", "4141414141414141"),
                Arguments.of("00000000000000000000000000000000", "4142434445464748", "a0390589f8b8efa5"),
                Arguments.of("00000000000000000000000000000000", "4141414141414141", "ed23375a821a8c2d"),
                Arguments.of("00000000000000000000000000000000", "70e1225d6e4e7655", "4141414141414141")
            )
        }
    }

}
