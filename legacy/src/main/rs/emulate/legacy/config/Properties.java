package rs.emulate.legacy.config;

import rs.emulate.shared.property.PropertyDecoders;
import rs.emulate.shared.property.PropertyEncoders;
import rs.emulate.shared.property.PropertyParsers;

/**
 * Contains static utility methods to create {@link SerializableProperty} objects.
 *
 * @author Major
 */
public final class Properties {

	/**
	 * Creates a byte {@link SerializableProperty} with no value.
	 *
	 * @param name The name of the property.
	 * @param defaultValue The default value of the property.
	 * @return The DefinitionProperty.
	 */
	public static SerializableProperty<Integer> unsignedByte(ConfigPropertyType name, int defaultValue) {
		return new SerializableProperty<>(name, defaultValue, PropertyEncoders.BYTE_ENCODER, PropertyDecoders.BYTE_DECODER,
				Byte.BYTES, PropertyParsers.unsignedByte());
	}

	/**
	 * Creates a boolean {@link SerializableProperty} that encodes and decodes no data, and simply returns {@code false}.
	 *
	 * @param name The name of the property.
	 * @param defaultValue The default value of the property.
	 * @return The DefinitionProperty.
	 */
	public static SerializableProperty<Boolean> alwaysFalse(ConfigPropertyType name, boolean defaultValue) {
		return new SerializableProperty<>(name, defaultValue, PropertyEncoders.nullEncoder(), PropertyDecoders.FALSE_DECODER,
				0, PropertyParsers.forBoolean());
	}

	/**
	 * Creates an integer {@link SerializableProperty} with no value.
	 *
	 * @param name The name of the property.
	 * @param defaultValue The default value of the property.
	 * @return The DefinitionProperty.
	 */
	public static SerializableProperty<Long> unsignedInt(ConfigPropertyType name, long defaultValue) {
		return new SerializableProperty<>(name, defaultValue, PropertyEncoders.LONG_ENCODER, PropertyDecoders.INT_DECODER,
				Integer.BYTES, PropertyParsers.unsignedInt());
	}

	/**
	 * Creates a short {@link SerializableProperty} with no value.
	 *
	 * @param name The name of the property.
	 * @param defaultValue The default value of the property.
	 * @return The DefinitionProperty.
	 */
	public static SerializableProperty<Integer> unsignedShort(ConfigPropertyType name, int defaultValue) {
		return new SerializableProperty<>(name, defaultValue, PropertyEncoders.SHORT_ENCODER, PropertyDecoders.SHORT_DECODER,
				Short.BYTES, PropertyParsers.unsignedShort());
	}

	/**
	 * Creates a signed byte {@link SerializableProperty} with no value.
	 *
	 * @param name The name of the property.
	 * @param defaultValue The default value of the property.
	 * @return The DefinitionProperty.
	 */
	public static SerializableProperty<Integer> signedByte(ConfigPropertyType name, int defaultValue) {
		return new SerializableProperty<>(name, defaultValue, PropertyEncoders.BYTE_ENCODER,
				PropertyDecoders.SIGNED_BYTE_DECODER, Byte.BYTES, PropertyParsers.signedByte());
	}

	/**
	 * Creates a signed short {@link SerializableProperty} with no value.
	 *
	 * @param name The name of the property.
	 * @param defaultValue The default value of the property.
	 * @return The DefinitionProperty.
	 */
	public static SerializableProperty<Integer> signedShort(ConfigPropertyType name, int defaultValue) {
		return new SerializableProperty<>(name, defaultValue, PropertyEncoders.SHORT_ENCODER,
				PropertyDecoders.SIGNED_SHORT_DECODER, Short.BYTES, PropertyParsers.signedShort());
	}

	/**
	 * Creates a string {@link SerializableProperty} with no value.
	 *
	 * @param name The name of the property.
	 * @param defaultValue The default value of the property.
	 * @return The DefinitionProperty.
	 */
	public static SerializableProperty<String> asciiString(ConfigPropertyType name, String defaultValue) {
		return new SerializableProperty<>(name, defaultValue, PropertyEncoders.ASCII_STRING_ENCODER,
				PropertyDecoders.ASCII_STRING_DECODER, string -> string.length() + Byte.BYTES, // terminator byte
				PropertyParsers.forAsciiString());
	}

	/**
	 * Creates an unsigned tri-byte {@link SerializableProperty} with no value.
	 *
	 * @param name The name of the property.
	 * @param defaultValue The default value of the property.
	 * @return The DefinitionProperty.
	 */
	public static SerializableProperty<Integer> unsignedTribyte(ConfigPropertyType name, int defaultValue) {
		return new SerializableProperty<>(name, defaultValue, PropertyEncoders.TRI_BYTE_ENCODER,
				PropertyDecoders.UNSIGNED_TRI_BYTE_DECODER, 3 * Byte.BYTES, PropertyParsers.unsignedTriByte());
	}

	/**
	 * Creates a boolean {@link SerializableProperty} that encodes and decodes no data, and simply returns {@code true}.
	 *
	 * @param name The name of the property.
	 * @param defaultValue The default value of the property.
	 * @return The DefinitionProperty.
	 */
	public static SerializableProperty<Boolean> alwaysTrue(ConfigPropertyType name, boolean defaultValue) {
		return new SerializableProperty<>(name, defaultValue, PropertyEncoders.nullEncoder(), PropertyDecoders.TRUE_DECODER,
				0, PropertyParsers.forBoolean());
	}

	/**
	 * Sole private constructor to prevent instantiation.
	 */
	private Properties() {

	}

}