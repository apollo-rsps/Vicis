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
        val predicateCount = buffer.readUnsignedByte().toInt()
        val operators = ArrayList<RelationalOperator>(predicateCount)
        val comparates = IntArray(predicateCount)

        for (index in 0 until predicateCount) {
            operators += RelationalOperator.valueOf(buffer.readUnsignedByte().toInt())
            comparates[index] = buffer.readUnsignedShort()
        }

        val scriptCount = buffer.readUnsignedByte().toInt()
        val scripts = ArrayList<LegacyClientScript>(scriptCount)

        for (index in 0 until scriptCount) {
            val instructionCount = buffer.readUnsignedShort()
            val instructions = ArrayList<LegacyInstruction>(instructionCount)

            var read = 0
            while (read < instructionCount) {
                val type = LegacyInstructionType.valueOf(buffer.readUnsignedShort())
                val operands = IntArray(type.operandCount) { buffer.readUnsignedShort() }

                read += (1 + type.operandCount)
                instructions.add(LegacyInstruction.create(type, operands))
            }

            scripts += if (index < predicateCount) {
                LegacyClientScript.Predicate(instructions, operators[index], comparates[index])
            } else {
                LegacyClientScript.Mathematical(instructions)
            }
        }

        return scripts
    }

    /**
     * Encodes the [List] of [LegacyClientScript]s into a [ByteBuf].
     */
    fun encode(scripts: List<LegacyClientScript>): ByteBuf {
        val count = scripts.size

        val operators = Unpooled.buffer(count * java.lang.Byte.BYTES)
        val comparates = Unpooled.buffer(count * java.lang.Short.BYTES)
        val buffers = ArrayList<ByteBuf>(count)
        var size = count * (java.lang.Short.BYTES + java.lang.Byte.BYTES)

        for (script in scripts) {
            if (script is LegacyClientScript.Predicate) {
                operators.writeByte(script.operator.value)
                comparates.writeShort(script.comparate)
            }

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
            addComponents(operators, comparates)
            addComponents(buffers)
        }
    }

}
