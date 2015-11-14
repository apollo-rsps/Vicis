package rs.emulate.modern;

/**
 * Represents a child entry within an {@link Entry} in the {@link ReferenceTable}.
 */
public final class ChildEntry {

	/**
	 * This entry's identifier.
	 */
	private int identifier = -1;

	/**
	 * Gets the identifier of this entry.
	 *
	 * @return The identifier.
	 */
	public int getIdentifier() {
		return identifier;
	}

	/**
	 * Sets the identifier of this entry.
	 *
	 * @param identifier The identifier.
	 */
	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}

}