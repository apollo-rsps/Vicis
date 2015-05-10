package rs.emulate.legacy.map;

/**
 * A single tile on the map.
 *
 * @author Major
 */
public final class Tile {

	/**
	 * A builder class for a Tile.
	 */
	public static final class Builder {

		/**
		 * The tile attributes.
		 */
		private int attributes;

		/**
		 * The tile height.
		 */
		private int height;

		/**
		 * The overlay id.
		 */
		private int overlay;

		/**
		 * The overlay orientation.
		 */
		private int overlayOrientation;

		/**
		 * The overlay type.
		 */
		private int overlayType;

		/**
		 * The underlay id.
		 */
		private int underlay;

		/**
		 * Builds the contents of this Builder into a Tile.
		 *
		 * @return The Tile.
		 */
		public Tile build() {
			return new Tile(attributes, height, overlay, overlayType, overlayOrientation, underlay);
		}

		/**
		 * Sets the attributes of the Tile.
		 *
		 * @param attributes The attributes.
		 */
		public void setAttributes(int attributes) {
			this.attributes = attributes;
		}

		/**
		 * Sets the height of the Tile.
		 *
		 * @param height The height.
		 */
		public void setHeight(int height) {
			this.height = height;
		}

		/**
		 * Sets the overlay id of the Tile.
		 *
		 * @param overlay The overlay id.
		 */
		public void setOverlay(int overlay) {
			this.overlay = overlay;
		}

		/**
		 * Sets the overlay orientation of the Tile.
		 *
		 * @param orientation The overlay orientation.
		 */
		public void setOverlayOrientation(int orientation) {
			this.overlayOrientation = orientation;
		}

		/**
		 * Sets the overlay type of the Tile.
		 *
		 * @param type The overlay type.
		 */
		public void setOverlayType(int type) {
			this.overlayType = type;
		}

		/**
		 * Sets the underlay id of the Tile.
		 *
		 * @param underlay The underlay.
		 */
		public void setUnderlay(int underlay) {
			this.underlay = underlay;
		}

	}

	/**
	 * Creates a {@link Builder} for a Tile.
	 *
	 * @return The Builder.
	 */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * The tile attributes.
	 */
	private final int attributes;

	/**
	 * The tile height.
	 */
	private final int height;

	/**
	 * The overlay id.
	 */
	private final int overlay;

	/**
	 * The overlay orientation.
	 */
	private final int overlayOrientation;

	/**
	 * The overlay type.
	 */
	private final int overlayType;

	/**
	 * The underlay id.
	 */
	private final int underlay;

	/**
	 * Creates the Tile.
	 *
	 * @param attributes The tile attributes.
	 * @param height The tile height.
	 * @param overlay The overlay id.
	 * @param overlayType The overlay type.
	 * @param overlayOrientation The overlay orientation.
	 * @param underlay The underlay id.
	 */
	public Tile(int attributes, int height, int overlay, int overlayType, int overlayOrientation, int underlay) {
		this.attributes = attributes;
		this.height = height;
		this.overlay = overlay;
		this.overlayType = overlayType;
		this.overlayOrientation = overlayOrientation;
		this.underlay = underlay;
	}

	/**
	 * Gets the attributes of this Tile.
	 *
	 * @return The attributes.
	 */
	public int getAttributes() {
		return attributes;
	}

	/**
	 * Gets the height of this Tile.
	 *
	 * @return The height.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the overlay id of this Tile.
	 *
	 * @return The overlay id.
	 */
	public int getOverlay() {
		return overlay;
	}

	/**
	 * Gets the overlay orientation of this Tile.
	 *
	 * @return The overlay orientation.
	 */
	public int getOverlayOrientation() {
		return overlayOrientation;
	}

	/**
	 * Gets the overlay type of this Tile.
	 *
	 * @return The overlay types.
	 */
	public int getOverlayType() {
		return overlayType;
	}

	/**
	 * Gets the underlay id of this Tile.
	 *
	 * @return The underlay id.
	 */
	public int getUnderlay() {
		return underlay;
	}

}