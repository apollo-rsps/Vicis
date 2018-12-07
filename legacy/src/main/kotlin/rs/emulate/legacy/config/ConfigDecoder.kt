package rs.emulate.legacy.config

import rs.emulate.shared.util.DataBuffer

interface ConfigDecoder<T> {
    val entryName: String
    fun decode(id: Int, buffer: DataBuffer): T

    companion object {
        const val DEFINITION_TERMINATOR = 0
    }
}
