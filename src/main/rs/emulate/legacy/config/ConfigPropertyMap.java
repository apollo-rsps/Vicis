package rs.emulate.legacy.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import rs.emulate.util.Assertions;

import com.google.common.base.Preconditions;

/**
 * A {@link Map} wrapper containing methods to get properties using their opcode or name.
 *
 * @author Major
 */
public final class ConfigPropertyMap {

	/**
	 * Validates the specified {@link ConfigProperty} (i.e. ensures that it is not {@code null}).
	 * 
	 * @param property The ConfigProperty to validate.
	 * @param key The key used to access the ConfigProperty.
	 * @return The ConfigProperty.
	 * @throws IllegalArgumentException If the ConfigProperty is {@code null}.
	 */
	@SuppressWarnings("unchecked")
	private static <T> ConfigProperty<T> validate(ConfigProperty<?> property, Object key) {
		return (ConfigProperty<T>) Objects.requireNonNull(property, "No property with a key of " + key + " exists.");
	}

	/**
	 * The Map of opcodes to ConfigProperty objects.
	 */
	private final Map<Integer, ConfigProperty<?>> opcodes;

	/**
	 * The Map of ConfigPropertyTypes to ConfigProperty objects.
	 */
	private final Map<ConfigPropertyType, ConfigProperty<?>> properties;

	/**
	 * Creates the PropertyMap.
	 *
	 * @param opcodes The {@link Map} of opcodes to {@link ConfigProperty} objects.
	 * @throws IllegalArgumentException If {@code opcodes} contains a mapping for opcode 0.
	 */
	public ConfigPropertyMap(Map<Integer, ConfigProperty<?>> opcodes) {
		Preconditions.checkArgument(!opcodes.containsKey(0), "Opcode 0 is reserved.");
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
	 * Creates the ConfigPropertyMap, using an existing one as the base.
	 * 
	 * @param map The base ConfigPropertyMap.
	 */
	public ConfigPropertyMap(ConfigPropertyMap map) {
		this(map.opcodes);
	}

	/**
	 * Gets the {@link ConfigProperty} with the specified {@link ConfigPropertyType}.
	 *
	 * @param name The name of the ConfigProperty.
	 * @return The ConfigProperty.
	 * @throws IllegalArgumentException If no ConfigProperty with the specified name exists.
	 */
	public <T> ConfigProperty<T> get(ConfigPropertyType name) {
		return validate(properties.get(name), name);
	}

	/**
	 * Gets the {@link ConfigProperty} with the specified opcode.
	 *
	 * @param opcode The opcode of the ConfigProperty.
	 * @return The ConfigProperty.
	 * @throws IllegalArgumentException If no ConfigProperty with the specified opcode exists.
	 */
	public <T> ConfigProperty<T> get(int opcode) {
		return validate(opcodes.get(opcode), opcode);
	}

	/**
	 * Gets a {@link Set} containing {@link java.util.Map.Entry Map.Entry} objects of opcodes to {@link ConfigProperty}
	 * objects.
	 * 
	 * @return The Set of Map Entry objects.
	 */
	public Set<Map.Entry<Integer, ConfigProperty<?>>> getProperties() {
		return opcodes.entrySet();
	}

	/**
	 * Adds a {@link ConfigProperty} with the specified opcode.
	 *
	 * @param opcode The opcode.
	 * @param property The ConfigProperty.
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
	 * Gets a {@link Collection} containing the values of this ConfigPropertyMap (i.e. the {@link ConfigProperty} objects).
	 *
	 * @return The Collection of values.
	 */
	public Collection<ConfigProperty<?>> values() {
		return properties.values();
	}

}