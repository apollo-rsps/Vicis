package rs.emulate.legacy.archive;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rs.emulate.shared.util.CompressionUtils;
import rs.emulate.shared.util.DataBuffer;

/**
 * Contains methods for encoding and decoding {@link Archive}s.
 * 
 * @author Major
 * @author Graham
 */
public final class ArchiveCodec {

	/**
	 * Decodes the {@link Archive} in the specified {@link DataBuffer}.
	 * 
	 * @param buffer The buffer.
	 * @return The archive.
	 * @throws IOException If there is an error decompressing the data.
	 */
	public static Archive decode(DataBuffer buffer) throws IOException {
		if (buffer.isEmpty()) {
			return Archive.EMPTY_ARCHIVE;
		}

		int extractedSize = buffer.getUnsignedTriByte();
		int size = buffer.getUnsignedTriByte();
		boolean extracted = false;

		if (size != extractedSize) {
			buffer = CompressionUtils.bunzip2(buffer);
			extracted = true;
		}

		int count = buffer.getUnsignedShort();
		int[] identifiers = new int[count];
		int[] extractedSizes = new int[count];
		int[] sizes = new int[count];

		for (int entry = 0; entry < count; entry++) {
			identifiers[entry] = buffer.getInt();
			extractedSizes[entry] = buffer.getUnsignedTriByte();
			sizes[entry] = buffer.getUnsignedTriByte();
		}

		List<ArchiveEntry> entries = new ArrayList<>(count);

		for (int entry = 0; entry < count; entry++) {
			DataBuffer data = DataBuffer.allocate(extracted ? extractedSizes[entry] : sizes[entry]);
			data.fill(buffer);

			entries.add(new ArchiveEntry(identifiers[entry], extracted ? data : CompressionUtils.bunzip2(data)));
		}

		return new Archive(entries);
	}
	
	// TODO theres a bug with archive compression...

	/**
	 * Encodes the specified {@link Archive} into a {@link DataBuffer}.
	 * 
	 * @param archive The archive.
	 * @param compression The compression type (see {@link CompressionType}).
	 * @return The buffer.
	 * @throws IOException If there is an error compressing the archive.
	 */
	public static DataBuffer encode(Archive archive, CompressionType compression) throws IOException {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream(archive.getSize())) {
			List<ArchiveEntry> entries = archive.getEntries();
			int entryCount = entries.size();

			DataBuffer meta = DataBuffer.allocate(entryCount * (Integer.BYTES + 2 * 3) + Short.BYTES);
			meta.putShort(entryCount);

			for (ArchiveEntry entry : entries) {
				DataBuffer uncompressed = entry.getBuffer();
				DataBuffer compressed = CompressionUtils.bzip2(uncompressed);
				uncompressed.position(0); // We just read from this buffer, so reset the position.

				meta.putInt(entry.getIdentifier());
				meta.putTriByte(uncompressed.remaining());
				meta.putTriByte(compressed.remaining());

				switch (compression) {
					case ARCHIVE_COMPRESSION:
					case NO_COMPRESSION:
						bos.write(uncompressed.getRemainingBytes());
						break;
					case ENTRY_COMPRESSION:
						bos.write(compressed.getRemainingBytes());
						break;
					default:
						throw new IllegalArgumentException("Unrecognised compression type " + compression + ".");
				}
			}

			meta.flip();

			byte[] compressed = bos.toByteArray();
			DataBuffer data = DataBuffer.allocate(meta.limit() + compressed.length);
			data.put(meta).put(compressed).flip();

			DataBuffer header = DataBuffer.allocate(2 * 3);
			int extracted = data.limit();
			header.putTriByte(extracted);

			if (compression == CompressionType.ARCHIVE_COMPRESSION) {
				data = CompressionUtils.bzip2(data);
			}

			int compressedLength = data.limit();
			header.putTriByte(compressedLength).flip();

			DataBuffer buffer = DataBuffer.allocate(header.limit() + data.limit());
			buffer.put(header).put(data).flip();

			return buffer;
		}
	}

	/**
	 * Sole private constructor to prevent instantiation.
	 */
	private ArchiveCodec() {

	}

}