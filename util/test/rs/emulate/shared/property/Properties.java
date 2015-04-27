package rs.emulate.shared.property;

import rs.emulate.legacy.config.ConfigProperty;
import rs.emulate.legacy.config.ConfigPropertyType;

/**
 * Contains static utility methods to create {@link ConfigProperty} objects.
 * 
 * @author Major
 */
public final class Properties {

	/**
	 * Creates a byte {@link ConfigProperty} with no value.
	 *
	 * @param name The name of the property.
	 * @param defaultValue The default value of the property.
	 * @return The DefinitionProperty.
	 */
	public static ConfigProperty<Integer> unsignedByte(ConfigPropertyType name, int defaultValue) {
		return new ConfigProperty<>(name, defaultValue, PropertyEncoders.BYTE_ENCODER, PropertyDecoders.BYTE_DECODER, Byte.BYTES);
	}

	/**
	 * Creates a boolean {@link ConfigProperty} that encodes and decodes no data, and simply returns {@code false}.
	 *
	 * @param name The name of the property.
	 * @param defaultValue The default value of the property.
	 * @return The DefinitionProperty.
	 */
	public static ConfigProperty<Boolean> alwaysFalse(ConfigPropertyType name, boolean defaultValue) {
		return new ConfigProperty<>(name, defaultValue, PropertyEncoders.nullEncoder(), PropertyDecoders.FALSE_DECODER, 0);
	}

	/**
	 * Creates an integer {@link ConfigProperty} with no value.
	 *
	 * @param name The name of the property.
	 * @param defaultValue The default value of the property.
	 * @return The DefinitionProperty.
	 */
	public static ConfigProperty<Integer> unsignedInt(ConfigPropertyType name, int defaultValue) {
		return new ConfigProperty<>(name, defaultValue, PropertyEncoders.INT_ENCODER, PropertyDecoders.INT_DECODER, Integer.BYTES);
	}

	/**
	 * Creates a short {@link ConfigProperty} with no value.
	 *
	 * @param name The name of the property.
	 * @param defaultValue The default value of the property.
	 * @return The DefinitionProperty.
	 */
	public static ConfigProperty<Integer> unsignedShort(ConfigPropertyType name, int defaultValue) {
		return new ConfigProperty<>(name, defaultValue, PropertyEncoders.SHORT_ENCODER, PropertyDecoders.SHORT_DECODER,
				Short.BYTES);
	}

	/**
	 * Creates a signed byte {@link ConfigProperty} with no value.
	 *
	 * @param name The name of the property.
	 * @param defaultValue The default value of the property.
	 * @return The DefinitionProperty.
	 */
	public static ConfigProperty<Integer> signedByte(ConfigPropertyType name, int defaultValue) {
		return new ConfigProperty<>(name, defaultValue, PropertyEncoders.BYTE_ENCODER, PropertyDecoders.SIGNED_BYTE_DECODER,
				Byte.BYTES);
	}

	/**
	 * Creates a signed short {@link ConfigProperty} with no value.
	 *
	 * @param name The name of the property.
	 * @param defaultValue The default value of the property.
	 * @return The DefinitionProperty.
	 */
	public static ConfigProperty<Integer> signedShort(ConfigPropertyType name, int defaultValue) {
		return new ConfigProperty<>(name, defaultValue, PropertyEncoders.SHORT_ENCODER, PropertyDecoders.SIGNED_SHORT_DECODER,
				Short.BYTES);
	}

	/**
	 * Creates a string {@link ConfigProperty} with no value.
	 * 
	 * @param name The name of the property.
	 * @param defaultValue The default value of the property.
	 * @return The DefinitionProperty.
	 */
	public static ConfigProperty<String> asciiString(ConfigPropertyType name, String defaultValue) {
		return new ConfigProperty<>(name, defaultValue, PropertyEncoders.ASCII_STRING_ENCODER,
				PropertyDecoders.ASCII_STRING_DECODER, string -> string.length() + Byte.BYTES); // terminator byte
	}

	/**
	 * Creates a tri-byte {@link ConfigProperty} with no value.
	 *
	 * @param name The name of the property.
	 * @param defaultValue The default value of the property.
	 * @return The DefinitionProperty.
	 */
	public static ConfigProperty<Integer> tribyte(ConfigPropertyType name, int defaultValue) {
		return new ConfigProperty<>(name, defaultValue, PropertyEncoders.TRI_BYTE_ENCODER, PropertyDecoders.TRI_BYTE_DECODER,
				3 * Byte.BYTES);
	}

	/**
	 * Creates a boolean {@link ConfigProperty} that encodes and decodes no data, and simply returns {@code true}.
	 *
	 * @param name The name of the property.
	 * @param defaultValue The default value of the property.
	 * @return The DefinitionProperty.
	 */
	public static ConfigProperty<Boolean> alwaysTrue(ConfigPropertyType name, boolean defaultValue) {
		return new ConfigProperty<>(name, defaultValue, PropertyEncoders.nullEncoder(), PropertyDecoders.TRUE_DECODER, 0);
	}

	/**
	 * Sole private constructor to prevent instantiation.
	 */
	private Properties() {

	}

}