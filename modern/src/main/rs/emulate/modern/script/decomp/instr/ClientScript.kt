package rs.emulate.modern.script.decomp.instr

/**
 * A modern ClientScript (i.e. CS2).
 *
 * @param operands The [OperandTable].
 * @param instructions The [List] of [Instruction]s.
 */
class ClientScript(val operands: OperandTable, instructions: List<Instruction>) {

    val instructions: List<Instruction> = instructions.toList()

}
