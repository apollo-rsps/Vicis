package rs.emulate.legacy.graphics.sprite

import com.google.common.collect.ImmutableList
import rs.emulate.legacy.IndexedFileSystem
import rs.emulate.legacy.archive.Archive
import rs.emulate.legacy.graphics.GraphicsConstants
import rs.emulate.legacy.graphics.GraphicsDecoder
import rs.emulate.legacy.graphics.ImageFormat
import rs.emulate.util.readUnsignedTriByte
import java.util.Arrays

// TODO: we should probably just always refer to media containers by id.
sealed class MediaId {
    data class Name(val value: String) : MediaId()
    data class Id(val value: Int) : MediaId()
}

/**
 * Decodes a [Sprite] from the 2D-graphics archive.
 *
 * @param graphics The [Archive] containing the sprite.
 * @param name The name of the [Sprite] to decode.
 */
class SpriteDecoder(graphics: Archive, name: MediaId) : GraphicsDecoder(graphics, name) {

    /**
     * Decodes all [Sprite]s, returning the decoded Sprites as an [ImmutableList].
     */
    fun decode(): List<Sprite> {
        index.readerIndex(data.readUnsignedShort())

        val resizeHeight = index.readUnsignedShort()
        val resizeWidth = index.readUnsignedShort()

        val colours = index.readUnsignedByte().toInt()
        val palette = IntArray(colours)

        for (index in 1 until colours) {
            val colour = this.index.readUnsignedTriByte()
            palette[index] = if (colour == 0) 1 else colour
        }

        val sprites = mutableListOf<Sprite>()

        while (data.readableBytes() > 0 && index.readableBytes() > 0) {
            sprites += decode(sprites.size, palette, resizeHeight, resizeWidth)
        }

        return sprites.toList()
    }

    /**
     * Decodes data into a [Sprite].
     */
    private fun decode(offset: Int, palette: IntArray, resizeHeight: Int, resizeWidth: Int): Sprite {
        val offsetX = index.readUnsignedByte()
        val offsetY = index.readUnsignedByte()

        val width = index.readUnsignedShort()
        val height = index.readUnsignedShort()

        val format = ImageFormat.valueOf(index.readUnsignedByte().toInt())
        val raster = decodeRaster(format, width, height, palette)

        return Sprite(name, offset, raster, format, height, width, offsetX.toInt(), offsetY.toInt(), resizeHeight, resizeWidth)
    }

    /**
     * Decodes the raster of a single [Sprite].
     */
    private fun decodeRaster(format: ImageFormat, width: Int, height: Int, palette: IntArray): IntArray {
        val raster = IntArray(width * height)

        when (format) {
            ImageFormat.COLUMN_ORDERED -> Arrays.setAll(raster) { getColour(palette, data.readUnsignedByte()) }
            ImageFormat.ROW_ORDERED -> for (x in 0 until width) {
                for (y in 0 until height) {
                    raster[x + y * width] = getColour(palette, data.readUnsignedByte())
                }
            }
        }

        return raster
    }

    /**
     * Gets the ARGB colour of a pixel of the [Sprite].
     */
    private fun getColour(palette: IntArray, index: Short): Int {
        val colour = palette[index.toInt()]
        return if (colour == 0) colour else colour or 0xFF00_0000.toInt()
    }

    companion object {

        /**
         * Creates a new SpriteDecoder to decode a Sprite (or Sprites) with the specified name, using the specified
         * [IndexedFileSystem].
         */
        fun create(fs: IndexedFileSystem, name: MediaId): SpriteDecoder {
            return SpriteDecoder(fs.getArchive(0, GraphicsConstants.GRAPHICS_FILE_ID), name)
        }
    }

}
