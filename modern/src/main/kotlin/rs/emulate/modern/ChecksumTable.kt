package rs.emulate.modern

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import org.bouncycastle.crypto.params.RSAKeyParameters
import org.bouncycastle.jcajce.provider.digest.Whirlpool
import rs.emulate.util.crypto.rsa.rsaEncrypt

class ChecksumTable {
    val size: Int
        get() = entries.size

    private val entries = mutableListOf<Entry>()

    fun getEntries(): List<Entry> {
        return entries.toList()
    }

    fun addEntry(): Entry {
        val size = entries.size
        check(size < 255) /* not 256, as (255, 255) can't be a reference table */

        val entry = Entry(size)
        entries += entry
        return entry
    }

    fun removeLastEntry() {
        val size = entries.size
        check(size > 0)
        entries.removeAt(size - 1)
    }

    operator fun contains(id: Int): Boolean {
        return id >= 0 && id < entries.size
    }

    operator fun get(id: Int): Entry = entries[id]

    /* returns an immutable buffer */
    fun write() = write(false, null)

    /* returns an immutable buffer */
    fun write(whirlpool: Boolean) = write(whirlpool, null)

    /* returns an immutable buffer */
    fun write(whirlpool: Boolean, privateKey: RSAKeyParameters?): ByteBuf {
        val buf = Unpooled.buffer()

        if (whirlpool) {
            buf.writeByte(entries.size)
        }

        for (entry in entries) {
            buf.writeInt(entry.checksum)
            buf.writeInt(entry.version)

            if (whirlpool) {
                buf.writeBytes(entry.whirlpoolDigest!!.slice())
            }
        }

        if (whirlpool) {
            val bytes = ByteArray(buf.writerIndex())
            buf.getBytes(0, bytes)

            val digest = Whirlpool.Digest()
            digest.update(bytes)

            var temp = Unpooled.buffer(rs.emulate.util.crypto.digest.Whirlpool.DIGEST_LENGTH + 1)
            temp.writeByte(1)
            temp.writeBytes(digest.digest())

            if (privateKey != null) {
                temp = temp.rsaEncrypt(privateKey)
            }

            buf.writeBytes(temp)
            temp.release()
        }

        return buf.asReadOnly()
    }

    class Entry(val id: Int) {
        var checksum = 0
        var version = 0
        var whirlpoolDigest: ByteBuf? = null
            set(value) {
                field = value?.let(ByteBuf::asReadOnly)
            }
    }
}

fun ByteBuf.readChecksumTable(): ChecksumTable {
    require(readableBytes() % 8 == 0) { "Checksum table length should be a multiple of 8" }

    val table = ChecksumTable()

    while (isReadable) {
        val entry = table.addEntry()
        entry.checksum = readInt()
        entry.version = readInt()
    }

    return table
}
