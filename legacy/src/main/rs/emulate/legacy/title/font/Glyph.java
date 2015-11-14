package rs.emulate.legacy.title.font;

import rs.emulate.legacy.graphics.ImageFormat;

/**
 * A glyph of a single character in a {@link Font}.
 *
 * @author Major
 */
public final class Glyph {

	/**
	 * The image format of this glyph.
	 */
	private final ImageFormat format;

	/**
	 * The height of this glyph.
	 */
	private final int height;

	/**
	 * The horizontal draw offset of this glyph.
	 */
	private final int horizontalOffset;

	/**
	 * The raster of this glyph.
	 */
	private final byte[] raster;

	/**
	 * The spacing of this glyph.
	 */
	private final int spacing;

	/**
	 * The vertical draw offset of this glyph.
	 */
	private final int verticalOffset;

	/**
	 * The width of this glyph.
	 */
	private final int width;

	/**
	 * Creates the glyph.
	 *
	 * @param format The {@link ImageFormat}.
	 * @param raster The raster.
	 * @param height The glyph height.
	 * @param width The glyph width.
	 * @param horizontalOffset The horizontal draw offset.
	 * @param verticalOffset The vertical draw offset.
	 * @param spacing The spacing.
	 */
	public Glyph(ImageFormat format, byte[] raster, int height, int width, int horizontalOffset, int verticalOffset, int spacing) {
		this.format = format;
		this.raster = raster.clone();
		this.height = height;
		this.width = width;
		this.horizontalOffset = horizontalOffset;
		this.verticalOffset = verticalOffset;
		this.spacing = spacing;
	}

	/**
	 * Gets the {@link ImageFormat} of this glyph.
	 *
	 * @return The image format.
	 */
	public ImageFormat getFormat() {
		return format;
	}

	/**
	 * Gets the height of this glyph.
	 *
	 * @return The height.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the horizontal draw offset of this glyph.
	 *
	 * @return The horizontal draw offset.
	 */
	public int getHorizontalOffset() {
		return horizontalOffset;
	}

	/**
	 * Gets the raster of this glyph.
	 *
	 * @return The raster.
	 */
	public byte[] getRaster() {
		return raster.clone();
	}

	/**
	 * Gets the spacing of this glyph.
	 *
	 * @return The spacing.
	 */
	public int getSpacing() {
		return spacing;
	}

	/**
	 * Gets the vertical draw offset of this glyph.
	 *
	 * @return The vertical draw offset.
	 */
	public int getVerticalOffset() {
		return verticalOffset;
	}

	/**
	 * Gets the width of this glyph.
	 *
	 * @return The width.
	 */
	public int getWidth() {
		return width;
	}

}