package rs.emulate.legacy.map;

import rs.emulate.shared.util.DataBuffer;

/**
 * A decoder for a {@link MapFile}.
 *
 * @author Major
 */
public final class MapFileDecoder {

	/**
	 * The multiplicand for height values.
	 */
	private static final int HEIGHT_MULTIPLICAND = 8;

	/**
	 * The minimum type that specifies the Tile attributes.
	 */
	private static final int MINIMUM_ATTRIBUTES_TYPE = 81;

	/**
	 * The minimum type that specifies the Tile underlay id.
	 */
	private static final int MINIMUM_UNDERLAY_TYPE = 49;

	/**
	 * The amount of possible overlay orientations.
	 */
	private static final int ORIENTATION_COUNT = 4;

	/**
	 * The height difference between two planes.
	 */
	private static final int PLANE_HEIGHT_DIFFERENCE = 240;

	/**
	 * The amount of planes in a map file.
	 */
	private static final int PLANES = 4;

	/**
	 * The width of a map file, in tiles.
	 */
	private static final int WIDTH = 64;

	/**
	 * The x coordinate offset, used for computing the tile height.
	 */
	private static final int X_OFFSET = 0xe3b7b;

	/**
	 * The z coordinate offset, used for computing the tile height.
	 */
	private static final int Z_OFFSET = 0x87cce;

	/**
	 * The DataBuffer containing the MapFile data.
	 */
	private final DataBuffer buffer;

	/**
	 * Creates the MapFileDecoder.
	 *
	 * @param buffer The {@link DataBuffer} containing the MapFile data.
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
		MapPlane[] planes = new MapPlane[PLANES];

		for (int level = 0; level < PLANES; level++) {
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
		Tile[][] tiles = new Tile[WIDTH][WIDTH];

		for (int x = 0; x < WIDTH; x++) {
			for (int z = 0; z < WIDTH; z++) {
				tiles[x][z] = decodeTile(planes, level, x, z);
			}
		}

		return new MapPlane(level, tiles);
	}

	/**
	 * Decodes the data into a {@link Tile}.
	 *
	 * @param planes The previously-decoded {@link MapPlane}s, for calculating the height of the tile.
	 * @param level The level the Tile is on.
	 * @param x The x coordinate of the Tile.
	 * @param z The z coordinate of the Tile.
	 * @return The MapFile.
	 */
	private Tile decodeTile(MapPlane[] planes, int level, int x, int z) {
		Tile.Builder builder = Tile.builder();

		int type;
		do {
			type = buffer.getUnsignedByte();

			if (type == 0) {
				if (level == 0) {
					builder.setHeight(HEIGHT_MULTIPLICAND * TileUtils.calculateHeight(X_OFFSET + x, Z_OFFSET + z));
				} else {
					Tile below = planes[level - 1].get(x, z);
					builder.setHeight(below.getHeight() + PLANE_HEIGHT_DIFFERENCE);
				}
			} else if (type == 1) {
				int height = buffer.getUnsignedByte();
				int below = (level == 0) ? 0 : planes[level - 1].get(x, z).getHeight();

				builder.setHeight((height == 1 ? 0 : height) * HEIGHT_MULTIPLICAND + below);
			} else if (type <= MINIMUM_UNDERLAY_TYPE) {
				builder.setOverlay(buffer.getUnsignedByte());
				builder.setOverlayType((type - 2) / 4);
				builder.setOverlayOrientation(type - 2 % ORIENTATION_COUNT);
			} else if (type <= MINIMUM_ATTRIBUTES_TYPE) {
				builder.setAttributes(type - MINIMUM_UNDERLAY_TYPE);
			} else {
				builder.setUnderlay(type - MINIMUM_ATTRIBUTES_TYPE);
			}
		} while (type > 1);

		return builder.build();
	}

}