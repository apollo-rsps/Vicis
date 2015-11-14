package rs.emulate.legacy.archive;

import rs.emulate.shared.util.ByteBufferUtils;
import rs.emulate.shared.util.DataBuffer;

/**
 * A single entry in an {@link Archive}. This class is immutable.
 *
 * @author Graham
 * @author Major
 */
public final class ArchiveEntry {

	/**
	 * The buffer of this entry.
	 */
	private final DataBuffer buffer;

	/**
	 * The identifier of this entry.
	 */
	private final int identifier;

	/**
	 * Creates the archive entry.
	 *
	 * @param identifier The identifier.
	 * @param buffer The buffer containing this entry's data.
	 */
	public ArchiveEntry(int identifier, DataBuffer buffer) {
		this.identifier = identifier;
		this.buffer = buffer.asReadOnlyBuffer();
	}

	/**
	 * Creates the archive entry.
	 *
	 * @param name The name of the archive.
	 * @param buffer The buffer containing this entry's data.
	 */
	public ArchiveEntry(String name, DataBuffer buffer) {
		this(ArchiveUtils.hash(name), buffer);
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof ArchiveEntry) {
			ArchiveEntry other = (ArchiveEntry) object;
			return identifier == other.identifier && buffer.equals(other.buffer);
		}

		return false;
	}

	/**
	 * Gets a deep copy of the buffer of this entry. The returned buffer will not be read-only (see
	 * {@link ByteBufferUtils#copy}.
	 *
	 * @return The buffer of this entry.
	 */
	public DataBuffer getBuffer() {
		return buffer.copy();
	}

	/**
	 * Gets the identifier of this entry.
	 *
	 * @return The identifier of this entry.
	 */
	public int getIdentifier() {
		return identifier;
	}

	/**
	 * Gets the size of this entry (i.e. the capacity of the {@link DataBuffer} backing it), in bytes.
	 *
	 * @return The size of this entry.
	 */
	public int getSize() {
		return buffer.limit();
	}

	@Override
	public int hashCode() {
		return 31 * buffer.hashCode() + identifier;
	}

	/**
	 * Returns a new archive entry with this entry's identifier, but the contents as the specified {@link DataBuffer}.
	 *
	 * @param buffer The byte buffer.
	 * @return The new archive entry.
	 */
	public ArchiveEntry update(DataBuffer buffer) {
		return new ArchiveEntry(identifier, buffer);
	}

}