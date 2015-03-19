package rs.emulate.modern;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import rs.emulate.shared.util.DataBuffer;
import rs.emulate.shared.util.FileChannelUtils;

/**
 * A file store holds multiple files inside a "virtual" file system made up of several index files and a single data
 * file.
 *
 * @author Graham
 */
public final class FileStore implements Closeable {

	/**
	 * Creates a new file store.
	 * 
	 * @param root The root directory.
	 * @param indices The number of indices.
	 * @return The file store.
	 * @throws IOException If there is an error creating a directory or a file.
	 */
	public static FileStore create(Path root, int indices) throws IOException {
		try {
			Files.createDirectories(root);

			for (int index = 0; index < indices; index++) {
				Path file = root.resolve("main_file_cache.idx" + index);
				Files.createDirectory(file);
			}

			Path meta = root.resolve("main_file_cache.idx255");
			Files.createDirectory(meta);

			Path data = root.resolve("main_file_cache.dat2");
			Files.createDirectory(data);
		} catch (IOException e) {
			throw new IOException("Could not make directories.", e);
		}

		return open(root);
	}

	/**
	 * Creates a new file store.
	 * 
	 * @param root The root path.
	 * @param indices The indices.
	 * @return The file store.
	 * @throws IOException If there is an error creating the file store.
	 */
	public static FileStore create(String root, int indices) throws IOException {
		return create(Paths.get(root), indices);
	}

	/**
	 * Opens the file store stored in the specified directory.
	 *
	 * @param root The directory containing the index and data files.
	 * @return The file store.
	 * @throws IOException if any of the {@code main_file_cache.*} files could not be opened.
	 */
	public static FileStore open(Path root) throws IOException {
		Path main = root.resolve("main_file_cache.dat2");
		if (!Files.exists(main)) {
			throw new FileNotFoundException("Main file cache does not exist in " + root + ".");
		}

		FileChannel data = FileChannel.open(main, StandardOpenOption.READ, StandardOpenOption.WRITE);

		List<FileChannel> indices = new ArrayList<>();
		for (int i = 0; i < 254; i++) {
			Path index = root.resolve("main_file_cache.idx" + i);
			if (!Files.exists(index)) {
				break;
			}

			FileChannel channel = FileChannel.open(index, StandardOpenOption.READ, StandardOpenOption.WRITE);
			indices.add(channel);
		}

		if (indices.isEmpty()) {
			throw new FileNotFoundException("Index file does not exist.");
		}

		Path reference = root.resolve("main_file_cache.idx255");
		if (!Files.exists(reference)) {
			throw new FileNotFoundException("Index 255 does not exist.");
		}

		FileChannel meta = FileChannel.open(reference, StandardOpenOption.READ, StandardOpenOption.WRITE);

		return new FileStore(data, indices.toArray(new FileChannel[0]), meta);
	}

	/**
	 * Opens the file store stored in the specified directory.
	 *
	 * @param root The directory containing the index and data files.
	 * @return The file store.
	 * @throws IOException If any of the {@code main_file_cache.*} files could not be opened.
	 */
	public static FileStore open(String root) throws IOException {
		return open(Paths.get(root));
	}

	/**
	 * The data file.
	 */
	private final FileChannel dataChannel;

	/**
	 * The index files.
	 */
	private final FileChannel[] indexChannels;

	/**
	 * The 'meta' index files.
	 */
	private final FileChannel metaChannel;

	/**
	 * Creates a new file store.
	 *
	 * @param data The data file.
	 * @param indexes The index files.
	 * @param meta The 'meta' index file.
	 */
	public FileStore(FileChannel data, FileChannel[] indexes, FileChannel meta) {
		dataChannel = data;
		indexChannels = indexes;
		metaChannel = meta;
	}

	@Override
	public void close() throws IOException {
		dataChannel.close();

		for (FileChannel channel : indexChannels) {
			channel.close();
		}

		metaChannel.close();
	}

	/**
	 * Gets the number of files of the specified type.
	 *
	 * @param type The type.
	 * @return The number of files.
	 * @throws IOException if an I/O error occurs.
	 */
	public int getFileCount(int type) throws IOException {
		if ((type < 0 || type >= indexChannels.length) && type != 255) {
			throw new IllegalArgumentException("Type must be within 0-" + indexChannels.length + " or 255.");
		}

		return (int) (type == 255 ? metaChannel.size() : indexChannels[type].size()) / Index.SIZE;
	}

	/**
	 * Gets the number of index files, not including the meta index file.
	 *
	 * @return The number of index files.
	 */
	public int getTypeCount() {
		return indexChannels.length;
	}

	/**
	 * Reads a file.
	 *
	 * @param type The type of the file.
	 * @param id The id of the file.
	 * @return A {@link DataBuffer} containing the contents of the file.
	 * @throws IOException if an I/O error occurs.
	 */
	public DataBuffer read(int type, int id) throws IOException {
		if ((type < 0 || type >= indexChannels.length) && type != 255) {
			throw new FileNotFoundException("Specified type is invalid.");
		}

		@SuppressWarnings("resource")
		FileChannel channel = (type == 255) ? metaChannel : indexChannels[type];
		long position = id * Index.SIZE;
		if (position < 0 || position >= channel.size()) {
			channel.close();
			throw new FileNotFoundException("Position to read from is invalid.");
		}

		DataBuffer buffer = DataBuffer.allocate(Index.SIZE);
		FileChannelUtils.readFully(channel, buffer, position);

		Index index = Index.decode(buffer.flip());

		DataBuffer data = DataBuffer.allocate(index.getSize());
		buffer = DataBuffer.allocate(Sector.SIZE);

		int chunk = 0, remaining = index.getSize();
		position = index.getSector() * Sector.SIZE;

		do {
			buffer.clear();
			FileChannelUtils.readFully(dataChannel, buffer, position);
			Sector sector = Sector.decode(buffer.flip());

			if (remaining > Sector.DATA_SIZE) {
				data.put(sector.getData().array(), 0, Sector.DATA_SIZE);
				remaining -= Sector.DATA_SIZE;

				if (sector.getType() != type) {
					throw new IOException("File type mismatch.");
				} else if (sector.getId() != id) {
					throw new IOException("File id mismatch.");
				} else if (sector.getChunk() != chunk++) {
					throw new IOException("Chunk mismatch.");
				}

				position = sector.getNextSector() * Sector.SIZE;
			} else {
				data.put(sector.getData().array(), 0, remaining);
				remaining = 0;
			}
		} while (remaining > 0);

		return data.flip();
	}

	/**
	 * Writes a file.
	 * 
	 * @param type The type of the file.
	 * @param id The id of the file.
	 * @param buffer A {@link ByteBuffer} containing the contents of the file.
	 * @throws IOException If there is an error writing the file.
	 */
	public void write(int type, int id, DataBuffer buffer) throws IOException {
		write(type, id, buffer.getByteBuffer());
	}

	/**
	 * Writes a file.
	 *
	 * @param type The type of the file.
	 * @param id The id of the file.
	 * @param data A {@link ByteBuffer} containing the contents of the file.
	 * @throws IOException If there is an error writing the file.
	 */
	public void write(int type, int id, ByteBuffer data) throws IOException {
		data.mark();
		if (!write(type, id, data, true)) {
			data.reset();
			write(type, id, data, false);
		}
	}

	/**
	 * Writes a file.
	 *
	 * @param type The type of the file.
	 * @param id The id of the file.
	 * @param data A {@link ByteBuffer} containing the contents of the file.
	 * @param overwrite A flag indicating if the existing file should be overwritten.
	 * @return A flag indicating if the file was written successfully.
	 * @throws IOException If there is an error writing the file.
	 */
	private boolean write(int type, int id, ByteBuffer data, boolean overwrite) throws IOException {
		if ((type < 0 || type >= indexChannels.length) && type != 255) {
			throw new FileNotFoundException("Specified type is invalid.");
		}

		try (FileChannel indexChannel = (type == 255) ? metaChannel : indexChannels[type]) {
			int nextSector;
			long pointer = id * Index.SIZE;
			if (overwrite) {
				if (pointer < 0) {
					throw new IOException("Pointer < 0.");
				} else if (pointer >= indexChannel.size()) {
					return false;
				}

				DataBuffer buffer = DataBuffer.allocate(Index.SIZE);
				FileChannelUtils.readFully(indexChannel, buffer, pointer);

				Index index = Index.decode(buffer.flip());
				nextSector = index.getSector();
				if (nextSector <= 0 || nextSector > dataChannel.size() * Sector.SIZE) {
					return false;
				}
			} else {
				nextSector = (int) (dataChannel.size() + Sector.SIZE - 1) / Sector.SIZE;
				if (nextSector == 0) {
					nextSector = 1;
				}
			}

			Index index = new Index(data.remaining(), nextSector);
			indexChannel.write(index.encode().getByteBuffer(), pointer);

			DataBuffer buf = DataBuffer.allocate(Sector.SIZE);

			int chunk = 0, remaining = index.getSize();
			do {
				int currentSector = nextSector;
				pointer = currentSector * Sector.SIZE;
				nextSector = 0;

				if (overwrite) {
					buf.clear();
					FileChannelUtils.readFully(dataChannel, buf, pointer);
					Sector sector = Sector.decode(buf.flip());

					if (sector.getType() != type) {
						return false;
					} else if (sector.getId() != id) {
						return false;
					} else if (sector.getChunk() != chunk) {
						return false;
					}

					nextSector = sector.getNextSector();
					if (nextSector < 0 || nextSector > dataChannel.size() / Sector.SIZE) {
						return false;
					}
				}

				if (nextSector == 0) {
					overwrite = false;
					nextSector = (int) ((dataChannel.size() + Sector.SIZE - 1) / Sector.SIZE);
					if (nextSector == 0) {
						nextSector++;
					}
					if (nextSector == currentSector) {
						nextSector++;
					}
				}

				byte[] bytes = new byte[Sector.DATA_SIZE];
				if (remaining < Sector.DATA_SIZE) {
					data.get(bytes, 0, remaining);
					nextSector = 0; // mark as EOF
					remaining = 0;
				} else {
					remaining -= Sector.DATA_SIZE;
					data.get(bytes, 0, Sector.DATA_SIZE);
				}

				Sector sector = new Sector(type, id, chunk++, nextSector, DataBuffer.wrap(bytes));
				dataChannel.write(sector.encode().getByteBuffer(), pointer);
			} while (remaining > 0);
		}

		return true;
	}

}