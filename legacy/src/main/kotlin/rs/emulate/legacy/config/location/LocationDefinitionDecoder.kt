package rs.emulate.legacy.config.location

import rs.emulate.legacy.config.Config
import rs.emulate.legacy.config.ConfigDecoder
import rs.emulate.legacy.config.npc.MorphismSet
import rs.emulate.shared.util.getAsciiString
import rs.emulate.shared.util.getByte
import rs.emulate.shared.util.getUnsignedByte
import rs.emulate.shared.util.getUnsignedShort
import java.nio.ByteBuffer

object LocationDefinitionDecoder : ConfigDecoder<LocationDefinition> {

    override val entryName: String = "loc"

    override fun decode(id: Int, buffer: ByteBuffer): LocationDefinition {
        val definition = LocationDefinition(id)
        var opcode = buffer.getUnsignedByte()

        while (opcode != Config.DEFINITION_TERMINATOR) {
            definition.decode(buffer, opcode)
            opcode = buffer.getUnsignedByte()
        }

        definition.apply {
            if (interactive == -1) {
                interactive = if (actions.isNotEmpty()) {
                    1
                } else if (models.isNotEmpty() && (modelTypes.isEmpty() || modelTypes.first() == 10)) 1 else 0
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

    private fun LocationDefinition.decode(buffer: ByteBuffer, opcode: Int) {
        when (opcode) {
            1 -> {
                val count = buffer.getUnsignedByte()
                models = IntArray(count)
                modelTypes = IntArray(count)

                repeat(count) { index ->
                    models[index] = buffer.getUnsignedShort()
                    modelTypes[index] = buffer.getUnsignedByte()
                }
            }
            2 -> name = buffer.getAsciiString()
            3 -> description = buffer.getAsciiString()
            5 -> {
                val count = buffer.getUnsignedByte()
                models = IntArray(count) { buffer.getUnsignedShort() }
            }
            14 -> width = buffer.getUnsignedByte()
            15 -> length = buffer.getUnsignedByte()
            17 -> solid = false
            18 -> impenetrable = false
            19 -> interactive = buffer.getUnsignedByte()
            21 -> contourGround = true
            22 -> delayShading = true
            23 -> occludes = true
            24 -> sequenceId = buffer.getUnsignedShort().let { if (it == 65535) -1 else it }
            28 -> decorDisplacement = buffer.getUnsignedByte()
            29 -> brightness = buffer.getByte()
            in 30 until 39 -> actions[opcode - 30] = buffer.getAsciiString().let { if (it == "hidden") null else it }
            39 -> diffusion = buffer.getByte()
            40 -> {
                val count = buffer.getUnsignedByte()

                repeat(count) {
                    val original = buffer.getUnsignedShort()
                    val replacement = buffer.getUnsignedShort()

                    colours[original] = replacement
                }
            }
            60 -> minimapFunction = buffer.getUnsignedShort()
            62 -> inverted = true
            64 -> castShadow = false
            65 -> scaleX = buffer.getUnsignedShort()
            66 -> scaleY = buffer.getUnsignedShort()
            67 -> scaleZ = buffer.getUnsignedShort()
            68 -> mapsceneId = buffer.getUnsignedShort()
            69 -> surroundings = buffer.getUnsignedByte()
            70 -> translateX = buffer.getShort().toInt()
            71 -> translateY = buffer.getShort().toInt()
            72 -> translateZ = buffer.getShort().toInt()
            73 -> obstructsGround = true
            74 -> hollow = true
            75 -> supportsItems = buffer.getUnsignedByte()
            77 -> morphisms = MorphismSet.decode(buffer)
        }
    }

}

