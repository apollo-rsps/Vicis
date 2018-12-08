package rs.emulate.legacy.config.varp

import rs.emulate.legacy.config.Config
import rs.emulate.legacy.config.ConfigDecoder
import rs.emulate.shared.util.DataBuffer

object VarpDefinitionDecoder : ConfigDecoder<VarpDefinition> {

    override val entryName: String = "varp"

    override fun decode(id: Int, buffer: DataBuffer): VarpDefinition {
        val definition = VarpDefinition(id)
        var opcode = buffer.getUnsignedByte()

        while (opcode != Config.DEFINITION_TERMINATOR) {
            definition.decode(buffer, opcode)
            opcode = buffer.getUnsignedByte()
        }

        return definition
    }

    private fun VarpDefinition.decode(buffer: DataBuffer, opcode: Int) {
        when (opcode) {
            1 -> buffer.getUnsignedByte()
            2 -> buffer.getUnsignedByte()
            5 -> parameter = buffer.getUnsignedShort()
            7 -> buffer.getUnsignedInt()
            10 -> buffer.getString()
            12 -> buffer.getUnsignedInt()
        }
    }

}
