package rs.emulate.shared.util

import java.io.EOFException
import java.io.IOException
import java.nio.channels.FileChannel

/**
 * Contains [FileChannel]-related utility methods.
 */
object FileChannelUtils {

    /**
     * Reads as much as possible from the channel into the buffer.
     *
     * @param channel The channel.
     * @param buffer The buffer.
     * @param position The initial position in the channel.
     * @throws IOException If an I/O error occurs.
     * @throws EOFException If the end of the file was reached and the buffer could not be completely populated.
     */
    fun readFully(channel: FileChannel, buffer: DataBuffer, position: Long) {
        var position = position

        while (buffer.remaining() > 0) {
            val read = channel.read(buffer.byteBuffer, position).toLong()
            if (read == -1L) {
                throw EOFException("Unexpected end of file whilst reading fully.")
            }

            position += read
        }
    }

}
