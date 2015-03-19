package rs.emulate.shared.prop;

/**
 * Contains static utility methods to create {@link DefinitionProperty} objects.
 * 
 * @author Major
 */
public final class Properties {

	/**
	 * Creates a byte {@link DefinitionProperty} with no value.
	 *
	 * @param name The name of the property.
	 * @param defaultValue The default value of the property.
	 * @return The DefinitionProperty.
	 */
	public static DefinitionProperty<Integer> unsignedByte(PropertyType name, int defaultValue) {
		return new DefinitionProperty<>(name, defaultValue, PropertyEncoders.BYTE_ENCODER, PropertyDecoders.BYTE_DECODER,
				Byte.BYTES);
	}

	/**
	 * Creates a boolean {@link DefinitionProperty} that encodes and decodes no data, and simply returns {@code false}.
	 *
	 * @param name The name of the property.
	 * @param defaultValue The default value of the property.
	 * @return The DefinitionProperty.
	 */
	public static DefinitionProperty<Boolean> alwaysFalse(PropertyType name, boolean defaultValue) {
		return new DefinitionProperty<>(name, defaultValue, PropertyEncoders.nullEncoder(), PropertyDecoders.FALSE_DECODER, 0);
	}

	/**
	 * Creates an integer {@link DefinitionProperty} with no value.
	 *
	 * @param name The name of the property.
	 * @param defaultValue The default value of the property.
	 * @return The DefinitionProperty.
	 */
	public static DefinitionProperty<Integer> unsignedInt(PropertyType name, int defaultValue) {
		return new DefinitionProperty<>(name, defaultValue, PropertyEncoders.INT_ENCODER, PropertyDecoders.INT_DECODER,
				Integer.BYTES);
	}

	/**
	 * Creates a short {@link DefinitionProperty} with no value.
	 *
	 * @param name The name of the property.
	 * @param defaultValue The default value of the property.
	 * @return The DefinitionProperty.
	 */
	public static DefinitionProperty<Integer> unsignedShort(PropertyType name, int defaultValue) {
		return new DefinitionProperty<>(name, defaultValue, PropertyEncoders.SHORT_ENCODER, PropertyDecoders.SHORT_DECODER,
				Short.BYTES);
	}

	/**
	 * Creates a signed byte {@link DefinitionProperty} with no value.
	 *
	 * @param name The name of the property.
	 * @param defaultValue The default value of the property.
	 * @return The DefinitionProperty.
	 */
	public static DefinitionProperty<Integer> signedByte(PropertyType name, int defaultValue) {
		return new DefinitionProperty<>(name, defaultValue, PropertyEncoders.BYTE_ENCODER, PropertyDecoders.SIGNED_BYTE_DECODER,
				Byte.BYTES);
	}

	/**
	 * Creates a signed short {@link DefinitionProperty} with no value.
	 *
	 * @param name The name of the property.
	 * @param defaultValue The default value of the property.
	 * @return The DefinitionProperty.
	 */
	public static DefinitionProperty<Integer> signedShort(PropertyType name, int defaultValue) {
		return new DefinitionProperty<>(name, defaultValue, PropertyEncoders.SHORT_ENCODER,
				PropertyDecoders.SIGNED_SHORT_DECODER, Short.BYTES);
	}

	/**
	 * Creates a string {@link DefinitionProperty} with no value.
	 * 
	 * @param name The name of the property.
	 * @param defaultValue The default value of the property.
	 * @return The DefinitionProperty.
	 */
	public static DefinitionProperty<String> string(PropertyType name, String defaultValue) {
		return new DefinitionProperty<>(name, defaultValue, PropertyEncoders.ASCII_STRING_ENCODER,
				PropertyDecoders.STRING_DECODER, string -> string.length() + Byte.BYTES); // terminator byte
	}

	/**
	 * Creates a tri-byte {@link DefinitionProperty} with no value.
	 *
	 * @param name The name of the property.
	 * @param defaultValue The default value of the property.
	 * @return The DefinitionProperty.
	 */
	public static DefinitionProperty<Integer> tribyte(PropertyType name, int defaultValue) {
		return new DefinitionProperty<>(name, defaultValue, PropertyEncoders.TRI_BYTE_ENCODER, PropertyDecoders.TRI_BYTE_DECODER,
				3 * Byte.BYTES);
	}

	/**
	 * Creates a boolean {@link DefinitionProperty} that encodes and decodes no data, and simply returns {@code true}.
	 *
	 * @param name The name of the property.
	 * @param defaultValue The default value of the property.
	 * @return The DefinitionProperty.
	 */
	public static DefinitionProperty<Boolean> alwaysTrue(PropertyType name, boolean defaultValue) {
		return new DefinitionProperty<>(name, defaultValue, PropertyEncoders.nullEncoder(), PropertyDecoders.TRUE_DECODER, 0);
	}

	/**
	 * Sole private constructor to prevent instantiation.
	 */
	private Properties() {

	}

}