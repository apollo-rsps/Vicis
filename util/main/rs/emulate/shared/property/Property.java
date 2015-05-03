package rs.emulate.shared.property;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import com.google.common.base.Preconditions;

/**
 * A mutable property belonging to some sort of definition.
 *
 * @author Major
 * @param <V> The type of the value.
 * @param <T> The type of {@link PropertyType}.
 */
public class Property<V, T extends PropertyType> {

	/**
	 * The default value of this DefinitionProperty.
	 */
	protected final V defaultValue;

	/**
	 * The Function that parses a value from the String passed to it.
	 */
	protected final Function<String, Optional<V>> parser;

	/**
	 * The type of this DefinitionProperty.
	 */
	protected final T type;

	/**
	 * The value of this DefinitionProperty, or {@link Optional#empty} if the value is the default.
	 */
	protected Optional<V> value;

	/**
	 * Creates the Property.
	 *
	 * @param type The {@link PropertyType}. Must not be {@code null}.
	 * @param defaultValue The default value. May be {@code null}.
	 * @param parser The {@link Function} that parses a value from a String. Must not be {@code null}.
	 */
	public Property(T type, V defaultValue, Function<String, Optional<V>> parser) {
		this(type, null, defaultValue, parser);
	}

	/**
	 * Creates the Property.
	 *
	 * @param type The {@link PropertyType}. Must not be {@code null}.
	 * @param value The value. May be {@code null}.
	 * @param defaultValue The default value. May be {@code null}.
	 * @param parser The {@link Function} that parses a value from a String. Must not be {@code null}.
	 */
	public Property(T type, V value, V defaultValue, Function<String, Optional<V>> parser) {
		Preconditions.checkNotNull(type, "PropertyType cannot be null.");
		this.type = type;
		this.value = Optional.ofNullable(value);
		this.defaultValue = defaultValue;
		this.parser = Objects.requireNonNull(parser, "Parser must not be null.");
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Property) {
			Property<?, ?> other = (Property<?, ?>) obj;
			return type.equals(other.type) && Objects.equals(getValue(), other.getValue());
		}

		return false;
	}

	/**
	 * Gets the default value of this DefinitionProperty.
	 *
	 * @return The default value.
	 */
	public final V getDefault() {
		return defaultValue;
	}

	/**
	 * Gets the {@code toString} message for the value of this DefinitionProperty.
	 *
	 * @return The message.
	 */
	private String getMessage() {
		V value = getValue();
		if (value == null) {
			return "null";
		}

		@SuppressWarnings("unchecked")
		Class<V> type = (Class<V>) value.getClass();

		if (!type.isArray()) {
			return value.toString();
		}

		Class<?> component = type.getComponentType();

		// This code is awful, but there's no alternative...

		if (component.equals(Boolean.TYPE)) {
			return Arrays.toString((boolean[]) value);
		} else if (component.equals(Character.TYPE)) {
			return Arrays.toString((char[]) value);
		} else if (component.equals(Byte.TYPE)) {
			return Arrays.toString((byte[]) value);
		} else if (component.equals((Short.TYPE))) {
			return Arrays.toString((short[]) value);
		} else if (component.equals(Integer.TYPE)) {
			return Arrays.toString((int[]) value);
		} else if (component.equals(Long.TYPE)) {
			return Arrays.toString((long[]) value);
		} else if (component.equals(Float.TYPE)) {
			return Arrays.toString((float[]) value);
		} else if (component.equals(Double.TYPE)) {
			return Arrays.toString((double[]) value);
		}

		return Arrays.toString((Object[]) value);
	}

	/**
	 * Gets the name of this DefinitionProperty.
	 *
	 * @return The name.
	 */
	public final String getName() {
		return type.formattedName();
	}

	/**
	 * Gets the type of this DefinitionProperty.
	 *
	 * @return The type.
	 */
	public final T getType() {
		return type;
	}

	/**
	 * Gets the value of this DefinitionProperty.
	 *
	 * @return The value.
	 */
	public final V getValue() {
		return value.orElse(defaultValue);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, getValue());
	}

	/**
	 * Attempts to parse the specified input String, setting the value of this Property to the parsed value if parsing
	 * was successful.
	 *
	 * @param input The input String.
	 * @return {@code true} if parsing was successful, {@code false} if not.
	 */
	public boolean parse(String input) {
		Optional<V> value = parser.apply(input);
		if (value.isPresent()) {
			this.value = value;
			return true;
		}

		return false;
	}

	/**
	 * Resets the value of this DefinitionProperty.
	 */
	public final void reset() {
		value = Optional.empty();
	}

	/**
	 * Sets the value of this DefinitionProperty.
	 *
	 * @param value The value. May be {@code null}.
	 */
	public final void setValue(V value) {
		this.value = Optional.ofNullable(value);
	}

	@Override
	public final String toString() {
		return getMessage();
	}

	/**
	 * Returns whether or not there is a custom value present.
	 *
	 * @return {@code true} if there is a value, or {@code false} if the value is the default.
	 */
	public final boolean valuePresent() {
		return value.isPresent();
	}

}