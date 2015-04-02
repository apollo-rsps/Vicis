package rs.emulate.modern;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.SortedMap;
import java.util.TreeMap;

import rs.emulate.shared.util.CacheStringUtils;
import rs.emulate.shared.util.DataBuffer;

/**
 * A {@link ReferenceTable} holds details for all the files with a single type, such as checksums, versions and archive
 * members. There are also optional fields for identifier hashes and whirlpool digests.
 *
 * @author Graham
 */
public final class ReferenceTable {

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
				table.entries.get(id).setIdentifier(buffer.getInt());
			}
		}

		for (int id : ids) {
			table.entries.get(id).setCrc(buffer.getInt());
		}

		if ((table.flags & FLAG_WHIRLPOOL) != 0) {
			for (int id : ids) {
				buffer.get(table.entries.get(id).getWhirlpool());
			}
		}

		for (int id : ids) {
			table.entries.get(id).setVersion(buffer.getInt());
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
				table.entries.get(id).putEntry(child, new ChildEntry());
			}
		}

		if ((table.flags & FLAG_IDENTIFIERS) != 0) {
			for (int id : ids) {
				for (int child : members[id]) {
					table.entries.get(id).getEntry(child).setIdentifier(buffer.getInt());
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
					os.writeInt(entry.getIdentifier());
				}
			}

			for (Entry entry : entries.values()) {
				os.writeInt(entry.getCrc());
			}

			if ((flags & FLAG_WHIRLPOOL) != 0) {
				for (Entry entry : entries.values()) {
					os.write(entry.getWhirlpool());
				}
			}

			for (Entry entry : entries.values()) {
				os.writeInt(entry.getVersion());
			}

			for (Entry entry : entries.values()) {
				os.writeShort(entry.size());
			}

			for (Entry entry : entries.values()) {
				last = 0;
				for (int id = 0; id < entry.capacity(); id++) {
					if (entry.hasChild(id)) {
						int delta = id - last;
						os.writeShort(delta);
						last = id;
					}
				}
			}

			if ((flags & FLAG_IDENTIFIERS) != 0) {
				for (Entry entry : entries.values()) {
					for (ChildEntry child : entry.getChildren()) {
						os.writeInt(child.getIdentifier());
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