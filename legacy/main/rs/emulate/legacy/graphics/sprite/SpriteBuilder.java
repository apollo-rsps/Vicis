package rs.emulate.legacy.graphics.sprite;

import rs.emulate.legacy.graphics.ImageFormat;
import rs.emulate.util.Assertions;

import com.google.common.base.Preconditions;

/**
 * A builder class for a {@link Sprite}.
 * 
 * @author Major
 */
public final class SpriteBuilder {

	/**
	 * The ImageFormat of the Sprite.
	 */
	private ImageFormat format;

	/**
	 * The height of the Sprite.
	 */
	private int height;

	/**
	 * The name of the Sprite.
	 */
	private String name;

	/**
	 * The x offset to draw the Sprite from.
	 */
	private int offsetX;

	/**
	 * The y offset to draw the Sprite from.
	 */
	private int offsetY;

	/**
	 * The raster of the Sprite.
	 */
	private int[] raster;

	/**
	 * The default height to resize the Sprite to.
	 */
	private int resizeHeight;

	/**
	 * The default width to resize the Sprite to.
	 */
	private int resizeWidth;

	/**
	 * The width of the Sprite.
	 */
	private int width;

	/**
	 * Creates the SpriteBuilder.
	 * 
	 * @param name The name of the Sprite.
	 */
	public SpriteBuilder(String name) {
		Assertions.checkNonEmpty(name, "Name cannot be null or empty.");
		this.name = name;
	}

	/**
	 * Builds the data contained in this builder into a {@link Sprite}.
	 * 
	 * @return The Sprite.
	 */
	public Sprite build() {
		Preconditions.checkNotNull(raster, "Raster must be specified before building.");
		Preconditions.checkArgument(height != 0 && width != 0, "Height and width must not be 0.");
		Preconditions.checkNotNull(format, "Format must be specified before building.");

		return new Sprite(name, raster, format, height, width, offsetX, offsetY, resizeHeight, resizeWidth);
	}

	/**
	 * Duplicates this SpriteBuilder. Every value of the duplicate builder will be the same as this one, although the
	 * raster will be a deep copy.
	 * 
	 * @return The duplicate Sprite builder.
	 */
	public SpriteBuilder duplicate() {
		SpriteBuilder duplicate = new SpriteBuilder(name);
		duplicate.format = format;
		duplicate.height = height;
		duplicate.offsetX = offsetX;
		duplicate.offsetY = offsetY;
		duplicate.raster = raster.clone();
		duplicate.resizeHeight = resizeHeight;
		duplicate.resizeWidth = resizeWidth;
		duplicate.width = width;
		return duplicate;
	}

	/**
	 * Sets the {@link ImageFormat} of the Sprite.
	 * 
	 * @param format The ImageFormat. Cannot be {@code null}.
	 * @return This SpriteBuilder, for chaining.
	 * @throws NullPointerException If the specified format is {@code null}.
	 */
	public SpriteBuilder setFormat(ImageFormat format) {
		Preconditions.checkNotNull(format, "Format cannot be null.");
		this.format = format;
		return this;
	}

	/**
	 * Sets the height of the Sprite.
	 *
	 * @param height The height.
	 * @return This SpriteBuilder, for chaining.
	 * @throws IllegalArgumentException If the height is not positive.
	 */
	public SpriteBuilder setHeight(int height) {
		Assertions.checkPositive(height, "Height must be positive.");
		this.height = height;
		return this;
	}

	/**
	 * Sets the name of the Sprite.
	 *
	 * @param name The name.
	 * @return This SpriteBuilder, for chaining.
	 * @throws NullPointerException If the string is {@code null}.
	 */
	public SpriteBuilder setName(String name) {
		Assertions.checkNonEmpty(name, "Name cannot be null or empty.");
		this.name = name;
		return this;
	}

	/**
	 * Sets the x draw offset of the Sprite.
	 *
	 * @param offsetX The x draw offset.
	 * @return This SpriteBuilder, for chaining.
	 * @throws IllegalArgumentException If the offset is negative.
	 */
	public SpriteBuilder setOffsetX(int offsetX) {
		Assertions.checkNonNegative(offsetX, "Horizontal offset cannot be negative.");
		this.offsetX = offsetX;
		return this;
	}

	/**
	 * Sets the y draw offset of the Sprite.
	 *
	 * @param offsetY The y draw offset.
	 * @return This SpriteBuilder, for chaining.
	 * @throws IllegalArgumentException If the offset is negative.
	 */
	public SpriteBuilder setOffsetY(int offsetY) {
		Assertions.checkNonNegative(offsetY, "Vertical offset cannot be negative.");
		this.offsetY = offsetY;
		return this;
	}

	/**
	 * Sets the raster of the Sprite. Further modification of the passed array will not affect the raster of this
	 * builder.
	 *
	 * @param raster The raster. Must not be {@code null}.
	 * @return This SpriteBuilder, for chaining.
	 * @throws NullPointerException If the specified array is {@code null}.
	 */
	public SpriteBuilder setRaster(int[] raster) {
		Preconditions.checkNotNull(raster, "Raster must not be null.");
		this.raster = raster.clone();
		return this;
	}

	/**
	 * Sets the default height to resize the Sprite to.
	 *
	 * @param resizeHeight The default resize height.
	 * @return This SpriteBuilder, for chaining.
	 * @throws IllegalArgumentException If the height is not positive.
	 */
	public SpriteBuilder setResizeHeight(int resizeHeight) {
		Assertions.checkPositive(resizeHeight, "Resize height must be positive.");
		this.resizeHeight = resizeHeight;
		return this;
	}

	/**
	 * Sets the default width to resize the Sprite to.
	 *
	 * @param resizeWidth The default resize width.
	 * @return This SpriteBuilder, for chaining.
	 * @throws IllegalArgumentException If the width is not positive.
	 */
	public SpriteBuilder setResizeWidth(int resizeWidth) {
		Assertions.checkPositive(resizeWidth, "Resize width must be positive.");
		this.resizeWidth = resizeWidth;
		return this;
	}

	/**
	 * Sets the width of the Sprite.
	 *
	 * @param width The width.
	 * @return This SpriteBuilder, for chaining.
	 * @throws IllegalArgumentException If the width is not positive.
	 */
	public SpriteBuilder setWidth(int width) {
		Assertions.checkPositive(width, "Width must be positive.");
		this.width = width;
		return this;
	}

}