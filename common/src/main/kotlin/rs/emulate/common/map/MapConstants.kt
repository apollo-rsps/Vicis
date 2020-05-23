package rs.emulate.common.map

/**
 * Contains [MapFile]-related constants.
 */
internal object MapConstants {

    /**
     * The index containing the map files.
     */
    const val MAP_INDEX = 4

    /**
     * The multiplicand for height values.
     */
    const val HEIGHT_MULTIPLICAND = 8

    /**
     * The lowest type value that will result in the decoding of a Tile being continued.
     */
    const val LOWEST_CONTINUED_TYPE = 2

    /**
     * The minimum type that specifies the Tile attributes.
     */
    const val MINIMUM_ATTRIBUTES_TYPE = 81

    /**
     * The minimum type that specifies the Tile underlay id.
     */
    const val MINIMUM_OVERLAY_TYPE = 49

    /**
     * The amount of possible overlay orientations.
     */
    const val ORIENTATION_COUNT = 4

    /**
     * The height difference between two planes.
     */
    const val PLANE_HEIGHT_DIFFERENCE = 240

}
