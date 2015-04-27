package rs.emulate.legacy.graphics.sprite;

import java.util.Arrays;
import java.util.Objects;

import rs.emulate.legacy.graphics.ImageFormat;

/**
 * A 2-dimensional sprite in the cache.
 * 
 * @author Major
 */
public final class Sprite {

	/**
	 * Creates a {@link SpriteBuilder} with the specified name.
	 * 
	 * @param name The name of the Sprite being built.
	 * @return The SpriteBuilder.
	 */
	public static SpriteBuilder builder(String name) {
		return new SpriteBuilder(name);
	}

	/**
	 * The format of this sprite.
	 */
	private final ImageFormat format;

	/**
	 * The height of this sprite.
	 */
	private final int height;

	/**
	 * The name of this sprite.
	 */
	private final String name;

	/**
	 * The x offset to draw this sprite from.
	 */
	private final int offsetX;

	/**
	 * The y offset to draw this sprite from.
	 */
	private final int offsetY;

	/**
	 * The raster of this sprite.
	 */
	private final int[] raster;

	/**
	 * The default height to resize this sprite to.
	 */
	private final int resizeHeight;

	/**
	 * The default width to resize this sprite to.
	 */
	private final int resizeWidth;

	/**
	 * The width of this sprite.
	 */
	private final int width;

	/**
	 * Creates the Sprite.
	 * 
	 * @param name The name of this sprite.
	 * @param raster The width of this sprite.
	 * @param format The sprite {@link ImageFormat}.
	 * @param height The height of this sprite.
	 * @param width The width of this sprite.
	 * @param offsetX The x offset to draw this sprite from.
	 * @param offsetY The y offset to draw this sprite from.
	 * @param resizeHeight The default height to resize this sprite to, when requested.
	 * @param resizeWidth The default width to resize this sprite to, when requested.
	 */
	public Sprite(String name, int[] raster, ImageFormat format, int height, int width, int offsetX, int offsetY,
			int resizeHeight, int resizeWidth) {
		this.format = format;
		this.name = name;
		this.height = height;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.raster = raster.clone();
		this.resizeHeight = resizeHeight;
		this.resizeWidth = resizeWidth;
		this.width = width;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Sprite) {
			Sprite other = (Sprite) obj;
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
	 * Gets the {@link ImageFormat} of this sprite.
	 * 
	 * @return The format.
	 */
	public ImageFormat getFormat() {
		return format;
	}

	/**
	 * Gets the height of this sprite.
	 *
	 * @return The height.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the name of this sprite.
	 *
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the horizontal draw offset of this sprite.
	 *
	 * @return The horizontal draw offset.
	 */
	public int getOffsetX() {
		return offsetX;
	}

	/**
	 * Gets the vertical draw offset of this sprite.
	 *
	 * @return The vertical draw offset.
	 */
	public int getOffsetY() {
		return offsetY;
	}

	/**
	 * Gets a deep copy of the raster of this sprite.
	 *
	 * @return The raster.
	 */
	public int[] getRaster() {
		return raster.clone();
	}

	/**
	 * Gets the default height to resize this sprite to, when requested.
	 *
	 * @return The resize height.
	 */
	public int getResizeHeight() {
		return resizeHeight;
	}

	/**
	 * Gets the default width to resize this sprite to, when requested.
	 *
	 * @return The resize width.
	 */
	public int getResizeWidth() {
		return resizeWidth;
	}

	/**
	 * Gets the width of this sprite.
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