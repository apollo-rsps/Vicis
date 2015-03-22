package rs.emulate.legacy.config;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import rs.emulate.shared.prop.DefinitionProperty;
import rs.emulate.shared.prop.PropertyMap;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Preconditions;

/**
 * A base class for a definition with properties that can be mutated.
 *
 * @author Major
 */
public abstract class MutableConfigDefinition {

	/**
	 * The PropertyMap.
	 */
	protected final PropertyMap properties;

	/**
	 * The id of this MutableDefinition.
	 */
	private final int id;

	/**
	 * Creates the MutableDefinition.
	 *
	 * @param id The id of the MutableDefinition.
	 * @param properties The {@link PropertyMap}. Must not be {@code null}.
	 */
	public MutableConfigDefinition(int id, PropertyMap properties) {
		Preconditions.checkNotNull(properties, "Map cannot be null.");
		this.id = id;
		this.properties = new PropertyMap(properties);
	}

	/**
	 * Adds a {@link DefinitionProperty} to this MutableDefinition, using the smallest available (positive) opcode.
	 *
	 * @param property The DefinitionProperty.
	 */
	public final void addProperty(DefinitionProperty<?> property) {
		addProperty(properties.size(), property);
	}

	/**
	 * Adds a {@link DefinitionProperty} with the specified opcode to this MutableDefinition.
	 *
	 * @param opcode The opcode.
	 * @param property The DefinitionProperty.
	 */
	public final void addProperty(int opcode, DefinitionProperty<?> property) {
		properties.put(opcode, property);
	}

	/**
	 * Gets the id of this MutableDefinition.
	 * 
	 * @return The id.
	 */
	public final int getId() {
		return id;
	}

	/**
	 * Gets the {@link Set} of {@link Map} {@link Entry} objects containing the opcodes and {@link DefinitionProperty}
	 * objects.
	 *
	 * @return The Set.
	 */
	public final Set<Entry<Integer, DefinitionProperty<?>>> getProperties() {
		return properties.getProperties();
	}

	/**
	 * Gets a {@link DefinitionProperty} with the specified opcode.
	 *
	 * @param opcode The opcode of the DefinitionProperty.
	 * @return The DefinitionProperty.
	 */
	public final <T> DefinitionProperty<T> getProperty(int opcode) {
		@SuppressWarnings("unchecked")
		DefinitionProperty<T> property = (DefinitionProperty<T>) properties.get(opcode);
		Preconditions.checkNotNull(property, "No property with opcode " + opcode + " exists.");
		return property;
	}

	/**
	 * Gets a {@link DefinitionProperty} with the specified {@link ConfigPropertyType}.
	 *
	 * @param name The name of the DefinitionProperty.
	 * @return The DefinitionProperty.
	 * @throws IllegalArgumentException If no DefinitionProperty with the specified name exists.
	 */
	public final <T> DefinitionProperty<T> getProperty(ConfigPropertyType name) {
		@SuppressWarnings("unchecked")
		DefinitionProperty<T> property = (DefinitionProperty<T>) properties.get(name);
		Preconditions.checkNotNull(property, "No property called " + name + " exists.");
		return property;
	}

	/**
	 * Sets the {@link DefinitionProperty} with the specified name.
	 *
	 * @param opcode The opcode of the DefinitionProperty.
	 * @param property The DefinitionProperty.
	 */
	public final void setProperty(int opcode, DefinitionProperty<?> property) {
		properties.put(opcode, property);
	}

	/**
	 * Sets the value of the {@link DefinitionProperty} with the specified opcode.
	 * 
	 * @param name The {@link ConfigPropertyType name} of the DefinitionProperty.
	 * @param value The value.
	 */
	public final <V> void setProperty(ConfigPropertyType name, V value) {
		getProperty(name).setValue(value);
	}

	@Override
	public final String toString() {
		ToStringHelper helper = MoreObjects.toStringHelper(this);

		for (Map.Entry<Integer, DefinitionProperty<?>> entry : properties.getProperties()) {
			DefinitionProperty<?> property = entry.getValue();
			String name = property.getFormattedName();

			helper.add(name, property);
		}

		return helper.toString();
	}

}