package rs.emulate.legacy.graphics

/**
 * A format an image may be encoded in.
 *
 * @param value The integer value of the ImageFormat.
 */
enum class ImageFormat(val value: Int) {

    /**
     * The format where pixels are ordered by column (e.g. `array[1]` is the pixel at (0, 1)).
     */
    COLUMN_ORDERED(0),

    /**
     * The format where pixels are ordered by row (e.g. `array[1]` is the pixel at (1, 0)).
     */
    ROW_ORDERED(1);

    companion object {

        /**
         * Returns the [ImageFormat] with the specified integer value.
         */
        fun valueOf(value: Int): ImageFormat {
            return values().find { format -> format.value == value } ?: throw IllegalArgumentException(
                "Invalid integer value of $value specified.")
        }
    }

}
