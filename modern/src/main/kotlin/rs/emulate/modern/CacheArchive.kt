package rs.emulate.modern

import io.netty.buffer.ByteBuf
import rs.emulate.util.crypto.xtea.XteaKey
import java.io.Closeable
import java.io.FileNotFoundException

class CacheArchive(
    private val cache: Cache,
    private val index: Int,
    private val archive: Archive,
    private val entry: ReferenceTable.Entry
) : Closeable {

    private var dirty: Boolean = false

    val capacity: Int
        get() = entry.capacity

    val listEntries: List<Int>
        get() = entry.childIds

    private fun read(child: ReferenceTable.ChildEntry): ByteBuf {
        return requireNotNull(archive[child.id]) { "Entry in ReferenceTable but not in Archive" }
    }

    private fun write(child: ReferenceTable.ChildEntry, buf: ByteBuf) {
        archive[child.id] = buf
    }

    operator fun contains(id: Int): Boolean {
        return id in entry
    }

    operator fun contains(name: String): Boolean {
        return name in entry
    }

    /* returns an immutable buffer */
    fun read(id: Int): ByteBuf {
        return read(entry[id] ?: throw FileNotFoundException())
    }

    /* returns an immutable buffer */
    fun read(name: String): ByteBuf {
        return read(entry[name] ?: throw FileNotFoundException())
    }

    /* accepts an immutable buffer */
    fun write(id: Int, buf: ByteBuf) {
        val child = entry[id]
        if (child != null) {
            write(child, buf)
        } else {
            val newEntry = entry.createChild(id)
            write(newEntry, buf)
        }

        dirty = true
    }

    /* accepts an immutable buffer */
    fun write(name: String, buf: ByteBuf) {
        val child = entry[name]
        if (child != null) {
            write(child, buf)
        } else {
            val newEntry = entry.createChild()
            newEntry.setName(name)
            write(newEntry, buf)
        }

        dirty = true
    }

    fun remove(id: Int) {
        entry[id] ?: throw FileNotFoundException()

        archive.removeEntry(id)
        entry.removeChild(id)

        dirty = true
    }

    fun remove(name: String) {
        val child = entry[name] ?: throw FileNotFoundException()

        val id = child.id
        archive.removeEntry(id)
        entry.removeChild(id)

        dirty = true
    }

    fun flush() = flush(XteaKey.NONE)

    fun flush(key: XteaKey) {
        if (dirty) {
            cache.flushArchive(index, entry, key, archive)
            dirty = false
        }
    }

    override fun close() = flush()
}
