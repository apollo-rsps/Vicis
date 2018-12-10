package rs.emulate.legacy.archive

import io.netty.buffer.ByteBuf
import rs.emulate.legacy.archive.Archive.Companion.entryHash

/**
 * A single entry in an [Archive].
 *
 * @param identifier The identifier.
 * @param buffer The buffer containing this entry's data.
 */
class ArchiveEntry(val identifier: Int, buffer: ByteBuf) {

    init {
        require(buffer.readerIndex() == 0) { "Cannot create an ArchiveEntry with a partially-read buffer" }
    }

    /**
     * The buffer of this entry.
     */
    val buffer: ByteBuf = buffer.copy()
        get() = field.copy()

    /**
     * Gets the size of this entry (i.e. the capacity of the [ByteBuf] backing it), in bytes.
     */
    val size: Int
        get() = buffer.readableBytes()

    /**
     * Creates the archive entry.
     *
     * @param name The name of the archive.
     * @param buffer The buffer containing this entry's data.
     */
    constructor(name: String, buffer: ByteBuf) : this(name.entryHash(), buffer)

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
