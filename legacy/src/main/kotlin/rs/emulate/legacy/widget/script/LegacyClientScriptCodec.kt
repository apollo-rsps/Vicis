package rs.emulate.legacy.widget.script

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import java.util.ArrayList

/**
 * Encodes and decodes [LegacyClientScript]s stored in binary form.
 */
object LegacyClientScriptCodec {

    /**
     * Decodes a [List] of [LegacyClientScript]s from the specified [ByteBuf].
     */
    fun decode(buffer: ByteBuf): List<LegacyClientScript> {
        var count = buffer.readUnsignedByte().toInt()
        val operators = mutableListOf<RelationalOperator>()
        val defaults = IntArray(count)

        for (index in 0 until count) {
            operators += RelationalOperator.valueOf(buffer.readUnsignedByte().toInt())

            defaults[index] = buffer.readUnsignedShort()
        }

        count = buffer.readUnsignedByte().toInt()
        val scripts = mutableListOf<LegacyClientScript>()

        for (index in 0 until count) {
            val instructionCount = buffer.readUnsignedShort()
            val instructions = ArrayList<LegacyInstruction>(instructionCount)

            var read = 0
            while (read < instructionCount) {
                val type = LegacyInstructionType.valueOf(buffer.readUnsignedShort())
                val operands = IntArray(type.operandCount) { buffer.readUnsignedShort() }

                read += java.lang.Short.BYTES * (1 + type.operandCount)
                instructions.add(LegacyInstruction.create(type, operands))
            }

            scripts += LegacyClientScript(operators[index], defaults[index], instructions)
        }

        return scripts
    }

    /**
     * Encodes the [List] of [LegacyClientScript]s into a [ByteBuf].
     */
    fun encode(scripts: List<LegacyClientScript>): ByteBuf {
        val count = scripts.size

        val operators = Unpooled.buffer(count * java.lang.Byte.BYTES)
        val defaults = Unpooled.buffer(count * java.lang.Short.BYTES)
        val buffers = ArrayList<ByteBuf>(count)
        var size = count * (java.lang.Short.BYTES + java.lang.Byte.BYTES)

        for (script in scripts) {
            operators.writeByte(script.operator.value)
            defaults.writeShort(script.default)

            val instructions = script.instructions
            val buffer = Unpooled.buffer(instructions.size * java.lang.Short.BYTES)

            for (instruction in instructions) {
                buffer.writeShort(instruction.type.toInteger())
                instruction.operands.forEach { buffer.writeShort(it) }
            }

            size += buffer.readableBytes()
            buffers += buffer
        }

        return Unpooled.compositeBuffer(2 + buffers.size).apply {
            addComponents(operators, defaults)
            addComponents(buffers)
        }
    }

}
