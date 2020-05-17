package rs.emulate.common.config.varbit

import io.netty.buffer.ByteBuf
import rs.emulate.common.config.Config
import rs.emulate.common.config.ConfigDecoder
import rs.emulate.util.readAsciiString

object VarbitDefinitionDecoder : ConfigDecoder<VarbitDefinition> {

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
