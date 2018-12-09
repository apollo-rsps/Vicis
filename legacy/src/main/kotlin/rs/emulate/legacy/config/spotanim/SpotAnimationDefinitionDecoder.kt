package rs.emulate.legacy.config.spotanim

import rs.emulate.legacy.config.Config
import rs.emulate.legacy.config.ConfigDecoder
import rs.emulate.util.getUnsignedByte
import rs.emulate.util.getUnsignedShort
import java.nio.ByteBuffer

object SpotAnimationDefinitionDecoder : ConfigDecoder<SpotAnimationDefinition> {

    override val entryName: String = "spotanim"

    override fun decode(id: Int, buffer: ByteBuffer): SpotAnimationDefinition {
        val definition = SpotAnimationDefinition(id)
        var opcode = buffer.getUnsignedByte()

        while (opcode != Config.DEFINITION_TERMINATOR) {
            definition.decode(buffer, opcode)
            opcode = buffer.getUnsignedByte()
        }

        return definition
    }

    private fun SpotAnimationDefinition.decode(buffer: ByteBuffer, opcode: Int) {
        when (opcode) {
            1 -> model = buffer.getUnsignedShort()
            2 -> sequenceId = buffer.getUnsignedShort()
            4 -> planarScale = buffer.getUnsignedShort()
            5 -> verticalScale = buffer.getUnsignedShort()
            6 -> orientation = buffer.getUnsignedShort()
            7 -> modelBrightness = buffer.getUnsignedByte()
            8 -> modelDiffusion = buffer.getUnsignedByte()
            in 40 until 50 -> originalColours[opcode - 40] = buffer.getUnsignedShort()
            in 50 until 60 -> replacementColours[opcode - 50] = buffer.getUnsignedShort()
        }
    }

}
