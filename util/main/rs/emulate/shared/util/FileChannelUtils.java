package rs.emulate.shared.util;

import java.io.EOFException;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Contains {@link FileChannel}-related utility methods.
 *
 * @author Graham
 */
public final class FileChannelUtils {

	/**
	 * Reads as much as possible from the channel into the buffer.
	 *
	 * @param channel The channel.
	 * @param buffer The buffer.
	 * @param position The initial position in the channel.
	 * @throws IOException If an I/O error occurs.
	 * @throws EOFException If the end of the file was reached and the buffer could not be completely populated.
	 */
	public static void readFully(FileChannel channel, DataBuffer buffer, long position) throws IOException {
		while (buffer.remaining() > 0) {
			long read = channel.read(buffer.getByteBuffer(), position);
			if (read == -1) {
				throw new EOFException("Unexpected end of file whilst reading fully.");
			}

			position += read;
		}
	}

	/**
	 * Sole private constructor to prevent instantiation.
	 */
	private FileChannelUtils() {

	}

}