package rs.emulate.legacy.config.sequence

import rs.emulate.legacy.config.Config
import rs.emulate.legacy.config.ConfigDecoder
import rs.emulate.shared.util.DataBuffer

object SequenceDefinitionDecoder : ConfigDecoder<SequenceDefinition> {

    override val entryName: String = "seq"

    override fun decode(id: Int, buffer: DataBuffer): SequenceDefinition {
        val definition = SequenceDefinition(id)
        var opcode = buffer.getUnsignedByte()

        while (opcode != Config.DEFINITION_TERMINATOR) {
            definition.decode(buffer, opcode)
            opcode = buffer.getUnsignedByte()
        }

        return definition
    }

    private fun SequenceDefinition.decode(buffer: DataBuffer, opcode: Int) {
        when (opcode) {
            1 -> frameCollection = FrameCollection.decode(buffer) // TODO inline and remove FrameCollection
            2 -> loopOffset = buffer.getUnsignedShort()
            3 -> {
                val count = buffer.getUnsignedByte()
                val order = IntArray(count + 1)

                repeat(count) { index -> order[index] = buffer.getUnsignedByte() }
                order[count + 1] = 9_999_999

                interleaveOrder = order
            }
            4 -> stretches = true
            5 -> priority = buffer.getUnsignedByte()
            6 -> characterMainhand = buffer.getUnsignedShort()
            7 -> characterOffhand = buffer.getUnsignedShort()
            8 -> maximumLoops = buffer.getUnsignedByte()
            9 -> animatingPrecedence = buffer.getUnsignedByte()
            10 -> walkingPrecedence = buffer.getUnsignedByte()
            11 -> replayMode = buffer.getUnsignedByte()
            12 -> /* unused */ buffer.getUnsignedInt()
        }
    }

}
