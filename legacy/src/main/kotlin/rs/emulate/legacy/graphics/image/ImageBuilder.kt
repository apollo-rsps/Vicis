package rs.emulate.legacy.graphics.image

import rs.emulate.legacy.graphics.ImageFormat

/**
 * A builder class for an [IndexedImage].
 *
 * @param name The name of the [IndexedImage].
 */
class ImageBuilder(val name: String) {

    lateinit var format: ImageFormat
    var height: Int = 0
    var offsetX: Int = 0
    var offsetY: Int = 0
    lateinit var palette: IntArray
    lateinit var raster: IntArray
    var resizeHeight: Int = 0
    var resizeWidth: Int = 0
    var width: Int = 0

    init {
        require(name.isNotEmpty()) { "Name cannot be empty." }
    }

    /**
     * Builds the data contained in this builder into a [IndexedImage].
     */
    fun build(): IndexedImage {
        require(height != 0 && width != 0) { "Height and width must not be 0." }
        return IndexedImage(name, format, width, height, raster, palette, offsetX, offsetY, resizeWidth, resizeHeight)
    }

    /**
     * Duplicates this ImageBuilder. Every value of the duplicate builder will be the same as this one, although the
     * raster will be a deep copy.
     */
    fun duplicate(): ImageBuilder {
        val duplicate = ImageBuilder(name)
        duplicate.width = width
        duplicate.height = height

        if (::format.isInitialized) {
            duplicate.format = format
        }

        if (::raster.isInitialized) {
            duplicate.raster = raster
        }

        if (::palette.isInitialized) {
            duplicate.palette = palette
        }

        duplicate.offsetX = offsetX
        duplicate.offsetY = offsetY
        duplicate.resizeWidth = resizeWidth
        duplicate.resizeHeight = resizeHeight

        return duplicate
    }

}
