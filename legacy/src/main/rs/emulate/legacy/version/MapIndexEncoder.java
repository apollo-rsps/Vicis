package rs.emulate.legacy.version;

import java.util.Arrays;

import rs.emulate.shared.util.DataBuffer;

/**
 * An encoder for a {@link MapIndex}.
 *
 * @author Major
 */
public final class MapIndexEncoder {

	/**
	 * The MapIndex to encode.
	 */
	private final MapIndex index;

	/**
	 * Creates the MapIndexEncoder.
	 *
	 * @param index The {@link MapIndex} to encode.
	 */
	public MapIndexEncoder(MapIndex index) {
		this.index = index;
	}

	/**
	 * Encodes the {@link MapIndex} into a {@link DataBuffer}.
	 * 
	 * @return The DataBuffer.
	 */
	public DataBuffer encode() {
		DataBuffer buffer = DataBuffer.allocate((3 * Short.BYTES + Byte.BYTES) * index.getSize());

		Arrays.stream(index.getAreas()).forEach(buffer::putShort);
		Arrays.stream(index.getMaps()).forEach(buffer::putShort);
		Arrays.stream(index.getObjects()).forEach(buffer::putShort);

		for (boolean members : index.getMembers()) {
			buffer.putBoolean(members);
		}

		return buffer.flip();
	}

}