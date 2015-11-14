package rs.emulate.legacy.graphics.sprite;

import com.google.common.collect.ImmutableList;
import rs.emulate.legacy.IndexedFileSystem;
import rs.emulate.legacy.archive.Archive;
import rs.emulate.legacy.graphics.GraphicsConstants;
import rs.emulate.legacy.graphics.GraphicsDecoder;
import rs.emulate.legacy.graphics.ImageFormat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Decodes a {@link Sprite} from the 2D-graphics archive.
 *
 * @author Major
 */
public final class SpriteDecoder extends GraphicsDecoder {

	/**
	 * Creates a new SpriteDecoder to decode a Sprite (or Sprites) with the specified name, using the specified
	 * {@link IndexedFileSystem}.
	 *
	 * @param fs The IndexedFileSystem.
	 * @param name The name of the Sprite.
	 * @return The SpriteDecoder.
	 * @throws IOException If there is an error decoding the graphics {@link Archive}.
	 */
	public static final SpriteDecoder create(IndexedFileSystem fs, String name) throws IOException {
		return new SpriteDecoder(fs.getArchive(0, GraphicsConstants.GRAPHICS_FILE_ID), name);
	}

	/**
	 * The name of the Sprite.
	 */
	private final String name;

	/**
	 * Creates the SpriteDecoder.
	 *
	 * @param graphics The graphics {@link Archive}.
	 * @param name The name of the {@link Sprite}(s) to decode.
	 * @throws FileNotFoundException If the data or index ArchiveEntry could not be found.
	 */
	public SpriteDecoder(Archive graphics, String name) throws FileNotFoundException {
		super(getDataEntry(graphics, name), getIndexEntry(graphics));
		this.name = name;
	}

	/**
	 * Decodes all {@link Sprite}s, returning the decoded Sprites as an {@link ImmutableList}.
	 *
	 * @return The list of Sprites.
	 * @throws IOException If there is an error decoding a Sprite.
	 */
	public List<Sprite> decode() throws IOException {
		index.position(data.getUnsignedShort());

		int resizeHeight = index.getUnsignedShort();
		int resizeWidth = index.getUnsignedShort();

		int colours = index.getUnsignedByte();
		int[] palette = new int[colours];

		for (int index = 1; index < colours; index++) {
			int colour = this.index.getUnsignedTriByte();
			palette[index] = (colour == 0) ? 1 : colour;
		}

		List<Sprite> sprites = new ArrayList<>();

		while (data.hasRemaining() && index.hasRemaining()) {
			sprites.add(decode(palette, resizeHeight, resizeWidth));
		}

		return ImmutableList.copyOf(sprites);
	}

	/**
	 * Decodes data into a {@link Sprite}.
	 *
	 * @param palette The colour palette of the Sprite.
	 * @param resizeHeight The resize height of the Sprite.
	 * @param resizeWidth The resize width of the Sprite.
	 * @return The Sprite.
	 * @throws IOException If there is an error decoding the archive or Sprite.
	 */
	private Sprite decode(int[] palette, int resizeHeight, int resizeWidth) throws IOException {
		int offsetX = index.getUnsignedByte();
		int offsetY = index.getUnsignedByte();

		int width = index.getUnsignedShort();
		int height = index.getUnsignedShort();

		ImageFormat format = ImageFormat.valueOf(index.getUnsignedByte());
		int[] raster = decodeRaster(format, width, height, palette);

		return new Sprite(name, raster, format, height, width, offsetX, offsetY, resizeHeight, resizeWidth);
	}

	/**
	 * Decodes the raster of a single {@link Sprite}.
	 *
	 * @param format The {@link ImageFormat}.
	 * @param width The width of the Sprite.
	 * @param height The height of the Sprite.
	 * @param palette The colour palette.
	 * @return The raster.
	 */
	private int[] decodeRaster(ImageFormat format, int width, int height, int[] palette) {
		int[] raster = new int[width * height];

		if (format == ImageFormat.COLUMN_ORDERED) {
			Arrays.setAll(raster, index -> getColour(palette, data.getUnsignedByte()));
		} else if (format == ImageFormat.ROW_ORDERED) {
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					raster[x + y * width] = getColour(palette, data.getUnsignedByte());
				}
			}
		} else {
			throw new UnsupportedOperationException("Unsupported sprite decoding format: " + format + ".");
		}

		return raster;
	}

	/**
	 * Gets the ARGB colour of a pixel of the {@link Sprite}.
	 *
	 * @param palette The colour palette.
	 * @param index The index in the palette.
	 * @return The ARGB colour.
	 */
	private int getColour(int[] palette, int index) {
		int colour = palette[index];
		return (colour == 0) ? colour : colour | 0xFF00_0000;
	}

}