package rs.emulate.legacy.archive

import rs.emulate.shared.util.DataBuffer

/**
 * A single entry in an [Archive]. This class is immutable.
 *
 * @param identifier The identifier.
 * @param buffer The buffer containing this entry's data.
 */
class ArchiveEntry(val identifier: Int, buffer: DataBuffer) {

    /**
     * The buffer of this entry.
     */
    val buffer: DataBuffer = buffer.asReadOnlyBuffer()
        get() = field.copy()

    /**
     * Gets the size of this entry (i.e. the capacity of the [DataBuffer] backing it), in bytes.
     */
    val size: Int
        get() = buffer.limit()

    /**
     * Creates the archive entry.
     *
     * @param name The name of the archive.
     * @param buffer The buffer containing this entry's data.
     */
    constructor(name: String, buffer: DataBuffer) : this(ArchiveUtils.hash(name), buffer) {}

    override fun equals(other: Any?): Boolean {
        if (other is ArchiveEntry) {
            return identifier == other.identifier && buffer == other.buffer
        }

        return false
    }

    override fun hashCode(): Int {
        return 31 * buffer.hashCode() + identifier
    }

    /**
     * Returns a new archive entry with this entry's identifier, but the contents as the specified [DataBuffer].
     */
    fun update(buffer: DataBuffer): ArchiveEntry {
        return ArchiveEntry(identifier, buffer)
    }

}
