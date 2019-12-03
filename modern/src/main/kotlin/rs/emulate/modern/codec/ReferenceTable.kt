package rs.emulate.modern.codec

import com.google.common.collect.HashMultimap
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import rs.emulate.modern.codec.store.FileStore
import rs.emulate.util.charset.hashCodeCp1252
import rs.emulate.util.crypto.digest.Whirlpool
import rs.emulate.util.readUnsignedIntSmart
import rs.emulate.util.writeUnsignedIntSmart
import java.io.IOException
import java.util.*

class ReferenceTable {
    private val entries = TreeMap<Int, Entry>()
    private val entriesByName = HashMultimap.create<Int, Entry>()
    private var flags = 0

    var version = 0

    val entryIds: List<Int>
        get() = entries.keys.toList()

    val size: Int
        get() = entries.size

    var format = FORMAT_VERSION
        set(value) {
            require(value in 0..FORMAT_MAX)
            field = value
        }

    val capacity: Int
        get() = if (entries.isEmpty()) 0 else entries.lastKey() + 1

    var nameHashingEnabled: Boolean
        get() = flags and FLAG_NAME_HASHES != 0
        set(value) {
            flags = if (value) {
                flags or FLAG_NAME_HASHES
            } else {
                flags and FLAG_NAME_HASHES.inv()
            }
        }

    var whirlpoolEnabled: Boolean
        get() = flags and FLAG_WHIRLPOOL != 0
        set(value) {
            flags = if (value) {
                flags or FLAG_WHIRLPOOL
            } else {
                flags and FLAG_WHIRLPOOL.inv()
            }
        }

    fun bumpVersion() {
        this.version++
    }

    fun containsEntry(id: Int): Boolean = id in entries
    fun containsEntry(name: String): Boolean = entriesByName.containsKey(name.hashCodeCp1252())

    fun getEntry(id: Int): Entry? = entries[id]

    fun getEntry(name: String): Entry? {
        val it = entriesByName.get(name.hashCodeCp1252()).iterator()
        return if (it.hasNext()) it.next() else null
    }

    fun createEntry(): Entry {
        (0 until FileStore.FILE_LEN)
            .find { it !in entries }
            ?.let { return createEntry(it) }

        throw IOException("Reference table is full")
    }

    fun createEntry(id: Int): Entry {
        val entry = Entry(this, id)

        if (entries.put(id, entry) != null) {
            throw IOException("Entry with id $id already exists")
        }

        entriesByName.put(entry.nameHash, entry)
        return entry
    }

    fun removeEntry(id: Int) {
        val entry = requireNotNull(entries.remove(id)) { "Entry with id $id does not exist" }
        entriesByName.remove(entry.nameHash, entry)
    }

    /* returns an immutable buffer */
    fun write(): ByteBuf {
        val buf = Unpooled.buffer()

        buf.writeByte(format)

        if (format >= FORMAT_VERSION) {
            buf.writeInt(version)
        } else {
            require(version != 0) { "Format must be 6 or higher for a non-zero version" }
        }

        buf.writeByte(flags)

        /* write size */
        when {
            format >= FORMAT_SMART -> buf.writeUnsignedIntSmart(size)
            size <= 0xffff -> buf.writeShort(size)
            else -> throw IOException("Format must be 7 or higher for reference tables with more than 65535 entries")
        }

        /* write entry ids */
        var last = 0
        for (id in entries.keys) {
            val delta = id - last
            if (format >= FORMAT_SMART) {
                buf.writeUnsignedIntSmart(delta)
            } else {
                buf.writeShort(delta)
            }

            last = id
        }

        if (flags and FLAG_NAME_HASHES != 0) {
            for (entry in entries.values) {
                buf.writeInt(entry.nameHash)
            }
        }

        for (entry in entries.values) {
            buf.writeInt(entry.checksum)
        }

        // TODO implement these once identified
        if (flags and FLAG_UNKNOWN8 != 0) {
            throw UnsupportedOperationException()
        }

        if (flags and FLAG_WHIRLPOOL != 0) {
            for (entry in entries.values) {
                buf.writeBytes(entry.whirlpoolDigest)
            }
        }

        // TODO implement these once identified
        if (flags and FLAG_UNKNOWN4 != 0) {
            throw UnsupportedOperationException()
        }

        for (entry in entries.values) {
            buf.writeInt(entry.version)
        }

        /* write child sizes */
        for (entry in entries.values) {
            val size = entry.size

            if (format >= FORMAT_SMART) {
                buf.writeUnsignedIntSmart(size)
            } else {
                buf.writeShort(size)
            }
        }

        /* write child ids */
        for (entry in entries.values) {
            last = 0

            for (id in entry.children.keys) {
                val delta = id - last
                if (format >= FORMAT_SMART) {
                    buf.writeUnsignedIntSmart(delta)
                } else {
                    buf.writeShort(delta)
                }

                last = id
            }
        }

        if (flags and FLAG_NAME_HASHES != 0) {
            for (entry in entries.values) {
                for (childEntry in entry.children.values) {
                    buf.writeInt(childEntry.nameHash)
                }
            }
        }

        return buf.asReadOnly()
    }

    companion object {
        private const val FORMAT_VERSION = 6
        private const val FORMAT_SMART = 7
        private const val FORMAT_MAX = FORMAT_SMART

        private const val FLAG_NAME_HASHES = 0x01
        private const val FLAG_WHIRLPOOL = 0x02
        private const val FLAG_UNKNOWN4 = 0x04
        private const val FLAG_UNKNOWN8 = 0x08

        fun ByteBuf.readRefTable(): ReferenceTable {
            val table = ReferenceTable()

            table.format = readUnsignedByte().toInt()
            if (table.format > FORMAT_MAX) {
                throw IOException("ReferenceTable format is too new")
            }

            if (table.format >= FORMAT_VERSION) {
                table.version = readInt()
            }

            table.flags = readUnsignedByte().toInt()

            val size = if (table.format >= FORMAT_SMART) readUnsignedIntSmart() else readUnsignedShort()

            /* read entry ids and create entries */
            var accumulator = 0
            val ids = IntArray(size) {
                accumulator += if (table.format >= FORMAT_SMART) readUnsignedIntSmart() else readUnsignedShort()
                table.createEntry(accumulator)

                accumulator
            }

            if (table.flags and FLAG_NAME_HASHES != 0) {
                ids.asSequence()
                    .map { table.entries[it]!! }
                    .forEach { it.nameHash = readInt() }
            }

            /* read checksums */
            ids.asSequence()
                .map { table.entries[it]!! }
                .forEach { it.checksum = readInt() }

            // TODO identify this flag
            if (table.flags and FLAG_UNKNOWN8 != 0) {
                for (id in ids) {
                    readInt()
                }
            }

            /* read whirlpool digests */
            if (table.flags and FLAG_WHIRLPOOL != 0) {
                for (id in ids) {
                    val entry = table.entries[id]

                    val digest = ByteArray(Whirlpool.DIGEST_LENGTH)
                    readBytes(digest)
                    entry!!.whirlpoolDigest = Unpooled.wrappedBuffer(digest)
                }
            }

            // TODO identify this flag
            if (table.flags and FLAG_UNKNOWN4 != 0) {
                for (id in ids) {
                    readInt()
                    readInt()
                }
            }

            /* read versions */
            for (id in ids) {
                val entry = table.entries[id]!!
                entry.version = readInt()
            }

            /* read child sizes */
            val childSizes = IntArray(size) {
                if (table.format >= FORMAT_SMART) readUnsignedIntSmart() else readUnsignedShort()
            }

            /* read child ids */
            val childIds = arrayOfNulls<IntArray>(size)
            repeat(size) { i ->
                val entry = table.entries[ids[i]]
                val childSize = childSizes[i]

                accumulator = 0

                childIds[i] = IntArray(childSize) {
                    accumulator += if (table.format >= FORMAT_SMART) readUnsignedIntSmart() else readUnsignedShort()
                    accumulator
                }

                for (childId in childIds[i]!!) {
                    entry!!.createChild(childId)
                }
            }

            if (table.flags and FLAG_NAME_HASHES != 0) {
                repeat(size) { i ->
                    val id = ids[i]
                    val entry = table.entries[id]

                    (0 until childSizes[i]).asSequence()
                        .map { childIds[i]!![it] }
                        .map { entry!!.children[it] }
                        .forEach { it!!.nameHash = readInt() }
                }
            }

            check(!isReadable) { "Trailing bytes after ReferenceTable structure" }

            return table
        }
    }

    inner class Entry(private val table: ReferenceTable, val id: Int) {
        val children = TreeMap<Int, ChildEntry>()
        val childrenByName: HashMultimap<Int, ChildEntry> = HashMultimap.create<Int, ChildEntry>()

        var checksum = 0
        var version = 0

        val size: Int
            get() = children.size

        val truncatedVersion: Int
            get() = version and 0xFFFF

        var whirlpoolDigest = Whirlpool.ZERO_DIGEST
            set(whirlpoolDigest) {
                field = whirlpoolDigest.asReadOnly()
            }

        var nameHash = 0
            set(nameHash) {
                table.entriesByName.remove(this.nameHash, this)
                field = nameHash
                table.entriesByName.put(this.nameHash, this)
            }

        val capacity: Int
            get() = if (children.isEmpty()) 0 else children.lastKey() + 1

        val childIds: List<Int>
            get() = children.keys.toList()

        fun setName(name: String) {
            nameHash = name.hashCodeCp1252()
        }

        fun bumpVersion() {
            version++
        }

        operator fun contains(id: Int): Boolean {
            return id in children
        }

        operator fun contains(name: String): Boolean {
            return childrenByName.containsKey(name.hashCodeCp1252())
        }

        operator fun get(id: Int): ChildEntry? = children[id]

        operator fun get(name: String): ChildEntry? {
            val it = childrenByName.get(name.hashCodeCp1252()).iterator()
            return if (it.hasNext()) it.next() else null
        }

        fun createChild(): ChildEntry {
            (0..FileStore.FILE_LEN)
                .find { it !in children }
                ?.let { return createChild(it) }

            throw IOException("Entry is full")
        }

        fun createChild(id: Int): ChildEntry {
            val child = ChildEntry(this, id)
            require(children.put(id, child) == null) { "ChildEntry with id $id already exists" }
            childrenByName.put(child.nameHash, child)
            return child
        }

        fun removeChild(id: Int) {
            val child = children.remove(id)
            require(child != null) { "ChildEntry with id $id does not exist" }
            childrenByName.remove(child.nameHash, child)
        }
    }

    inner class ChildEntry(private val parent: Entry, val id: Int) {
        var nameHash = 0
            set(nameHash) {
                this.parent.childrenByName.remove(this.nameHash, this)
                field = nameHash
                this.parent.childrenByName.put(this.nameHash, this)
            }

        fun setName(name: String) {
            nameHash = name.hashCodeCp1252()
        }
    }
}
