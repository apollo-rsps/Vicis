package rs.emulate.legacy.graphics.sprite

import com.google.common.collect.ImmutableList
import rs.emulate.legacy.IndexedFileSystem
import rs.emulate.legacy.archive.Archive
import rs.emulate.legacy.graphics.GraphicsConstants
import rs.emulate.legacy.graphics.GraphicsDecoder
import rs.emulate.legacy.graphics.ImageFormat
import java.util.Arrays

/**
 * Decodes a [Sprite] from the 2D-graphics archive.
 *
 * @param graphics The [Archive] containing the sprite.
 * @param name The name of the [Sprite] to decode.
 */
class SpriteDecoder(graphics: Archive, name: String) : GraphicsDecoder(graphics, name) {

    /**
     * Decodes all [Sprite]s, returning the decoded Sprites as an [ImmutableList].
     */
    fun decode(): List<Sprite> {
        index.position(data.getUnsignedShort())

        val resizeHeight = index.getUnsignedShort()
        val resizeWidth = index.getUnsignedShort()

        val colours = index.getUnsignedByte()
        val palette = IntArray(colours)

        for (index in 1 until colours) {
            val colour = this.index.getUnsignedTriByte()
            palette[index] = if (colour == 0) 1 else colour
        }

        val sprites = mutableListOf<Sprite>()

        while (data.hasRemaining() && index.hasRemaining()) {
            sprites += decode(palette, resizeHeight, resizeWidth)
        }

        return sprites.toList()
    }

    /**
     * Decodes data into a [Sprite].
     */
    private fun decode(palette: IntArray, resizeHeight: Int, resizeWidth: Int): Sprite {
        val offsetX = index.getUnsignedByte()
        val offsetY = index.getUnsignedByte()

        val width = index.getUnsignedShort()
        val height = index.getUnsignedShort()

        val format = ImageFormat.valueOf(index.getUnsignedByte())
        val raster = decodeRaster(format, width, height, palette)

        return Sprite(name, raster, format, height, width, offsetX, offsetY, resizeHeight, resizeWidth)
    }

    /**
     * Decodes the raster of a single [Sprite].
     */
    private fun decodeRaster(format: ImageFormat, width: Int, height: Int, palette: IntArray): IntArray {
        val raster = IntArray(width * height)

        when (format) {
            ImageFormat.COLUMN_ORDERED -> Arrays.setAll(raster) { getColour(palette, data.getUnsignedByte()) }
            ImageFormat.ROW_ORDERED -> for (x in 0 until width) {
                for (y in 0 until height) {
                    raster[x + y * width] = getColour(palette, data.getUnsignedByte())
                }
            }
        }

        return raster
    }

    /**
     * Gets the ARGB colour of a pixel of the [Sprite].
     */
    private fun getColour(palette: IntArray, index: Int): Int {
        val colour = palette[index]
        return if (colour == 0) colour else colour or 0xFF00_0000.toInt()
    }

    companion object {

        /**
         * Creates a new SpriteDecoder to decode a Sprite (or Sprites) with the specified name, using the specified
         * [IndexedFileSystem].
         */
        fun create(fs: IndexedFileSystem, name: String): SpriteDecoder {
            return SpriteDecoder(fs.getArchive(0, GraphicsConstants.GRAPHICS_FILE_ID), name)
        }
    }

}
