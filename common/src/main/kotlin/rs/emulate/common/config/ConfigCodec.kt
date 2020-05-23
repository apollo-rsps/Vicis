package rs.emulate.common.config

import io.netty.buffer.ByteBuf
import rs.emulate.common.CacheItemDecoder

interface ConfigDecoder<T : Definition> : CacheItemDecoder<Int, T>

interface ConfigEncoder<T : Definition> {
    val entryName: String
    fun encode(id: Int, definition: T): ByteBuf
}
