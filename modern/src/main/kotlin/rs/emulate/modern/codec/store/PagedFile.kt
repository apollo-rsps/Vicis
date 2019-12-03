package rs.emulate.modern.codec.store

import rs.emulate.util.channels.readFullyOrToEof
import java.io.Closeable
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

class PagedFile(
    private val channel: FileChannel,
    private val pageSize: Int,
    private val pages: Int,
    private val strict: Boolean
) : Closeable {

    private val cache = ByteBuffer.allocateDirect(pageSize * pages)
    private var cachePos = -1L

    init {
        require(!strict || channel.size() % pageSize == 0L)
        require(pageSize >= 1)
        require(pages >= 1)
    }

    fun read(buf: ByteBuffer, pos: Long) {
        cache.clear()

        if (cachePos == -1L || pos < cachePos || pos >= cachePos + pages) {
            cachePos = pos
            channel.readFullyOrToEof(cache, pos * pageSize)
        }

        val bytePos = (pos - cachePos).toInt() * pageSize
        val byteLimit = bytePos + pageSize

        cache.position(bytePos).limit(byteLimit)
        buf.put(cache)
    }

    fun write(buf: ByteBuffer, pos: Long) {
        // TODO change this to write back in the future?

        /* invalidate the cache */
        if (pos >= cachePos && pos < cachePos + pages) {
            cachePos = -1
        }

        channel.write(buf, pos * pageSize)
    }

    fun size(): Long {
        return if (strict) {
            channel.size() / pageSize
        } else {
            (channel.size() + pageSize - 1) / pageSize
        }
    }

    override fun close() {
        channel.close()
    }
}
