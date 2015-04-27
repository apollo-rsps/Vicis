package rs.emulate.legacy.widget.type;

/**
 * An option for a Widget.
 *
 * @author Major
 */
public final class Option {

	/**
	 * The attributes of this Option.
	 */
	private final int attributes;

	/**
	 * The circumfix of this Option.
	 */
	private final String circumfix;

	/**
	 * The text of this Option.
	 */
	private final String text;

	/**
	 * Creates the Option.
	 *
	 * @param circumfix The circumfix.
	 * @param text The text.
	 * @param attributes The attributes.
	 */
	public Option(String circumfix, String text, int attributes) {
		this.circumfix = circumfix;
		this.text = text;
		this.attributes = attributes;
	}

	/**
	 * Gets the attributes of this Option.
	 *
	 * @return The attributes.
	 */
	public int getAttributes() {
		return attributes;
	}

	/**
	 * Gets the circumfix of this Option.
	 *
	 * @return The circumfix.
	 */
	public String getCircumfix() {
		return circumfix;
	}

	/**
	 * Gets the text of this Option.
	 *
	 * @return The text.
	 */
	public String getText() {
		return text;
	}

}