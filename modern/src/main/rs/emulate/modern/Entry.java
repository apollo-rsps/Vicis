package rs.emulate.modern;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Represents a single entry within a {@link ReferenceTable}.
 */
public final class Entry {

	/**
	 * The CRC32 checksum of this entry.
	 */
	private int crc;

	/**
	 * The children in this entry.
	 */
	private SortedMap<Integer, ChildEntry> entries = new TreeMap<>();

	/**
	 * The identifier of this entry.
	 */
	private int identifier = -1;

	/**
	 * The version of this entry.
	 */
	private int version;

	/**
	 * The whirlpool digest of this entry.
	 */
	private byte[] whirlpool = new byte[64];

	/**
	 * Gets the maximum number of child entries.
	 *
	 * @return The maximum number of child entries.
	 */
	public int capacity() {
		return entries.isEmpty() ? 0 : entries.lastKey() + 1;
	}

	/**
	 * Gets the CRC32 checksum of this entry.
	 *
	 * @return The CRC32 checksum.
	 */
	public int getCrc() {
		return crc;
	}

	/**
	 * Gets the child entry with the specified id.
	 *
	 * @param id The id.
	 * @return The entry, or {@code null} if it does not exist.
	 */
	public ChildEntry getEntry(int id) {
		return entries.get(id);
	}

	/**
	 * Gets the {@link ChildEntry} objects of this Entry.
	 * 
	 * @return The ChildEntry objects.
	 */
	public Collection<ChildEntry> getChildren() {
		return entries.values();
	}

	/**
	 * Gets the identifier of this entry.
	 *
	 * @return The identifier.
	 */
	public int getIdentifier() {
		return identifier;
	}

	/**
	 * Gets the version of this entry.
	 *
	 * @return The version.
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Gets the whirlpool digest of this entry.
	 *
	 * @return The whirlpool digest.
	 */
	public byte[] getWhirlpool() {
		return whirlpool.clone();
	}

	/**
	 * Returns whether or not this Entry has a {@link ChildEntry} with the specified id.
	 * 
	 * @param id The id.
	 * @return {@code true} if this Entry has a ChildEntry with the specified id, {@code false} if not.
	 */
	public boolean hasChild(int id) {
		return entries.containsKey(id);
	}

	/**
	 * Replaces or inserts the child entry with the specified id.
	 *
	 * @param id The id.
	 * @param entry The entry.
	 */
	public void putEntry(int id, ChildEntry entry) {
		entries.put(id, entry);
	}

	/**
	 * Removes the entry with the specified id.
	 *
	 * @param id The id.
	 * @param entry The entry.
	 */
	public void removeEntry(int id, ChildEntry entry) {
		entries.remove(id);
	}

	/**
	 * Sets the CRC32 checksum of this entry.
	 *
	 * @param crc The CRC32 checksum.
	 */
	public void setCrc(int crc) {
		this.crc = crc;
	}

	/**
	 * Sets the identifier of this entry.
	 *
	 * @param identifier The identifier.
	 */
	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}

	/**
	 * Sets the version of this entry.
	 *
	 * @param version The version.
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * Sets the whirlpool digest of this entry.
	 *
	 * @param whirlpool The whirlpool digest.
	 * @throws IllegalArgumentException if the digest is not 64 bytes long.
	 */
	public void setWhirlpool(byte[] whirlpool) {
		if (whirlpool.length != 64) {
			throw new IllegalArgumentException("Whirlpool length must be 64.");
		}

		this.whirlpool = whirlpool.clone();
	}

	/**
	 * Gets the number of actual child entries.
	 *
	 * @return The number of actual child entries.
	 */
	public int size() {
		return entries.size();
	}

}