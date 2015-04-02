package rs.emulate.modern;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;

import rs.emulate.shared.util.DataBuffer;
import rs.emulate.shared.util.crypto.Whirlpool;

/**
 * The {@link Cache} class provides a unified, high-level API for modifying the cache of a Jagex game.
 *
 * @author Graham
 */
public final class Cache implements Closeable {

	/**
	 * The file store that backs this cache.
	 */
	private final FileStore store;

	/**
	 * Creates a new {@link Cache} backed by the specified {@link FileStore}.
	 *
	 * @param store The file store that backs this cache.
	 */
	public Cache(FileStore store) {
		this.store = store;
	}

	@Override
	public void close() throws IOException {
		store.close();
	}

	/**
	 * Computes the {@link ChecksumTable} for this cache. The checksum table forms part of the so-called "update keys".
	 *
	 * @return The checksum table.
	 * @throws IOException If an I/O error occurs.
	 */
	public ChecksumTable createChecksumTable() throws IOException {
		int size = store.getTypeCount();
		ChecksumTable table = new ChecksumTable(size);

		for (int id = 0; id < size; id++) {
			DataBuffer buffer = store.read(255, id);

			int crc = 0;
			int version = 0;
			DataBuffer whirlpool = DataBuffer.allocate(64);

			// if there is actually a reference table, calculate the CRC, version and whirlpool hash
			if (buffer.limit() > 0) { // some indices are not used, is this appropriate?
				ReferenceTable references = ReferenceTable.decode(Container.decode(buffer).getData());
				crc = buffer.getCrcChecksum();
				version = references.getVersion();
				buffer.position(0);
				whirlpool = buffer.whirlpool();
			}

			table.setEntry(id, new ChecksumTable.Entry(crc, version, whirlpool));
		}

		return table;
	}

	/**
	 * Gets the number of files of the specified type.
	 *
	 * @param type The type.
	 * @return The number of files.
	 * @throws IOException If an I/O error occurs.
	 */
	public int getFileCount(int type) throws IOException {
		return store.getFileCount(type);
	}

	/**
	 * Gets the {@link FileStore} that backs this {@link Cache}.
	 *
	 * @return The underlying file store.
	 */
	public FileStore getStore() {
		return store;
	}

	/**
	 * Gets the number of index files, not including the meta index file.
	 *
	 * @return The number of index files.
	 * @throws IOException If an I/O error occurs.
	 */
	public int getTypeCount() throws IOException {
		return store.getTypeCount();
	}

	/**
	 * Reads a file from the cache.
	 *
	 * @param type The type of file.
	 * @param file The file id.
	 * @return The file.
	 * @throws IOException If an I/O error occurred.
	 */
	public Container read(int type, int file) throws IOException {
		if (type == 255) {
			throw new IOException("Reference tables can only be read with the low level FileStore API!");
		}

		return Container.decode(store.read(type, file));
	}

	/**
	 * Reads a file contained in an archive in the cache.
	 *
	 * @param type The type of the file.
	 * @param file The archive id.
	 * @param member The file within the archive.
	 * @return The file.
	 * @throws IOException If an I/O error occurred.
	 */
	public ByteBuffer read(int type, int file, int member) throws IOException {
		Container container = read(type, file);
		Container tableContainer = Container.decode(store.read(255, type));
		ReferenceTable table = ReferenceTable.decode(tableContainer.getData());

		Entry entry = table.getEntry(file);
		if (entry == null || member < 0 || member >= entry.capacity()) {
			throw new FileNotFoundException();
		}

		int validMember = 0;
		for (int index = 0; index < member; index++) {
			if (entry.getEntry(index) != null) {
				validMember++;
			}
		}

		Archive archive = Archive.decode(container.getData(), entry.size());
		return archive.getEntry(validMember);
	}

	/**
	 * Writes a file to the cache and updates the {@link ReferenceTable} that it is associated with.
	 *
	 * @param type The type of file.
	 * @param file The file id.
	 * @param container The {@link Container} to write.
	 * @throws IOException If an I/O error occurs.
	 */
	public void write(int type, int file, Container container) throws IOException {
		if (type == 255) {
			throw new IOException("Reference tables can only be modified with the low level FileStore API!");
		}

		container.setVersion(container.getVersion() + 1);

		Container tableContainer = Container.decode(store.read(255, type));
		ReferenceTable table = ReferenceTable.decode(tableContainer.getData());

		DataBuffer buffer = container.encode();
		byte[] bytes = new byte[buffer.limit() - 2]; // last two bytes are the version and shouldn't be included
		buffer.mark();
		try {
			buffer.position(0);
			buffer.get(bytes);
		} finally {
			buffer.reset();
		}

		CRC32 crc = new CRC32();
		crc.update(bytes, 0, bytes.length);

		Entry entry = table.getEntry(file);
		if (entry == null) {
			entry = new Entry();
			table.putEntry(file, entry);
		}
		entry.setVersion(container.getVersion());
		entry.setCrc((int) crc.getValue());

		if ((table.getFlags() & ReferenceTable.FLAG_WHIRLPOOL) != 0) {
			byte[] whirlpool = Whirlpool.whirlpool(bytes, 0, bytes.length);
			entry.setWhirlpool(whirlpool);
		}

		table.setVersion(table.getVersion() + 1);

		tableContainer = new Container(tableContainer.getType(), table.encode());
		store.write(255, type, tableContainer.encode().getByteBuffer());

		store.write(type, file, buffer.getByteBuffer());
	}

	/**
	 * Writes a file contained in an archive to the cache.
	 *
	 * @param type The type of file.
	 * @param file The id of the archive.
	 * @param member The file within the archive.
	 * @param data The data to write.
	 * @throws IOException If an I/O error occurs.
	 */
	public void write(int type, int file, int member, ByteBuffer data) throws IOException {
		Container tableContainer = Container.decode(store.read(255, type));
		ReferenceTable table = ReferenceTable.decode(tableContainer.getData());

		Entry entry = table.getEntry(file);
		int oldArchiveSize = -1;
		if (entry == null) {
			entry = new Entry();
			table.putEntry(file, entry);
		} else {
			oldArchiveSize = entry.capacity();
		}

		ChildEntry child = entry.getEntry(member);
		if (child == null) {
			child = new ChildEntry();
			entry.putEntry(member, child);
		}

		Archive archive;
		int containerType, containerVersion;
		if (file < store.getFileCount(type) && oldArchiveSize != -1) {
			Container container = read(type, file);
			containerType = container.getType();
			containerVersion = container.getVersion();
			archive = Archive.decode(container.getData(), oldArchiveSize);
		} else {
			containerType = Container.COMPRESSION_GZIP;
			containerVersion = 1;
			archive = new Archive(member + 1);
		}

		// expand the archive if it is not large enough
		if (member >= archive.size()) {
			Archive newArchive = new Archive(member + 1);
			for (int id = 0; id < archive.size(); id++) {
				newArchive.putEntry(id, archive.getEntry(id));
			}
			archive = newArchive;
		}

		archive.putEntry(member, data);

		// create 'dummy' entries
		for (int id = 0; id < archive.size(); id++) {
			if (archive.getEntry(id) == null) {
				entry.putEntry(id, new ChildEntry());
				archive.putEntry(id, ByteBuffer.allocate(1));
			}
		}

		tableContainer = new Container(tableContainer.getType(), table.encode());
		store.write(255, type, tableContainer.encode().getByteBuffer());

		Container container = new Container(containerType, archive.encode(), containerVersion);
		write(type, file, container);
	}

}