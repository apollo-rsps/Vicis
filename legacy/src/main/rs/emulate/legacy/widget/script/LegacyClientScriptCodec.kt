package rs.emulate.legacy.widget.script

import rs.emulate.shared.util.DataBuffer
import java.util.ArrayList
import java.util.function.Consumer

/**
 * Encodes and decodes [LegacyClientScript]s stored in binary form.
 */
object LegacyClientScriptCodec {

    /**
     * Decodes a [List] of [LegacyClientScript]s from the specified [DataBuffer].
     */
    fun decode(buffer: DataBuffer): List<LegacyClientScript> {
        var count = buffer.getUnsignedByte()
        val operators = mutableListOf<RelationalOperator>()
        val defaults = IntArray(count)

        for (index in 0 until count) {
            operators += RelationalOperator.valueOf(buffer.getUnsignedByte())

            defaults[index] = buffer.getUnsignedShort()
        }

        count = buffer.getUnsignedByte()
        val scripts = mutableListOf<LegacyClientScript>()

        for (index in 0 until count) {
            val instructionCount = buffer.getUnsignedShort()
            val instructions = ArrayList<LegacyInstruction>(instructionCount)

            for (instruction in 0 until instructionCount) {
                val type = LegacyInstructionType.valueOf(buffer.getUnsignedShort())
                val operands = IntArray(type.operandCount)

                for (operand in operands.indices) {
                    operands[operand] = buffer.getUnsignedShort()
                }

                instructions.add(LegacyInstruction.create(type, operands))
            }

            scripts += LegacyClientScript(operators[index], defaults[index], instructions)
        }

        return scripts
    }

    /**
     * Encodes the [List] of [LegacyClientScript]s into a [DataBuffer].
     */
    fun encode(scripts: List<LegacyClientScript>): DataBuffer {
        val count = scripts.size

        val operators = DataBuffer.allocate(count * java.lang.Byte.BYTES)
        val defaults = DataBuffer.allocate(count * java.lang.Short.BYTES)
        val buffers = ArrayList<DataBuffer>(count)
        var size = count * (java.lang.Short.BYTES + java.lang.Byte.BYTES)

        for (index in 0 until count) {
            val script = scripts[index]

            operators.putByte(script.operator.value)
            defaults.putShort(script.default)

            val instructions = script.instructions
            val buffer = DataBuffer.allocate(instructions.size * java.lang.Short.BYTES)

            for (instruction in instructions) {
                buffer.putShort(instruction.type.toInteger())
                instruction.operands.forEach { buffer.putShort(it) }
            }

            size += buffer.position()
            buffers.add(buffer.flip())
        }

        val buffer = DataBuffer.allocate(size)
        buffer.put(operators.flip()).put(defaults.flip())
        buffers.forEach(Consumer<DataBuffer> { buffer.put(it) })

        return buffer.flip()
    }

}
