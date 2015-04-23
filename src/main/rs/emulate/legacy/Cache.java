package rs.emulate.legacy;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rs.emulate.legacy.archive.Archive;
import rs.emulate.legacy.archive.ArchiveCodec;
import rs.emulate.legacy.archive.CompressionType;
import rs.emulate.shared.util.DataBuffer;

import com.google.common.base.Preconditions;

/**
 * A container for a set of {@link Archive}s.
 *
 * @author Major
 */
public final class Cache {

	/**
	 * The default name of the data file.
	 */
	private static final String DEFAULT_DATA_FILE_NAME = "main_file_cache.dat";

	/**
	 * The default prefix of an index file.
	 */
	private static final String DEFAULT_INDEX_FILE_PREFIX = "main_file_cache.idx";

	/**
	 * The Map of FileDescriptors to Buffers.
	 */
	private final Map<FileDescriptor, DataBuffer> files = new HashMap<>();

	/**
	 * Encodes this Cache, writing the output to files in the specified {@link Path}.
	 * 
	 * @param base The base Path.
	 * @throws IOException If there is an error writing the data in this Cache to disk.
	 */
	public void encode(Path base) throws IOException {
		encode(base, DEFAULT_DATA_FILE_NAME, DEFAULT_INDEX_FILE_PREFIX);
	}

	/**
	 * Encodes this cache, writing the output to the specified files in the specified {@link Path}.
	 * 
	 * @param base The directory to place the output files in.
	 * @param dataFile The name of the data file.
	 * @param indexPrefix The prefix of the index files.
	 * @throws IOException If there is an error writing the data to disk.
	 */
	public void encode(Path base, String dataFile, String indexPrefix) throws IOException {
		if (Files.notExists(base)) {
			Files.createDirectories(base);
		} else if (!Files.isDirectory(base)) {
			throw new IllegalArgumentException("Specified base path (" + base + ") must be a directory.");
		}

		OpenOption[] options = { StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE };
		Path dataPath = base.resolve(dataFile);

		try (FileChannel data = FileChannel.open(dataPath, options)) {
			data.write(ByteBuffer.allocate(520)); // write the empty block
			Map<Integer, FileChannel> indices = new HashMap<>();

			FileChannel zero = FileChannel.open(base.resolve(indexPrefix + '0'), options);
			indices.put(0, zero);

			zero.write(IndexCodec.encode(new Index(0, 0)).getByteBuffer()); // Index for the empty block.
			int block = 1;

			for (Map.Entry<FileDescriptor, DataBuffer> entry : sortedFileList()) {
				FileDescriptor descriptor = entry.getKey();
				DataBuffer buffer = entry.getValue();

				int size = buffer.remaining(), type = descriptor.getType();
				int blocks = (size + FileSystemConstants.CHUNK_SIZE - 1) / FileSystemConstants.CHUNK_SIZE;

				FileChannel index = indices.get(type);

				if (index == null) {
					index = FileChannel.open(base.resolve(indexPrefix + type), options);
					indices.put(type, index);
				}

				DataBuffer pointer = IndexCodec.encode(new Index(size, block));
				index.write(pointer.getByteBuffer());

				int file = descriptor.getFile();
				for (int id = 0; id < blocks; id++) {
					DataBuffer header = DataBuffer.allocate(FileSystemConstants.HEADER_SIZE);
					header.putShort(file).putShort(id);
					header.putTriByte(++block).putByte(type + 1).flip();
					data.write(header.getByteBuffer());

					int remaining = buffer.remaining();
					if (remaining < FileSystemConstants.CHUNK_SIZE) {
						data.write(buffer.getByteBuffer());
						int padding = FileSystemConstants.CHUNK_SIZE - remaining;

						data.write(ByteBuffer.allocate(padding));
					} else {
						DataBuffer chunk = DataBuffer.allocate(FileSystemConstants.CHUNK_SIZE);
						chunk.fill(buffer);

						data.write(chunk.getByteBuffer());
					}
				}
			}

			for (FileChannel index : indices.values()) {
				index.close();
			}
		}
	}

	/**
	 * Gets the {@link Archive} with the specified {@link FileDescriptor}.
	 * 
	 * @param descriptor The descriptor.
	 * @return The Archive.
	 */
	public Archive getArchive(FileDescriptor descriptor) {
		DataBuffer file = files.get(descriptor);

		if (file != null) {
			try {
				return ArchiveCodec.decode(file);
			} catch (IOException e) {
				throw new IllegalArgumentException("Cannot decode a regular file into an archive.", e);
			}
		}

		throw new IllegalArgumentException("No archive with the specified descriptor is stored in this Cache.");
	}

	/**
	 * Gets the file data with the specified {@link FileDescriptor}.
	 * 
	 * @param descriptor The FileDescriptor.
	 * @return The file data, in a {@link DataBuffer}.
	 */
	public DataBuffer getFile(FileDescriptor descriptor) {
		return files.get(descriptor);
	}

	/**
	 * Places an {@link Archive} with the specified {@link FileDescriptor} into this Cache.
	 * 
	 * @param descriptor The FileDescriptor of the Archive. Must not be {@code null}.
	 * @param archive The Archive. Must not be {@code null}.
	 * @param type The {@link CompressionType} to apply to the Archive. Must not be {@code null}.
	 */
	public void putArchive(FileDescriptor descriptor, Archive archive, CompressionType type) {
		try {
			System.out.println("Encoding " + descriptor + " with type " + type + ", size= " + archive.getSize());
			files.put(descriptor, ArchiveCodec.encode(archive, type));
		} catch (IOException e) {
			throw new IllegalArgumentException("Invalid archive provided.", e);
		}
	}

	/**
	 * Places the file with the specified {@link FileDescriptor} into this Cache.
	 * 
	 * @param descriptor The FileDescriptor.
	 * @param file The file data.
	 */
	public void putFile(FileDescriptor descriptor, DataBuffer file) {
		files.put(descriptor, file);
	}

	/**
	 * Replaces the {@link Archive} represented by the specified {@link FileDescriptor} with the specified Archive.
	 * 
	 * @param descriptor The FileDescriptor of the Archive. Must not be {@code null}.
	 * @param archive The Archive to replace the existing one with. Must not be {@code null}.
	 * @param type The {@link CompressionType} to apply to the new Archive. Must not be {@code null}.
	 * @throws IllegalArgumentException If the Archive does not exist.
	 */
	public void replaceArchive(FileDescriptor descriptor, Archive archive, CompressionType type) {
		DataBuffer buffer = files.get(descriptor);
		Preconditions.checkArgument(buffer != null, "Archive does not exist, cannot replace it (use putArchive() instead).");

		putArchive(descriptor, archive, type);
	}

	/**
	 * Gets the {@link List} of files to encode, in ascending order.
	 * 
	 * @return The {@link List} of {@link Map} entries.
	 */
	private List<Map.Entry<FileDescriptor, DataBuffer>> sortedFileList() {
		List<Map.Entry<FileDescriptor, DataBuffer>> entries = new ArrayList<>(files.entrySet());
		entries.sort((a, b) -> {
			FileDescriptor first = a.getKey(), second = b.getKey();
			int type = Integer.compare(first.getType(), second.getType());

			return type == 0 ? Integer.compare(first.getFile(), second.getFile()) : type;
		});

		return entries;
	}

}