package rs.emulate.legacy.map;

import rs.emulate.shared.world.Position;

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
		 * The attributes of the Tile.
		 */
		private int attributes;

		/**
		 * The height of the Tile.
		 */
		private int height;

		/**
		 * The overlay id of the Tile.
		 */
		private int overlay;

		/**
		 * The overlay orientation of the Tile.
		 */
		private int overlayOrientation;

		/**
		 * The overlay type of the Tile.
		 */
		private int overlayType;

		/**
		 * The Position of the Tile.
		 */
		private Position position;

		/**
		 * The underlay id of the Tile.
		 */
		private int underlay;

		/**
		 * Creates the Builder.
		 *
		 * @param position The {@link Position} of the Tile.
		 */
		public Builder(Position position) {
			this.position = position;
		}

		/**
		 * Builds the contents of this Builder into a Tile.
		 *
		 * @return The Tile.
		 */
		public Tile build() {
			return new Tile(position, attributes, height, overlay, overlayType, overlayOrientation, underlay);
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
		 * Sets the {@link Position} of the Tile.
		 *
		 * @param position The Position.
		 */
		public void setPosition(Position position) {
			this.position = position;
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
	 * @param position The {@link Position} of the Tile.
	 * @return The Builder.
	 */
	public static Builder builder(Position position) {
		return new Builder(position);
	}

	/**
	 * The attributes of this Tile.
	 */
	private final int attributes;

	/**
	 * The height of this Tile.
	 */
	private final int height;

	/**
	 * The overlay id of this Tile.
	 */
	private final int overlay;

	/**
	 * The overlay orientation of this Tile.
	 */
	private final int overlayOrientation;

	/**
	 * The overlay type of this Tile.
	 */
	private final int overlayType;

	/**
	 * The position of this Tile.
	 */
	private final Position position;

	/**
	 * The underlay id of this Tile.
	 */
	private final int underlay;

	/**
	 * Creates the Tile.
	 *
	 * @param position The {@link Position}.
	 * @param attributes The attributes.
	 * @param height The height.
	 * @param overlay The overlay id.
	 * @param overlayType The overlay type.
	 * @param overlayOrientation The overlay orientation.
	 * @param underlay The underlay id.
	 */
	public Tile(Position position, int attributes, int height, int overlay, int overlayType, int overlayOrientation,
	            int underlay) {
		this.position = position;
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
	 * Gets the {@link Position} of this Tile.
	 *
	 * @return The Position.
	 */
	public Position getPosition() {
		return position;
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