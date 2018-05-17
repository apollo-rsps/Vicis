package rs.emulate.shared.world

/**
 * A position in the world.
 */
data class Position(val x: Int, val z: Int, val height: Int = 0) {

    init {
        require(height in 0 until HEIGHT_LEVELS) { "Height level out of bounds." }
    }

    /**
     * Gets the x coordinate of the central sector.
     */
    val centralSectorX: Int
        get() = x / 8

    /**
     * Gets the z coordinate of the central sector.
     */
    val centralSectorZ: Int
        get() = z / 8

    /**
     * Gets the x coordinate inside the sector of this position.
     */
    val localX: Int
        get() = getLocalX(this)

    /**
     * Gets the z coordinate inside the sector of this position.
     */
    val localZ: Int
        get() = getLocalZ(this)

    /**
     * Gets the x coordinate of the sector this position is in.
     */
    val topLeftSectorX: Int
        get() = x / 8 - 6

    /**
     * Gets the z coordinate of the sector this position is in.
     */
    val topLeftSectorZ: Int
        get() = z / 8 - 6

    /**
     * Gets the local x coordinate inside the sector of the `base` position.
     */
    fun getLocalX(base: Position): Int {
        return x - base.topLeftSectorX * 8
    }

    /**
     * Gets the local z coordinate inside the sector of the `base` position.
     */
    fun getLocalZ(base: Position): Int {
        return z - base.topLeftSectorZ * 8
    }

    companion object {

        /**
         * The number of height levels.
         */
        const val HEIGHT_LEVELS = 4

        /**
         * The maximum distance players/NPCs can 'see'.
         */
        const val MAX_DISTANCE = 15
    }

}
