package rs.emulate.legacy.config

import rs.emulate.legacy.archive.Archive
import rs.emulate.util.get
import rs.emulate.util.getUnsignedShort
import java.nio.ByteBuffer

object ConfigEntryDecoder {

    fun <T : Definition> count(config: Archive, decoder: ConfigDecoder<T>): Int {
        val index = config[decoder.entryName + Config.INDEX_EXTENSION].buffer
        return index.getUnsignedShort()
    }

    fun <T : Definition> decode(config: Archive, decoder: ConfigDecoder<T>): List<T> {
        val data = config[decoder.entryName + Config.DATA_EXTENSION].buffer
        val index = config[decoder.entryName + Config.INDEX_EXTENSION].buffer

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

    fun get(config: Archive, entry: String, file: Int): ByteBuffer? {
        val data = config[entry + Config.DATA_EXTENSION].buffer
        val index = config[entry + Config.INDEX_EXTENSION].buffer

        val position = (file + 1) * java.lang.Short.BYTES // +1 to skip the count
        if (position < index.remaining()) {
            index.position(position)
            val fileStart = index.getUnsignedShort()
            val nextFileStart = index.getUnsignedShort()

            data.position(fileStart)

            return ByteBuffer.allocate(nextFileStart - fileStart).also {
                data.get(it)
            }
        }

        return null
    }

}
