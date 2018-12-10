package rs.emulate.legacy.config.npc

import io.netty.buffer.ByteBuf
import rs.emulate.legacy.config.Config
import rs.emulate.legacy.config.ConfigDecoder
import rs.emulate.util.readAsciiString

object NpcDefinitionDecoder : ConfigDecoder<NpcDefinition> {

    override val entryName: String = "npc"

    override fun decode(id: Int, buffer: ByteBuf): NpcDefinition {
        val definition = NpcDefinition(id)
        var opcode = buffer.readUnsignedByte().toInt()

        while (opcode != Config.DEFINITION_TERMINATOR) {
            definition.decode(buffer, opcode)
            opcode = buffer.readUnsignedByte().toInt()
        }

        return definition
    }

    private fun NpcDefinition.decode(buffer: ByteBuf, opcode: Int) {
        when (opcode) {
            1 -> {
                val count = buffer.readUnsignedByte().toInt()
                models = IntArray(count) { buffer.readUnsignedShort() }
            }
            2 -> name = buffer.readAsciiString()
            3 -> description = buffer.readAsciiString()
            12 -> size = buffer.readByte().toInt()
            13 -> standingSequence = buffer.readUnsignedShort()
            14 -> walkingSequence = buffer.readUnsignedShort()
            17 -> movementSequences = MovementAnimationSet.decode(buffer)
            in 30..34 -> actions[opcode - 30] = buffer.readAsciiString()
            40 -> {
                val count = buffer.readUnsignedByte().toInt()

                repeat(count) {
                    val original = buffer.readUnsignedShort()
                    val replacement = buffer.readUnsignedShort()

                    replacementColours[original] = replacement
                }
            }
            60 -> {
                val count = buffer.readUnsignedByte().toInt()
                widgetModels = IntArray(count) { buffer.readUnsignedShort() }
            }
            93 -> visibleOnMinimap = false
            95 -> combatLevel = buffer.readUnsignedShort()
            97 -> planarScale = buffer.readUnsignedShort()
            98 -> verticalScale = buffer.readUnsignedShort()
            99 -> priorityRender = true
            100 -> brightness = buffer.readByte().toInt()
            101 -> diffusion = buffer.readByte().toInt()
            102 -> headIconId = buffer.readUnsignedShort()
            103 -> defaultOrientation = buffer.readUnsignedShort()
            106 -> morphisms = MorphismSet.decode(buffer)
            107 -> clickable = false
        }
    }

}
