package rs.emulate.legacy.config

import rs.emulate.legacy.archive.Archive
import rs.emulate.shared.util.DataBuffer
import java.util.ArrayList

/**
 * Decodes data from ArchiveEntry objects in the `config` [Archive].
 *
 * @param config The config [Archive].
 * @param supplier The [DefinitionSupplier].
 * @param T The type the data is decoded into.
 */
class ConfigDecoder<out T : MutableConfigDefinition>(config: Archive, private val supplier: DefinitionSupplier<T>) {

    /**
     * The Buffer containing the data.
     */
    private val data: DataBuffer = config.getEntry(supplier.name + ConfigConstants.DATA_EXTENSION).buffer

    /**
     * The Buffer containing the indices.
     */
    private val index: DataBuffer = config.getEntry(supplier.name + ConfigConstants.INDEX_EXTENSION).buffer

    /**
     * Decodes the data into a [List] of definitions.
     */
    fun decode(): List<T> {
        val count = index.getUnsignedShort()
        val definitions = ArrayList<T>(count)

        var position = java.lang.Short.BYTES // Skip the count

        for (id in 0 until count) {
            data.position(position)
            definitions.add(decode(id))
            position += index.getUnsignedShort()
        }

        return definitions
    }

    /**
     * Decodes an individual definition with the specified id from the specified [DataBuffer].
     */
    private fun decode(id: Int): T {
        val properties = supplier.default

        var opcode = data.getUnsignedByte()
        while (opcode != 0) {
            properties.get<Any>(opcode).decode(data)
            opcode = data.getUnsignedByte()
        }

        return supplier.create(id, properties)
    }

}
