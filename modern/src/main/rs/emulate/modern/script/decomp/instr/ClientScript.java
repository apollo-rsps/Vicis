package rs.emulate.modern.script.decomp.instr;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * A modern ClientScript (i.e. CS2).
 *
 * @author Major
 */
public final class ClientScript {

	/**
	 * The List of Instructions in this ClientScript.
	 */
	private final List<Instruction> instructions;

	/**
	 * The table of operands in this ClientScript.
	 */
	private final OperandTable operands;

	/**
	 * Creates the ClientScript.
	 *
	 * @param operands The {@link OperandTable}.
	 * @param instructions The {@link List} of {@link Instruction}s.
	 */
	public ClientScript(OperandTable operands, List<Instruction> instructions) {
		this.operands = operands;
		this.instructions = ImmutableList.copyOf(instructions);
	}

	/**
	 * Gets the {@link List} of {@link Instruction}s. No guarantees about the mutability of the returned List are made
	 * (i.e. if modification is required, the List should be duplicated).
	 *
	 * @return The List.
	 */
	public List<Instruction> getInstructions() {
		return instructions;
	}

	/**
	 * Gets the {@link OperandTable} of this ClientScript.
	 *
	 * @return The OperandTable.
	 */
	public OperandTable getOperands() {
		return operands;
	}

}