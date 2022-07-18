package rs.emulate.common.config.location

import io.netty.buffer.ByteBuf
import rs.emulate.common.config.Config
import rs.emulate.common.config.ConfigDecoder
import rs.emulate.common.config.npc.MorphismSet
import rs.emulate.util.readAsciiString
import rs.emulate.util.readCString

object LocationDefinitionDecoder : ConfigDecoder<LocationDefinition> {

    override fun decode(id: Int, buffer: ByteBuf): LocationDefinition {
        val definition = LocationDefinition(id)
        var opcode = buffer.readUnsignedByte().toInt()

        while (opcode != Config.DEFINITION_TERMINATOR) {
            definition.decode(buffer, opcode)
            opcode = buffer.readUnsignedByte().toInt()
        }

        definition.apply {
            if (interactive == -1) {
                interactive = if (actions.isNotEmpty()) {
                    1
                } else if (models.isNotEmpty() && (modelTypes.isEmpty() || modelTypes.first() == 10)) {
                    1
                } else {
                    0
                }
            }

            if (hollow) {
                solid = false
                impenetrable = false
            }

            if (supportsItems == -1) {
                supportsItems = if (solid) 1 else 0
            }
        }

        return definition
    }

    private fun LocationDefinition.decode(buffer: ByteBuf, opcode: Int) {
        when (opcode) {
            1 -> {
                val count = buffer.readUnsignedByte().toInt()
                models = IntArray(count)
                modelTypes = IntArray(count)

                repeat(count) { index ->
                    models[index] = buffer.readUnsignedShort()
                    modelTypes[index] = buffer.readUnsignedByte().toInt()
                }
            }
            2 -> name = buffer.readAsciiString()
            3 -> description = buffer.readAsciiString()
            5 -> {
                val count = buffer.readUnsignedByte().toInt()
                models = IntArray(count) { buffer.readUnsignedShort() }
            }
            14 -> width = buffer.readUnsignedByte().toInt()
            15 -> length = buffer.readUnsignedByte().toInt()
            17 -> solid = false
            18 -> impenetrable = false
            19 -> interactive = buffer.readUnsignedByte().toInt()
            21 -> contourGround = true
            22 -> delayShading = true
            23 -> occludes = true
            24 -> sequenceId = buffer.readUnsignedShort().let { if (it == 65535) -1 else it }
            28 -> decorDisplacement = buffer.readUnsignedByte().toInt()
            29 -> brightness = buffer.readByte().toInt()
            in 30 until 39 -> actions[opcode - 30] = buffer.readAsciiString().let { if (it == "hidden") null else it }
            39 -> diffusion = buffer.readByte().toInt()
            40 -> {
                val count = buffer.readUnsignedByte().toInt()

                repeat(count) {
                    val original = buffer.readUnsignedShort()
                    val replacement = buffer.readUnsignedShort()

                    colours[original] = replacement
                }
            }
            60 -> minimapFunction = buffer.readUnsignedShort()
            62 -> inverted = true
            64 -> castShadow = false
            65 -> scaleX = buffer.readUnsignedShort()
            66 -> scaleY = buffer.readUnsignedShort()
            67 -> scaleZ = buffer.readUnsignedShort()
            68 -> mapsceneId = buffer.readUnsignedShort()
            69 -> surroundings = buffer.readUnsignedByte().toInt()
            70 -> translateX = buffer.readShort().toInt()
            71 -> translateY = buffer.readShort().toInt()
            72 -> translateZ = buffer.readShort().toInt()
            73 -> obstructsGround = true
            74 -> hollow = true
            75 -> supportsItems = buffer.readUnsignedByte().toInt()
            77 -> morphisms = MorphismSet.decode(buffer)
        }
    }

}

