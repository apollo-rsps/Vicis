package rs.emulate.legacy.graphics.image;

import com.google.common.base.Preconditions;
import rs.emulate.legacy.graphics.ImageFormat;
import rs.emulate.util.Assertions;

/**
 * A builder class for an {@link IndexedImage}.
 *
 * @author Major
 */
public final class ImageBuilder {

	/**
	 * The ImageFormat of the IndexedImage.
	 */
	private ImageFormat format;

	/**
	 * The height of the IndexedImage.
	 */
	private int height;

	/**
	 * The name of the IndexedImage.
	 */
	private String name;

	/**
	 * The x offset to draw the IndexedImage from.
	 */
	private int offsetX;

	/**
	 * The y offset to draw the IndexedImage from.
	 */
	private int offsetY;

	/**
	 * The palette of the IndexedImage.
	 */
	private int[] palette;

	/**
	 * The raster of the IndexedImage.
	 */
	private int[] raster;

	/**
	 * The default height to resize the IndexedImage to.
	 */
	private int resizeHeight;

	/**
	 * The default width to resize the IndexedImage to.
	 */
	private int resizeWidth;

	/**
	 * The width of the IndexedImage.
	 */
	private int width;

	/**
	 * Creates the ImageBuilder.
	 *
	 * @param name The name of the {@link IndexedImage}.
	 */
	public ImageBuilder(String name) {
		Assertions.checkNonEmpty(name, "Name cannot be null or empty.");
		this.name = name;
	}

	/**
	 * Builds the data contained in this builder into a {@link IndexedImage}.
	 *
	 * @return The IndexedImage.
	 */
	public IndexedImage build() {
		Preconditions.checkNotNull(raster, "Raster must be specified before building.");
		Preconditions.checkNotNull(palette, "Palette must be specified before building.");
		Preconditions.checkArgument(height != 0 && width != 0, "Height and width must not be 0.");
		Preconditions.checkNotNull(format, "Format must be specified before building.");

		return new IndexedImage(name, format, width, height, raster, palette, offsetX, offsetY, resizeWidth,
				resizeHeight);
	}

	/**
	 * Duplicates this ImageBuilder. Every value of the duplicate builder will be the same as this one, although the
	 * raster will be a deep copy.
	 *
	 * @return The duplicate ImageBuilder.
	 */
	public ImageBuilder duplicate() {
		ImageBuilder duplicate = new ImageBuilder(name);
		duplicate.format = format;
		duplicate.width = width;
		duplicate.height = height;
		duplicate.raster = raster.clone();
		duplicate.palette = palette.clone();
		duplicate.offsetX = offsetX;
		duplicate.offsetY = offsetY;
		duplicate.resizeWidth = resizeWidth;
		duplicate.resizeHeight = resizeHeight;
		return duplicate;
	}

	/**
	 * Sets the {@link ImageFormat} of the {@link IndexedImage}.
	 *
	 * @param format The ImageFormat. Cannot be {@code null}.
	 * @return This ImageBuilder, for chaining.
	 * @throws NullPointerException If the specified format is {@code null}.
	 */
	public ImageBuilder setFormat(ImageFormat format) {
		Preconditions.checkNotNull(format, "Format cannot be null.");
		this.format = format;
		return this;
	}

	/**
	 * Sets the height of the {@link IndexedImage}.
	 *
	 * @param height The height.
	 * @return This ImageBuilder, for chaining.
	 * @throws IllegalArgumentException If the height is not positive.
	 */
	public ImageBuilder setHeight(int height) {
		Assertions.checkPositive(height, "Height must be positive.");
		this.height = height;
		return this;
	}

	/**
	 * Sets the name of the {@link IndexedImage}.
	 *
	 * @param name The name.
	 * @return This ImageBuilder, for chaining.
	 * @throws NullPointerException If the string is {@code null}.
	 */
	public ImageBuilder setName(String name) {
		Assertions.checkNonEmpty(name, "Name cannot be null or empty.");
		this.name = name;
		return this;
	}

	/**
	 * Sets the x draw offset of the {@link IndexedImage}.
	 *
	 * @param offsetX The x draw offset.
	 * @return This ImageBuilder, for chaining.
	 * @throws IllegalArgumentException If the offset is negative.
	 */
	public ImageBuilder setOffsetX(int offsetX) {
		Assertions.checkNonNegative(offsetX, "Horizontal offset cannot be negative.");
		this.offsetX = offsetX;
		return this;
	}

	/**
	 * Sets the y draw offset of the {@link IndexedImage}.
	 *
	 * @param offsetY The y draw offset.
	 * @return This ImageBuilder, for chaining.
	 * @throws IllegalArgumentException If the offset is negative.
	 */
	public ImageBuilder setOffsetY(int offsetY) {
		Assertions.checkNonNegative(offsetY, "Vertical offset cannot be negative.");
		this.offsetY = offsetY;
		return this;
	}

	/**
	 * Sets the palette of the {@link IndexedImage}. Further modification of the passed array will not affect the
	 * palette of this builder.
	 *
	 * @param palette The Palette.
	 * @return This ImageBuilder, for chaining.
	 * @throws NullPointerException If the specified array is {@code null}.
	 */
	public ImageBuilder setPalette(int[] palette) {
		Preconditions.checkNotNull(palette, "Palette must not be null.");
		this.palette = palette.clone();
		return this;
	}

	/**
	 * Sets the raster of the {@link IndexedImage}. Further modification of the passed array will not affect the raster
	 * of this builder.
	 *
	 * @param raster The raster. Must not be {@code null}.
	 * @return This ImageBuilder, for chaining.
	 * @throws NullPointerException If the specified array is {@code null}.
	 */
	public ImageBuilder setRaster(int[] raster) {
		Preconditions.checkNotNull(raster, "Raster must not be null.");
		this.raster = raster.clone();
		return this;
	}

	/**
	 * Sets the default height to resize the {@link IndexedImage} to.
	 *
	 * @param resizeHeight The default resize height.
	 * @return This ImageBuilder, for chaining.
	 * @throws IllegalArgumentException If the height is not positive.
	 */
	public ImageBuilder setResizeHeight(int resizeHeight) {
		Assertions.checkPositive(resizeHeight, "Resize height must be positive.");
		this.resizeHeight = resizeHeight;
		return this;
	}

	/**
	 * Sets the default width to resize the {@link IndexedImage} to.
	 *
	 * @param resizeWidth The default resize width.
	 * @return This ImageBuilder, for chaining.
	 * @throws IllegalArgumentException If the width is not positive.
	 */
	public ImageBuilder setResizeWidth(int resizeWidth) {
		Assertions.checkPositive(resizeWidth, "Resize width must be positive.");
		this.resizeWidth = resizeWidth;
		return this;
	}

	/**
	 * Sets the width of the {@link IndexedImage}.
	 *
	 * @param width The width.
	 * @return This ImageBuilder, for chaining.
	 * @throws IllegalArgumentException If the width is not positive.
	 */
	public ImageBuilder setWidth(int width) {
		Assertions.checkPositive(width, "Width must be positive.");
		this.width = width;
		return this;
	}

}