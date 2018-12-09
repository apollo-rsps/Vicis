package rs.emulate.legacy.archive

import rs.emulate.legacy.archive.Archive.Companion.entryHash
import rs.emulate.util.copy
import java.nio.ByteBuffer

/**
 * A single entry in an [Archive].
 *
 * @param identifier The identifier.
 * @param buffer The buffer containing this entry's data.
 */
class ArchiveEntry(val identifier: Int, buffer: ByteBuffer) {

    /**
     * The buffer of this entry.
     */
    val buffer: ByteBuffer = buffer.asReadOnlyBuffer()
        get() = field.copy()

    /**
     * Gets the size of this entry (i.e. the capacity of the [ByteBuffer] backing it), in bytes.
     */
    val size: Int
        get() = buffer.limit()

    /**
     * Creates the archive entry.
     *
     * @param name The name of the archive.
     * @param buffer The buffer containing this entry's data.
     */
    constructor(name: String, buffer: ByteBuffer) : this(name.entryHash(), buffer)

    override fun equals(other: Any?): Boolean {
        if (other is ArchiveEntry) {
            return identifier == other.identifier && buffer == other.buffer
        }

        return false
    }

    override fun hashCode(): Int {
        return 31 * buffer.hashCode() + identifier
    }

}
