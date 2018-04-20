package rs.emulate.legacy.title.font

import rs.emulate.legacy.graphics.ImageFormat

/**
 * A glyph of a single character in a [Font].
 */
class Glyph(
    val format: ImageFormat,
    raster: ByteArray,
    val height: Int,
    val width: Int,
    val horizontalOffset: Int,
    val verticalOffset: Int,
    val spacing: Int
) {

    /**
     * The raster of this glyph.
     */
    val raster: ByteArray = raster.clone()
        get() = field.clone()

}
