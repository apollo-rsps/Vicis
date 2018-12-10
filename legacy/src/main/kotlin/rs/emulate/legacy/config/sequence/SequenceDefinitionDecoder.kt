package rs.emulate.legacy.config.sequence

import io.netty.buffer.ByteBuf
import rs.emulate.legacy.config.Config
import rs.emulate.legacy.config.ConfigDecoder

object SequenceDefinitionDecoder : ConfigDecoder<SequenceDefinition> {

    override val entryName: String = "seq"

    override fun decode(id: Int, buffer: ByteBuf): SequenceDefinition {
        val definition = SequenceDefinition(id)
        var opcode = buffer.readUnsignedByte().toInt()

        while (opcode != Config.DEFINITION_TERMINATOR) {
            definition.decode(buffer, opcode)
            opcode = buffer.readUnsignedByte().toInt()
        }

        return definition
    }

    private fun SequenceDefinition.decode(buffer: ByteBuf, opcode: Int) {
        when (opcode) {
            1 -> frameCollection = FrameCollection.decode(buffer) // TODO inline and remove FrameCollection
            2 -> loopOffset = buffer.readUnsignedShort()
            3 -> {
                val count = buffer.readUnsignedByte().toInt()
                val order = IntArray(count + 1)

                repeat(count) { index -> order[index] = buffer.readUnsignedByte().toInt() }
                order[count + 1] = 9_999_999

                interleaveOrder = order
            }
            4 -> stretches = true
            5 -> priority = buffer.readUnsignedByte().toInt()
            6 -> characterMainhand = buffer.readUnsignedShort()
            7 -> characterOffhand = buffer.readUnsignedShort()
            8 -> maximumLoops = buffer.readUnsignedByte().toInt()
            9 -> animatingPrecedence = buffer.readUnsignedByte().toInt()
            10 -> walkingPrecedence = buffer.readUnsignedByte().toInt()
            11 -> replayMode = buffer.readUnsignedByte().toInt()
            12 -> /* unused */ buffer.readUnsignedInt()
        }
    }

}
