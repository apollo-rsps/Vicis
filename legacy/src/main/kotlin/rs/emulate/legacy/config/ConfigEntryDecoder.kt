package rs.emulate.legacy.config

import io.netty.buffer.ByteBuf
import rs.emulate.legacy.archive.Archive

object ConfigEntryDecoder {

    fun <T : Definition> count(config: Archive, decoder: ConfigDecoder<T>): Int {
        val index = config[decoder.entryName + Config.INDEX_EXTENSION].buffer
        return index.readUnsignedShort()
    }

    fun <T : Definition> decode(config: Archive, decoder: ConfigDecoder<T>): List<T> {
        val data = config[decoder.entryName + Config.DATA_EXTENSION].buffer
        val index = config[decoder.entryName + Config.INDEX_EXTENSION].buffer

        val count = index.readUnsignedShort()
        val definitions = ArrayList<T>(count)

        var position = Short.SIZE_BYTES // Skip the count

        for (id in 0 until count) {
            data.readerIndex(position)

            definitions += decoder.decode(id, data)
            position += index.readUnsignedShort()
        }

        return definitions
    }

    fun get(config: Archive, entry: String, file: Int): ByteBuf? {
        val data = config[entry + Config.DATA_EXTENSION].buffer
        val index = config[entry + Config.INDEX_EXTENSION].buffer

        val indexPosition = (file + 1) * Short.SIZE_BYTES
        if (indexPosition + Short.SIZE_BYTES > index.writerIndex()) {
            return null
        }

        var position = Short.SIZE_BYTES // skip the count
        index.readerIndex(position)

        repeat(file) {
            position += index.readUnsignedShort()
        }

        val length = index.getUnsignedShort(indexPosition)

        return if (position + length > data.writerIndex()) {
            null
        } else {
            data.slice(position, length)
        }
    }

}
