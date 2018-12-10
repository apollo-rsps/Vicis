package rs.emulate.modern

import io.netty.buffer.ByteBuf
import rs.emulate.modern.Archive.Companion.readArchive
import rs.emulate.modern.Container.Companion.readContainer
import rs.emulate.modern.ReferenceTable.Companion.readRefTable
import rs.emulate.modern.VersionedContainer.Companion.readVersionedContainer
import rs.emulate.modern.fs.FileStore
import rs.emulate.modern.fs.FileStoreOption
import rs.emulate.util.crc32
import rs.emulate.util.crypto.digest.readWhirlpoolDigest
import rs.emulate.util.crypto.xtea.XteaKey
import java.io.Closeable
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.file.Path

class Cache(
    private val store: FileStore,
    private val referenceTables: List<ReferenceTable?>
) : Closeable {

    private val dirtyReferenceTables = BooleanArray(referenceTables.size)

    val listIndexes: List<Int>
        get() = store.listIndexes()

    fun capacity(index: Int): Int {
        return getReferenceTable(index).capacity
    }

    fun listFiles(index: Int): List<Int> {
        return getReferenceTable(index).entryIds
    }

    fun flushArchive(index: Int, entry: ReferenceTable.Entry, key: XteaKey, archive: Archive) {
        entry.bumpVersion()
        write(index, entry, key, archive.write())
        dirtyReferenceTables[index] = true
    }

    fun contains(index: Int, file: Int): Boolean {
        val table = getReferenceTable(index)
        return table.containsEntry(file)
    }

    fun contains(index: Int, name: String): Boolean {
        val table = getReferenceTable(index)
        return table.containsEntry(name)
    }

    /* returns an immutable buffer */
    fun read(index: Int, name: String, key: XteaKey = XteaKey.NONE): ByteBuf {
        val table = getReferenceTable(index)
        val entry = table.getEntry(name) ?: throw FileNotFoundException()

        return read(index, entry, key)
    }

    /* returns an immutable buffer */
    fun read(index: Int, file: Int, key: XteaKey = XteaKey.NONE): ByteBuf {
        val table = getReferenceTable(index)
        val entry = table.getEntry(file) ?: throw FileNotFoundException()

        return read(index, entry, key)
    }

    fun createArchive(index: Int, name: String): CacheArchive {
        val table = getReferenceTable(index)
        val entry = table.createEntry()
        entry.setName(name)
        return createArchive(index, entry)
    }

    fun createArchive(index: Int, file: Int): CacheArchive {
        val table = getReferenceTable(index)
        val entry = table.createEntry(file)
        return createArchive(index, entry)
    }

    fun openArchive(index: Int, name: String, key: XteaKey = XteaKey.NONE): CacheArchive {
        val table = getReferenceTable(index)
        val entry = table.getEntry(name) ?: throw FileNotFoundException()

        return openArchive(index, entry, key)
    }

    fun openArchive(index: Int, file: Int, key: XteaKey = XteaKey.NONE): CacheArchive {
        val table = getReferenceTable(index)
        val entry = table.getEntry(file) ?: throw FileNotFoundException()

        return openArchive(index, entry, key)
    }

    /* accepts any buffer */
    fun write(index: Int, name: String, buf: ByteBuf, key: XteaKey = XteaKey.NONE) {
        val table = getReferenceTable(index)
        val entry = table.getEntry(name)

        if (entry != null) {
            entry.bumpVersion()
            write(index, entry, key, buf)
        } else {
            val newEntry = table.createEntry()
            newEntry.setName(name)
            write(index, newEntry, key, buf)
        }

        dirtyReferenceTables[index] = true
    }

    /* accepts any buffer */
    fun write(index: Int, file: Int, buf: ByteBuf, key: XteaKey = XteaKey.NONE) {
        val table = getReferenceTable(index)
        val entry = table.getEntry(file)

        if (entry != null) {
            entry.bumpVersion()
            write(index, entry, key, buf)
        } else {
            val newEntry = table.createEntry(file)
            write(index, newEntry, key, buf)
        }

        dirtyReferenceTables[index] = true
    }

    fun remove(index: Int, file: Int) {
        val table = getReferenceTable(index)
        table.getEntry(file) ?: throw FileNotFoundException()

        store.remove(index, file)
        table.removeEntry(file)

        dirtyReferenceTables[index] = true
    }

    fun remove(index: Int, name: String) {
        val table = getReferenceTable(index)
        val entry = table.getEntry(name) ?: throw FileNotFoundException()

        val file = entry.id
        store.remove(index, file)
        table.removeEntry(file)

        dirtyReferenceTables[index] = true
    }

    fun flush() {
        referenceTables.indices.asSequence()
            .filter(dirtyReferenceTables::get)
            .forEach {
                val table = referenceTables[it]
                table!!.bumpVersion()

                val buf = Container.pack(table.write())
                store.write(Index.ARCHIVESET, it, buf)

                dirtyReferenceTables[it] = false
            }
    }

    fun createChecksumTable(): ChecksumTable {
        referenceTables.indices.find { dirtyReferenceTables[it] }?.let {
            throw IllegalStateException("flush() must be called before createChecksumTable()")
        }

        val checksumTable = ChecksumTable()

        var index = 0
        while (index < referenceTables.size) {
            val table = referenceTables[index] ?: break

            val buf = store.read(Index.ARCHIVESET, index)

            val entry = checksumTable.addEntry()
            entry.version = table.version
            entry.checksum = buf.crc32()
            entry.whirlpoolDigest = buf.readWhirlpoolDigest()
            index++
        }

        while (index < referenceTables.size) {
            if (referenceTables[index] != null) {
                throw IOException("Reference tables are not contiguous")
            }
            index++
        }

        return checksumTable
    }

    fun readRaw(index: Int, file: Int) = store.read(index, file)

    fun readRaw(index: Int, name: String): ByteBuf {
        val table = getReferenceTable(index)
        val entry = table.getEntry(name) ?: throw FileNotFoundException()

        val file = entry.id
        return store.read(index, file)
    }

    override fun close() {
        store.use { flush() }
    }

    private fun getReferenceTable(index: Int): ReferenceTable {
        return referenceTables[index] ?: throw FileNotFoundException()
    }

    private fun read(index: Int, entry: ReferenceTable.Entry, key: XteaKey): ByteBuf {
        val file = entry.id
        val versionedContainer = store.read(index, file).readVersionedContainer()

        if (versionedContainer.version != entry.truncatedVersion) {
            throw IOException("Container is out of date")
        }

        if (versionedContainer.checksum != entry.checksum) {
            throw IOException("Container is corrupt")
        }

        val versionedContainerBuf = versionedContainer.getBuffer()
        val buf = versionedContainerBuf.readContainer(key).buffer

        if (versionedContainerBuf.isReadable) {
            throw IOException("Trailing bytes after Container structure")
        }

        return buf
    }

    private fun openArchive(index: Int, entry: ReferenceTable.Entry, key: XteaKey): CacheArchive {
        val archive = read(index, entry, key).readArchive(entry)
        return CacheArchive(this, index, archive, entry)
    }

    private fun createArchive(index: Int, entry: ReferenceTable.Entry): CacheArchive {
        val archive = Archive()
        return CacheArchive(this, index, archive, entry)
    }

    private fun write(index: Int, entry: ReferenceTable.Entry, key: XteaKey, buf: ByteBuf) {
        val file = entry.id
        val version = entry.version

        val versionedContainer = VersionedContainer(
            Container.pack(buf, key), version
        )

        entry.checksum = versionedContainer.checksum
        store.write(index, file, versionedContainer.write())
    }

    companion object {
        fun open(root: Path, vararg options: FileStoreOption): Cache {
            return open(FileStore.open(root, *options))
        }

        fun open(store: FileStore): Cache {
            val referenceTables = arrayOfNulls<ReferenceTable>(FileStore.INDEX_LEN - 1)

            for (file in store.listFiles(Index.ARCHIVESET)) {
                val buf = store.read(Index.ARCHIVESET, file)
                val container = buf.readContainer()

                check(!buf.isReadable) { "Trailing bytes after Container structure" }

                buf.release()
                referenceTables[file] = container.buffer.readRefTable()
            }

            return Cache(store, referenceTables.toList())
        }
    }
}
