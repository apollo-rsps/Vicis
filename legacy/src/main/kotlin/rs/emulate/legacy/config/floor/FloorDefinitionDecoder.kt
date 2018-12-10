package rs.emulate.legacy.config.floor

import io.netty.buffer.ByteBuf
import rs.emulate.legacy.config.Config
import rs.emulate.legacy.config.ConfigDecoder
import rs.emulate.util.readAsciiString
import rs.emulate.util.readUnsignedTriByte

object FloorDefinitionDecoder : ConfigDecoder<FloorDefinition> {

    override val entryName: String = "flo"

    override fun decode(id: Int, buffer: ByteBuf): FloorDefinition {
        val definition = FloorDefinition(id)
        var opcode = buffer.readUnsignedByte().toInt()

        while (opcode != Config.DEFINITION_TERMINATOR) {
            definition.decode(buffer, opcode)
            opcode = buffer.readUnsignedByte().toInt()
        }

        return definition
    }

    private fun FloorDefinition.decode(buffer: ByteBuf, opcode: Int) {
        when (opcode) {
            1 -> rgb = buffer.readUnsignedTriByte()
            2 -> texture = buffer.readUnsignedByte().toInt()
            3 -> return /* unused */
            5 -> occludes = false
            6 -> buffer.readAsciiString()
            7 -> minimapColour = buffer.readUnsignedTriByte()
        }
    }

}
