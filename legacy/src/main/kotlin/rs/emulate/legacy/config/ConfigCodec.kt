package rs.emulate.legacy.config

import java.nio.ByteBuffer

interface ConfigDecoder<T> {
    val entryName: String
    fun decode(id: Int, buffer: ByteBuffer): T
}

interface ConfigEncoder<T> {
    val entryName: String
    fun encode(id: Int, definition: T): ByteBuffer
}
