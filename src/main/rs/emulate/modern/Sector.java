package rs.emulate.modern;

import java.nio.ByteBuffer;

import rs.emulate.shared.util.DataBuffer;

import com.google.common.base.Preconditions;

/**
 * A {@link Sector} contains a header and data. The header contains information used to verify the integrity of the
 * cache like the current file id, type and chunk. It also contains a pointer to the next sector such that the sectors
 * form a singly-linked list. The data is simply up to 512 bytes of the file.
 *
 * @author Graham
 */
public final class Sector {

	/**
	 * The size of the data within a sector in bytes.
	 */
	public static final int DATA_SIZE = 512;

	/**
	 * The size of the header within a sector in bytes.
	 */
	public static final int HEADER_SIZE = 8;

	/**
	 * The total size of a sector in bytes.
	 */
	public static final int SIZE = HEADER_SIZE + DATA_SIZE;

	/**
	 * Decodes the specified {@link DataBuffer} into a {@link Sector} object.
	 *
	 * @param buffer The buffer.
	 * @return The sector.
	 */
	public static Sector decode(DataBuffer buffer) {
		Preconditions.checkArgument(buffer.remaining() == SIZE, "Buffer must have " + SIZE + " bytes remaining.");

		int id = buffer.getUnsignedShort();
		int chunk = buffer.getUnsignedShort();
		int nextSector = buffer.getUnsignedTriByte();
		int type = buffer.getUnsignedByte();

		DataBuffer data = DataBuffer.allocate(DATA_SIZE);
		data.fill(buffer);

		return new Sector(type, id, chunk, nextSector, data);
	}

	/**
	 * The chunk within the file that this sector contains.
	 */
	private final int chunk;

	/**
	 * The data in this sector.
	 */
	private final DataBuffer data;

	/**
	 * The id of the file this sector contains.
	 */
	private final int id;

	/**
	 * The next sector.
	 */
	private final int nextSector;

	/**
	 * The type of file this sector contains.
	 */
	private final int type;

	/**
	 * Creates a new sector.
	 *
	 * @param type The type of the file.
	 * @param id The file's id.
	 * @param chunk The chunk of the file this sector contains.
	 * @param nextSector The sector containing the next chunk.
	 * @param data The data in this sector.
	 */
	public Sector(int type, int id, int chunk, int nextSector, DataBuffer data) {
		this.type = type;
		this.id = id;
		this.chunk = chunk;
		this.nextSector = nextSector;
		this.data = data;
	}

	/**
	 * Encodes this sector into a {@link ByteBuffer}.
	 *
	 * @return The encoded buffer.
	 */
	public DataBuffer encode() {
		DataBuffer buffer = DataBuffer.allocate(SIZE);

		buffer.putShort((short) id);
		buffer.putShort((short) chunk);
		buffer.putTriByte(nextSector);
		buffer.putByte((byte) type);
		buffer.put(data);

		return buffer.flip();
	}

	/**
	 * Gets the chunk of the file this sector contains.
	 *
	 * @return The chunk of the file this sector contains.
	 */
	public int getChunk() {
		return chunk;
	}

	/**
	 * Gets this sector's data.
	 *
	 * @return The data within this sector.
	 */
	public DataBuffer getData() {
		return data;
	}

	/**
	 * Gets the id of the file within this sector.
	 *
	 * @return The id of the file in this sector.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the next sector.
	 *
	 * @return The next sector.
	 */
	public int getNextSector() {
		return nextSector;
	}

	/**
	 * Gets the type of file in this sector.
	 *
	 * @return The type of file in this sector.
	 */
	public int getType() {
		return type;
	}

}