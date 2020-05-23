package rs.emulate.common.map

/**
 * A plane of a map, which is a distinct height level.
 *
 * @param level The height level of the MapPlane.
 * @param tiles The 2D array of [Tile]s. Must be square.
 */
class MapPlane(val level: Int, tiles: Array<Array<Tile>>) {

    private val tiles: Array<Array<Tile>> = clone(tiles)

    /**
     * The amount of tiles in this MapPlane.
     */
    val size: Int = tiles.size * tiles.size

    /**
     * Returns a shallow copy of the specified 2-dimensional array.
     */
    private fun <T> clone(array: Array<Array<T>>): Array<Array<T>> {
        val copy = array.clone()
        for (index in copy.indices) {
            copy[index] = array[index].clone()
        }

        return copy
    }

    /**
     * Gets the [Tile] at the specified (x, z) coordinate.
     */
    fun getTile(x: Int, z: Int): Tile {
        return tiles[x][z]
    }

    /**
     * Gets the [Tile]s in this MapPlane.
     *
     * This method returns the Tiles according on a column-based ordering: for a 2x2 tile set, the order will be
     * `(0, 0), (0, 1), (1, 0), (1, 1)`.
     */
    fun tiles(): List<Tile> {
        return tiles.flatMap { it.toList() }
    }

}
