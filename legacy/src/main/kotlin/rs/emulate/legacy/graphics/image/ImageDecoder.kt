package rs.emulate.legacy.graphics.image

import com.google.common.collect.ImmutableList
import rs.emulate.legacy.IndexedFileSystem
import rs.emulate.legacy.archive.Archive
import rs.emulate.legacy.graphics.GraphicsConstants
import rs.emulate.legacy.graphics.GraphicsDecoder
import rs.emulate.legacy.graphics.ImageFormat
import rs.emulate.util.readUnsignedTriByte
import java.io.IOException
import java.util.Arrays

/**
 * Decodes an [IndexedImage] from the 2D-graphics archive.
 *
 * @param graphics The [Archive] containing the image.
 * @param name The name of the [IndexedImage(s)][IndexedImage] to decode.
 */
class ImageDecoder(graphics: Archive, name: String) : GraphicsDecoder(graphics, name) {

    /**
     * Decodes all available [IndexedImage]s.
     */
    fun decode(): List<IndexedImage> {
        index.readerIndex(data.readUnsignedShort())

        val resizeWidth = index.readUnsignedShort()
        val resizeHeight = index.readUnsignedShort()

        val colours = index.readUnsignedByte().toInt()
        val palette = IntArray(colours)

        for (index in 1 until colours) {
            palette[index] = this.index.readUnsignedTriByte()
        }

        val images = mutableListOf<IndexedImage>()

        while (data.readableBytes() > 0 && index.readableBytes() > 0) {
            images.add(decode(palette, resizeHeight, resizeWidth))
        }

        return ImmutableList.copyOf(images)
    }

    /**
     * Decodes data into a [IndexedImage].
     */
    private fun decode(palette: IntArray, resizeHeight: Int, resizeWidth: Int): IndexedImage {
        val offsetX = index.readUnsignedByte().toInt()
        val offsetY = index.readUnsignedByte().toInt()

        val width = index.readUnsignedShort()
        val height = index.readUnsignedShort()

        val format = ImageFormat.valueOf(index.readUnsignedByte().toInt())
        val raster = decodeRaster(format, width, height, palette)

        return IndexedImage(name, format, width, height, raster, palette, offsetX, offsetY, resizeWidth, resizeHeight)
    }

    /**
     * Decodes the raster of a single [IndexedImage].
     */
    private fun decodeRaster(format: ImageFormat, width: Int, height: Int, palette: IntArray): IntArray {
        val raster = IntArray(width * height)

        when (format) {
            ImageFormat.COLUMN_ORDERED -> Arrays.setAll(raster) { getColour(palette, data.readByte()) }
            ImageFormat.ROW_ORDERED -> for (x in 0 until width) {
                for (y in 0 until height) {
                    raster[x + y * width] = getColour(palette, data.readByte())
                }
            }
        }

        return raster
    }

    /**
     * Gets the ARGB colour of a pixel of the [IndexedImage].
     */
    private fun getColour(palette: IntArray, index: Byte): Int {
        val colour = palette[index.toInt()]
        return if (colour == 0) colour else colour or 0xFF00_0000.toInt()
    }

    companion object {

        /**
         * Creates a new ImageDecoder to decode a Image (or Images) with the specified name, using the specified
         * [IndexedFileSystem].
         *
         * @throws IOException If there is an error decoding the graphics [Archive].
         */
        fun create(fs: IndexedFileSystem, name: String): ImageDecoder {
            return ImageDecoder(fs.getArchive(0, GraphicsConstants.GRAPHICS_FILE_ID), name)
        }
    }

}
