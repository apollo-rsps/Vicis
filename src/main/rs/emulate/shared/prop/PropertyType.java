package rs.emulate.shared.prop;

import rs.emulate.legacy.config.MutableDefinition;

/**
 * A type of a property. Should <strong>only</strong> be implemented by enumerators (excluding the existing
 * {@link DynamicPropertyType} class).
 * 
 * @author Major
 */
public interface PropertyType {

	/**
	 * Gets the opcode of the property.
	 * 
	 * @return The opcode.
	 */
	public int getOpcode();

	/**
	 * Gets the name, as a string.
	 * 
	 * @return The name.
	 */
	public String name();

	/**
	 * Gets the {@link DefinitionProperty} associated with this property name for the specified
	 * {@link MutableDefinition}.
	 * 
	 * @param definition The definition.
	 * @return The definition property.
	 */
	default public <T> DefinitionProperty<T> propertyFor(MutableDefinition definition) {
		return definition.getProperty(this);
	}

}