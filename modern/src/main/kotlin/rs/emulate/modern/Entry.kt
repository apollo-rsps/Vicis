package rs.emulate.modern

import java.util.TreeMap

/**
 * A single entry in a [ReferenceTable].
 */
class Entry {

    /**
     * The CRC32 checksum of this entry.
     */
    var crc: Int = 0

    /**
     * The children in this entry.
     */
    private val entries = TreeMap<Int, ChildEntry>()

    var identifier = -1

    var version: Int = 0

    private var whirlpool = ByteArray(64)

    val children: Collection<ChildEntry>
        get() = entries.values

    /**
     * The number of actual child entries.
     */
    val size: Int
        get() = entries.size

    /**
     * Gets the maximum number of child entries.
     */
    fun capacity(): Int {
        return if (entries.isEmpty()) 0 else entries.lastKey() + 1
    }

    /**
     * Gets the child entry with the specified id.
     */
    fun getEntry(id: Int): ChildEntry? {
        return entries[id]
    }

    /**
     * Gets the whirlpool digest of this entry.
     */
    fun getWhirlpool(): ByteArray {
        return whirlpool.clone()
    }

    /**
     * Returns whether or not this Entry has a [ChildEntry] with the specified id.
     */
    fun hasChild(id: Int): Boolean {
        return entries.containsKey(id)
    }

    /**
     * Replaces or inserts the child entry with the specified id.
     */
    fun putEntry(id: Int, entry: ChildEntry) {
        entries[id] = entry
    }

    /**
     * Removes the entry with the specified id.
     */
    fun removeEntry(id: Int) {
        entries.remove(id)
    }

    /**
     * Sets the whirlpool digest of this entry.
     */
    fun setWhirlpool(whirlpool: ByteArray) {
        require(whirlpool.size == 64) { ("Whirlpool length must be 64.") }
        this.whirlpool = whirlpool.clone()
    }

}
