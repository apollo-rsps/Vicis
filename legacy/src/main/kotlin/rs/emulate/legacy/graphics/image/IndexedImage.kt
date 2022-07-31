package rs.emulate.legacy.graphics.image

import rs.emulate.legacy.graphics.ImageFormat
import rs.emulate.legacy.graphics.sprite.MediaId

import java.util.Arrays
import java.util.Objects

/**
 * A 2-dimensional image that stores its data using indexed colours.
 *
 * @param name The name of the Image.
 * @param format The Image [ImageFormat].
 * @param width The width of the Image.
 * @param height The height of the Image.
 * @param raster The width of the Image.
 * @param palette The palette of the Image.
 * @param offsetX The x offset to draw the Image from.
 * @param offsetY The y offset to draw the Image from.
 * @param resizeWidth The default width to resize the Image to, when requested.
 * @param resizeHeight The default height to resize the Image to, when requested.
 */
class IndexedImage(
    val name: MediaId,
    val format: ImageFormat,
    val width: Int,
    val height: Int,
    raster: IntArray,
    palette: IntArray,
    val offsetX: Int,
    val offsetY: Int,
    val resizeWidth: Int,
    val resizeHeight: Int) {

    /**
     * The palette.
     */
    val palette: IntArray = palette.clone()
        get() = field.clone()

    /**
     * The raster of this Image.
     */
    val raster: IntArray = raster.clone()
        get() = field.clone()

    override fun equals(other: Any?): Boolean {
        if (other is IndexedImage) {
            if (format != other.format || offsetX != other.offsetX || offsetY != other.offsetY) {
                return false
            } else if (resizeHeight != other.resizeHeight || resizeWidth != other.resizeWidth) {
                return false
            }

            return name == other.name && Arrays.equals(raster, other.raster)
        }

        return false
    }

    override fun hashCode(): Int {
        return Objects.hash(name, Arrays.hashCode(raster), format, offsetX, offsetY, resizeHeight, resizeWidth)
    }

}
