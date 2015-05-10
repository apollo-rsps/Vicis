package rs.emulate.legacy.map;

/**
 * Contains {@link MapFile}-related constants.
 *
 * @author Major
 */
final class MapFileConstants {

	/**
	 * The minimum type that specifies the Tile attributes.
	 */
	static final int MINIMUM_ATTRIBUTES_TYPE = 81;
	/**
	 * The minimum type that specifies the Tile underlay id.
	 */
	static final int MINIMUM_UNDERLAY_TYPE = 49;
	/**
	 * The amount of possible overlay orientations.
	 */
	static final int ORIENTATION_COUNT = 4;
	/**
	 * The multiplicand for height values.
	 */
	static final int HEIGHT_MULTIPLICAND = 8;
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