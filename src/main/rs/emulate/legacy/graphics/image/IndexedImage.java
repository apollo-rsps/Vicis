package rs.emulate.legacy.graphics.image;

import java.util.Arrays;
import java.util.Objects;

import rs.emulate.legacy.graphics.ImageFormat;

/**
 * A 2-dimensional image that stores its data using indexed colours.
 * 
 * @author Major
 */
public final class IndexedImage {

	/**
	 * Creates a {@link ImageBuilder} with the specified name.
	 * 
	 * @param name The name of the Image being built.
	 * @return The ImageBuilder.
	 */
	public static ImageBuilder builder(String name) {
		return new ImageBuilder(name);
	}

	/**
	 * The format of this Image.
	 */
	private final ImageFormat format;

	/**
	 * The height of this Image.
	 */
	private final int height;

	/**
	 * The name of this Image.
	 */
	private final String name;

	/**
	 * The x offset to draw this Image from.
	 */
	private final int offsetX;

	/**
	 * The y offset to draw this Image from.
	 */
	private final int offsetY;

	/**
	 * The palette.
	 */
	private final int[] palette;

	/**
	 * The raster of this Image.
	 */
	private final int[] raster;

	/**
	 * The default height to resize this Image to.
	 */
	private final int resizeHeight;

	/**
	 * The default width to resize this Image to.
	 */
	private final int resizeWidth;

	/**
	 * The width of this Image.
	 */
	private final int width;

	/**
	 * Creates the IndexedImage.
	 * 
	 * @param name The name of the Image.
	 * @param raster The width of the Image.
	 * @param palette The palette of the Image.
	 * @param format The Image {@link ImageFormat}.
	 * @param height The height of the Image.
	 * @param width The width of the Image.
	 * @param offsetX The x offset to draw the Image from.
	 * @param offsetY The y offset to draw the Image from.
	 * @param resizeHeight The default height to resize the Image to, when requested.
	 * @param resizeWidth The default width to resize the Image to, when requested.
	 */
	public IndexedImage(String name, int[] raster, int[] palette, ImageFormat format, int height, int width, int offsetX,
			int offsetY, int resizeHeight, int resizeWidth) {
		this.format = format;
		this.name = name;
		this.height = height;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.raster = raster.clone();
		this.resizeHeight = resizeHeight;
		this.resizeWidth = resizeWidth;
		this.palette = palette.clone();
		this.width = width;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IndexedImage) {
			IndexedImage other = (IndexedImage) obj;
			if (format != other.format || offsetX != other.offsetX || offsetY != other.offsetY) {
				return false;
			} else if (resizeHeight != other.resizeHeight || resizeWidth != other.resizeWidth) {
				return false;
			}

			return name.equals(other.name) && Arrays.equals(raster, other.raster);
		}

		return false;
	}

	/**
	 * Gets the {@link ImageFormat} of this Image.
	 * 
	 * @return The format.
	 */
	public ImageFormat getFormat() {
		return format;
	}

	/**
	 * Gets the height of this Image.
	 *
	 * @return The height.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the name of this Image.
	 *
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the horizontal draw offset of this Image.
	 *
	 * @return The horizontal draw offset.
	 */
	public int getOffsetX() {
		return offsetX;
	}

	/**
	 * Gets the vertical draw offset of this Image.
	 *
	 * @return The vertical draw offset.
	 */
	public int getOffsetY() {
		return offsetY;
	}

	/**
	 * Gets the palette used by this IndexedImage.
	 * 
	 * @return The palette.
	 */
	public int[] getPalette() {
		return palette.clone();
	}

	/**
	 * Gets a deep copy of the raster of this Image.
	 *
	 * @return The raster.
	 */
	public int[] getRaster() {
		return raster.clone();
	}

	/**
	 * Gets the default height to resize this Image to, when requested.
	 *
	 * @return The resize height.
	 */
	public int getResizeHeight() {
		return resizeHeight;
	}

	/**
	 * Gets the default width to resize this Image to, when requested.
	 *
	 * @return The resize width.
	 */
	public int getResizeWidth() {
		return resizeWidth;
	}

	/**
	 * Gets the width of this Image.
	 *
	 * @return The width.
	 */
	public int getWidth() {
		return width;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, Arrays.hashCode(raster), format, offsetX, offsetY, resizeHeight, resizeWidth);
	}

}