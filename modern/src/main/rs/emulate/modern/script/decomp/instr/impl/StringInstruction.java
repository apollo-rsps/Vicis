package rs.emulate.modern.script.decomp.instr.impl;

import rs.emulate.modern.script.decomp.instr.Instruction;
import rs.emulate.modern.script.decomp.instr.OperandType;
import rs.emulate.modern.script.interp.ScriptContext;

import java.util.function.Consumer;

/**
 * An {@link Instruction} that takes only a single String operand.
 *
 * @author Major
 */
public final class StringInstruction extends Instruction {

	/**
	 * Creates a StringInstruction.
	 *
	 * @param name The name of the StringInstruction.
	 * @param opcode The opcode of the instruction.
	 * @param action The {@link ScriptContext} {@link Consumer} that performs the side effect of the StringInstruction.
	 * @return The StringInstruction.
	 */
	public static StringInstruction create(String name, int opcode, Consumer<ScriptContext> action) {
		return new StringInstruction(name, opcode, action);
	}

	/**
	 * The action that occurs when this StringInstruction is executed.
	 */
	private final Consumer<ScriptContext> action;

	/**
	 * Creates the StringInstruction.
	 *
	 * @param name The name of this StringInstruction.
	 * @param opcode The opcode of this StringInstruction.
	 * @param action The {@link ScriptContext} {@link Consumer} that performs the side effect of this
	 * StringInstruction.
	 */
	private StringInstruction(String name, int opcode, Consumer<ScriptContext> action) {
		super(name, opcode, OperandType.STRING);
		this.action = action;
	}

	@Override
	public void evaluate(ScriptContext context) {
		action.accept(context);
	}

}