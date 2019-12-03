package rs.emulate.modern.codec.bundle

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import rs.emulate.modern.codec.Archive
import rs.emulate.modern.codec.Archive.Companion.readArchive
import rs.emulate.modern.codec.Container
import rs.emulate.modern.codec.Container.Companion.readContainer
import rs.emulate.modern.codec.ReferenceTable
import rs.emulate.modern.codec.ReferenceTable.Companion.readRefTable
import java.io.FileNotFoundException
import java.util.*

class Bundle(private val referenceTable: ReferenceTable = ReferenceTable()) {

    private val entries = TreeMap<Int, ByteBuf>()
    private var dirtyReferenceTable = false

    val capacity: Int
        get() = referenceTable.capacity

    val listFiles: List<Int>
        get() = referenceTable.entryIds

    private fun createArchive(entry: ReferenceTable.Entry): BundleArchive {
        val archive = Archive()
        return BundleArchive(this, archive, entry)
    }

    fun createArchive(name: String): BundleArchive {
        val entry = referenceTable.createEntry()
        entry.setName(name)
        return createArchive(entry)
    }

    fun createArchive(file: Int): BundleArchive {
        val entry = referenceTable.createEntry(file)
        return createArchive(entry)
    }

    private fun openArchive(entry: ReferenceTable.Entry): BundleArchive {
        val archive = read(entry).readArchive(entry)
        return BundleArchive(this, archive, entry)
    }

    fun openArchive(name: String): BundleArchive {
        return openArchive(referenceTable.getEntry(name) ?: throw FileNotFoundException())
    }

    fun openArchive(file: Int): BundleArchive {
        return openArchive(referenceTable.getEntry(file) ?: throw FileNotFoundException())
    }

    fun flushArchive(entry: ReferenceTable.Entry, archive: Archive) {
        entry.bumpVersion()
        write(entry, archive.write())
        dirtyReferenceTable = true
    }

    operator fun contains(file: Int) = referenceTable.containsEntry(file)
    operator fun contains(name: String) = referenceTable.containsEntry(name)

    fun read(file: Int): ByteBuf {
        return read(referenceTable.getEntry(file) ?: throw FileNotFoundException())
    }

    fun read(name: String): ByteBuf {
        return read(referenceTable.getEntry(name) ?: throw FileNotFoundException())
    }

    private fun read(entry: ReferenceTable.Entry): ByteBuf {
        return requireNotNull(entries[entry.id]) { "Entry in ReferenceTable but not in Bundle" }
    }

    fun write(file: Int, buf: ByteBuf) {
        val entry = referenceTable.getEntry(file)

        if (entry != null) {
            entry.bumpVersion()
            write(entry, buf)
        } else {
            val newEntry = referenceTable.createEntry(file)
            write(newEntry, buf)
        }

        dirtyReferenceTable = true
    }

    fun write(name: String, buf: ByteBuf) {
        val entry = referenceTable.getEntry(name)

        if (entry != null) {
            entry.bumpVersion()
            write(entry, buf)
        } else {
            val newEntry = referenceTable.createEntry()
            newEntry.setName(name)
            write(newEntry, buf)
        }

        dirtyReferenceTable = true
    }

    private fun write(entry: ReferenceTable.Entry, buf: ByteBuf) {
        entries[entry.id] = buf
    }

    fun remove(file: Int) {
        referenceTable.getEntry(file) ?: throw FileNotFoundException()

        entries.remove(file)
        referenceTable.removeEntry(file)

        dirtyReferenceTable = true
    }

    fun remove(name: String) {
        val entry = referenceTable.getEntry(name) ?: throw FileNotFoundException()

        val file = entry.id
        entries.remove(file)
        referenceTable.removeEntry(file)

        dirtyReferenceTable = true
    }

    fun write(): ByteBuf {
        if (dirtyReferenceTable) {
            dirtyReferenceTable = false
            referenceTable.bumpVersion()
        }

        val buf = Unpooled.compositeBuffer()
        buf.addComponent(true, Container.pack(referenceTable.write()))

        for (entry in entries.values) {
            buf.addComponent(true, Container.pack(entry))
        }

        return buf.asReadOnly()
    }

    companion object {
        fun ByteBuf.readBundle(): Bundle {
            val table = readContainer().buffer.readRefTable()

            val bundle = Bundle(table)

            for (id in table.entryIds) {
                bundle.entries[id] = readContainer().buffer
            }

            return bundle
        }
    }
}
