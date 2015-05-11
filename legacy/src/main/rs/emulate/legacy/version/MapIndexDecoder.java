package rs.emulate.legacy.version;

import java.io.FileNotFoundException;

import rs.emulate.legacy.archive.Archive;
import rs.emulate.shared.util.DataBuffer;

/**
 * Decoder for the version lists "map_index" entry.
 *
 * @author sfix
 * @author Major
 */
public class MapIndexDecoder {

	/**
	 * The name of the archive entry containing the map index data.
	 */
	private static final String ENTRY_NAME = "map_index";

	/**
	 * The DataBuffer containing the data.
	 */
	private final DataBuffer data;

	/**
	 * Creates the MapIndexDecoder.
	 *
	 * @param archive The version list {@link Archive}.
	 * @throws FileNotFoundException If the {@code map_index} entry could not be found.
	 */
	public MapIndexDecoder(Archive archive) throws FileNotFoundException {
		data = archive.getEntry(ENTRY_NAME).getBuffer().asReadOnlyBuffer();
	}

	/**
	 * Decodes the contents of the {@code map_index} entry into a {@link MapIndex}.
	 *
	 * @return The MapIndex.
	 */
	public MapIndex decode() {
		int count = data.remaining() / (3 * Short.BYTES + Byte.BYTES);

		int[] areas = new int[count];
		int[] landscapes = new int[count];
		int[] maps = new int[count];
		boolean[] members = new boolean[count];

		for (int index = 0; index < count; index++) {
			areas[index] = data.getUnsignedShort();
			maps[index] = data.getUnsignedShort();
			landscapes[index] = data.getUnsignedShort();
			members[index] = data.getBoolean();
		}

		return new MapIndex(areas, landscapes, maps, members);
	}

}