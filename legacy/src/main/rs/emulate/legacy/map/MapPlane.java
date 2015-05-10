package rs.emulate.legacy.map;

/**
 * A plane of a map, which is a distinct height level.
 *
 * @author Major
 */
public final class MapPlane {

	/**
	 * The 2-dimensional array of Tiles.
	 */
	private final Tile[][] tiles;

	/**
	 * The level of this MapPlane.
	 */
	private final int level;

	/**
	 * Creates the MapPlane.
	 *
	 * @param level The level of the MapPlane.
	 * @param tiles The 2-dimensional array of {@link Tile}s.
	 */
	public MapPlane(int level, Tile[][] tiles) {
		this.level = level;
		this.tiles = clone(tiles);
	}

	/**
	 * Gets the {@link Tile} at the specified (x, z) coordinate.
	 *
	 * @param x The x coordinate.
	 * @param z The z coordinate.
	 * @return The Tile.
	 */
	public Tile get(int x, int z) {
		return tiles[x][z];
	}

	/**
	 * Gets the level of this MapPlane.
	 *
	 * @return The level.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Returns a shallow copy of the specified 2-dimensional array.
	 *
	 * @param array The array to copy.
	 * @return The copy.
	 */
	private static <T> T[][] clone(T[][] array) {
		T[][] copy = array.clone();
		for (int index = 0; index < copy.length; index++) {
			copy[index] = array[index].clone();
		}

		return copy;
	}

}