package rs.emulate.legacy.config;

import rs.emulate.shared.property.DynamicPropertyType;
import rs.emulate.shared.property.PropertyType;

/**
 * A {@link PropertyType} used as part of the {@link ConfigEncoder} and {@link ConfigDecoder}, {@link #opcode()}. Should
 * <strong>only</strong> be implemented by enumerators (excluding the existing {@link DynamicPropertyType} class).
 * 
 * @author Major
 */
public interface ConfigPropertyType extends PropertyType {

	/**
	 * Gets the opcode of this ConfigPropertyType.
	 * 
	 * @return The opcode.
	 */
	public int opcode();

	/**
	 * Gets the {@link ConfigProperty} associated with this ConfigPropertyType from the specified
	 * {@link MutableConfigDefinition}.
	 * 
	 * @param definition The MutableDefinition.
	 * @return The DefinitionProperty.
	 */
	default public <T> ConfigProperty<T> propertyFrom(MutableConfigDefinition definition) {
		return definition.getProperty(this);
	}

}