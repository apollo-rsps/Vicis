package rs.emulate.legacy.archive

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import rs.emulate.legacy.archive.CompressionType.ARCHIVE_COMPRESSION
import rs.emulate.legacy.archive.CompressionType.ENTRY_COMPRESSION
import rs.emulate.legacy.archive.CompressionType.NO_COMPRESSION
import rs.emulate.util.compression.bunzip2
import rs.emulate.util.compression.bzip2
import rs.emulate.util.readUnsignedTriByte
import rs.emulate.util.toByteArray
import rs.emulate.util.writeTriByte
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.ArrayList

/**
 * Contains methods for encoding and decoding [Archive]s.
 */
object ArchiveCodec {

    /**
     * Decodes the [Archive] in the specified [ByteBuf].
     * @throws IOException If there is an error decompressing the data.
     */
    fun decode(buffer: ByteBuf): Archive {
        var buffer = buffer
        if (buffer.readableBytes() == 0) {
            return Archive.EMPTY_ARCHIVE
        }

        val extractedSize = buffer.readUnsignedTriByte()
        val size = buffer.readUnsignedTriByte()
        var extracted = false

        if (size != extractedSize) {
            buffer = buffer.bunzip2(extractedSize)
            extracted = true
        }

        val count = buffer.readUnsignedShort()
        val identifiers = IntArray(count)
        val extractedSizes = IntArray(count)
        val compressedSizes = IntArray(count)

        for (entry in 0 until count) {
            identifiers[entry] = buffer.readInt()
            extractedSizes[entry] = buffer.readUnsignedTriByte()
            compressedSizes[entry] = buffer.readUnsignedTriByte()
        }

        val entries = ArrayList<ArchiveEntry>(count)

        for (entry in 0 until count) {
            val entrySize = if (extracted) extractedSizes[entry] else compressedSizes[entry]
            val data = buffer.readSlice(entrySize)
            val uncompressed = if (extracted) data else data.bunzip2(extractedSizes[entry])

            entries += ArchiveEntry(identifiers[entry], uncompressed)
        }

        return Archive(entries)
    }

    /**
     * Encodes the specified [Archive] into a [ByteBuf].
     * @throws IOException If there is an error compressing the archive.
     */
    fun encode(archive: Archive, compression: CompressionType): ByteBuf {
        ByteArrayOutputStream(archive.size).use { bos ->
            val entries = archive.entries
            val entryCount = entries.size

            val meta = Unpooled.buffer(entryCount * (Integer.BYTES + 2 * 3) + java.lang.Short.BYTES)
            meta.writeShort(entryCount)

            for (entry in entries) {
                val uncompressed = entry.buffer
                val compressed = uncompressed.bzip2()
                uncompressed.readerIndex(0) // We just read from this buffer, so reset the position

                meta.writeInt(entry.identifier)
                meta.writeTriByte(uncompressed.readableBytes())
                meta.writeTriByte(compressed.readableBytes())

                when (compression) {
                    ARCHIVE_COMPRESSION, NO_COMPRESSION -> bos.write(uncompressed.toByteArray())
                    ENTRY_COMPRESSION -> bos.write(compressed.toByteArray())
                }
            }

            val compressed = bos.toByteArray()
            var data = Unpooled.buffer(meta.readableBytes() + compressed.size)
            data.writeBytes(meta).writeBytes(compressed)

            val header = Unpooled.buffer(2 * 3)
            val extracted = data.readableBytes()
            header.writeTriByte(extracted)

            if (compression === ARCHIVE_COMPRESSION) {
                data = data.bzip2()
            }

            val compressedLength = data.readableBytes()
            header.writeTriByte(compressedLength)

            val buffer = Unpooled.buffer(header.readableBytes() + data.readableBytes())
            buffer.writeBytes(header).writeBytes(data)

            return buffer
        }
    }

}
