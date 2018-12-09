package rs.emulate.legacy.config.kit

import rs.emulate.legacy.config.Config
import rs.emulate.legacy.config.ConfigDecoder
import rs.emulate.util.getUnsignedByte
import rs.emulate.util.getUnsignedShort
import java.nio.ByteBuffer

object IdentikitDefinitionDecoder : ConfigDecoder<IdentikitDefinition> {

    override val entryName: String = "idk"

    override fun decode(id: Int, buffer: ByteBuffer): IdentikitDefinition {
        val definition = IdentikitDefinition(id)
        var opcode = buffer.getUnsignedByte()

        while (opcode != Config.DEFINITION_TERMINATOR) {
            definition.decode(buffer, opcode)
            opcode = buffer.getUnsignedByte()
        }

        return definition
    }

    private fun IdentikitDefinition.decode(buffer: ByteBuffer, opcode: Int) {
        when (opcode) {
            1 -> part = buffer.getUnsignedByte()
            2 -> {
                val count = buffer.getUnsignedByte()
                models = IntArray(count) { buffer.getUnsignedShort() }
            }
            3 -> playerDesignStyle = true
            in 40 until 50 -> originalColours[opcode - 40] = buffer.getUnsignedShort()
            in 50 until 60 -> replacementColours[opcode - 50] = buffer.getUnsignedShort()
            in 60 until 70 -> widgetModels[opcode - 50] = buffer.getUnsignedShort()
        }
    }

}
