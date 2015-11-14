package rs.emulate.legacy.widget.type;

/**
 * A pair of colours, one of them the default, and one the secondary.
 *
 * @author Major
 */
public final class ColourPair {

	/**
	 * The default colour.
	 */
	private final int primary;

	/**
	 * The secondary colour.
	 */
	private final int secondary;

	/**
	 * Creates the ColourPair.
	 *
	 * @param defaultColour The default colour.
	 * @param secondaryColour The secondary colour.
	 */
	public ColourPair(int defaultColour, int secondaryColour) {
		this.primary = defaultColour;
		this.secondary = secondaryColour;
	}

	/**
	 * Gets the default colour of this ColourPair.
	 *
	 * @return The default colour.
	 */
	public int getDefault() {
		return primary;
	}

	/**
	 * Gets the secondary colour of this ColourPair.
	 *
	 * @return The secondary colour.
	 */
	public int getSecondary() {
		return secondary;
	}

}