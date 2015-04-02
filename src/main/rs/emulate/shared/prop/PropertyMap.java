package rs.emulate.shared.prop;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import rs.emulate.legacy.config.ConfigProperty;
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
	 * @param property The {@link ConfigProperty} to validate.
	 * @param key The key used to access the invalid property.
	 * @return The property, if it is valid.
	 * @throws IllegalArgumentException If the property is {@code null}.
	 */
	@SuppressWarnings("unchecked")
	private static <T> ConfigProperty<T> validate(ConfigProperty<?> property, Object key) {
		return (ConfigProperty<T>) Objects.requireNonNull(property, "No property with a key of " + key + " exists.");
	}

	/**
	 * The map of opcodes to DefinitionProperty objects.
	 */
	private final Map<Integer, ConfigProperty<?>> opcodes;

	/**
	 * The map of PropertyTypes to DefinitionProperty objects.
	 */
	private final Map<ConfigPropertyType, ConfigProperty<?>> properties;

	/**
	 * Creates the PropertyMap.
	 *
	 * @param opcodes The {@link Map} of opcodes to {@link ConfigProperty} objects.
	 * @throws IllegalArgumentException If the contains a mapping for opcode 0.
	 */
	public PropertyMap(Map<Integer, ConfigProperty<?>> opcodes) {
		Preconditions.checkArgument(opcodes.get(0) == null, "Opcode 0 is reserved.");
		this.opcodes = new HashMap<>(opcodes);

		Map<ConfigPropertyType, ConfigProperty<?>> properties = new HashMap<>(opcodes.size());

		for (Map.Entry<Integer, ConfigProperty<?>> entry : opcodes.entrySet()) {
			ConfigProperty<?> property = entry.getValue().duplicate();
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
	 * Gets the {@link ConfigProperty} with the specified {@link ConfigPropertyType}.
	 *
	 * @param name The name of the DefinitionProperty.
	 * @return The DefinitionProperty.
	 * @throws IllegalArgumentException If no DefinitionProperty with the specified name exists.
	 */
	public <T> ConfigProperty<T> get(ConfigPropertyType name) {
		return validate(properties.get(name), name);
	}

	/**
	 * Gets the {@link ConfigProperty} with the specified opcode.
	 *
	 * @param opcode The opcode of the DefinitionProperty.
	 * @return The DefinitionProperty.
	 * @throws IllegalArgumentException If no DefinitionProperty with the specified opcode exists.
	 */
	public <T> ConfigProperty<T> get(int opcode) {
		return validate(opcodes.get(opcode), opcode);
	}

	/**
	 * Gets a {@link Set} containing {@link java.util.Map.Entry Map.Entry} objects of opcodes to
	 * {@link ConfigProperty} objects.
	 * 
	 * @return The set of map entries.
	 */
	public Set<Map.Entry<Integer, ConfigProperty<?>>> getProperties() {
		return opcodes.entrySet();
	}

	/**
	 * Adds a {@link ConfigProperty} with the specified opcode.
	 *
	 * @param opcode The opcode.
	 * @param property The DefinitionProperty.
	 */
	public void put(int opcode, ConfigProperty<?> property) {
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
	 * Gets a {@link Collection} containing the values of this map (i.e. the {@link ConfigProperty} objects).
	 *
	 * @return The values.
	 */
	public Collection<ConfigProperty<?>> values() {
		return properties.values();
	}

}