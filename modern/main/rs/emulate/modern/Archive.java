package rs.emulate.modern;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import rs.emulate.shared.util.DataBuffer;

/**
 * An {@link Archive} is a file within the cache that can have multiple member files inside it.
 *
 * @author Graham
 */
public final class Archive {

	/**
	 * Decodes the specified {@link ByteBuffer} into an archive.
	 *
	 * @param buffer The buffer.
	 * @param size The size of the archive.
	 * @return The decoded archive.
	 */
	public static Archive decode(DataBuffer buffer, int size) {
		Archive archive = new Archive(size);

		buffer.position(buffer.limit() - 1);
		int chunks = buffer.getByte() & 0xFF;

		int[][] chunkSizes = new int[chunks][size];
		int[] sizes = new int[size];
		buffer.position(buffer.limit() - 1 - chunks * size * 4);
		for (int chunk = 0; chunk < chunks; chunk++) {
			int chunkSize = 0;
			for (int id = 0; id < size; id++) {
				int delta = buffer.getInt();
				chunkSize += delta;

				chunkSizes[chunk][id] = chunkSize; /* store the size of this chunk */
				sizes[id] += chunkSize; /* and add it to the size of the whole file */
			}
		}

		for (int id = 0; id < size; id++) {
			archive.entries[id] = ByteBuffer.allocate(sizes[id]);
		}

		buffer.position(0);
		for (int chunk = 0; chunk < chunks; chunk++) {
			for (int id = 0; id < size; id++) {
				int chunkSize = chunkSizes[chunk][id];

				byte[] temp = new byte[chunkSize];
				buffer.get(temp);

				archive.entries[id].put(temp);
			}
		}

		for (int id = 0; id < size; id++) {
			archive.entries[id].flip();
		}

		return archive;
	}

	/**
	 * The array of entries in this archive.
	 */
	private final ByteBuffer[] entries;

	/**
	 * Creates a new archive.
	 *
	 * @param size The number of entries in the archive.
	 */
	public Archive(int size) {
		entries = new ByteBuffer[size];
	}

	/**
	 * Encodes this {@link Archive} into a {@link ByteBuffer}.
	 * <p />
	 * Please note that this is a fairly simple implementation that does not attempt to use more than one chunk.
	 *
	 * @return An encoded byte buffer.
	 * @throws IOException If an I/O error occurs.
	 */
	public DataBuffer encode() throws IOException { // TODO: an implementation that can use more than one chunk
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		try (DataOutputStream os = new DataOutputStream(bout)) {
			for (ByteBuffer entry : entries) {
				byte[] temp = new byte[entry.limit()];
				entry.position(0);
				entry.get(temp);
				entry.position(0);

				os.write(temp);
			}

			int prev = 0;
			for (ByteBuffer entry : entries) {
				// since each file is stored in the only chunk, just write the delta-encoded file size
				int chunkSize = entry.limit();
				os.writeInt(chunkSize - prev);
				prev = chunkSize;
			}

			bout.write(1);

			byte[] bytes = bout.toByteArray();
			return DataBuffer.wrap(bytes);
		}
	}

	/**
	 * Gets the entry with the specified id.
	 *
	 * @param id The id.
	 * @return The entry.
	 */
	public ByteBuffer getEntry(int id) {
		return entries[id];
	}

	/**
	 * Inserts/replaces the entry with the specified id.
	 *
	 * @param id The id.
	 * @param buffer The entry.
	 */
	public void putEntry(int id, ByteBuffer buffer) {
		entries[id] = buffer;
	}

	/**
	 * Gets the size of this archive.
	 *
	 * @return The size of this archive.
	 */
	public int size() {
		return entries.length;
	}

}