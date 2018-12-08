package rs.emulate.legacy.config.varp

import rs.emulate.legacy.config.Config
import rs.emulate.legacy.config.ConfigDecoder
import rs.emulate.shared.util.getAsciiString
import rs.emulate.shared.util.getUnsignedByte
import rs.emulate.shared.util.getUnsignedInt
import rs.emulate.shared.util.getUnsignedShort
import java.nio.ByteBuffer

object VarpDefinitionDecoder : ConfigDecoder<VarpDefinition> {

    override val entryName: String = "varp"

    override fun decode(id: Int, buffer: ByteBuffer): VarpDefinition {
        val definition = VarpDefinition(id)
        var opcode = buffer.getUnsignedByte()

        while (opcode != Config.DEFINITION_TERMINATOR) {
            definition.decode(buffer, opcode)
            opcode = buffer.getUnsignedByte()
        }

        return definition
    }

    private fun VarpDefinition.decode(buffer: ByteBuffer, opcode: Int) {
        when (opcode) {
            1 -> buffer.getUnsignedByte()
            2 -> buffer.getUnsignedByte()
            5 -> parameter = buffer.getUnsignedShort()
            7 -> buffer.getUnsignedInt()
            10 -> buffer.getAsciiString()
            12 -> buffer.getUnsignedInt()
        }
    }

}
