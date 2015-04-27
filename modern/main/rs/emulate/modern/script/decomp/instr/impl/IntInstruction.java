package rs.emulate.modern.script.decomp.instr.impl;

import java.util.function.Consumer;

import rs.emulate.modern.script.decomp.instr.Instruction;
import rs.emulate.modern.script.decomp.instr.OperandType;
import rs.emulate.modern.script.interp.ScriptContext;

/**
 * An {@link Instruction} that takes only a single {@code int} operand.
 *
 * @author Major
 */
public final class IntInstruction extends Instruction {

	/**
	 * Creates a IntInstruction.
	 * 
	 * @param name The name of the IntInstruction.
	 * @param opcode The opcode of the instruction.
	 * @param action The {@link ScriptContext} {@link Consumer} that performs the side effect of the IntInstruction.
	 * @return The IntInstruction.
	 */
	public static IntInstruction create(String name, int opcode, Consumer<ScriptContext> action) {
		return new IntInstruction(name, opcode, action);
	}

	/**
	 * The action that occurs when this IntInstruction is executed.
	 */
	private final Consumer<ScriptContext> action;

	/**
	 * Creates the IntInstruction.
	 * 
	 * @param name The name of this IntInstruction.
	 * @param opcode The opcode of this IntInstruction.
	 * @param action The {@link ScriptContext} {@link Consumer} that performs the side effect of this IntInstruction.
	 */
	private IntInstruction(String name, int opcode, Consumer<ScriptContext> action) {
		super(name, opcode, OperandType.INT);
		this.action = action;
	}

	@Override
	public void evaluate(ScriptContext context) {
		action.accept(context);
	}

}