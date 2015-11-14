package rs.emulate.legacy;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import rs.emulate.legacy.archive.Archive;
import rs.emulate.legacy.archive.ArchiveCodec;
import rs.emulate.shared.util.DataBuffer;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.CRC32;

/**
 * A file system based on top of the operating system's file system. It consists of a data file and multiple index
 * files. Index files point to blocks in the data file, which contain the actual data.
 *
 * @author Graham
 * @author Major
 */
public final class IndexedFileSystem implements Closeable {

	/**
	 * The maximum amount of indices.
	 */
	private static final int INDEX_COUNT = 256;

	/**
	 * The data file.
	 */
	private final RandomAccessFile data;

	/**
	 * The index files.
	 */
	private final List<RandomAccessFile> indices;

	/**
	 * The access mode.
	 */
	private final AccessMode mode;

	/**
	 * The cached CRC table.
	 */
	private Optional<DataBuffer> crcs;

	/**
	 * Creates the file system with the specified base directory.
	 *
	 * @param base The base directory.
	 * @param mode The {@link AccessMode}.
	 * @throws FileNotFoundException If the data files could not be found.
	 */
	public IndexedFileSystem(Path base, AccessMode mode) throws FileNotFoundException {
		this.mode = mode;

		indices = getIndexFiles(base);
		data = getDataFile(base);
	}

	@Override
	public void close() throws IOException {
		if (data != null) {
			synchronized (data) {
				data.close();
			}
		}

		for (RandomAccessFile index : indices) {
			if (index != null) {
				synchronized (index) {
					index.close();
				}
			}
		}
	}

	/**
	 * Decodes a file into an {@link Archive}.
	 *
	 * @param type The file type.
	 * @param file The file id.
	 * @return The archive.
	 * @throws IOException If there is an error getting or decoding the file.
	 */
	public Archive getArchive(int type, int file) throws IOException {
		return ArchiveCodec.decode(getFile(type, file));
	}

	/**
	 * Gets the CRC table.
	 *
	 * @return The CRC table.
	 * @throws IOException If there is an error getting any of the files.
	 */
	public DataBuffer getCrcTable() throws IOException {
		if (isReadOnly()) {
			synchronized (this) {
				if (crcs.isPresent()) {
					return crcs.get().duplicate();
				}
			}

			int archives = getFileCount(0);
			int hash = 1234;

			DataBuffer buffer = DataBuffer.allocate((archives + 1) * Integer.BYTES);
			CRC32 crc = new CRC32();

			for (int file = 1; file < archives; file++) {
				byte[] bytes = getFile(0, file).getRemainingBytes();
				crc.update(bytes);

				int value = (int) crc.getValue();
				buffer.putInt(value);
				hash = (hash << 1) + value;
			}

			buffer.putInt(hash);
			buffer.flip();

			synchronized (this) {
				DataBuffer duplicate = buffer.asReadOnlyBuffer();
				crcs = Optional.of(duplicate);
				return duplicate;
			}
		}

		throw new IOException("Cannot get CRC table from a writable file system.");
	}

	/**
	 * Gets a file.
	 *
	 * @param descriptor The {@link FileDescriptor} which points to the file.
	 * @return A {@link DataBuffer} which contains the contents of the file.
	 * @throws IOException If an I/O error occurs.
	 */
	public DataBuffer getFile(FileDescriptor descriptor) throws IOException {
		Index index = getIndex(descriptor);
		DataBuffer buffer = DataBuffer.allocate(index.getSize());

		long position = index.getBlock() * FileSystemConstants.BLOCK_SIZE;
		int read = 0;
		int size = index.getSize();
		int blocks = size / FileSystemConstants.CHUNK_SIZE;

		if (size % FileSystemConstants.CHUNK_SIZE != 0) {
			blocks++;
		}

		for (int id = 0; id < blocks; id++) {
			DataBuffer header = DataBuffer.allocate(FileSystemConstants.HEADER_SIZE);
			synchronized (data) {
				data.seek(position);
				data.readFully(header.array());
			}

			position += FileSystemConstants.HEADER_SIZE;

			int nextFile = header.getUnsignedShort();
			int currentChunk = header.getUnsignedShort();
			int nextBlock = header.getUnsignedTriByte();
			int nextType = header.getUnsignedByte();

			Preconditions.checkArgument(id == currentChunk, "Chunk id mismatch: id=" + id + ", chunk=" + currentChunk + ", type="
					+ descriptor.getType() + ", file=" + descriptor.getFile() + ".");
			int chunkSize = Math.min(FileSystemConstants.CHUNK_SIZE, size - read);

			DataBuffer chunk = DataBuffer.allocate(chunkSize);
			synchronized (data) {
				data.seek(position);
				data.readFully(chunk.array());
			}
			buffer.put(chunk);

			read += chunkSize;
			position = nextBlock * FileSystemConstants.BLOCK_SIZE;

			if (size > read) {
				Preconditions.checkArgument(nextType == descriptor.getType() + 1, "File type mismatch.");
				Preconditions.checkArgument(nextFile == descriptor.getFile(), "File id mismatch.");
			}
		}

		return buffer.flip();
	}

	/**
	 * Gets a file.
	 *
	 * @param type The file type.
	 * @param file The file id.
	 * @return A {@link DataBuffer} containing the contents of the file.
	 * @throws IOException If there is an error getting the file.
	 */
	public DataBuffer getFile(int type, int file) throws IOException {
		return getFile(new FileDescriptor(type, file));
	}

	/**
	 * Gets the number of files with the specified type.
	 *
	 * @param type The type.
	 * @return The number of files.
	 * @throws IOException If there is an error getting the length of the index file.
	 * @throws IndexOutOfBoundsException If the file {@code type} is out of bounds.
	 */
	public int getFileCount(int type) throws IOException {
		Preconditions.checkElementIndex(type, indices.size(), "File type out of bounds.");

		RandomAccessFile index = indices.get(type);
		synchronized (index) {
			return (int) (index.length() / Index.BYTES);
		}
	}

	/**
	 * Gets the index of a file.
	 *
	 * @param descriptor The {@link FileDescriptor} which points to the file.
	 * @return The {@link Index}.
	 * @throws IOException If there is an error seeking the index file.
	 */
	public Index getIndex(FileDescriptor descriptor) throws IOException {
		int type = descriptor.getType();
		Preconditions.checkElementIndex(type, indices.size(), "File descriptor type out of bounds.");

		RandomAccessFile index = indices.get(type);
		long position = descriptor.getFile() * Index.BYTES;
		Preconditions.checkArgument(position >= 0 && index.length() >= position + Index.BYTES, "Could not find find index.");

		DataBuffer buffer = DataBuffer.allocate(Index.BYTES);
		synchronized (index) {
			index.seek(position);
			index.readFully(buffer.array());
		}

		return IndexCodec.decode(buffer);
	}

	/**
	 * Gets the amount of indices in this IndexedFileSystem.
	 *
	 * @return The amount of indices.
	 */
	public int getIndexCount() {
		return indices.size();
	}

	/**
	 * Checks if this {@link IndexedFileSystem} is read only.
	 *
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isReadOnly() {
		return mode == AccessMode.READ;
	}

	/**
	 * Gets the data file ({@code main_file_cache.dat}), as a {@link RandomAccessFile}.
	 *
	 * @param base The base directory.
	 * @return The data file.
	 * @throws FileNotFoundException If the data file could not be found.
	 */
	private RandomAccessFile getDataFile(Path base) throws FileNotFoundException {
		Path resources = base.resolve("main_file_cache.dat");

		if (!Files.exists(resources) || Files.isDirectory(resources)) {
			throw new FileNotFoundException("No data file present.");
		}

		return new RandomAccessFile(resources.toFile(), mode.asUnix());
	}

	/**
	 * Gets the index files, as a {@link List} of {@link RandomAccessFile}s.
	 *
	 * @param base The base {@link Path}.
	 * @return The list of indices.
	 * @throws FileNotFoundException If there are no index files present.
	 */
	private List<RandomAccessFile> getIndexFiles(Path base) throws FileNotFoundException {
		List<RandomAccessFile> indices = new ArrayList<>();

		for (int id = 0; id < INDEX_COUNT; id++) {
			Path index = base.resolve("main_file_cache.idx" + id);

			if (Files.exists(index) && !Files.isDirectory(index)) {
				indices.add(new RandomAccessFile(index.toFile(), mode.asUnix()));
			}
		}

		if (indices.size() == 0) {
			throw new FileNotFoundException("No index files present.");
		}

		return ImmutableList.copyOf(indices);
	}

}