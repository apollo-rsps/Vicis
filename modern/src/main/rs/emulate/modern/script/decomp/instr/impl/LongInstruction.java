package rs.emulate.modern.script.decomp.instr.impl;

import rs.emulate.modern.script.decomp.instr.Instruction;
import rs.emulate.modern.script.decomp.instr.OperandType;
import rs.emulate.modern.script.interp.ScriptContext;

import java.util.function.Consumer;

/**
 * An {@link Instruction} that takes only a single {@code long} operand.
 *
 * @author Major
 */
public final class LongInstruction extends Instruction {

	/**
	 * Creates a LongInstruction.
	 *
	 * @param name The name of the LongInstruction.
	 * @param opcode The opcode of the instruction.
	 * @param action The {@link ScriptContext} {@link Consumer} that performs the side effect of the LongInstruction.
	 * @return The LongInstruction.
	 */
	public static LongInstruction create(String name, int opcode, Consumer<ScriptContext> action) {
		return new LongInstruction(name, opcode, action);
	}

	/**
	 * The action that occurs when this LongInstruction is executed.
	 */
	private final Consumer<ScriptContext> action;

	/**
	 * Creates the LongInstruction.
	 *
	 * @param name The name of this LongInstruction.
	 * @param opcode The opcode of this LongInstruction.
	 * @param action The {@link ScriptContext} {@link Consumer} that performs the side effect of this LongInstruction.
	 */
	private LongInstruction(String name, int opcode, Consumer<ScriptContext> action) {
		super(name, opcode, OperandType.STRING);
		this.action = action;
	}

	@Override
	public void evaluate(ScriptContext context) {
		action.accept(context);
	}

}