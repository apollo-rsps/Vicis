package rs.emulate.legacy.config.varbit

import rs.emulate.legacy.config.ConfigDecoder
import rs.emulate.shared.util.DataBuffer

object VarbitDefinitionDecoder : ConfigDecoder<VarbitDefinition> {

    override val entryName: String = "varbit"

    override fun decode(id: Int, buffer: DataBuffer): VarbitDefinition {
        val definition = VarbitDefinition(id)
        var opcode = buffer.getUnsignedByte()

        while (opcode != ConfigDecoder.DEFINITION_TERMINATOR) {
            definition.decode(buffer, opcode)
            opcode = buffer.getUnsignedByte()
        }

        return definition
    }

    private fun VarbitDefinition.decode(buffer: DataBuffer, opcode: Int) {
        when (opcode) {
            1 -> {
                varp = buffer.getUnsignedShort()
                low = buffer.getUnsignedByte()
                high = buffer.getUnsignedByte()
            }
            3 -> buffer.getUnsignedInt()
            4 -> buffer.getUnsignedInt()
            5 -> /* unused */ return
            10 -> buffer.getString()
        }
    }

}
