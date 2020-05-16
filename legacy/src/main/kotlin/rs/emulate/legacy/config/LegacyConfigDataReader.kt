package rs.emulate.legacy.config

import io.netty.buffer.ByteBuf
import rs.emulate.common.CacheDataReader
import rs.emulate.legacy.FileDescriptor
import rs.emulate.legacy.LegacyCache

class LegacyConfigDataReader(private val fs: LegacyCache, private val entryName: String) : CacheDataReader<Int> {

    private val config by lazy { fs.getArchive(FileDescriptor(ARCHIVE_INDEX, CONFIG_ARCHIVE_ID)) }

    override fun load(id: Int): ByteBuf {
        val data = config[entryName + Config.DATA_EXTENSION].buffer
        val index = config[entryName + Config.INDEX_EXTENSION].buffer

        val indexPosition = (id + 1) * Short.SIZE_BYTES
        if (indexPosition + Short.SIZE_BYTES > index.writerIndex()) {
            error("todo")
        }

        var position = Short.SIZE_BYTES // skip the count
        index.readerIndex(position)

        repeat(id) {
            position += index.readUnsignedShort()
        }

        val length = index.getUnsignedShort(indexPosition)

        return if (position + length > data.writerIndex()) {
            error("")
        } else {
            data.slice(position, length)
        }
    }

    override fun listing(): List<Int> {
        val index = config[entryName + Config.INDEX_EXTENSION].buffer
        return (0 until index.readableBytes() / Short.SIZE_BYTES - 2).toList()
    }

    private companion object { // TODO move these elsewhere
        const val ARCHIVE_INDEX = 0
        const val CONFIG_ARCHIVE_ID = 2
    }

}
