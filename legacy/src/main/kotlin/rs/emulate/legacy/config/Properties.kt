package rs.emulate.legacy.config

import rs.emulate.shared.property.PropertyDecoders
import rs.emulate.shared.property.PropertyEncoders

/**
 * Contains static utility methods to create [SerializableProperty] objects.
 */
object Properties {

    /**
     * Creates a boolean [SerializableProperty] that encodes and decodes no data, and simply returns `false`.
     */
    fun alwaysFalse(name: ConfigPropertyType<Boolean>, defaultValue: Boolean): SerializableProperty<Boolean> {
        return SerializableProperty(name, defaultValue, PropertyEncoders.nullEncoder(), PropertyDecoders.FALSE_DECODER,
            0)
    }

    /**
     * Creates a boolean [SerializableProperty] that encodes and decodes no data, and simply returns `true`.
     */
    fun alwaysTrue(name: ConfigPropertyType<Boolean>, defaultValue: Boolean): SerializableProperty<Boolean> {
        return SerializableProperty(name, defaultValue, PropertyEncoders.nullEncoder(),
            PropertyDecoders.TRUE_DECODER, 0)
    }

    /**
     * Creates a string [SerializableProperty] with no value.
     */
    fun asciiString(name: ConfigPropertyType<String>, defaultValue: String): SerializableProperty<String> {
        return SerializableProperty(name, defaultValue, PropertyEncoders.ASCII_STRING_ENCODER,
            PropertyDecoders.ASCII_STRING_DECODER,
            { string: String -> string.length + java.lang.Byte.BYTES })
    }

    /**
     * Creates a signed byte [SerializableProperty] with no value.
     */
    fun signedByte(name: ConfigPropertyType<Int>, defaultValue: Int): SerializableProperty<Int> {
        return SerializableProperty(name, defaultValue, PropertyEncoders.BYTE_ENCODER,
            PropertyDecoders.SIGNED_BYTE_DECODER, java.lang.Byte.BYTES)
    }

    /**
     * Creates a signed short [SerializableProperty] with no value.
     */
    fun signedShort(name: ConfigPropertyType<Int>, defaultValue: Int): SerializableProperty<Int> {
        return SerializableProperty(name, defaultValue, PropertyEncoders.SHORT_ENCODER,
            PropertyDecoders.SIGNED_SHORT_DECODER, java.lang.Short.BYTES)
    }

    /**
     * Creates a byte [SerializableProperty] with no value.
     */
    fun unsignedByte(name: ConfigPropertyType<Int>, defaultValue: Int): SerializableProperty<Int> {
        return SerializableProperty(name, defaultValue, PropertyEncoders.BYTE_ENCODER, PropertyDecoders.BYTE_DECODER,
            java.lang.Byte.BYTES)
    }

    /**
     * Creates an integer [SerializableProperty] with no value.
     */
    fun unsignedInt(name: ConfigPropertyType<Long>, defaultValue: Long): SerializableProperty<Long> {
        return SerializableProperty(name, defaultValue, PropertyEncoders.LONG_ENCODER, PropertyDecoders.INT_DECODER,
            Integer.BYTES)
    }

    /**
     * Creates a short [SerializableProperty] with no value.
     */
    fun unsignedShort(name: ConfigPropertyType<Int>, defaultValue: Int): SerializableProperty<Int> {
        return SerializableProperty(name, defaultValue, PropertyEncoders.SHORT_ENCODER, PropertyDecoders.SHORT_DECODER,
            java.lang.Short.BYTES)
    }

    /**
     * Creates an unsigned tri-byte [SerializableProperty] with no value.
     */
    fun unsignedTribyte(name: ConfigPropertyType<Int>, defaultValue: Int): SerializableProperty<Int> {
        return SerializableProperty(name, defaultValue, PropertyEncoders.TRI_BYTE_ENCODER,
            PropertyDecoders.UNSIGNED_TRI_BYTE_DECODER, 3 * java.lang.Byte.BYTES)
    }

}
