package rs.emulate.modern

import rs.emulate.shared.util.CacheStringUtils
import rs.emulate.shared.util.DataBuffer
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.nio.ByteBuffer
import java.util.TreeMap

/**
 * A [ReferenceTable] holds details for all the files with a single type, such as checksums, versions and archive
 * members. There are also optional fields for identifier hashes and whirlpool digests.
 */
class ReferenceTable {

    private val entries = TreeMap<Int, Entry>()

    var flags: Int = 0

    var format: Int = 0

    var version: Int = 0

    /**
     * Gets the maximum number of entries in this table.
     */
    fun capacity(): Int {
        return if (entries.isEmpty()) 0 else entries.lastKey() + 1
    }

    /**
     * Encodes this [ReferenceTable] into a [ByteBuffer].
     */
    fun encode(): DataBuffer {
        // we can't (easily) predict the size ahead of time, so we write to a stream and then to the buffer
        val bout = ByteArrayOutputStream()
        DataOutputStream(bout).use { os ->
            os.write(format)
            if (format >= 6) {
                os.writeInt(version)
            }

            os.write(flags)
            val size = entries.size

            if (format >= 7 && size >= 65535) {
                os.writeInt(size)
            } else {
                os.writeShort(size)
            }

            var last = 0
            for (id in 0 until capacity()) {
                if (entries.containsKey(id)) {
                    val delta = id - last
                    os.writeShort(delta)
                    last = id
                }
            }

            if (flags and HAS_IDENTIFIERS != 0) {
                for (entry in entries.values) {
                    os.writeInt(entry.identifier)
                }
            }

            for (entry in entries.values) {
                os.writeInt(entry.crc)
            }

            if (flags and HAS_HASHES != 0) {
                for (entry in entries.values) {
                    os.write(entry.getWhirlpool())
                }
            }

            for (entry in entries.values) {
                os.writeInt(entry.version)
            }

            for (entry in entries.values) {
                os.writeShort(entry.size)
            }

            for (entry in entries.values) {
                last = 0
                for (id in 0 until entry.capacity()) {
                    if (entry.hasChild(id)) {
                        val delta = id - last
                        os.writeShort(delta)
                        last = id
                    }
                }
            }

            if (flags and HAS_IDENTIFIERS != 0) {
                for (entry in entries.values) {
                    for (child in entry.children) {
                        os.writeInt(child.identifier)
                    }
                }
            }

            return DataBuffer.wrap(bout.toByteArray())
        }
    }

    /**
     * Gets the entry with the specified id, or `null` if it does not exist.
     */
    fun getEntry(id: Int): Entry? {
        return entries[id]
    }

    /**
     * Gets the child entry with the specified id, or `null` if it does not exist.
     */
    fun getEntry(id: Int, child: Int): ChildEntry? {
        val entry = entries[id] ?: return null
        return entry.getEntry(child)
    }

    /**
     * Replaces or inserts the entry with the specified id.
     */
    fun putEntry(id: Int, entry: Entry) {
        entries[id] = entry
    }

    /**
     * Removes the entry with the specified id.
     */
    fun removeEntry(id: Int) {
        entries.remove(id)
    }

    /**
     * Gets the number of actual entries.
     */
    fun size(): Int {
        return entries.size
    }

    companion object {

        /**
         * A flag which indicates this [ReferenceTable] contains whirlpool digests for its entries.
         */
        const val HAS_HASHES = 0x02

        /**
         * A flag which indicates this [ReferenceTable] contains [CacheStringUtils] hashed identifiers.
         */
        const val HAS_IDENTIFIERS = 0x01

        private const val HAS_UNKNOWN_4 = 0x4

        private const val HAS_UNKNOWN_8 = 0x8

        /**
         * Decodes the checksum table contained in the specified [ByteBuffer].
         */
        fun decode(buffer: DataBuffer): ReferenceTable {
            val table = ReferenceTable()

            table.format = buffer.getUnsignedByte()
            if (table.format >= 6) {
                table.version = buffer.getInt()
            }

            table.flags = buffer.getUnsignedByte()

            val entryCount = if (table.format >= 7) buffer.largeSmart else buffer.getUnsignedShort()

            val ids = IntArray(entryCount)
            var accumulator = 0
            var size = -1

            for (i in ids.indices) {
                val delta = if (table.format >= 7) buffer.largeSmart else buffer.getUnsignedShort()
                accumulator += delta
                ids[i] = accumulator

                if (ids[i] > size) {
                    size = ids[i]
                }
            }
            size++

            for (id in ids) {
                table.entries[id] = Entry()
            }

            if (table.flags and HAS_IDENTIFIERS != 0) {
                for (id in ids) {
                    table.entries[id]!!.identifier = buffer.getInt()
                }
            }

            for (id in ids) {
                table.entries[id]!!.crc = buffer.getInt()
            }

            if (table.flags and HAS_UNKNOWN_8 != 0) {
                for (time in ids.indices) {
                    buffer.getInt()
                }
            }

            if (table.flags and HAS_HASHES != 0) {
                for (id in ids) {
                    buffer.read(table.entries[id]!!.getWhirlpool())
                }
            }

            if (table.flags and HAS_UNKNOWN_4 != 0) {
                for (time in ids.indices) {
                    buffer.getInt()
                    buffer.getInt()
                }
            }

            for (id in ids) {
                table.entries[id]!!.version = buffer.getInt()
            }

            val members = arrayOfNulls<IntArray>(size)
            for (id in ids) {
                members[id] = IntArray(buffer.getUnsignedShort())
            }

            for (id in ids) {
                accumulator = 0
                size = -1

                val member = members[id]!!

                for (i in 0 until member.size) {
                    val delta = buffer.getUnsignedShort()
                    accumulator += delta
                    member[i] = accumulator
                    if (member[i] > size) {
                        size = member[i]
                    }
                }

                size++

                for (child in member) {
                    table.entries[id]!!.putEntry(child, ChildEntry())
                }
            }

            if (table.flags and HAS_IDENTIFIERS != 0) {
                for (id in ids) {
                    for (child in members[id]!!) {
                        table.entries[id]!!.getEntry(child)!!.identifier = buffer.getInt()
                    }
                }
            }

            return table
        }

        /**
         * Decodes the specified [Container] into a reference table.
         */
        fun decode(container: Container): ReferenceTable {
            return decode(container.data)
        }
    }

}
