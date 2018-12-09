package rs.emulate.legacy.config.varbit

import rs.emulate.legacy.config.Config
import rs.emulate.legacy.config.ConfigDecoder
import rs.emulate.util.getAsciiString
import rs.emulate.util.getUnsignedByte
import rs.emulate.util.getUnsignedInt
import rs.emulate.util.getUnsignedShort
import java.nio.ByteBuffer

object VarbitDefinitionDecoder : ConfigDecoder<VarbitDefinition> {

    override val entryName: String = "varbit"

    override fun decode(id: Int, buffer: ByteBuffer): VarbitDefinition {
        val definition = VarbitDefinition(id)
        var opcode = buffer.getUnsignedByte()

        while (opcode != Config.DEFINITION_TERMINATOR) {
            definition.decode(buffer, opcode)
            opcode = buffer.getUnsignedByte()
        }

        return definition
    }

    private fun VarbitDefinition.decode(buffer: ByteBuffer, opcode: Int) {
        when (opcode) {
            1 -> {
                varp = buffer.getUnsignedShort()
                low = buffer.getUnsignedByte()
                high = buffer.getUnsignedByte()
            }
            3 -> buffer.getUnsignedInt()
            4 -> buffer.getUnsignedInt()
            5 -> /* unused */ return
            10 -> buffer.getAsciiString()
        }
    }

}
