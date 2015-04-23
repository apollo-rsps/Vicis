package rs.emulate.shared.property;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * A {@link Map} wrapper containing methods to get properties using their opcode or name.
 *
 * @author Major
 * @param <T> The {@link PropertyType type} of properties in this Map.
 */
public final class PropertyMap<T extends PropertyType> {

	/**
	 * Validates that the specified property is valid (i.e. is not {@code null}).
	 * 
	 * @param property The {@link Property} to validate.
	 * @param key The key used to access the invalid property.
	 * @return The property, if it is valid.
	 * @throws IllegalArgumentException If the property is {@code null}.
	 */
	private static <V, T extends PropertyType> Property<V, T> validate(Property<V, T> property, Object key) {
		return Objects.requireNonNull(property, "No property with a key of " + key + " exists.");
	}

	/**
	 * The Map of PropertyTypes to Property objects.
	 */
	private final Map<T, Property<?, T>> properties;

	/**
	 * Creates the PropertyMap.
	 *
	 * @param properties The {@link Map} of {@link PropertyType}s to {@link Property} objects.
	 */
	public PropertyMap(Map<T, Property<?, T>> properties) {
		this.properties = properties;
	}

	/**
	 * Creates the PropertyMap, using an existing one as the base.
	 * 
	 * @param map The base PropertyMap.
	 */
	public PropertyMap(PropertyMap<T> map) {
		this(map.properties);
	}

	/**
	 * Gets the {@link Property} with the specified {@link PropertyType}.
	 *
	 * @param name The name of the Property.
	 * @return The Property.
	 * @throws IllegalArgumentException If no Property with the specified name exists.
	 */
	@SuppressWarnings("unchecked")
	public <V> Property<V, T> get(PropertyType name) {
		return (Property<V, T>) validate(properties.get(name), name);
	}

	/**
	 * Adds a {@link Property} to this PropertyMap.
	 * 
	 * @param property The Property.
	 */
	public void put(Property<?, T> property) {
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
	 * Gets a {@link Collection} containing the values of this PropertyMap (i.e. the {@link Property} objects).
	 *
	 * @return The Collection of values.
	 */
	public Collection<Property<?, T>> values() {
		return properties.values();
	}

}