package rs.emulate.modern.tools;

import rs.emulate.modern.Container;
import rs.emulate.modern.Entry;
import rs.emulate.modern.FileStore;
import rs.emulate.modern.ReferenceTable;
import rs.emulate.shared.util.DataBuffer;

import java.io.IOException;
import java.util.zip.CRC32;

/**
 * A utility to verify an existing cache is complete.
 *
 * @author Graham
 */
public final class CacheVerifier {

	/**
	 * The entry point for the application.
	 *
	 * @param args The program arguments.
	 * @throws IOException If there is an error verifying the cache.
	 */
	public static void main(String[] args) throws IOException {
		try (FileStore store = FileStore.open("../game/data/cache/")) {
			for (int type = 0; type < store.getFileCount(255); type++) {
				ReferenceTable table = ReferenceTable.decode(Container.decode(store.read(255, type)));
				for (int file = 0; file < table.capacity(); file++) {
					Entry entry = table.getEntry(file);
					if (entry == null) {
						continue;
					}

					DataBuffer buffer;
					try {
						buffer = store.read(type, file);
					} catch (IOException ex) {
						System.out.println(type + ":" + file + " error");
						continue;
					}

					if (buffer.capacity() <= 2) {
						System.out.println(type + ":" + file + " missing");
						continue;
					}

					byte[] bytes = new byte[buffer.limit() - 2];
					buffer.position(0); // last two bytes are the version and shouldn't
					buffer.get(bytes); // be included

					CRC32 crc = new CRC32();
					crc.update(bytes, 0, bytes.length);

					if (crc.getValue() != entry.getCrc()) {
						System.out.println(type + ":" + file + " corrupt");
					}

					buffer.position(buffer.limit() - 2);
					if (buffer.getUnsignedShort() != (entry.getVersion() & 0xFFFF)) {
						System.out.println(type + ":" + file + " out of date");
					}
				}
			}
		}
	}

}