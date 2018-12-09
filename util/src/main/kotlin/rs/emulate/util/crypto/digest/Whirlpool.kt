package rs.emulate.util.crypto.digest

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import org.bouncycastle.jcajce.provider.digest.Whirlpool

object Whirlpool {
    const val DIGEST_LENGTH = 64
    val ZERO_DIGEST: ByteBuf = Unpooled.buffer(DIGEST_LENGTH).asReadOnly()
}

fun ByteBuf.readWhirlpoolDigest(): ByteBuf {
    val bytes = ByteArray(capacity())
    readBytes(bytes)

    val digest = Whirlpool.Digest()
    digest.update(bytes, 0, bytes.size)

    return Unpooled.wrappedBuffer(digest.digest()).asReadOnly()
}
