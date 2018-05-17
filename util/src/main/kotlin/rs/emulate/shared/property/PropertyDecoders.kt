package rs.emulate.shared.property

import rs.emulate.shared.util.DataBuffer

import java.util.function.Function

/**
 * Contains static [Function]s to decode values.
 */
object PropertyDecoders {

    val ASCII_STRING_DECODER: (DataBuffer) -> String = DataBuffer::getString

    val BYTE_DECODER: (DataBuffer) -> Int = DataBuffer::getUnsignedByte

    val FALSE_DECODER: (DataBuffer) -> Boolean = { false }

    val INT_DECODER: (DataBuffer) -> Long = DataBuffer::getUnsignedInt

    val SHORT_DECODER: (DataBuffer) -> Int = DataBuffer::getUnsignedShort

    val SIGNED_BYTE_DECODER: (DataBuffer) -> Int = DataBuffer::getByte

    val SIGNED_INT_DECODER: (DataBuffer) -> Int = DataBuffer::getInt

    val SIGNED_SHORT_DECODER: (DataBuffer) -> Int = DataBuffer::getShort

    val SMART_DECODER: (DataBuffer) -> Int = DataBuffer::getSmart

    val TRUE_DECODER: (DataBuffer) -> Boolean = { true }

    val UNSIGNED_TRI_BYTE_DECODER: (DataBuffer) -> Int = DataBuffer::getUnsignedTriByte

}
