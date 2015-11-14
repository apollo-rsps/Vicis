package rs.emulate.legacy.title.font;

import rs.emulate.legacy.IndexedFileSystem;
import rs.emulate.legacy.archive.Archive;
import rs.emulate.legacy.graphics.GraphicsDecoder;
import rs.emulate.legacy.graphics.ImageFormat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

/**
 * A {@link GraphicsDecoder} for {@link Font}s.
 *
 * @author Major
 */
public final class FontDecoder extends GraphicsDecoder {

	/**
	 * The amount of Glyphs in a font.
	 */
	private static final int GLYPHS_PER_FONT = 256;

	/**
	 * The default spacing offset.
	 */
	private static final int SPACING = 2;

	/**
	 * The file id of the title archive.
	 */
	private static final int TITLE_FILE_ID = 1;

	/**
	 * Creates a new FontDecoder for the Font with the specified name, using the specified {@link IndexedFileSystem}..
	 *
	 * @param fs The {@link IndexedFileSystem}.
	 * @param name The name of the {@link Font}.
	 * @return The FontDecoder.
	 * @throws IOException If there is an error decoding the title {@link Archive}.
	 */
	public static FontDecoder create(IndexedFileSystem fs, String name) throws IOException {
		return new FontDecoder(fs.getArchive(0, TITLE_FILE_ID), name);
	}

	/**
	 * The name of the Font.
	 */
	private final String name;

	/**
	 * Creates the FontDecoder.
	 *
	 * @param graphics The graphics {@link Archive}.
	 * @param name The name of the font.
	 * @throws FileNotFoundException If the data archive entry or the index archive entry could not be found.
	 */
	public FontDecoder(Archive graphics, String name) throws FileNotFoundException {
		super(getDataEntry(graphics, name), getIndexEntry(graphics));
		this.name = name;
	}

	/**
	 * Decodes the {@link Font}.
	 *
	 * @return The Font.
	 */
	public Font decode() {
		index.position(data.getUnsignedShort() + 4);
		int skip = index.getUnsignedByte();

		if (skip > 0) {
			index.skip(3 * (skip - 1));
		}

		Glyph[] glyphs = new Glyph[GLYPHS_PER_FONT];
		Arrays.setAll(glyphs, index -> decodeGlyph());

		return new Font(name, glyphs);
	}

	/**
	 * Decodes a single {@link Glyph}.
	 *
	 * @return The Glyph.
	 */
	private Glyph decodeGlyph() {
		int horizontalOffset = index.getUnsignedByte();
		int verticalOffset = index.getUnsignedByte();
		int width = index.getUnsignedShort();
		int height = index.getUnsignedShort();

		ImageFormat format = ImageFormat.valueOf(index.getUnsignedByte());
		byte[] raster = decodeRaster(width, height, format);

		int spacing = width + SPACING;
		int left = 0, right = 0;

		for (int y = height / 7; y < height; y++) {
			left += raster[y * width];
			right += raster[(y + 1) * width - 1];
		}

		if (left <= height / 7) {
			spacing--;
			horizontalOffset = 0;
		}
		if (right <= height / 7) {
			spacing--;
		}

		return new Glyph(format, raster, height, width, horizontalOffset, verticalOffset, spacing);
	}

	/**
	 * Decodes the raster of a single {@link Glyph}.
	 *
	 * @param width The width of the Glyph.
	 * @param height The height of the Glyph.
	 * @param format The {@link ImageFormat}.
	 * @return The raster.
	 */
	private byte[] decodeRaster(int width, int height, ImageFormat format) {
		int count = width * height;
		byte[] raster = new byte[count];

		if (format == ImageFormat.COLUMN_ORDERED) {
			data.get(raster);
		} else if (format == ImageFormat.ROW_ORDERED) {
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					raster[x + y * width] = (byte) data.getByte();
				}
			}
		} else {
			throw new UnsupportedOperationException("Unsupported ImageFormat " + format + ".");
		}

		return raster;
	}

}