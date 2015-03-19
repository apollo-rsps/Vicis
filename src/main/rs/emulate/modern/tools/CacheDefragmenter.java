package rs.emulate.modern.tools;

import java.io.IOException;

import rs.emulate.modern.Container;
import rs.emulate.modern.FileStore;
import rs.emulate.modern.ReferenceTable;
import rs.emulate.shared.util.DataBuffer;

/**
 * A utility to defragment a cache.
 * 
 * @author Graham
 */
public final class CacheDefragmenter {

	/**
	 * The entry point for the application.
	 * 
	 * @param args The application arguments.
	 * @throws IOException If there is an error encoding or decoding the cache.
	 */
	public static void main(String[] args) throws IOException {
		try (FileStore in = FileStore.open("../game/data/cache/");
				FileStore out = FileStore.create("/tmp/defragmented-cache", in.getTypeCount())) {
			for (int type = 0; type < in.getTypeCount(); type++) {
				DataBuffer buffer = in.read(255, type);
				buffer.mark();
				out.write(255, type, buffer);
				buffer.reset();

				ReferenceTable table = ReferenceTable.decode(Container.decode(buffer).getData());
				for (int file = 0; file < table.capacity(); file++) {
					if (table.getEntry(file) == null) {
						continue;
					}

					out.write(type, file, in.read(type, file));
				}
			}
		}
	}

}