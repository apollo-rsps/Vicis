package rs.emulate.legacy.widget.script

import rs.emulate.shared.util.getUnsignedByte
import rs.emulate.shared.util.getUnsignedShort
import rs.emulate.shared.util.putByte
import java.nio.ByteBuffer
import java.util.ArrayList

/**
 * Encodes and decodes [LegacyClientScript]s stored in binary form.
 */
object LegacyClientScriptCodec {

    /**
     * Decodes a [List] of [LegacyClientScript]s from the specified [ByteBuffer].
     */
    fun decode(buffer: ByteBuffer): List<LegacyClientScript> {
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

            var read = 0
            while (read < instructionCount) {
                val type = LegacyInstructionType.valueOf(buffer.getUnsignedShort())
                val operands = IntArray(type.operandCount) { buffer.getUnsignedShort() }

                read += java.lang.Short.BYTES * (1 + type.operandCount)
                instructions.add(LegacyInstruction.create(type, operands))
            }

            scripts += LegacyClientScript(operators[index], defaults[index], instructions)
        }

        return scripts
    }

    /**
     * Encodes the [List] of [LegacyClientScript]s into a [ByteBuffer].
     */
    fun encode(scripts: List<LegacyClientScript>): ByteBuffer {
        val count = scripts.size

        val operators = ByteBuffer.allocate(count * java.lang.Byte.BYTES)
        val defaults = ByteBuffer.allocate(count * java.lang.Short.BYTES)
        val buffers = ArrayList<ByteBuffer>(count)
        var size = count * (java.lang.Short.BYTES + java.lang.Byte.BYTES)

        for (script in scripts) {
            operators.putByte(script.operator.value)
            defaults.putShort(script.default.toShort())

            val instructions = script.instructions
            val buffer = ByteBuffer.allocate(instructions.size * java.lang.Short.BYTES)

            for (instruction in instructions) {
                buffer.putShort(instruction.type.toInteger())
                instruction.operands.forEach { buffer.putShort(it.toShort()) }
            }

            size += buffer.position()
            buffers += buffer.apply { flip() }
        }

        val buffer = ByteBuffer.allocate(size)
        buffer.put(operators.apply { flip() }).put(defaults.apply { flip() })
        buffers.forEach { buffer.put(it) }

        return buffer.apply { flip() }
    }

}
