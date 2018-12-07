package rs.emulate.legacy.config

import rs.emulate.legacy.archive.Archive

object ConfigArchiveDecoder {

    /**
     * The extension for entries containing data.
     */
    private const val DATA_EXTENSION = ".dat"

    /**
     * The extension for entries containing the index.
     */
    private const val INDEX_EXTENSION = ".idx"

    fun <T> decode(config: Archive, decoder: ConfigDecoder<T>): List<T> {
        val data = config.getEntry(decoder.entryName + DATA_EXTENSION).buffer
        val index = config.getEntry(decoder.entryName + INDEX_EXTENSION).buffer

        val count = index.getUnsignedShort()
        val definitions = ArrayList<T>(count)

        var position = java.lang.Short.BYTES // Skip the count

        for (id in 0 until count) {
            data.position(position)

            definitions += decoder.decode(id, data)
            position += index.getUnsignedShort()
        }

        return definitions
    }

}
