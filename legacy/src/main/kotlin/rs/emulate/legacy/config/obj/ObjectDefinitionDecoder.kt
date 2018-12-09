package rs.emulate.legacy.config.obj

import rs.emulate.legacy.config.Config
import rs.emulate.legacy.config.ConfigDecoder
import rs.emulate.util.getAsciiString
import rs.emulate.util.getByte
import rs.emulate.util.getUnsignedByte
import rs.emulate.util.getUnsignedShort
import java.nio.ByteBuffer

object ObjectDefinitionDecoder : ConfigDecoder<ObjectDefinition> {

    override val entryName: String = "seq"

    override fun decode(id: Int, buffer: ByteBuffer): ObjectDefinition {
        val definition = ObjectDefinition(id)
        var opcode = buffer.getUnsignedByte()

        while (opcode != Config.DEFINITION_TERMINATOR) {
            definition.decode(buffer, opcode)
            opcode = buffer.getUnsignedByte()
        }

        return definition
    }

    private fun ObjectDefinition.decode(buffer: ByteBuffer, opcode: Int) {
        when (opcode) {
            1 -> modelId = buffer.getUnsignedShort()
            2 -> name = buffer.getAsciiString()
            3 -> description = buffer.getAsciiString()
            4 -> spriteScale = buffer.getUnsignedShort()
            5 -> spriteRoll = buffer.getUnsignedShort()
            6 -> spriteYaw = buffer.getUnsignedShort()
            7 -> spriteTranslateX = buffer.getUnsignedShort().let { if (it > 32767) it - 65536 else it }
            8 -> spriteTranslateY = buffer.getUnsignedShort().let { if (it > 32767) it - 65536 else it }
            10 -> /* unused */ buffer.getUnsignedShort()
            11 -> stackable = true
            12 -> value = buffer.getInt()
            16 -> members = true
            23 -> {
                primaryMaleEquipmentId = buffer.getUnsignedShort()
                maleTranslateY = buffer.getByte()
            }
            24 -> secondaryMaleEquipmentId = buffer.getUnsignedShort()
            25 -> {
                primaryFemaleEquipmentId = buffer.getUnsignedShort()
                femaleTranslateY = buffer.getByte()
            }
            26 -> secondaryFemaleEquipmentId = buffer.getUnsignedShort()
            in 30 until 35 -> groundActions[opcode - 30] = buffer.getAsciiString().let { if (it == "hidden") null else it }
            in 35 until 40 -> widgetActions[opcode - 35] = buffer.getAsciiString()
            40 -> {
                val count = buffer.getUnsignedByte()

                repeat(count) {
                    val original = buffer.getUnsignedShort()
                    val replacement = buffer.getUnsignedShort()

                    colours[original] = replacement
                }
            }
            78 -> tertiaryMaleEquipmentId = buffer.getUnsignedShort()
            79 -> tertiaryFemaleEquipmentId = buffer.getUnsignedShort()
            90 -> primaryMaleHeadId = buffer.getUnsignedShort()
            91 -> primaryFemaleHeadId = buffer.getUnsignedShort()
            92 -> secondaryMaleHeadId = buffer.getUnsignedShort()
            93 -> secondaryFemaleHeadId = buffer.getUnsignedShort()
            95 -> spriteCameraPitch = buffer.getUnsignedShort()
            97 -> noteInfoId = buffer.getUnsignedShort()
            98 -> noteTemplateId = buffer.getUnsignedShort()
            in 100 until 110 -> {
                val id = buffer.getUnsignedShort()
                val amount = buffer.getUnsignedShort()

                stacks[opcode - 100] = ObjectStack(amount, id)
            }
            110 -> groundScaleX = buffer.getUnsignedShort()
            111 -> groundScaleY = buffer.getUnsignedShort()
            112 -> groundScaleZ = buffer.getUnsignedShort()
            113 -> brightness = buffer.getByte()
            114 -> diffusion = buffer.getByte()
            115 -> team = buffer.getUnsignedByte()
        }
    }

}
