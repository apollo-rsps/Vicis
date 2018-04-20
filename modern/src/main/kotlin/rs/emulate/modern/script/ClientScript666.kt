package rs.emulate.modern.script

import rs.emulate.shared.util.DataBuffer

import java.util.ArrayList
import java.util.HashMap

/**
 * A 'ClientScript' used to provide functionality.
 *
 * Variables: like local variables.
 * Parameters: like things passed to method calls
 */
class ClientScript666 {

    val intArgsCount: Int = 0

    val strArgsCount: Int = 0

    val intStackDepth: Int = 0

    val strStackDepth: Int = 0

    lateinit var name: String
        private set

    private lateinit var opcodes: IntArray
    private lateinit var intOperands: IntArray
    private lateinit var longOperands: LongArray
    private lateinit var stringOperands: Array<String?>

    var switchTables: List<Map<Int, Int>>? = null
        private set

    val length: Int
        get() = opcodes.size

    fun getIntOperand(index: Int): Int {
        return intOperands[index]
    }

    fun getLongOperand(index: Int): Long {
        return longOperands[index]
    }

    fun getOpcode(index: Int): Int {
        return opcodes[index]
    }

    fun getStringOperand(index: Int): String? {
        return stringOperands[index]
    }

    companion object {

        fun decode(buffer: DataBuffer): ClientScript666 {
            val cs = ClientScript666()

            buffer.position(buffer.limit() - 2)
            val trailerLength = buffer.getShort() and 0xFFFF
            val trailerPosition = buffer.limit() - 18 - trailerLength
            buffer.position(trailerPosition)

            val operations = buffer.getInt() // TODO
            val intVariables = buffer.getUnsignedShort()
            val stringVariables = buffer.getUnsignedShort()
            val longVariables = buffer.getUnsignedShort()

            val intParameterCount = buffer.getUnsignedShort()
            val stringParameterCount = buffer.getUnsignedShort()
            val longParameterCount = buffer.getUnsignedShort()

            val switches = buffer.getUnsignedByte()

            val tables = ArrayList<Map<Int, Int>>(switches)
            for (index in 0 until switches) {
                var cases = buffer.getUnsignedShort()
                val table = HashMap<Int, Int>()

                while (cases-- > 0) {
                    val value = buffer.getInt()
                    val offset = buffer.getInt()
                    table[value] = offset
                }

                tables.add(table)
            }

            cs.switchTables = tables

            buffer.position(0)

            cs.name = buffer.getCString()

            val opcodes = IntArray(operations)
            val intOperands = IntArray(operations)
            val stringOperands: Array<String?> = arrayOfNulls(operations)
            val longOperands = LongArray(operations)

            var index = 0
            while (buffer.position() < trailerPosition) {
                val opcode = buffer.getUnsignedShort()

                if (opcode == 3) {
                    stringOperands[index] = buffer.getCString()
                } else if (opcode == 54) {
                    longOperands[index] = buffer.getLong()
                } else if (opcode >= 150 || opcode == 21 || opcode == 38 || opcode == 39) {
                    intOperands[index] = buffer.getUnsignedByte()
                } else {
                    intOperands[index] = buffer.getInt()
                }

                opcodes[index++] = opcode
            }

            cs.opcodes = opcodes
            cs.intOperands = intOperands
            cs.stringOperands = stringOperands
            cs.longOperands = longOperands

            return cs
        }
    }

}
