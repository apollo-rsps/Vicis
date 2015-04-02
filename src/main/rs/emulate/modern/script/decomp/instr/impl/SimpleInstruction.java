package rs.emulate.modern.script.decomp.instr.impl;

import java.util.function.Consumer;

import rs.emulate.modern.script.decomp.instr.Instruction;
import rs.emulate.modern.script.decomp.instr.OperandType;
import rs.emulate.modern.script.interp.ScriptContext;

/**
 * A simple type of {@link Instruction} that is too specific to require its own class.
 *
 * @author Major
 */
public final class SimpleInstruction extends Instruction {

	/**
	 * Creates a SimpleInstruction.
	 * 
	 * @param name The name of the {@link Instruction}.
	 * @param opcode The opcode of the Instruction.
	 * @param action The {@link ScriptContext} {@link Consumer} that performs the side effect of the Instruction.
	 * @param types The {@link OperandType}s of the Instruction.
	 * @return The SimpleInstruction.
	 */
	public static SimpleInstruction create(String name, int opcode, Consumer<ScriptContext> action, OperandType... types) {
		return new SimpleInstruction(name, opcode, action, types);
	}

	/**
	 * The action that occurs when this Instruction is executed.
	 */
	private final Consumer<ScriptContext> action;

	/**
	 * Creates the SimpleInstruction.
	 * 
	 * @param name The name of this {@link Instruction}.
	 * @param opcode The opcode of this Instruction.
	 * @param action The {@link ScriptContext} {@link Consumer} that performs the side effect of this Instruction.
	 * @param types The {@link OperandType}s of this Instruction.
	 */
	private SimpleInstruction(String name, int opcode, Consumer<ScriptContext> action, OperandType... types) {
		super(name, opcode, types);
		this.action = action;
	}

	@Override
	public void evaluate(ScriptContext context) {
		action.accept(context);
	}

}