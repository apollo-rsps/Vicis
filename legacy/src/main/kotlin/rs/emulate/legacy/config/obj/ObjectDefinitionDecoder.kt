package rs.emulate.legacy.config.obj

import io.netty.buffer.ByteBuf
import rs.emulate.legacy.config.Config
import rs.emulate.legacy.config.ConfigDecoder
import rs.emulate.util.readAsciiString

object ObjectDefinitionDecoder : ConfigDecoder<ObjectDefinition> {

    override val entryName: String = "obj"

    override fun decode(id: Int, buffer: ByteBuf): ObjectDefinition {
        val definition = ObjectDefinition(id)
        var opcode = buffer.readUnsignedByte().toInt()

        while (opcode != Config.DEFINITION_TERMINATOR) {
            definition.decode(buffer, opcode)
            opcode = buffer.readUnsignedByte().toInt()
        }

        return definition
    }

    private fun ObjectDefinition.decode(buffer: ByteBuf, opcode: Int) {
        when (opcode) {
            1 -> modelId = buffer.readUnsignedShort()
            2 -> name = buffer.readAsciiString()
            3 -> description = buffer.readAsciiString()
            4 -> spriteScale = buffer.readUnsignedShort()
            5 -> spriteRoll = buffer.readUnsignedShort()
            6 -> spriteYaw = buffer.readUnsignedShort()
            7 -> spriteTranslateX = buffer.readUnsignedShort().let { if (it > 32767) it - 65536 else it }
            8 -> spriteTranslateY = buffer.readUnsignedShort().let { if (it > 32767) it - 65536 else it }
            10 -> /* unused */ buffer.readUnsignedShort()
            11 -> stackable = true
            12 -> value = buffer.readInt()
            16 -> members = true
            23 -> {
                primaryMaleEquipmentId = buffer.readUnsignedShort()
                maleTranslateY = buffer.readByte().toInt()
            }
            24 -> secondaryMaleEquipmentId = buffer.readUnsignedShort()
            25 -> {
                primaryFemaleEquipmentId = buffer.readUnsignedShort()
                femaleTranslateY = buffer.readByte().toInt()
            }
            26 -> secondaryFemaleEquipmentId = buffer.readUnsignedShort()
            in 30 until 35 -> groundActions[opcode - 30] = buffer.readAsciiString().let { if (it == "hidden") null else it }
            in 35 until 40 -> widgetActions[opcode - 35] = buffer.readAsciiString()
            40 -> {
                val count = buffer.readUnsignedByte().toInt()

                repeat(count) {
                    val original = buffer.readUnsignedShort()
                    val replacement = buffer.readUnsignedShort()

                    colours[original] = replacement
                }
            }
            78 -> tertiaryMaleEquipmentId = buffer.readUnsignedShort()
            79 -> tertiaryFemaleEquipmentId = buffer.readUnsignedShort()
            90 -> primaryMaleHeadId = buffer.readUnsignedShort()
            91 -> primaryFemaleHeadId = buffer.readUnsignedShort()
            92 -> secondaryMaleHeadId = buffer.readUnsignedShort()
            93 -> secondaryFemaleHeadId = buffer.readUnsignedShort()
            95 -> spriteCameraPitch = buffer.readUnsignedShort()
            97 -> noteInfoId = buffer.readUnsignedShort()
            98 -> noteTemplateId = buffer.readUnsignedShort()
            in 100 until 110 -> {
                val id = buffer.readUnsignedShort()
                val amount = buffer.readUnsignedShort()

                stacks[opcode - 100] = ObjectStack(amount, id)
            }
            110 -> groundScaleX = buffer.readUnsignedShort()
            111 -> groundScaleY = buffer.readUnsignedShort()
            112 -> groundScaleZ = buffer.readUnsignedShort()
            113 -> brightness = buffer.readByte().toInt()
            114 -> diffusion = buffer.readByte().toInt()
            115 -> team = buffer.readUnsignedByte().toInt()
        }
    }

}
