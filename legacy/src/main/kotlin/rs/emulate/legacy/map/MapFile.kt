package rs.emulate.legacy.map

/**
 * A 3-dimensional 64x64 area of the map.
 *
 * @param planes The [MapPlane]s.
 */
class MapFile(planes: Array<MapPlane>) {

    val planes: Array<MapPlane> = planes.clone()
        get() = field.clone()

    /**
     * Gets the [MapPlane] with the specified level.
     */
    fun getPlane(plane: Int): MapPlane {
        require(plane < planes.size) { "Plane index out of bounds, must be [0, ${planes.size})." }
        return planes[plane]
    }

    companion object {

        /**
         * The amount of planes in a MapFile.
         */
        const val PLANES = 4

        /**
         * The width of a MapFile, in [Tile]s.
         */
        const val WIDTH = 64
    }

}
