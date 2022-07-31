package rs.emulate.legacy.title.font

import rs.emulate.legacy.IndexedFileSystem
import rs.emulate.legacy.archive.Archive
import rs.emulate.legacy.graphics.GraphicsDecoder
import rs.emulate.legacy.graphics.ImageFormat
import rs.emulate.legacy.graphics.sprite.MediaId

/**
 * A [GraphicsDecoder] for [Font]s.
 *
 * @param graphics The [Archive] containing the font.
 * @param name The name of the [Font] to decode.
 */
class FontDecoder(graphics: Archive, name: MediaId) : GraphicsDecoder(graphics, name) {

    /**
     * Decodes the [Font].
     */
    fun decode(): Font {
        index.readerIndex(data.readUnsignedShort() + 4)
        val offset = index.readUnsignedByte()

        if (offset > 0) {
            index.readerIndex(3 * (offset - 1))
        }

        val glyphs = Array(GLYPHS_PER_FONT) { decodeGlyph() }
        return Font(name, glyphs)
    }

    /**
     * Decodes a single [Glyph].
     */
    private fun decodeGlyph(): Glyph {
        var horizontalOffset = index.readUnsignedByte()
        val verticalOffset = index.readUnsignedByte()
        val width = index.readUnsignedShort()
        val height = index.readUnsignedShort()

        val format = ImageFormat.valueOf(index.readUnsignedByte().toInt())
        val raster = decodeRaster(width, height, format)

        var spacing = width + SPACING
        var left = 0
        var right = 0

        for (y in height / 7 until height) {
            left += raster[y * width].toInt()
            right += raster[(y + 1) * width - 1].toInt()
        }

        if (left <= height / 7) {
            spacing--
            horizontalOffset = 0
        }
        if (right <= height / 7) {
            spacing--
        }

        return Glyph(format, raster, height, width, horizontalOffset.toInt(), verticalOffset.toInt(), spacing)
    }

    /**
     * Decodes the raster of a single [Glyph].
     */
    private fun decodeRaster(width: Int, height: Int, format: ImageFormat): ByteArray {
        val count = width * height
        val raster = ByteArray(count)

        when (format) {
            ImageFormat.COLUMN_ORDERED -> data.readBytes(raster)
            ImageFormat.ROW_ORDERED -> for (x in 0 until width) {
                for (y in 0 until height) {
                    raster[x + y * width] = data.readByte()
                }
            }
        }

        return raster
    }

    companion object {

        /**
         * The amount of Glyphs in a font.
         */
        private const val GLYPHS_PER_FONT = 256

        /**
         * The default spacing offset.
         */
        private const val SPACING = 2

        /**
         * The file id of the title archive.
         */
        private const val TITLE_FILE_ID = 1

        /**
         * Creates a new FontDecoder for the Font with the specified name, using the specified [IndexedFileSystem].
         */
        fun create(fs: IndexedFileSystem, name: MediaId): FontDecoder {
            return FontDecoder(fs.getArchive(0, TITLE_FILE_ID), name)
        }
    }

}
