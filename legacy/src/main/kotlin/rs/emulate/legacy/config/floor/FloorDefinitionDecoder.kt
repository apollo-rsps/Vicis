package rs.emulate.legacy.config.floor

import rs.emulate.legacy.config.ConfigDecoder
import rs.emulate.shared.util.DataBuffer

object FloorDefinitionDecoder : ConfigDecoder<FloorDefinition> {

    override val entryName: String = "flo"

    override fun decode(id: Int, buffer: DataBuffer): FloorDefinition {
        val definition = FloorDefinition(id)
        var opcode = buffer.getUnsignedByte()

        while (opcode != ConfigDecoder.DEFINITION_TERMINATOR) {
            definition.decode(buffer, opcode)
            opcode = buffer.getUnsignedByte()
        }

        return definition
    }

    private fun FloorDefinition.decode(buffer: DataBuffer, opcode: Int) {
        when (opcode) {
            1 -> rgb = buffer.getUnsignedTriByte()
            2 -> texture = buffer.getUnsignedByte()
            3 -> return /* unused */
            5 -> occludes = false
            6 -> buffer.getString()
            7 -> minimapColour = buffer.getUnsignedTriByte()
        }
    }

}
