package rs.emulate.legacy.graphics.image;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rs.emulate.legacy.IndexedFileSystem;
import rs.emulate.legacy.archive.Archive;
import rs.emulate.legacy.graphics.GraphicsConstants;
import rs.emulate.legacy.graphics.GraphicsDecoder;
import rs.emulate.legacy.graphics.ImageFormat;

import com.google.common.collect.ImmutableList;

/**
 * Decodes an {@link IndexedImage} from the 2D-graphics archive.
 * 
 * @author Major
 */
public final class ImageDecoder extends GraphicsDecoder {

	/**
	 * Creates a new ImageDecoder to decode a Image (or Images) with the specified name, using the specified
	 * {@link IndexedFileSystem}.
	 * 
	 * @param fs The IndexedFileSystem.
	 * @param name The name of the Image.
	 * @return The ImageDecoder.
	 * @throws IOException If there is an error decoding the graphics {@link Archive}.
	 */
	public static final ImageDecoder create(IndexedFileSystem fs, String name) throws IOException {
		return new ImageDecoder(fs.getArchive(0, GraphicsConstants.GRAPHICS_FILE_ID), name);
	}

	/**
	 * The name of the Image.
	 */
	private final String name;

	/**
	 * Creates the ImageDecoder.
	 * 
	 * @param archive The {@link Archive}.
	 * @param name The name of the {@link IndexedImage}(s) to decode.
	 * @throws FileNotFoundException If the data or index ArchiveEntry could not be found.
	 */
	public ImageDecoder(Archive archive, String name) throws FileNotFoundException {
		super(getDataEntry(archive, name), getIndexEntry(archive));
		this.name = name;
	}

	/**
	 * Decodes all {@link IndexedImage}s, returning the decoded Images as an {@link ImmutableList}.
	 * 
	 * @return The list of Images.
	 * @throws IOException If there is an error decoding a Image.
	 */
	public List<IndexedImage> decode() throws IOException {
		index.position(data.getUnsignedShort());

		int resizeWidth = index.getUnsignedShort();
		int resizeHeight = index.getUnsignedShort();

		int colours = index.getUnsignedByte();
		int[] palette = new int[colours];

		for (int index = 1; index < colours; index++) {
			palette[index] = this.index.getUnsignedTriByte();
		}

		List<IndexedImage> images = new ArrayList<>();

		while (data.hasRemaining() && index.hasRemaining()) {
			images.add(decode(palette, resizeHeight, resizeWidth));
		}

		return ImmutableList.copyOf(images);
	}

	/**
	 * Decodes data into a {@link IndexedImage}.
	 * 
	 * @param palette The color palette of the Image.
	 * @param resizeHeight The resize height of the Image.
	 * @param resizeWidth The resize width of the Image.
	 * @return The Image.
	 * @throws IOException If there is an error decoding the archive or Image.
	 */
	private IndexedImage decode(int[] palette, int resizeHeight, int resizeWidth) throws IOException {
		int offsetX = index.getUnsignedByte();
		int offsetY = index.getUnsignedByte();

		int width = index.getUnsignedShort();
		int height = index.getUnsignedShort();

		ImageFormat format = ImageFormat.valueOf(index.getUnsignedByte());
		int[] raster = decodeRaster(format, width, height, palette);

		return new IndexedImage(name, raster, palette, format, height, width, offsetX, offsetY, resizeHeight, resizeWidth);
	}

	/**
	 * Decodes the raster of a single {@link IndexedImage}.
	 * 
	 * @param format The {@link ImageFormat}.
	 * @param width The width of the Image.
	 * @param height The height of the Image.
	 * @param palette The colour palette.
	 * @return The raster.
	 */
	private int[] decodeRaster(ImageFormat format, int width, int height, int[] palette) {
		int[] raster = new int[width * height];

		if (format == ImageFormat.COLUMN_ORDERED) {
			Arrays.setAll(raster, index -> getColour(palette, data.getByte()));
		} else if (format == ImageFormat.ROW_ORDERED) {
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					raster[x + y * width] = getColour(palette, data.getByte());
				}
			}
		} else {
			throw new UnsupportedOperationException("Unsupported image decoding format: " + format + ".");
		}

		return raster;
	}

	/**
	 * Gets the ARGB colour of a pixel of the {@link IndexedImage}.
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