package rs.emulate.legacy.archive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rs.emulate.legacy.IndexedFileSystem;
import rs.emulate.shared.util.DataBuffer;
import rs.emulate.shared.util.CompressionUtils;
import rs.emulate.shared.world.GameObject;
import rs.emulate.shared.world.Position;

/**
 * Decodes map object data from the {@code map_index.dat} file into {@link GameObject}s. TODO redo and move and stuff
 * 
 * @author Chris Fletcher
 */
public final class MapFileDecoder {

	/**
	 * The {@link IndexedFileSystem}.
	 */
	private final IndexedFileSystem fs;

	/**
	 * Creates the decoder.
	 * 
	 * @param fs The indexed file system.
	 */
	public MapFileDecoder(IndexedFileSystem fs) {
		this.fs = fs;
	}

	/**
	 * Decodes all static objects and places them in the returned array.
	 * 
	 * @return The decoded objects.
	 * @throws IOException If an I/O error occurs.
	 */
	public GameObject[] decode() throws IOException {
		Archive versionList = ArchiveCodec.decode(fs.getFile(0, 5));
		DataBuffer buffer = versionList.getEntry("map_index").getBuffer();

		int indices = buffer.remaining() / 7;
		int[] areas = new int[indices];
		int[] landscapes = new int[indices];

		for (int i = 0; i < indices; i++) {
			areas[i] = buffer.getShort() & 0xFFFF;

			@SuppressWarnings("unused")
			int mapFile = buffer.getShort() & 0xFFFF;

			landscapes[i] = buffer.getShort() & 0xFFFF;

			@SuppressWarnings("unused")
			boolean members = (buffer.getByte() & 0xFF) == 1;
		}

		List<GameObject> objects = new ArrayList<>();

		for (int i = 0; i < indices; i++) {
			DataBuffer compressed = fs.getFile(4, landscapes[i]);
			DataBuffer uncompressed = CompressionUtils.gunzip(compressed);

			List<GameObject> areaObjects = parseArea(areas[i], uncompressed);
			objects.addAll(areaObjects);
		}

		return objects.toArray(new GameObject[objects.size()]);
	}

	/**
	 * Parses a single area from the specified buffer.
	 * 
	 * @param area The identifier of that area.
	 * @param buffer The buffer which holds the area's data.
	 * @return A collection of all parsed objects.
	 */
	private static List<GameObject> parseArea(int area, DataBuffer buffer) {
		List<GameObject> objects = new ArrayList<>();

		int x = (area >> 8 & 0xFF) * 64;
		int y = (area & 0xFF) * 64;

		int id = -1;
		int idOffset;

		while ((idOffset = buffer.getSmart()) != 0) {
			id += idOffset;

			int packedPosition = 0;
			int positionOffset;

			while ((positionOffset = buffer.getSmart()) != 0) {
				packedPosition += positionOffset - 1;

				int localX = packedPosition >> 6 & 0x3F;
				int localY = packedPosition & 0x3F;
				int height = packedPosition >> 12;

				int info = buffer.getByte() & 0xFF;
				int type = info >> 2;
				int rotation = info & 3;

				Position position = new Position(x + localX, y + localY, height);

				GameObject object = new GameObject(id, position, type, rotation);
				objects.add(object);
			}
		}

		return objects;
	}

}