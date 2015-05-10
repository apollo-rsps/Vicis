package rs.emulate.legacy.map;

import java.io.IOException;

import rs.emulate.legacy.IndexedFileSystem;
import rs.emulate.shared.util.CompressionUtils;
import rs.emulate.shared.util.DataBuffer;
import rs.emulate.shared.world.Position;

/**
 * A decoder for a {@link MapFile}.
 *
 * @author Major
 */
public final class MapFileDecoder {

	/**
	 * Creates a MapFileDecoder for the specified map file.
	 *
	 * @param fs The {@link IndexedFileSystem} to get the file from.
	 * @param map The map file id.
	 * @return The MapFileDecoder.
	 * @throws IOException If there is an error reading or decompressing the file.
	 */
	public static MapFileDecoder create(IndexedFileSystem fs, int map) throws IOException {
		DataBuffer compressed = fs.getFile(MapFileConstants.MAP_INDEX, map);
		DataBuffer decompressed = CompressionUtils.gunzip(compressed);
		return new MapFileDecoder(decompressed);
	}

	/**
	 * The DataBuffer containing the MapFile data.
	 */
	private final DataBuffer buffer;

	/**
	 * Creates the MapFileDecoder.
	 * <p>
	 * This constructor expects the {@link DataBuffer} to <strong>not</strong> be compressed.
	 *
	 * @param buffer The DataBuffer containing the MapFile data.
	 */
	public MapFileDecoder(DataBuffer buffer) {
		this.buffer = buffer.asReadOnlyBuffer();
	}

	/**
	 * Decodes the data into a {@link MapFile}.
	 *
	 * @return The MapFile.
	 */
	public MapFile decode() {
		MapPlane[] planes = new MapPlane[MapFile.PLANES];

		for (int level = 0; level < MapFile.PLANES; level++) {
			planes[level] = decodePlane(planes, level);
		}

		return new MapFile(planes);
	}

	/**
	 * Decodes a {@link MapPlane} with the specified level.
	 *
	 * @param planes The previously-decoded {@link MapPlane}s, for calculating the height of the tiles.
	 * @param level The level.
	 * @return The MapPlane.
	 */
	private MapPlane decodePlane(MapPlane[] planes, int level) {
		Tile[][] tiles = new Tile[MapFile.WIDTH][MapFile.WIDTH];

		for (int x = 0; x < MapFile.WIDTH; x++) {
			for (int z = 0; z < MapFile.WIDTH; z++) {
				tiles[x][z] = decodeTile(planes, level, x, z);
			}
		}

		return new MapPlane(level, tiles);
	}

	/**
	 * Decodes the data into a {@link Tile}.
	 *
	 * @param planes The previously-decoded {@link MapPlane}s, for calculating the height of the Tile.
	 * @param level The level the Tile is on.
	 * @param x The x coordinate of the Tile.
	 * @param z The z coordinate of the Tile.
	 * @return The MapFile.
	 */
	private Tile decodeTile(MapPlane[] planes, int level, int x, int z) {
		Tile.Builder builder = Tile.builder(new Position(x, z, level));

		int type;
		do {
			type = buffer.getUnsignedByte();

			if (type == 0) {
				if (level == 0) {
					builder.setHeight(MapFileConstants.HEIGHT_MULTIPLICAND * TileUtils.calculateHeight(x, z));
				} else {
					Tile below = planes[level - 1].getTile(x, z);
					builder.setHeight(below.getHeight() + MapFileConstants.PLANE_HEIGHT_DIFFERENCE);
				}
			} else if (type == 1) {
				int height = buffer.getUnsignedByte();
				int below = (level == 0) ? 0 : planes[level - 1].getTile(x, z).getHeight();

				builder.setHeight((height == 1 ? 0 : height) * MapFileConstants.HEIGHT_MULTIPLICAND + below);
			} else if (type <= MapFileConstants.MINIMUM_OVERLAY_TYPE) {
				builder.setOverlay(buffer.getByte());
				builder.setOverlayType((type - MapFileConstants.LOWEST_CONTINUED_TYPE)
						/ MapFileConstants.ORIENTATION_COUNT);
				builder.setOverlayOrientation(type - MapFileConstants.LOWEST_CONTINUED_TYPE
						% MapFileConstants.ORIENTATION_COUNT);
			} else if (type <= MapFileConstants.MINIMUM_ATTRIBUTES_TYPE) {
				builder.setAttributes(type - MapFileConstants.MINIMUM_OVERLAY_TYPE);
			} else {
				builder.setUnderlay(type - MapFileConstants.MINIMUM_ATTRIBUTES_TYPE);
			}
		} while (type >= MapFileConstants.LOWEST_CONTINUED_TYPE);

		return builder.build();
	}
}