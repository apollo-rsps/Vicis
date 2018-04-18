package rs.emulate.legacy.config

import rs.emulate.legacy.archive.Archive
import rs.emulate.legacy.archive.ArchiveEntry
import rs.emulate.legacy.archive.ArchiveUtils
import rs.emulate.shared.util.DataBuffer

import java.io.ByteArrayOutputStream
import kotlin.collections.Map.Entry

/**
 * Encodes data from [ArchiveEntry] objects in the `config` [Archive].
 *
 * @param name The name of the [ArchiveEntry].
 * @param definitions The [List] of definitions to encode.
 * @param T The type the data is encoded from.
 */
class ConfigEncoder<out T : MutableConfigDefinition>(private val name: String, private val definitions: List<T>) {

    /**
     * Encodes the [List] of `T`s into an [ArchiveEntry].
     *
     * @return The archive entry.
     */
    fun encode(): Array<ArchiveEntry> {
        val data = ByteArrayOutputStream()
        val index = ByteArrayOutputStream()

        val size = definitions.size
        var last = 2
        writeShort(data, size)
        writeShort(index, size)

        for (definition in definitions) {
            definition.serializableProperties()
                .filter(this::validProperty)
                .forEach { write(it, data) }
            data.write(ConfigConstants.DEFINITION_TERMINATOR)

            val change = data.size() - last
            writeShort(index, change)
            last = data.size()
        }

        var hash = ArchiveUtils.hash(name + ConfigConstants.DATA_EXTENSION)
        val dataEntry = ArchiveEntry(hash, DataBuffer.wrap(data.toByteArray()))

        hash = ArchiveUtils.hash(name + ConfigConstants.INDEX_EXTENSION)
        val indexEntry = ArchiveEntry(hash, DataBuffer.wrap(index.toByteArray()))

        return arrayOf(dataEntry, indexEntry)
    }

    /**
     * Encodes the [List] of `T`s into an [ArchiveEntry], and returns a new [Archive] containing
     * the entries in the specified Archive, and the newly created one.
     */
    fun encodeInto(archive: Archive): Archive {
        return archive.addEntries(*encode())
    }

    /**
     * Returns whether or not the specified [Map] entry contains a [SerializableProperty] that should be
     * encoded.
     */
    private fun validProperty(entry: Entry<Int, SerializableProperty<*>>): Boolean {
        return entry.value.valuePresent()
    }

    /**
     * Writes the specified [Map] entry containing the opcode and [SerializableProperty] to the specified
     * [ByteArrayOutputStream].
     */
    private fun write(entry: Entry<Int, SerializableProperty<*>>, os: ByteArrayOutputStream) {
        val buffer = entry.value.encode()
        val bytes = buffer.remainingBytes

        os.write(entry.key)
        os.write(bytes)
    }

    /**
     * Writes a `short` (i.e. a 16-bit value) to the specified [ByteArrayOutputStream].
     */
    private fun writeShort(stream: ByteArrayOutputStream, value: Int) {
        stream.write(value shr 8)
        stream.write(value)
    }

}
