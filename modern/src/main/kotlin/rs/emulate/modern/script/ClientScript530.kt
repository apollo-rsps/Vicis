package rs.emulate.modern.script

import rs.emulate.shared.util.DataBuffer
import java.util.ArrayList
import java.util.HashMap

/**
 * A 'ClientScript' used by the client to provide functionality.
 */
class ClientScript530 {

    var intArgsCount: Int = 0
        private set
    var strArgsCount: Int = 0
        private set
    var intStackDepth: Int = 0
        private set

    var strStackDepth: Int = 0
        private set

    lateinit var name: String
        private set

    private lateinit var opcodes: IntArray
    private lateinit var intOperands: IntArray

    private lateinit var stringOperands: Array<String>

    private lateinit var switchTables: MutableList<MutableMap<Int, Int>>

    val length: Int
        get() = opcodes.size

    fun getIntOperand(index: Int): Int {
        return intOperands[index]
    }

    fun getOpcode(index: Int): Int {
        return opcodes[index]
    }

    fun getStringOperand(index: Int): String {
        return stringOperands[index]
    }

    fun getSwitchTables(): List<Map<Int, Int>>? {
        return switchTables
    }

    companion object {

        /**
         * Decodes a [ClientScript530] from a [DataBuffer].
         */
        fun decode(buffer: DataBuffer): ClientScript530 {
            val script = ClientScript530()

            buffer.position(buffer.limit() - 2)
            val trailerLength = buffer.getShort() and 0xFFFF
            val trailerPosition = buffer.limit() - 14 - trailerLength
            buffer.position(trailerPosition)

            val ops = buffer.getInt()
            script.intArgsCount = buffer.getUnsignedShort()
            script.strArgsCount = buffer.getUnsignedShort()
            script.intStackDepth = buffer.getUnsignedShort()
            script.strStackDepth = buffer.getUnsignedShort()

            val switches = buffer.getUnsignedByte()
            val tables = ArrayList<MutableMap<Int, Int>>(switches)

            for (table in 0 until switches) {
                tables.add(table, HashMap())

                var size = buffer.getUnsignedShort()
                while (size-- > 0) {
                    val index = buffer.getInt()
                    val value = buffer.getInt()

                    tables[table][index] = value
                }
            }

            script.switchTables = ArrayList(switches)

            buffer.position(0)
            script.name = buffer.getCString()

            val opcodes = IntArray(ops)
            val intOperands = IntArray(ops)
            val strings = arrayOfNulls<String>(ops)

            var op = 0
            while (buffer.position() < trailerPosition) {
                val opcode = buffer.getUnsignedShort()

                if (opcode == 3) {
                    strings[op] = buffer.getCString()
                } else if (opcode >= 100 || opcode == 21 || opcode == 38 || opcode == 39) {
                    intOperands[op] = buffer.getByte()
                } else {
                    intOperands[op] = buffer.getInt()
                }

                opcodes[op++] = opcode
            }

            script.opcodes = opcodes
            script.intOperands = intOperands
            script.stringOperands = strings.requireNoNulls()

            return script
        }
    }

}
