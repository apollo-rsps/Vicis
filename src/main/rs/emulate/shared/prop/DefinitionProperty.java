package rs.emulate.shared.prop;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import rs.emulate.shared.util.DataBuffer;
import rs.emulate.util.Assertions;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

/**
 * A property belonging to a definition.
 * 
 * @author Major
 * @param <T> The property value type.
 */
public final class DefinitionProperty<T> {

	/**
	 * A Function that decodes a value from a Buffer.
	 */
	private final Function<DataBuffer, T> decoder;

	/**
	 * The default value of this property.
	 */
	private final T defaultValue;

	/**
	 * A BiConsumer that encodes the value into a Buffer.
	 */
	private final BiConsumer<DataBuffer, T> encoder;

	/**
	 * The name of this property.
	 */
	private final PropertyType type;

	/**
	 * A Function that returns the size of this property, in bytes.
	 */
	private final Function<T, Integer> size;

	/**
	 * The value of this property, or {@link Optional#empty} if the value is the default.
	 */
	private Optional<T> value;

	/**
	 * Creates the DefinitionProperty.
	 * 
	 * @param name The name of the property.
	 * @param defaultValue The default value. May be null.
	 * @param encoder A {@link BiConsumer} that encodes the value into a {@link DataBuffer}.
	 * @param decoder A {@link Function} that decodes the value from a Buffer.
	 * @param size A Function that takes the value of this property as the parameter and returns the size of the value,
	 *            in bytes.
	 */
	public DefinitionProperty(PropertyType name, T defaultValue, BiConsumer<DataBuffer, T> encoder, Function<DataBuffer, T> decoder,
			Function<T, Integer> size) {
		this(name, null, defaultValue, encoder, decoder, size);
	}

	/**
	 * Creates the DefinitionProperty.
	 * 
	 * @param name The name of the property.
	 * @param defaultValue The default value. May be null.
	 * @param encoder A {@link BiConsumer} that encodes the value into a {@link DataBuffer}.
	 * @param decoder A {@link Function} that decodes the value from a Buffer.
	 * @param size The size of the value, in bytes.
	 */
	public DefinitionProperty(PropertyType name, T defaultValue, BiConsumer<DataBuffer, T> encoder, Function<DataBuffer, T> decoder,
			int size) {
		this(name, defaultValue, encoder, decoder, value -> size);
	}

	/**
	 * Creates the DefinitionProperty.
	 * 
	 * @param name The name of the property.
	 * @param value The value. May be {@code null}.
	 * @param defaultValue The default value. May be {@code null}.
	 * @param encoder A {@link BiConsumer} that encodes the value into a {@link DataBuffer}.
	 * @param decoder A {@link Function} that decodes the value from a Buffer.
	 * @param size A function that takes the value of this property as the parameter and returns the size of the value,
	 *            in bytes.
	 */
	public DefinitionProperty(PropertyType name, T value, T defaultValue, BiConsumer<DataBuffer, T> encoder,
			Function<DataBuffer, T> decoder, Function<T, Integer> size) {
		Assertions.checkNonNull("Decoder, encoder, and size lambdas must not be null.", decoder, encoder, size);
		Preconditions.checkNotNull(name, "Name of property cannot be null.");

		this.type = name;
		this.value = Optional.ofNullable(value);
		this.defaultValue = defaultValue;
		this.encoder = encoder;
		this.decoder = decoder;
		this.size = size;
	}

	/**
	 * Decodes a value from the specified {@link DataBuffer}, and sets the value of this property to it.
	 *
	 * @param buffer The buffer.
	 */
	public void decode(DataBuffer buffer) {
		T value = decoder.apply(buffer);
		this.value = Optional.of(value);
	}

	/**
	 * Creates a duplicate of this DefinitionProperty.
	 * 
	 * @return The definition property.
	 */
	public DefinitionProperty<T> duplicate() {
		return new DefinitionProperty<>(type, value.orElse(null), defaultValue, encoder, decoder, size);
	}

	/**
	 * Encodes this property into a {@link DataBuffer}.
	 *
	 * @return The buffer.
	 * @throws NoSuchElementException If this method is called on a property without a value set.
	 */
	public DataBuffer encode() {
		T value = this.value.get();
		DataBuffer buffer = DataBuffer.allocate(size.apply(value));

		encoder.accept(buffer, value);
		return buffer.flip();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DefinitionProperty) {
			DefinitionProperty<?> other = (DefinitionProperty<?>) obj;
			T value = getValue();

			return type.equals(other.type) && (value == null) ? other.getValue() == null : value.equals(other.getValue());
		}

		return false;
	}

	/**
	 * Gets the default value of this property.
	 *
	 * @return The default value.
	 */
	public T getDefault() {
		return defaultValue;
	}

	/**
	 * Gets the type of this property.
	 *
	 * @return The type.
	 */
	public PropertyType getType() {
		return type;
	}

	/**
	 * Gets the value of this property.
	 *
	 * @return The value.
	 */
	public T getValue() {
		return value.orElse(defaultValue);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, getValue());
	}

	/**
	 * Resets the value of this property.
	 */
	public void reset() {
		value = Optional.empty();
	}

	/**
	 * Sets the value of this property.
	 *
	 * @param value The value. May be {@code null}.
	 */
	public void setValue(T value) {
		this.value = Optional.ofNullable(value);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper("").add("value", getMessage()).toString();
	}

	/**
	 * Returns whether or not there is a custom value present.
	 *
	 * @return {@code true} if there is a value, or {@code false} if the value is the default.
	 */
	public boolean valuePresent() {
		return value.isPresent();
	}

	/**
	 * Gets the {@code toString} message for the value of this property.
	 * 
	 * @return The message.
	 */
	private String getMessage() {
		T value = getValue();
		@SuppressWarnings("unchecked")
		Class<T> type = (Class<T>) value.getClass();

		if (!type.isArray()) {
			return value.toString();
		}

		Class<?> component = type.getComponentType();

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
		}

		return Arrays.toString((Object[]) value);
	}

}