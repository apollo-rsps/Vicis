package rs.emulate.legacy.config.animation

import rs.emulate.legacy.config.ConfigDecoder
import rs.emulate.shared.util.DataBuffer

object AnimationDefinitionDecoder : ConfigDecoder<AnimationDefinition> {

    override val entryName: String = "seq"

    override fun decode(id: Int, buffer: DataBuffer): AnimationDefinition {
        val definition = AnimationDefinition(id)
        var opcode = buffer.getUnsignedByte()

        while (opcode != ConfigDecoder.DEFINITION_TERMINATOR) {
            definition.decode(buffer, opcode)
            opcode = buffer.getUnsignedByte()
        }

        return definition
    }

    private fun AnimationDefinition.decode(buffer: DataBuffer, opcode: Int) {
        when (opcode) {
            1 -> frameCollection = FrameCollection.decode(buffer)
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
