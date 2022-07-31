package rs.emulate.legacy.graphics.sprite

import rs.emulate.legacy.graphics.ImageFormat
import java.util.Arrays

/**
 * A 2-dimensional sprite in the cache.
 *
 * @param name The name of this sprite.
 * @param raster The width of this sprite.
 * @param format The sprite [ImageFormat].
 * @param height The height of this sprite.
 * @param width The width of this sprite.
 * @param offsetX The x offset to draw this sprite from.
 * @param offsetY The y offset to draw this sprite from.
 * @param resizeHeight The default height to resize this sprite to, when requested.
 * @param resizeWidth The default width to resize this sprite to, when requested.
 */
class Sprite(
    val name: MediaId,
    val index: Int,
    raster: IntArray,
    val format: ImageFormat,
    val height: Int,
    val width: Int,
    val offsetX: Int,
    val offsetY: Int,
    val resizeHeight: Int,
    val resizeWidth: Int
) {

    /**
     * The raster of this sprite.
     */
    val raster: IntArray = raster.clone()
        get() = field.clone()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Sprite

        if (name != other.name) return false
        if (format != other.format) return false
        if (height != other.height) return false
        if (width != other.width) return false
        if (offsetX != other.offsetX) return false
        if (offsetY != other.offsetY) return false
        if (resizeHeight != other.resizeHeight) return false
        if (resizeWidth != other.resizeWidth) return false
        return Arrays.equals(raster, other.raster)
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + format.hashCode()
        result = 31 * result + height
        result = 31 * result + width
        result = 31 * result + offsetX
        result = 31 * result + offsetY
        result = 31 * result + resizeHeight
        result = 31 * result + resizeWidth
        return 31 * result + Arrays.hashCode(raster)
    }

}
