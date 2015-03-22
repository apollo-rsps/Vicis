package rs.emulate.shared.prop;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import rs.emulate.legacy.config.ConfigPropertyType;
import rs.emulate.util.Assertions;

import com.google.common.base.Preconditions;

/**
 * A {@link Map} wrapper containing methods to get properties using their opcode or name.
 *
 * @author Major
 */
public final class PropertyMap {

	/**
	 * Validates that the specified property is valid (i.e. is not {@code null}).
	 * 
	 * @param property The {@link DefinitionProperty} to validate.
	 * @param key The key used to access the invalid property.
	 * @return The property, if it is valid.
	 * @throws IllegalArgumentException If the property is {@code null}.
	 */
	@SuppressWarnings("unchecked")
	private static <T> DefinitionProperty<T> validate(DefinitionProperty<?> property, Object key) {
		return (DefinitionProperty<T>) Objects.requireNonNull(property, "No property with a key of " + key + " exists.");
	}

	/**
	 * The map of opcodes to DefinitionProperty objects.
	 */
	private final Map<Integer, DefinitionProperty<?>> opcodes;

	/**
	 * The map of PropertyTypes to DefinitionProperty objects.
	 */
	private final Map<ConfigPropertyType, DefinitionProperty<?>> properties;

	/**
	 * Creates the PropertyMap.
	 *
	 * @param opcodes The {@link Map} of opcodes to {@link DefinitionProperty} objects.
	 * @throws IllegalArgumentException If the contains a mapping for opcode 0.
	 */
	public PropertyMap(Map<Integer, DefinitionProperty<?>> opcodes) {
		Preconditions.checkArgument(opcodes.get(0) == null, "Opcode 0 is reserved.");
		this.opcodes = new HashMap<>(opcodes);

		Map<ConfigPropertyType, DefinitionProperty<?>> properties = new HashMap<>(opcodes.size());

		for (Map.Entry<Integer, DefinitionProperty<?>> entry : opcodes.entrySet()) {
			DefinitionProperty<?> property = entry.getValue().duplicate();
			this.opcodes.put(entry.getKey(), property);
			properties.put(property.getType(), property);
		}

		this.properties = properties;
	}

	/**
	 * Creates the PropertyMap, using an existing one as the base.
	 * 
	 * @param map The base PropertyMap.
	 */
	public PropertyMap(PropertyMap map) {
		this(map.opcodes);
	}

	/**
	 * Gets the {@link DefinitionProperty} with the specified opcode.
	 *
	 * @param opcode The opcode of the property.
	 * @return The DefinitionProperty.
	 * @throws IllegalArgumentException If no property with the specified opcode exists.
	 */
	public <T> DefinitionProperty<T> get(int opcode) {
		return validate(opcodes.get(opcode), opcode);
	}

	/**
	 * Gets the {@link DefinitionProperty} with the specified {@link ConfigPropertyType}.
	 *
	 * @param name The name of the property.
	 * @return The DefinitionProperty.
	 * @throws IllegalArgumentException If no property with the specified name exists.
	 */
	public <T> DefinitionProperty<T> get(ConfigPropertyType name) {
		return validate(properties.get(name), name);
	}

	/**
	 * Gets a {@link Set} containing map entries of opcodes to {@link DefinitionProperty} objects.
	 * 
	 * @return The set of map entries.
	 */
	public Set<Map.Entry<Integer, DefinitionProperty<?>>> getProperties() {
		return opcodes.entrySet();
	}

	/**
	 * Adds a {@link DefinitionProperty} with the specified opcode.
	 *
	 * @param opcode The opcode.
	 * @param property The DefinitionProperty.
	 */
	public void put(int opcode, DefinitionProperty<?> property) {
		Assertions.checkPositive(opcode, "Error placing property - opcode must be positive.");

		opcodes.put(opcode, property);
		properties.put(property.getType(), property);
	}

	/**
	 * Gets the size of this PropertyMap.
	 * 
	 * @return The size.
	 */
	public int size() {
		return properties.size();
	}

	/**
	 * Gets a {@link Collection} containing the values of this map (i.e. the {@link DefinitionProperty} objects).
	 *
	 * @return The values.
	 */
	public Collection<DefinitionProperty<?>> values() {
		return properties.values();
	}

}