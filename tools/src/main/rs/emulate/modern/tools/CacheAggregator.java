package rs.emulate.modern.tools;

import rs.emulate.modern.Container;
import rs.emulate.modern.Entry;
import rs.emulate.modern.FileStore;
import rs.emulate.modern.ReferenceTable;
import rs.emulate.shared.util.DataBuffer;

import java.io.IOException;
import java.util.zip.CRC32;

/**
 * A utility to aggregate multiple caches into one, to fill missing entries.
 *
 * @author Graham
 */
public final class CacheAggregator {

	/**
	 * The entry point for the application.
	 *
	 * @param args The program arguments.
	 * @throws IOException If there is an error aggregating the cache.
	 */
	public static void main(String[] args) throws IOException {
		FileStore otherStore = FileStore.open("/rscd/data/");
		FileStore store = FileStore.open("../game/data/cache/");

		for (int type = 0; type < store.getFileCount(255); type++) {
			if (type == 7) {
				continue; // TODO need support for newer ref table format for this index
			}

			ReferenceTable otherTable = ReferenceTable.decode(Container.decode(otherStore.read(255, type)));
			ReferenceTable table = ReferenceTable.decode(Container.decode(store.read(255, type)));

			for (int file = 0; file < table.capacity(); file++) {
				Entry entry = table.getEntry(file);
				if (entry == null) {
					continue;
				}

				if (isRepackingRequired(store, entry, type, file)) {
					Entry otherEntry = otherTable.getEntry(file);
					if (entry.getVersion() == otherEntry.getVersion() && entry.getCrc() == otherEntry.getCrc()) {
						store.write(type, file, otherStore.read(type, file));
					}
				}
			}
		}
	}

	/**
	 * Returns whether or not cache repacking is required.
	 *
	 * @param store The {@link FileStore}.
	 * @param entry The {@link Entry}.
	 * @param type The type.
	 * @param file The file.
	 * @return {@code true} if repacking is required, {@code false} if not.
	 */
	private static boolean isRepackingRequired(FileStore store, Entry entry, int type, int file) {
		DataBuffer buffer;
		try {
			buffer = store.read(type, file);
		} catch (IOException ex) {
			return true;
		}

		if (buffer.capacity() <= 2) {
			return true;
		}

		byte[] bytes = new byte[buffer.limit() - 2]; // last two bytes are the version and shouldn't be included
		buffer.position(0);
		buffer.get(bytes);

		CRC32 crc = new CRC32();
		crc.update(bytes, 0, bytes.length);

		if ((int) crc.getValue() != entry.getCrc()) {
			return true;
		}

		buffer.position(buffer.limit() - 2);
		return buffer.getUnsignedShort() == entry.getVersion();
	}

}