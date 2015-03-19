package rs.emulate.modern;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.SortedMap;
import java.util.TreeMap;

import rs.emulate.shared.util.DataBuffer;
import rs.emulate.shared.util.CacheStringUtils;

/**
 * A {@link ReferenceTable} holds details for all the files with a single type, such as checksums, versions and archive
 * members. There are also optional fields for identifier hashes and whirlpool digests.
 *
 * @author Graham
 */
public final class ReferenceTable {

	/**
	 * Represents a child entry within an {@link Entry} in the {@link ReferenceTable}.
	 */
	public static class ChildEntry {

		/**
		 * This entry's identifier.
		 */
		private int identifier = -1;

		/**
		 * Gets the identifier of this entry.
		 *
		 * @return The identifier.
		 */
		public int getIdentifier() {
			return identifier;
		}

		/**
		 * Sets the identifier of this entry.
		 *
		 * @param identifier The identifier.
		 */
		public void setIdentifier(int identifier) {
			this.identifier = identifier;
		}

	}

	/**
	 * Represents a single entry within a {@link ReferenceTable}.
	 *
	 * @author Graham Edgecombe
	 */
	public static class Entry {

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
			return whirlpool;
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

			System.arraycopy(whirlpool, 0, this.whirlpool, 0, whirlpool.length);
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

	/**
	 * A flag which indicates this {@link ReferenceTable} contains {@link CacheStringUtils} hashed identifiers.
	 */
	public static final int FLAG_IDENTIFIERS = 0x01;

	/**
	 * A flag which indicates this {@link ReferenceTable} contains whirlpool digests for its entries.
	 */
	public static final int FLAG_WHIRLPOOL = 0x02;

	/**
	 * Decodes the slave checksum table contained in the specified {@link ByteBuffer}.
	 *
	 * @param buffer The buffer.
	 * @return The slave checksum table.
	 */
	public static ReferenceTable decode(DataBuffer buffer) {
		ReferenceTable table = new ReferenceTable();

		table.format = buffer.getUnsignedByte();
		if (table.format >= 6) {
			table.version = buffer.getInt();
		}
		table.flags = buffer.getUnsignedByte();

		int[] ids = new int[buffer.getUnsignedShort()];
		int accumulator = 0, size = -1;
		for (int i = 0; i < ids.length; i++) {
			int delta = buffer.getUnsignedShort();
			ids[i] = accumulator += delta;
			if (ids[i] > size) {
				size = ids[i];
			}
		}
		size++;

		for (int id : ids) {
			table.entries.put(id, new Entry());
		}

		if ((table.flags & FLAG_IDENTIFIERS) != 0) {
			for (int id : ids) {
				table.entries.get(id).identifier = buffer.getInt();
			}
		}

		for (int id : ids) {
			table.entries.get(id).crc = buffer.getInt();
		}

		if ((table.flags & FLAG_WHIRLPOOL) != 0) {
			for (int id : ids) {
				buffer.get(table.entries.get(id).whirlpool);
			}
		}

		for (int id : ids) {
			table.entries.get(id).version = buffer.getInt();
		}

		int[][] members = new int[size][];
		for (int id : ids) {
			members[id] = new int[buffer.getUnsignedShort()];
		}

		for (int id : ids) {
			accumulator = 0;
			size = -1;

			for (int i = 0; i < members[id].length; i++) {
				int delta = buffer.getUnsignedShort();
				members[id][i] = accumulator += delta;
				if (members[id][i] > size) {
					size = members[id][i];
				}
			}
			size++;

			for (int child : members[id]) {
				table.entries.get(id).entries.put(child, new ChildEntry());
			}
		}

		if ((table.flags & FLAG_IDENTIFIERS) != 0) {
			for (int id : ids) {
				for (int child : members[id]) {
					table.entries.get(id).entries.get(child).identifier = buffer.getInt();
				}
			}
		}

		return table;
	}

	/**
	 * Decodes the specified {@link Container} into a reference table.
	 * 
	 * @param container The container.
	 * @return The reference table.
	 */
	public static ReferenceTable decode(Container container) {
		return decode(container.getData());
	}

	/**
	 * The entries in this table.
	 */
	private SortedMap<Integer, Entry> entries = new TreeMap<>();

	/**
	 * The flags of this table.
	 */
	private int flags;

	/**
	 * The format of this table.
	 */
	private int format;

	/**
	 * The version of this table.
	 */
	private int version;

	/**
	 * Gets the maximum number of entries in this table.
	 *
	 * @return The maximum number of entries.
	 */
	public int capacity() {
		return entries.isEmpty() ? 0 : entries.lastKey() + 1;
	}

	/**
	 * Encodes this {@link ReferenceTable} into a {@link ByteBuffer}.
	 *
	 * @return The {@link ByteBuffer}.
	 * @throws IOException if an I/O error occurs.
	 */
	public DataBuffer encode() throws IOException {
		// we can't (easily) predict the size ahead of time, so we write to a stream and then to the buffer
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		try (DataOutputStream os = new DataOutputStream(bout)) {
			os.write(format);
			if (format >= 6) {
				os.writeInt(version);
			}
			os.write(flags);

			os.writeShort(entries.size());

			int last = 0;
			for (int id = 0; id < capacity(); id++) {
				if (entries.containsKey(id)) {
					int delta = id - last;
					os.writeShort(delta);
					last = id;
				}
			}

			if ((flags & FLAG_IDENTIFIERS) != 0) {
				for (Entry entry : entries.values()) {
					os.writeInt(entry.identifier);
				}
			}

			for (Entry entry : entries.values()) {
				os.writeInt(entry.crc);
			}

			if ((flags & FLAG_WHIRLPOOL) != 0) {
				for (Entry entry : entries.values()) {
					os.write(entry.whirlpool);
				}
			}

			for (Entry entry : entries.values()) {
				os.writeInt(entry.version);
			}

			for (Entry entry : entries.values()) {
				os.writeShort(entry.entries.size());
			}

			for (Entry entry : entries.values()) {
				last = 0;
				for (int id = 0; id < entry.capacity(); id++) {
					if (entry.entries.containsKey(id)) {
						int delta = id - last;
						os.writeShort(delta);
						last = id;
					}
				}
			}

			if ((flags & FLAG_IDENTIFIERS) != 0) {
				for (Entry entry : entries.values()) {
					for (ChildEntry child : entry.entries.values()) {
						os.writeInt(child.identifier);
					}
				}
			}

			return DataBuffer.wrap(bout.toByteArray());
		}
	}

	/**
	 * Gets the entry with the specified id, or {@code null} if it does not exist.
	 *
	 * @param id The id.
	 * @return The entry.
	 */
	public Entry getEntry(int id) {
		return entries.get(id);
	}

	/**
	 * Gets the child entry with the specified id, or {@code null} if it does not exist.
	 *
	 * @param id The parent id.
	 * @param child The child id.
	 * @return The entry.
	 */
	public ChildEntry getEntry(int id, int child) {
		Entry entry = entries.get(id);
		if (entry == null) {
			return null;
		}

		return entry.getEntry(child);
	}

	/**
	 * Gets the flags of this table.
	 *
	 * @return The flags.
	 */
	public int getFlags() {
		return flags;
	}

	/**
	 * Gets the format of this table.
	 *
	 * @return The format.
	 */
	public int getFormat() {
		return format;
	}

	/**
	 * Gets the version of this table.
	 *
	 * @return The version of this table.
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Replaces or inserts the entry with the specified id.
	 *
	 * @param id The id.
	 * @param entry The entry.
	 */
	public void putEntry(int id, Entry entry) {
		entries.put(id, entry);
	}

	/**
	 * Removes the entry with the specified id.
	 *
	 * @param id The id.
	 */
	public void removeEntry(int id) {
		entries.remove(id);
	}

	/**
	 * Sets the flags of this table.
	 *
	 * @param flags The flags.
	 */
	public void setFlags(int flags) {
		this.flags = flags;
	}

	/**
	 * Sets the format of this table.
	 *
	 * @param format The format.
	 */
	public void setFormat(int format) {
		this.format = format;
	}

	/**
	 * Sets the version of this table.
	 *
	 * @param version The version.
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * Gets the number of actual entries.
	 *
	 * @return The number of actual entries.
	 */
	public int size() {
		return entries.size();
	}

}