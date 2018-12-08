package rs.emulate.legacy.config

import rs.emulate.shared.util.DataBuffer

interface ConfigDecoder<T> {
    val entryName: String
    fun decode(id: Int, buffer: DataBuffer): T
}

interface ConfigEncoder<T> {
    val entryName: String
    fun encode(id: Int, definition: T): DataBuffer
}
