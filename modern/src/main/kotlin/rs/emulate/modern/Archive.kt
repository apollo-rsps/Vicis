package rs.emulate.modern

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import java.util.TreeMap

class Archive {
    private val entries = TreeMap<Int, ByteBuf>()

    val size: Int
        get() = entries.size

    operator fun contains(id: Int): Boolean {
        return id in entries
    }

    /* returns an immutable buffer (each call returns a new slice) */
    operator fun get(id: Int): ByteBuf? {
        return entries[id]?.slice()
    }

    /* accepts an immutable buffer */
    operator fun set(id: Int, buf: ByteBuf) {
        entries[id] = buf
    }

    fun removeEntry(id: Int) {
        entries.remove(id)
    }

    /* returns an immutable buffer */
    fun write(): ByteBuf {
        val size = entries.size
        check(size > 0) { "Size must at least 1" }

        if (size == 1) {
            val firstEntry = entries.values.iterator().next()
            return firstEntry.slice()
        }

        val trailerLen = size * 4 + 1
        val trailer = Unpooled.buffer(trailerLen, trailerLen)

        var last = 0
        for (entry in entries.values) {
            val len = entry.readableBytes()

            val delta = len - last
            trailer.writeInt(delta)

            last = len
        }

        trailer.writeByte(1)

        val buf = Unpooled.compositeBuffer(size + 1)

        for (entry in entries.values) {
            buf.addComponent(entry)
            buf.writerIndex(buf.writerIndex() + entry.readableBytes())
        }

        buf.addComponent(trailer)
        buf.writerIndex(buf.writerIndex() + trailer.readableBytes())

        return buf.asReadOnly()
    }

    companion object {

        /* accepts an immutable buffer, returns an Archive containing a collection of immutable buffers */
        fun ByteBuf.readArchive(entry: ReferenceTable.Entry): Archive {
            val size = entry.size
            val childIds = entry.childIds

            require(size > 0) { "Size must at least 1" }

            if (size == 1) {
                val archive = Archive()

                val id = childIds[0]
                archive.entries[id] = this

                return archive
            }

            /* mark the start of the file and calculate length */
            val start = readerIndex()
            val len = readableBytes()

            /* read trailer */
            readerIndex(start + len - 1)

            val stripes = readUnsignedByte().toInt()
            require(stripes > 0) { "Stripes must at least 1" }

            /* read entry sizes */
            readerIndex(start + len - 1 - stripes * size * 4)

            val stripeSizes = Array(stripes) { IntArray(size) }
            val sizes = IntArray(size)

            repeat(stripes) {
                var accumulator = 0

                for (i in 0 until size) {
                    val delta = readInt()
                    accumulator += delta

                    stripeSizes[it][i] = accumulator
                    sizes[i] += accumulator
                }
            }

            /* allocate entry buffers */
            val entries = (0 until size).map {
                Unpooled.buffer(sizes[it], sizes[it])
            }

            /* read entries */
            readerIndex(start)

            repeat(stripes) { stripe ->
                repeat(size) { index ->
                    val slice = readSlice(stripeSizes[stripe][index])
                    entries[index].writeBytes(slice)
                }
            }

            val archive = Archive()

            for ((index, id) in childIds.withIndex()) {
                archive.entries[id] = entries[index].asReadOnly()
            }

            return archive
        }
    }
}
