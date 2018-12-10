package rs.emulate.legacy.config.spotanim

import io.netty.buffer.ByteBuf
import rs.emulate.legacy.config.Config
import rs.emulate.legacy.config.ConfigDecoder

object SpotAnimationDefinitionDecoder : ConfigDecoder<SpotAnimationDefinition> {

    override val entryName: String = "spotanim"

    override fun decode(id: Int, buffer: ByteBuf): SpotAnimationDefinition {
        val definition = SpotAnimationDefinition(id)
        var opcode = buffer.readUnsignedByte().toInt()

        while (opcode != Config.DEFINITION_TERMINATOR) {
            definition.decode(buffer, opcode)
            opcode = buffer.readUnsignedByte().toInt()
        }

        return definition
    }

    private fun SpotAnimationDefinition.decode(buffer: ByteBuf, opcode: Int) {
        when (opcode) {
            1 -> model = buffer.readUnsignedShort()
            2 -> sequenceId = buffer.readUnsignedShort()
            4 -> planarScale = buffer.readUnsignedShort()
            5 -> verticalScale = buffer.readUnsignedShort()
            6 -> orientation = buffer.readUnsignedShort()
            7 -> modelBrightness = buffer.readUnsignedByte().toInt()
            8 -> modelDiffusion = buffer.readUnsignedByte().toInt()
            in 40 until 50 -> originalColours[opcode - 40] = buffer.readUnsignedShort()
            in 50 until 60 -> replacementColours[opcode - 50] = buffer.readUnsignedShort()
        }
    }

}
