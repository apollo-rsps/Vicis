package rs.emulate.legacy.archive

import rs.emulate.shared.util.CompressionUtils
import rs.emulate.shared.util.get
import rs.emulate.shared.util.getRemainingBytes
import rs.emulate.shared.util.getUnsignedShort
import rs.emulate.shared.util.getUnsignedTriByte
import rs.emulate.shared.util.isEmpty
import rs.emulate.shared.util.putTriByte
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.util.ArrayList

/**
 * Contains methods for encoding and decoding [Archive]s.
 */
object ArchiveCodec {

    /**
     * Decodes the [Archive] in the specified [ByteBuffer].
     * @throws IOException If there is an error decompressing the data.
     */
    fun decode(buffer: ByteBuffer): Archive {
        var buffer = buffer
        if (buffer.isEmpty()) {
            return Archive.EMPTY_ARCHIVE
        }

        val extractedSize = buffer.getUnsignedTriByte()
        val size = buffer.getUnsignedTriByte()
        var extracted = false

        if (size != extractedSize) {
            buffer = CompressionUtils.bunzip2(buffer)
            extracted = true
        }

        val count = buffer.getUnsignedShort()
        val identifiers = IntArray(count)
        val extractedSizes = IntArray(count)
        val sizes = IntArray(count)

        for (entry in 0 until count) {
            identifiers[entry] = buffer.getInt()
            extractedSizes[entry] = buffer.getUnsignedTriByte()
            sizes[entry] = buffer.getUnsignedTriByte()
        }

        val entries = ArrayList<ArchiveEntry>(count)

        for (entry in 0 until count) {
            val uncompressedSize = if (extracted) extractedSizes[entry] else sizes[entry]
            val data = ByteBuffer.allocate(uncompressedSize).apply {
                buffer.get(this)
                flip()
            }

            val uncompressed = if (extracted) data else CompressionUtils.bunzip2(data)
            entries.add(ArchiveEntry(identifiers[entry], uncompressed))
        }

        return Archive(entries)
    }

    // TODO theres a bug with archive compression...

    /**
     * Encodes the specified [Archive] into a [ByteBuffer].
     * @throws IOException If there is an error compressing the archive.
     */
    fun encode(archive: Archive, compression: CompressionType): ByteBuffer {
        ByteArrayOutputStream(archive.size).use { bos ->
            val entries = archive.entries
            val entryCount = entries.size

            val meta = ByteBuffer.allocate(entryCount * (Integer.BYTES + 2 * 3) + java.lang.Short.BYTES)
            meta.putShort(entryCount.toShort())

            for (entry in entries) {
                val uncompressed = entry.buffer
                val compressed = CompressionUtils.bzip2(uncompressed)
                uncompressed.position(0) // We just read from this buffer, so reset the position.

                meta.putInt(entry.identifier)
                meta.putTriByte(uncompressed.remaining())
                meta.putTriByte(compressed.remaining())

                when (compression) {
                    CompressionType.ARCHIVE_COMPRESSION, CompressionType.NO_COMPRESSION -> bos.write(
                        uncompressed.getRemainingBytes())
                    CompressionType.ENTRY_COMPRESSION -> bos.write(compressed.getRemainingBytes())
                }
            }

            meta.flip()

            val compressed = bos.toByteArray()
            var data = ByteBuffer.allocate(meta.limit() + compressed.size)
            data.put(meta).put(compressed).flip()

            val header = ByteBuffer.allocate(2 * 3)
            val extracted = data.limit()
            header.putTriByte(extracted)

            if (compression === CompressionType.ARCHIVE_COMPRESSION) {
                data = CompressionUtils.bzip2(data)
            }

            val compressedLength = data.limit()
            header.putTriByte(compressedLength).flip()

            val buffer = ByteBuffer.allocate(header.limit() + data.limit())
            buffer.put(header).put(data).flip()

            return buffer
        }
    }

}
