package rs.emulate.modern;

import com.google.common.base.Preconditions;
import rs.emulate.shared.util.DataBuffer;
import rs.emulate.shared.util.crypto.Rsa;
import rs.emulate.shared.util.crypto.Whirlpool;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * A {@link ChecksumTable} stores checksums and versions of {@link ReferenceTable}s. When encoded in a {@link
 * Container}
 * and prepended with the file type and id it is more commonly known as the client's "update keys".
 *
 * @author Graham
 */
public final class ChecksumTable {

	/**
	 * Represents a single entry in a {@link ChecksumTable}. Each entry contains a CRC32 checksum and version of the
	 * corresponding {@link ReferenceTable}.
	 */
	public static final class Entry {

		/**
		 * The CRC32 checksum of the reference table.
		 */
		private final int crc;

		/**
		 * The version of the reference table.
		 */
		private final int version;

		/**
		 * The whirlpool digest of the reference table.
		 */
		private final DataBuffer whirlpool;

		/**
		 * Creates a new entry.
		 *
		 * @param crc The CRC32 checksum of the slave table.
		 * @param version The version of the slave table.
		 * @param whirlpool The whirlpool digest of the reference table.
		 * @throws IllegalArgumentException If the whirlpool buffer limit is not 64.
		 */
		public Entry(int crc, int version, DataBuffer whirlpool) {
			Preconditions.checkArgument(whirlpool.limit() == 64, "Whirlpool buffer limit must be 64.");

			this.crc = crc;
			this.version = version;
			this.whirlpool = whirlpool;
		}

		/**
		 * Gets the CRC32 checksum of the reference table.
		 *
		 * @return The CRC32 checksum.
		 */
		public int getCrc() {
			return crc;
		}

		/**
		 * Gets the version of the reference table.
		 *
		 * @return The version.
		 */
		public int getVersion() {
			return version;
		}

		/**
		 * Gets the whirlpool digest of the reference table.
		 *
		 * @return The whirlpool digest.
		 */
		public DataBuffer getWhirlpool() {
			return whirlpool;
		}

	}

	/**
	 * Decodes the {@link ChecksumTable} in the specified {@link ByteBuffer}. Whirlpool digests are not read.
	 *
	 * @param buffer The {@link ByteBuffer} containing the table.
	 * @return The decoded {@link ChecksumTable}.
	 * @throws IOException if an I/O error occurs.
	 */
	public static ChecksumTable decode(DataBuffer buffer) throws IOException {
		return decode(buffer, false);
	}

	/**
	 * Decodes the {@link ChecksumTable} in the specified {@link ByteBuffer}.
	 *
	 * @param buffer The {@link ByteBuffer} containing the table.
	 * @param whirlpool If whirlpool digests should be read.
	 * @return The decoded {@link ChecksumTable}.
	 * @throws IOException if an I/O error occurs.
	 */
	public static ChecksumTable decode(DataBuffer buffer, boolean whirlpool) throws IOException {
		return decode(buffer, whirlpool, null, null);
	}

	/**
	 * Decodes the {@link ChecksumTable} in the specified {@link ByteBuffer} and decrypts the final whirlpool hash.
	 *
	 * @param buffer The {@link ByteBuffer} containing the table.
	 * @param whirlpool If whirlpool digests should be read.
	 * @param modulus The modulus.
	 * @param publicKey The public key.
	 * @return The decoded {@link ChecksumTable}.
	 * @throws IOException if an I/O error occurs.
	 */
	public static ChecksumTable decode(DataBuffer buffer, boolean whirlpool, BigInteger modulus, BigInteger publicKey)
			throws IOException {
		int size = whirlpool ? buffer.getUnsignedByte() : buffer.limit() / 8;
		ChecksumTable table = new ChecksumTable(size);

		byte[] masterDigest = null;
		if (whirlpool) {
			byte[] temp = new byte[size * 72 + 1];
			buffer.position(0);
			buffer.get(temp);
			masterDigest = Whirlpool.whirlpool(temp, 0, temp.length);
		}

		buffer.position(whirlpool ? 1 : 0);
		for (int entry = 0; entry < size; entry++) {
			int crc = buffer.getInt();
			int version = buffer.getInt();
			DataBuffer digest = DataBuffer.allocate(64);

			if (whirlpool) {
				digest.fill(buffer);
			}

			table.entries[entry] = new Entry(crc, version, digest);
		}

		if (whirlpool) {
			DataBuffer remaining = DataBuffer.allocate(buffer.remaining());
			remaining.fill(buffer);

			if (modulus != null && publicKey != null) {
				remaining = Rsa.crypt(buffer, modulus, publicKey);
			}

			if (remaining.limit() != 65) {
				throw new IllegalStateException("Decrypted data is not 65 bytes long.");
			}

			for (int index = 0; index < 64; index++) {
				if (remaining.get(index + 1) != masterDigest[index]) {
					throw new IllegalStateException("Whirlpool digest mismatch.");
				}
			}
		}

		return table;
	}

	/**
	 * The entries in this table.
	 */
	private Entry[] entries;

	/**
	 * Creates a new {@link ChecksumTable} with the specified size.
	 *
	 * @param size The number of entries in this table.
	 */
	public ChecksumTable(int size) {
		entries = new Entry[size];
	}

	/**
	 * Encodes this {@link ChecksumTable}. Whirlpool digests are not encoded.
	 *
	 * @return The encoded {@link ByteBuffer}.
	 * @throws IOException if an I/O error occurs.
	 */
	public DataBuffer encode() throws IOException {
		return encode(false);
	}

	/**
	 * Encodes this {@link ChecksumTable}.
	 *
	 * @param whirlpool If whirlpool digests should be encoded.
	 * @return The encoded {@link ByteBuffer}.
	 * @throws IOException if an I/O error occurs.
	 */
	public DataBuffer encode(boolean whirlpool) throws IOException {
		return encode(whirlpool, null, null);
	}

	/**
	 * Encodes this {@link ChecksumTable} and encrypts the final whirlpool hash.
	 *
	 * @param whirlpool If whirlpool digests should be encoded.
	 * @param modulus The modulus.
	 * @param privateKey The private key.
	 * @return The encoded {@link ByteBuffer}.
	 * @throws IOException if an I/O error occurs.
	 */
	public DataBuffer encode(boolean whirlpool, BigInteger modulus, BigInteger privateKey) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		try (DataOutputStream os = new DataOutputStream(bout)) {
			/* as the new whirlpool format is more complicated we must write the number of entries */
			if (whirlpool) {
				os.write(entries.length);
			}

			for (Entry entry : entries) {
				os.writeInt(entry.getCrc());
				os.writeInt(entry.getVersion());

				if (whirlpool) {
					os.write(entry.getWhirlpool().array());
				}
			}

			if (whirlpool) {
				byte[] bytes = bout.toByteArray();
				DataBuffer buffer = DataBuffer.wrap(bytes).whirlpool();

				if (modulus != null && privateKey != null) {
					buffer = Rsa.crypt(buffer, modulus, privateKey);
				}

				os.write(buffer.array());
			}

			byte[] bytes = bout.toByteArray();
			return DataBuffer.wrap(bytes);
		}
	}

	/**
	 * Gets an entry from this table.
	 *
	 * @param id The id.
	 * @return The entry.
	 * @throws IndexOutOfBoundsException If the id is less than {@code 0} and not less than the size of the table.
	 */
	public Entry getEntry(int id) {
		Preconditions.checkElementIndex(id, entries.length, "Entry id is out of bounds.");
		return entries[id];
	}

	/**
	 * Gets the size of this table.
	 *
	 * @return The size of this table.
	 */
	public int getSize() {
		return entries.length;
	}

	/**
	 * Sets an entry in this table.
	 *
	 * @param id The id.
	 * @param entry The entry.
	 * @throws IndexOutOfBoundsException If the id is less than {@code 0} and not less than the size of the table.
	 */
	public void setEntry(int id, Entry entry) {
		Preconditions.checkElementIndex(id, entries.length, "Entry id is out of bounds.");
		entries[id] = entry;
	}

}