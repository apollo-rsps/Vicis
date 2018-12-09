package rs.emulate.util.crypto.xtea

import io.netty.buffer.ByteBuf

object Xtea {
    const val LOG_BLOCK_SIZE = 3
    const val BLOCK_SIZE = 1 shl LOG_BLOCK_SIZE
    const val MASK_BLOCK_SIZE = BLOCK_SIZE - 1

    const val GOLDEN_RATIO = -0x61c88647
    const val ROUNDS = 32
}

fun ByteBuf.xteaEncrypt(start: Int, end: Int, key: XteaKey) {
    val k = key.toIntArray()
    val blocks = (end - start) / 8

    repeat(blocks) { block ->
        var sum = 0
        var v0 = getInt(start + block * 8)
        var v1 = getInt(start + block * 8 + 4)

        repeat(Xtea.ROUNDS) {
            v0 += (((v1 shl 4) xor (v1 ushr 5)) + v1) xor (sum + k[sum and 3])
            sum += Xtea.GOLDEN_RATIO
            v1 += (((v0 shl 4) xor (v0 ushr 5)) + v0) xor (sum + k[(sum ushr 11) and 3])
        }

        setInt(start + block * 8, v0)
        setInt(start + block * 8 + 4, v1)
    }
}

@Suppress("INTEGER_OVERFLOW")
fun ByteBuf.xteaDecrypt(start: Int, end: Int, key: XteaKey) {
    val k = key.toIntArray()
    val blocks = (end - start) / 8

    repeat(blocks) { block ->
        var sum = Xtea.GOLDEN_RATIO * Xtea.ROUNDS
        var v0 = getInt(start + block * 8)
        var v1 = getInt(start + block * 8 + 4)

        repeat(Xtea.ROUNDS) {
            v1 -= (((v0 shl 4) xor (v0 ushr 5)) + v0) xor (sum + k[(sum ushr 11) and 3])
            sum -= Xtea.GOLDEN_RATIO
            v0 -= (((v1 shl 4) xor (v1 ushr 5)) + v1) xor (sum + k[sum and 3])
        }

        setInt(start + block * 8, v0)
        setInt(start + block * 8 + 4, v1)
    }
}
