package rs.emulate.legacy.graphics.sprite

import rs.emulate.legacy.graphics.ImageFormat

/**
 * A builder class for a [Sprite].
 *
 * @param name The name of the [Sprite].
 */
class SpriteBuilder(private var name: MediaId, private var index: Int) {

    lateinit var format: ImageFormat
    var height: Int = 0
    var offsetX: Int = 0
    var offsetY: Int = 0
    lateinit var raster: IntArray
    var resizeHeight: Int = 0
    var resizeWidth: Int = 0
    var width: Int = 0

    /**
     * Builds the data contained in this builder into a [Sprite].
     */
    fun build(): Sprite {
        require(height != 0 && width != 0) { "Height and width must not be 0." }
        return Sprite(name, index, raster, format, height, width, offsetX, offsetY, resizeHeight, resizeWidth)
    }

    /**
     * Duplicates this SpriteBuilder. Every value of the duplicate builder will be the same as this one, although the
     * raster will be a deep copy.
     */
    fun duplicate(): SpriteBuilder {
        val duplicate = SpriteBuilder(name, index)

        if (::format.isInitialized) {
            duplicate.format = format
        }

        if (::raster.isInitialized) {
            duplicate.raster = raster
        }

        duplicate.height = height
        duplicate.offsetX = offsetX
        duplicate.offsetY = offsetY
        duplicate.resizeHeight = resizeHeight
        duplicate.resizeWidth = resizeWidth
        duplicate.width = width

        return duplicate
    }

}
