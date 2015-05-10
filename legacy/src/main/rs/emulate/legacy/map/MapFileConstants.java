package rs.emulate.legacy.map;

/**
 * Contains {@link MapFile}-related constants.
 *
 * @author Major
 */
final class MapFileConstants {

	/**
	 * The index containing the map files.
	 */
	public static final int MAP_INDEX = 4;

	/**
	 * The multiplicand for height values.
	 */
	static final int HEIGHT_MULTIPLICAND = 8;

	/**
	 * The lowest type value that will result in the decoding of a Tile being continued.
	 */
	static final int LOWEST_CONTINUED_TYPE = 2;

	/**
	 * The minimum type that specifies the Tile attributes.
	 */
	static final int MINIMUM_ATTRIBUTES_TYPE = 81;

	/**
	 * The minimum type that specifies the Tile underlay id.
	 */
	static final int MINIMUM_OVERLAY_TYPE = 49;

	/**
	 * The amount of possible overlay orientations.
	 */
	static final int ORIENTATION_COUNT = 4;

	/**
	 * The height difference between two planes.
	 */
	static final int PLANE_HEIGHT_DIFFERENCE = 240;

	/**
	 * Sole private constructor to prevent instantiation.
	 */
	private MapFileConstants() {

	}

}