package rs.emulate.legacy.config

import java.nio.ByteBuffer

interface ConfigDecoder<T : Definition> {
    val entryName: String
    fun decode(id: Int, buffer: ByteBuffer): T
}

interface ConfigEncoder<T : Definition> {
    val entryName: String
    fun encode(id: Int, definition: T): ByteBuffer
}
