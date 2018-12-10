package rs.emulate.legacy.config

import io.netty.buffer.ByteBuf

interface ConfigDecoder<T : Definition> {
    val entryName: String
    fun decode(id: Int, buffer: ByteBuf): T
}

interface ConfigEncoder<T : Definition> {
    val entryName: String
    fun encode(id: Int, definition: T): ByteBuf
}
