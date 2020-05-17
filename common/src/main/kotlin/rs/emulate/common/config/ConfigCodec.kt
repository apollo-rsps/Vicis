package rs.emulate.common.config

import io.netty.buffer.ByteBuf

interface ConfigDecoder<T : Definition> {
    fun decode(id: Int, buffer: ByteBuf): T
}

interface ConfigEncoder<T : Definition> {
    val entryName: String
    fun encode(id: Int, definition: T): ByteBuf
}
