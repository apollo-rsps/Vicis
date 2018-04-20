package rs.emulate.shared.property

import rs.emulate.shared.util.DataBuffer

import java.nio.ByteBuffer
import java.util.function.BiConsumer

/**
 * Contains static [BiConsumer]s to encode values.
 */
object PropertyEncoders {

    val ASCII_STRING_ENCODER: (DataBuffer, String) -> DataBuffer = DataBuffer::putAsciiString

    val BYTE_BUFFER_ENCODER: (DataBuffer, ByteBuffer) -> DataBuffer = DataBuffer::put

    val BYTE_ENCODER: (DataBuffer, Int) -> DataBuffer = { buffer, value -> buffer.putByte(value.toInt()) }

    val C_STRING_ENCODER: (DataBuffer, String) -> DataBuffer = DataBuffer::putCString

    val INT_ENCODER: (DataBuffer, Int) -> DataBuffer = DataBuffer::putInt

    val LONG_ENCODER: (DataBuffer, Long) -> DataBuffer = DataBuffer::putLong

    val SHORT_ENCODER: (DataBuffer, Int) -> DataBuffer = { buffer, value -> buffer.putShort(value.toInt()) }

    val TRI_BYTE_ENCODER: (DataBuffer, Int) -> DataBuffer = DataBuffer::putTriByte

    /**
     * Gets a [BiConsumer] that acts as a disposer, writing the opcode of the property but no data. This is a
     * method rather than a constant because type inference is required.
     */
    fun <T> nullEncoder(): (DataBuffer, T) -> DataBuffer = { buffer, _ -> buffer }

}
