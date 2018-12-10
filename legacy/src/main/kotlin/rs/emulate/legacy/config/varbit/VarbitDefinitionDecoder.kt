package rs.emulate.legacy.config.varbit

import io.netty.buffer.ByteBuf
import rs.emulate.legacy.config.Config
import rs.emulate.legacy.config.ConfigDecoder
import rs.emulate.util.readAsciiString

object VarbitDefinitionDecoder : ConfigDecoder<VarbitDefinition> {

    override val entryName: String = "varbit"

    override fun decode(id: Int, buffer: ByteBuf): VarbitDefinition {
        val definition = VarbitDefinition(id)
        var opcode = buffer.readUnsignedByte().toInt()

        while (opcode != Config.DEFINITION_TERMINATOR) {
            definition.decode(buffer, opcode)
            opcode = buffer.readUnsignedByte().toInt()
        }

        return definition
    }

    private fun VarbitDefinition.decode(buffer: ByteBuf, opcode: Int) {
        when (opcode) {
            1 -> {
                varp = buffer.readUnsignedShort()
                low = buffer.readUnsignedByte().toInt()
                high = buffer.readUnsignedByte().toInt()
            }
            3 -> buffer.readUnsignedInt()
            4 -> buffer.readUnsignedInt()
            5 -> /* unused */ return
            10 -> buffer.readAsciiString()
        }
    }

}
