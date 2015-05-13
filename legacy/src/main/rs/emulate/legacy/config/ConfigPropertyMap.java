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
	 * Validates the specified {@link SerializableProperty} (i.e. ensures that it is not {@code null}).
	 * 
	 * @param property The ConfigProperty to validate.
	 * @param key The key used to access the ConfigProperty.
	 * @return The ConfigProperty.
	 * @throws IllegalArgumentException If the ConfigProperty is {@code null}.
	 */
	@SuppressWarnings("unchecked")
	private static <T> SerializableProperty<T> validate(SerializableProperty<?> property, Object key) {
		return (SerializableProperty<T>) Objects.requireNonNull(property, "No property with a key of " + key + " exists.");
	}

	/**
	 * The Map of opcodes to ConfigProperty objects.
	 */
	private final Map<Integer, SerializableProperty<?>> opcodes;

	/**
	 * The Map of ConfigPropertyTypes to ConfigProperty objects.
	 */
	private final Map<ConfigPropertyType, SerializableProperty<?>> properties;

	/**
	 * Creates the PropertyMap.
	 *
	 * @param opcodes The {@link Map} of opcodes to {@link SerializableProperty} objects.
	 * @throws IllegalArgumentException If {@code opcodes} contains a mapping for opcode 0.
	 */
	public ConfigPropertyMap(Map<Integer, SerializableProperty<?>> opcodes) {
		Preconditions.checkArgument(!opcodes.containsKey(0), "Opcode 0 is reserved.");
		this.opcodes = new HashMap<>(opcodes);

		Map<ConfigPropertyType, SerializableProperty<?>> properties = new HashMap<>(opcodes.size());

		for (Map.Entry<Integer, SerializableProperty<?>> entry : opcodes.entrySet()) {
			SerializableProperty<?> property = entry.getValue().duplicate();
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
	 * Gets the {@link SerializableProperty} with the specified {@link ConfigPropertyType}.
	 *
	 * @param name The name of the ConfigProperty.
	 * @return The ConfigProperty.
	 * @throws IllegalArgumentException If no ConfigProperty with the specified name exists.
	 */
	public <T> SerializableProperty<T> get(ConfigPropertyType name) {
		return validate(properties.get(name), name);
	}

	/**
	 * Gets the {@link SerializableProperty} with the specified opcode.
	 *
	 * @param opcode The opcode of the ConfigProperty.
	 * @return The ConfigProperty.
	 * @throws IllegalArgumentException If no ConfigProperty with the specified opcode exists.
	 */
	public <T> SerializableProperty<T> get(int opcode) {
		return validate(opcodes.get(opcode), opcode);
	}

	/**
	 * Gets a {@link Set} containing {@link java.util.Map.Entry Map.Entry} objects of opcodes to {@link SerializableProperty}
	 * objects.
	 * 
	 * @return The Set of Map Entry objects.
	 */
	public Set<Map.Entry<Integer, SerializableProperty<?>>> getProperties() {
		return opcodes.entrySet();
	}

	/**
	 * Adds a {@link SerializableProperty} with the specified opcode.
	 *
	 * @param opcode The opcode.
	 * @param property The ConfigProperty.
	 */
	public void put(int opcode, SerializableProperty<?> property) {
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
	 * Gets a {@link Collection} containing the values of this ConfigPropertyMap (i.e. the {@link SerializableProperty} objects).
	 *
	 * @return The Collection of values.
	 */
	public Collection<SerializableProperty<?>> values() {
		return properties.values();
	}

}