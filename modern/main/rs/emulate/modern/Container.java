package rs.emulate.modern;

import java.io.IOException;

import rs.emulate.shared.util.DataBuffer;
import rs.emulate.shared.util.CompressionUtils;
import rs.emulate.shared.util.crypto.Xtea;

/**
 * A {@link Container} holds an optionally compressed file. This class can be used to decompress and compress
 * containers. A container can also have a two byte trailer that specifies the version of the file within it.
 *
 * @author Graham
 */
public final class Container {

	/**
	 * This type indicates that BZIP2 compression is used.
	 */
	public static final int COMPRESSION_BZIP2 = 1;

	/**
	 * This type indicates that GZIP compression is used.
	 */
	public static final int COMPRESSION_GZIP = 2;

	/**
	 * This type indicates that no compression is used.
	 */
	public static final int COMPRESSION_NONE = 0;

	/**
	 * An empty/'null' XTEA key.
	 */
	private static final int[] NULL_KEY = new int[4];

	/**
	 * Decodes and decompresses the container.
	 *
	 * @param buffer The buffer.
	 * @return The decompressed container.
	 * @throws IOException If there is an error decompressing the container.
	 */
	public static Container decode(DataBuffer buffer) throws IOException {
		return Container.decode(buffer, NULL_KEY);
	}

	/**
	 * Decodes and decompresses the container, applying the XTEA cipher with the specified key..
	 * 
	 * @param buffer The buffer.
	 * @param key The key.
	 * @return The decoded container.
	 * @throws IOException If there is an error decompressing the container.
	 */
	public static Container decode(DataBuffer buffer, int[] key) throws IOException {
		int type = buffer.getUnsignedByte();
		int length = buffer.getInt();

		/* decrypt (TODO what to do about version number trailer?) */
		if (key[0] != 0 || key[1] != 0 || key[2] != 0 || key[3] != 0) {
			Xtea.decipher(buffer, 5, length + (type == COMPRESSION_NONE ? 5 : 9), key);
		}

		if (type == COMPRESSION_NONE) {
			DataBuffer data = DataBuffer.allocate(length);
			data.fill(buffer);

			// decode the version, if present
			int version = -1;
			if (buffer.remaining() >= 2) {
				version = buffer.getShort();
			}

			return new Container(type, data, version);
		}

		int uncompressedLength = buffer.getInt();

		DataBuffer compressed = DataBuffer.allocate(length);
		compressed.fill(buffer);

		DataBuffer uncompressed = null;
		if (type == COMPRESSION_BZIP2) {
			uncompressed = CompressionUtils.bunzip2(compressed);
		} else if (type == COMPRESSION_GZIP) {
			uncompressed = CompressionUtils.gunzip(compressed);
		} else {
			throw new IOException("Invalid compression type " + type + ".");
		}

		int limit = uncompressed.limit();
		if (limit != uncompressedLength) {
			throw new IOException("Length mismatch: expected=" + uncompressedLength + ", got " + limit + ".");
		}

		// decode the version if present
		int version = -1;
		if (buffer.remaining() >= 2) {
			version = buffer.getShort();
		}

		return new Container(type, uncompressed, version);
	}

	/**
	 * The decompressed data.
	 */
	private DataBuffer data;

	/**
	 * The type of compression this container uses.
	 */
	private int type;

	/**
	 * The version of the file within this container.
	 */
	private int version;

	/**
	 * Creates a new unversioned container.
	 *
	 * @param type The type of compression.
	 * @param data The decompressed data.
	 */
	public Container(int type, DataBuffer data) {
		this(type, data, -1);
	}

	/**
	 * Creates a new versioned container.
	 *
	 * @param type The type of compression.
	 * @param data The decompressed data.
	 * @param version The version of the file within this container.
	 */
	public Container(int type, DataBuffer data, int version) {
		this.type = type;
		this.data = data;
		this.version = version;
	}

	/**
	 * Encodes and compresses this container.
	 *
	 * @return The buffer.
	 * @throws IOException If there is an error compressing the buffer.
	 */
	public DataBuffer encode() throws IOException {
		DataBuffer data = this.data.asReadOnlyBuffer(); // read-only makes this method thread-safe
		DataBuffer bytes = DataBuffer.allocate(data.limit());
		data.mark();
		data.get(bytes);
		data.reset();

		DataBuffer compressed = bytes;
		if (type == COMPRESSION_GZIP) {
			compressed = CompressionUtils.gzip(bytes);
		} else if (type == COMPRESSION_BZIP2) {
			compressed = CompressionUtils.bzip2(bytes);
		} else {
			throw new IOException("Invalid compression type.");
		}

		int header = 5 + (type == COMPRESSION_NONE ? 0 : 4) + (isVersioned() ? 2 : 0);
		DataBuffer buffer = DataBuffer.allocate(header + compressed.limit());

		buffer.putByte((byte) type);
		buffer.putInt(compressed.limit());

		if (type != COMPRESSION_NONE) {
			buffer.putInt(data.limit());
		}

		buffer.put(compressed);

		if (isVersioned()) {
			buffer.putShort((short) version);
		}

		return buffer.flip();
	}

	/**
	 * Gets the decompressed data.
	 *
	 * @return The decompressed data.
	 */
	public DataBuffer getData() {
		return data.asReadOnlyBuffer();
	}

	/**
	 * Gets the type of this container.
	 *
	 * @return The compression type.
	 */
	public int getType() {
		return type;
	}

	/**
	 * Gets the version of the file in this container.
	 *
	 * @return The version of the file.
	 * @throws IllegalArgumentException if this container is not versioned.
	 */
	public int getVersion() {
		if (!isVersioned()) {
			throw new IllegalStateException("Container does not have a version.");
		}

		return version;
	}

	/**
	 * Checks if this container is versioned.
	 *
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isVersioned() {
		return version != -1;
	}

	/**
	 * Removes the version on this container so it becomes unversioned.
	 */
	public void removeVersion() {
		version = -1;
	}

	/**
	 * Sets the type of this container.
	 *
	 * @param type The compression type.
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * Sets the version of this container.
	 *
	 * @param version The version.
	 */
	public void setVersion(int version) {
		this.version = version;
	}

}