package rs.emulate.util.crypto.rsa

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import org.bouncycastle.crypto.params.RSAKeyParameters
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters
import org.bouncycastle.util.BigIntegers
import java.math.BigInteger
import java.security.SecureRandom

private val random = SecureRandom()

fun ByteBuf.rsaEncrypt(key: RSAKeyParameters): ByteBuf {
    val len = readableBytes()

    val input = ByteArray(len)
    readBytes(input)

    val ciphertext = BigInteger(input)
    val plaintext = ciphertext.modPow(key.exponent, key.modulus)

    val output = plaintext.toByteArray()
    return Unpooled.wrappedBuffer(output)
}

fun ByteBuf.rsaDecrypt(key: RSAKeyParameters): ByteBuf {
    require(key.isPrivate)

    val len = readableBytes()

    val input = ByteArray(len)
    readBytes(input)

    val ciphertext = BigInteger(input)
    val plaintext: BigInteger

    if (key is RSAPrivateCrtKeyParameters) {

        /* blind the input */
        val e = key.publicExponent
        val m = key.modulus
        val r = BigIntegers.createRandomInRange(BigInteger.ONE, m.subtract(BigInteger.ONE),
            random
        )

        val blindedCiphertext = r.modPow(e, m).multiply(ciphertext).mod(m)

        /* decrypt using the Chinese Remainder Theorem */
        val p = key.p
        val q = key.q
        val dP = key.dp
        val dQ = key.dq
        val qInv = key.qInv

        val mP = blindedCiphertext.remainder(p).modPow(dP, p)
        val mQ = blindedCiphertext.remainder(q).modPow(dQ, q)

        val h = mP.subtract(mQ).multiply(qInv).mod(p)

        val blindedPlaintext = h.multiply(q).add(mQ)

        /* unblind the output */
        val rInv = r.modInverse(m)
        plaintext = blindedPlaintext.multiply(rInv).mod(m)

        /* see https://people.redhat.com/~fweimer/rsa-crt-leaks.pdf */
        check(ciphertext == plaintext.modPow(e, m))
    } else {
        plaintext = ciphertext.modPow(key.exponent, key.modulus)
    }

    val out = plaintext.toByteArray()
    return Unpooled.wrappedBuffer(out)
}
