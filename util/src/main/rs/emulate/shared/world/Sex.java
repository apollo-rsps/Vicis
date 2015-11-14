package rs.emulate.shared.world;

/**
 * Contains the two biological sexes used in Runescape.
 *
 * @author Major
 */
public enum Sex {

	/**
	 * The female sex.
	 */
	FEMALE,

	/**
	 * The male sex.
	 */
	MALE;

	/**
	 * Returns whether or not this sex is the {@link #MALE} sex.
	 *
	 * @return {@code true} if this sex is male, {@code false} if it's female.
	 */
	public boolean isMale() {
		return this == MALE;
	}

}