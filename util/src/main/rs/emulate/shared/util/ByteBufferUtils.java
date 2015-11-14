package rs.emulate.shared.util;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Contains {@link ByteBuffer}-related utility methods.
 *
 * @author Graham
 * @author Major
 */
public final class ByteBufferUtils {

	/**
	 * Copies the contents in the specified {@link ByteBuffer}, but <strong>not</strong> any of its attributes (e.g.
	 * mark, read-only). The capacity and limit of the new buffer will be the limit of the specified one, the position
	 * of the new buffer will be 0, and the mark will be undefined. The specified buffer will be flipped after writing.
	 * <p>
	 * This method uses {@link ByteBuffer#put(ByteBuffer)} and so will write from the specified byte buffers current
	 * position.
	 *
	 * @param buffer The byte buffer.
	 * @return The copied byte buffer.
	 */
	public static ByteBuffer copy(ByteBuffer buffer) {
		ByteBuffer copy = ByteBuffer.allocate(buffer.limit());
		copy.put(buffer).flip();
		buffer.flip();
		return copy;
	}

	/**
	 * Converts the contents of the specified {@link ByteBuffer} to a string, which is formatted similarly to the
	 * output
	 * of the {@link Arrays#toString()} method.
	 *
	 * @param buffer The buffer.
	 * @return The string.
	 */
	public static String toString(ByteBuffer buffer) {
		StringBuilder builder = new StringBuilder("[");

		int limit = buffer.limit();
		for (int index = 0; index < limit; index++) {
			String hex = Integer.toHexString(buffer.get() & 0xFF).toUpperCase();
			if (hex.length() == 1) {
				hex = "0" + hex;
			}

			builder.append("0x").append(hex);
			if (index != limit - 1) {
				builder.append(", ");
			}
		}

		builder.append("]");
		return builder.toString();
	}

	/**
	 * Sole private constructor to prevent instantiation.
	 */
	private ByteBufferUtils() {

	}

}