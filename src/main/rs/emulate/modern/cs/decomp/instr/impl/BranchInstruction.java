package rs.emulate.modern.cs.decomp.instr.impl;

import java.util.function.Consumer;

import rs.emulate.modern.cs.decomp.instr.Instruction;
import rs.emulate.modern.cs.decomp.instr.OperandType;
import rs.emulate.modern.cs.interp.ScriptContext;

/**
 * An {@link Instruction} that results in a branch, such as {@code goto}.
 *
 * @author Major
 */
public final class BranchInstruction extends Instruction {

	/**
	 * Creates a BranchInstruction.
	 * 
	 * @param name The name of the BranchInstruction.
	 * @param opcode The opcode of the instruction.
	 * @param action The {@link ScriptContext} {@link Consumer} that performs the side effect of the instruction.
	 * @return The BranchInstruction.
	 */
	public static BranchInstruction create(String name, int opcode, Consumer<ScriptContext> action) {
		return new BranchInstruction(name, opcode, action);
	}

	/**
	 * The action that occurs when this BranchInstruction is executed.
	 */
	private final Consumer<ScriptContext> action;

	/**
	 * Creates the BranchInstruction.
	 * 
	 * @param name The name of this BranchInstruction.
	 * @param opcode The opcode of this BranchInstruction.
	 * @param action The {@link ScriptContext} {@link Consumer} that performs the side effect of this BranchInstruction.
	 */
	private BranchInstruction(String name, int opcode, Consumer<ScriptContext> action) {
		super(name, opcode, OperandType.INT);
		this.action = action;
	}

	@Override
	public void evaluate(ScriptContext context) {
		action.accept(context);
	}

}