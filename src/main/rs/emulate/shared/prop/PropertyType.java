package rs.emulate.shared.prop;

import rs.emulate.util.StringUtils;

/**
 * A type of a property. Should <strong>only</strong> be implemented by enumerators (excluding the existing
 * {@link DynamicPropertyType} class).
 * 
 * @author Major
 */
public interface PropertyType {

	/**
	 * Gets the name of this PropertyType, capitalised and with underscores ('_') replaced with spaces (' ').
	 * 
	 * @return The formatted name.
	 */
	default public String formattedName() {
		return StringUtils.capitalise(name().replace('_', ' '));
	}

	/**
	 * Gets the name of this PropertyType, as a String.
	 * 
	 * @return The name.
	 */
	public String name();

}