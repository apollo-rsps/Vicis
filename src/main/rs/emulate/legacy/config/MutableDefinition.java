package rs.emulate.legacy.config;

import java.util.Map;
import java.util.Set;

import rs.emulate.shared.prop.DefinitionProperty;
import rs.emulate.shared.prop.PropertyMap;
import rs.emulate.shared.prop.PropertyType;
import rs.emulate.util.StringUtils;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Preconditions;

/**
 * A base class for a definition with properties that can be mutated.
 *
 * @author Major
 */
public abstract class MutableDefinition {

	/**
	 * The map of opcodes to properties.
	 */
	protected final PropertyMap properties;

	/**
	 * The id of this definition.
	 */
	private final int id;

	/**
	 * Creates the definition.
	 *
	 * @param id The id of the definition.
	 * @param properties The {@link Map} of opcodes to {@link DefinitionProperty properties}. Cannot be null.
	 */
	public MutableDefinition(int id, PropertyMap properties) {
		Preconditions.checkNotNull(properties, "Map cannot be null.");
		this.id = id;
		this.properties = new PropertyMap(properties);
	}

	/**
	 * Adds a {@link DefinitionProperty} to this definition, using the smallest available (positive) opcode.
	 *
	 * @param property The property.
	 */
	public final void addProperty(DefinitionProperty<?> property) {
		addProperty(properties.size(), property);
	}

	/**
	 * Adds a {@link DefinitionProperty} with the specified opcode to this definition.
	 *
	 * @param opcode The opcode.
	 * @param property The property.
	 */
	public final void addProperty(int opcode, DefinitionProperty<?> property) {
		properties.put(opcode, property);
	}

	/**
	 * Gets the id of this definition.
	 * 
	 * @return The id.
	 */
	public final int getId() {
		return id;
	}

	/**
	 * Gets the {@link Set} of {@link Map} entries containing the opcodes and {@link DefinitionProperty} objects.
	 *
	 * @return The set of properties.
	 */
	public final Set<Map.Entry<Integer, DefinitionProperty<?>>> getProperties() {
		return properties.getProperties();
	}

	/**
	 * Gets a {@link DefinitionProperty} with the specified opcode.
	 *
	 * @param opcode The opcode of the property.
	 * @return The property.
	 */
	public final <T> DefinitionProperty<T> getProperty(int opcode) {
		@SuppressWarnings("unchecked")
		DefinitionProperty<T> property = (DefinitionProperty<T>) properties.get(opcode);
		Preconditions.checkNotNull(property, "No property with opcode " + opcode + " exists.");
		return property;
	}

	/**
	 * Gets a {@link DefinitionProperty} with the specified {@link PropertyType}.
	 *
	 * @param name The name of the property.
	 * @return The property.
	 * @throws IllegalArgumentException If no property with the specified name exists.
	 */
	public final <T> DefinitionProperty<T> getProperty(PropertyType name) {
		@SuppressWarnings("unchecked")
		DefinitionProperty<T> property = (DefinitionProperty<T>) properties.get(name);
		Preconditions.checkNotNull(property, "No property called " + name + " exists.");
		return property;
	}

	/**
	 * Sets the {@link DefinitionProperty} with the specified name.
	 *
	 * @param opcode The opcode of the property.
	 * @param property The property.
	 */
	public final void setProperty(int opcode, DefinitionProperty<?> property) {
		properties.put(opcode, property);
	}

	/**
	 * Sets the value of the {@link DefinitionProperty} with the specified opcode.
	 * 
	 * @param name The {@link PropertyType name} of the property.
	 * @param value The value.
	 */
	public final <V> void setProperty(PropertyType name, V value) {
		getProperty(name).setValue(value);
	}

	@Override
	public final String toString() {
		ToStringHelper helper = MoreObjects.toStringHelper(this);

		for (Map.Entry<Integer, DefinitionProperty<?>> entry : properties.getProperties()) {
			DefinitionProperty<?> property = entry.getValue();
			String name = StringUtils.capitalise(property.getType().name().replace('_', ' '));
			helper.add(name, property);
		}

		return helper.toString();
	}

}