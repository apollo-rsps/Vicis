package rs.emulate.legacy;

import rs.emulate.shared.util.DataBuffer;

import com.google.common.base.Preconditions;

/**
 * Contains methods for encoding and decoding {@link Index} objects.
 * 
 * @author Major
 * @author Graham
 */
final class IndexCodec {

	/**
	 * Decodes the {@link DataBuffer} into an index.
	 * 
	 * @param buffer The buffer.
	 * @return The index.
	 * @throws IllegalArgumentException If the buffer length is invalid.
	 */
	public static Index decode(DataBuffer buffer) {
		Preconditions.checkArgument(buffer.remaining() == Index.BYTES, "Incorrect buffer length.");

		int size = buffer.getUnsignedTriByte();
		int block = buffer.getUnsignedTriByte();

		return new Index(size, block);
	}

	/**
	 * Encodes the index into a {@link DataBuffer}.
	 * 
	 * @param index The index.
	 * @return The byte buffer.
	 */
	public static DataBuffer encode(Index index) {
		return DataBuffer.allocate(6).putTriByte(index.getSize()).putTriByte(index.getBlock()).flip();
	}

	/**
	 * Default private constructor to prevent instantiation.
	 */
	private IndexCodec() {

	}

}