package rs.emulate.legacy.config

import rs.emulate.shared.property.Property
import rs.emulate.shared.util.DataBuffer
import java.util.function.BiConsumer
import java.util.function.Function

/**
 * A property belonging to a definition in the config archive.
 *
 * @param type The name of the property.
 * @param value The value. May be `null`.
 * @param defaultValue The default value. May be `null`.
 * @param encoder A [BiConsumer] that encodes the value into a [DataBuffer].
 * @param decoder A [Function] that decodes the value from a Buffer.
 * @param size A Function that returns the size of the value, in bytes.
 *
 * @param T The property value type.
 */
class SerializableProperty<T : Any>(
    type: ConfigPropertyType,
    value: T?,
    defaultValue: T?,
    private val encoder: (DataBuffer, T) -> DataBuffer,
    private val decoder: (DataBuffer) -> T,
    private val size: (T) -> Int
) : Property<T, ConfigPropertyType>(type, value, defaultValue) {

    /**
     * Creates a [SerializableProperty] with an initial value of `null`.
     */
    constructor(
        type: ConfigPropertyType,
        defaultValue: T?,
        encoder: (DataBuffer, T) -> DataBuffer,
        decoder: (DataBuffer) -> T,
        size: (T) -> Int
    ) : this(type, null, defaultValue, encoder, decoder, size)


    /**
     * Creates a [SerializableProperty] with an initial value of `null`.
     */
    constructor(
        type: ConfigPropertyType,
        defaultValue: T?,
        encoder: (DataBuffer, T) -> DataBuffer,
        decoder: (DataBuffer) -> T,
        size: Int
    ) : this(type, defaultValue, encoder, decoder, { size })

    /**
     * Decodes a value from the specified [DataBuffer], and sets the value of this DefinitionProperty to it.
     */
    fun decode(buffer: DataBuffer) {
        value = decoder(buffer)
    }

    /**
     * Creates a duplicate of this DefinitionProperty.
     */
    fun duplicate(): SerializableProperty<T> {
        return SerializableProperty(type, value, default, encoder, decoder, size)
    }

    /**
     * Encodes this DefinitionProperty into a [DataBuffer].
     */
    fun encode(): DataBuffer {
        val value = value!! // TODO
        val buffer = DataBuffer.allocate(size(value))

        encoder(buffer, value)
        return buffer.flip()
    }

}
