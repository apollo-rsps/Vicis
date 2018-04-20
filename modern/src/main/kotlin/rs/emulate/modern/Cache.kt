package rs.emulate.modern

import rs.emulate.shared.util.DataBuffer
import rs.emulate.shared.util.crypto.Whirlpool
import java.io.Closeable
import java.io.FileNotFoundException
import java.nio.ByteBuffer
import java.util.zip.CRC32

/**
 * The [Cache] class provides a unified, high-level API for modifying the cache of a Jagex game.
 *
 * @param store The file store that backs this cache.
 */
class Cache(val store: FileStore) : Closeable {

    /**
     * Gets the number of index files, not including the meta index file.
     */
    val typeCount: Int
        get() = store.typeCount

    override fun close() {
        store.close()
    }

    /**
     * Computes the [ChecksumTable] for this cache.
     */
    fun createChecksumTable(): ChecksumTable {
        val size = store.typeCount
        val table = ChecksumTable(size)

        for (id in 0 until size) {
            val buffer = store.read(255, id)

            var crc = 0
            var version = 0
            var whirlpool = DataBuffer.allocate(64)

            // if there is actually a reference table, calculate the CRC, version and whirlpool hash
            if (buffer.limit() > 0) { // some indices are not used, is this appropriate?
                val references = ReferenceTable.decode(Container.decode(buffer).data)
                crc = buffer.getCrcChecksum()
                version = references.version
                buffer.position(0)
                whirlpool = buffer.whirlpool()
            }

            table.setEntry(id, ChecksumTable.Entry(crc, version, whirlpool))
        }

        return table
    }

    /**
     * Gets the number of files of the specified type.
     */
    fun getFileCount(type: Int): Int {
        return store.getFileCount(type)
    }

    /**
     * Reads a file from the cache.
     */
    fun read(type: Int, file: Int): Container {
        require(type == 255) { "Reference tables can only be read with the low level FileStore API." }

        return Container.decode(store.read(type, file))
    }

    /**
     * Reads a file contained in an archive in the cache.
     */
    fun read(type: Int, file: Int, member: Int): ByteBuffer {
        val container = read(type, file)
        val tableContainer = Container.decode(store.read(255, type))
        val table = ReferenceTable.decode(tableContainer.data)

        val entry = table.getEntry(file)
        if (entry == null || member < 0 || member >= entry.capacity()) {
            throw FileNotFoundException()
        }

        var validMember = 0
        for (index in 0 until member) {
            if (entry.getEntry(index) != null) {
                validMember++
            }
        }

        val archive = Archive.decode(container.data, entry.size)
        return archive.getEntry(validMember)!!
    }

    /**
     * Writes a file to the cache and updates the [ReferenceTable] that it is associated with.
     */
    fun write(type: Int, file: Int, container: Container) {
        require(type == 255) { "Reference tables can only be read with the low level FileStore API." }

        container.version = container.version + 1

        var tableContainer = Container.decode(store.read(255, type))
        val table = ReferenceTable.decode(tableContainer.data)

        val buffer = container.encode()
        val bytes = ByteArray(buffer.limit() - 2) // last two bytes are the version and shouldn't be included
        buffer.mark()
        try {
            buffer.position(0)
            buffer.read(bytes)
        } finally {
            buffer.reset()
        }

        val crc = CRC32()
        crc.update(bytes, 0, bytes.size)

        var entry: Entry? = table.getEntry(file)
        if (entry == null) {
            entry = Entry()
            table.putEntry(file, entry)
        }

        entry.version = container.version
        entry.crc = crc.value.toInt()

        if (table.flags and ReferenceTable.HAS_HASHES != 0) {
            val whirlpool = Whirlpool.whirlpool(bytes, 0, bytes.size)
            entry.setWhirlpool(whirlpool)
        }

        table.version = table.version + 1

        tableContainer = Container(tableContainer.type, table.encode())
        store.write(255, type, tableContainer.encode().byteBuffer)

        store.write(type, file, buffer.byteBuffer)
    }

    /**
     * Writes a file contained in an archive to the cache.
     *
     * @param type The type of file.
     * @param file The id of the archive.
     * @param member The file within the archive.
     * @param data The data to write.
     */
    fun write(type: Int, file: Int, member: Int, data: ByteBuffer) {
        var tableContainer = Container.decode(store.read(255, type))
        val table = ReferenceTable.decode(tableContainer.data)

        var entry: Entry? = table.getEntry(file)
        var oldArchiveSize = -1
        if (entry == null) {
            entry = Entry()
            table.putEntry(file, entry)
        } else {
            oldArchiveSize = entry.capacity()
        }

        var child: ChildEntry? = entry.getEntry(member)
        if (child == null) {
            child = ChildEntry()
            entry.putEntry(member, child)
        }

        var archive: Archive
        val containerType: Int
        val containerVersion: Int

        if (file < store.getFileCount(type) && oldArchiveSize != -1) {
            val container = read(type, file)
            containerType = container.type
            containerVersion = container.version

            archive = Archive.decode(container.data, oldArchiveSize)
        } else {
            containerType = Container.COMPRESSION_GZIP
            containerVersion = 1

            archive = Archive(member + 1)
        }

        if (member >= archive.size()) {
            val newArchive = Archive(member + 1)
            for (id in 0 until archive.size()) {
                newArchive.putEntry(id, archive.getEntry(id)!!)
            }

            archive = newArchive
        }

        archive.putEntry(member, data)

        for (id in 0 until archive.size()) {
            if (archive.getEntry(id) == null) {
                entry.putEntry(id, ChildEntry())
                archive.putEntry(id, ByteBuffer.allocate(1))
            }
        }

        tableContainer = Container(tableContainer.type, table.encode())
        store.write(255, type, tableContainer.encode().byteBuffer)

        val container = Container(containerType, archive.encode(), containerVersion)
        write(type, file, container)
    }

}
