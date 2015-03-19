package rs.emulate.modern;

import java.nio.ByteBuffer;

import rs.emulate.shared.util.DataBuffer;

/**
 * An {@link Index} points to a file inside a {@link FileStore}.
 *
 * @author Graham
 */
public final class Index {

	/**
	 * The size of an index, in bytes.
	 */
	public static final int SIZE = 6;

	/**
	 * Decodes the specified {@link ByteBuffer} into an {@link Index} object.
	 *
	 * @param buffer The buffer.
	 * @return The index.
	 */
	public static Index decode(DataBuffer buffer) {
		if (buffer.remaining() != SIZE) {
			throw new IllegalArgumentException();
		}

		int size = buffer.getUnsignedTriByte();
		int sector = buffer.getUnsignedTriByte();
		return new Index(size, sector);
	}

	/**
	 * The number of the first sector that contains the file.
	 */
	private int sector;

	/**
	 * The size of the file in bytes.
	 */
	private int size;

	/**
	 * Creates a new index.
	 *
	 * @param size The size of the file in bytes.
	 * @param sector The number of the first sector that contains the file.
	 */
	public Index(int size, int sector) {
		this.size = size;
		this.sector = sector;
	}

	/**
	 * Encodes this index into a byte buffer.
	 *
	 * @return The buffer.
	 */
	public DataBuffer encode() {
		DataBuffer buffer = DataBuffer.allocate(Index.SIZE);
		buffer.putTriByte(size);
		buffer.putTriByte(sector);
		return buffer.flip();
	}

	/**
	 * Gets the number of the first sector that contains the file.
	 *
	 * @return The number of the first sector that contains the file.
	 */
	public int getSector() {
		return sector;
	}

	/**
	 * Gets the size of the file.
	 *
	 * @return The size of the file in bytes.
	 */
	public int getSize() {
		return size;
	}

}