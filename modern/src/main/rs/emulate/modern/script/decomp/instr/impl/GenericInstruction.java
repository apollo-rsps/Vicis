package rs.emulate.modern.script.decomp.instr.impl;

import rs.emulate.modern.script.decomp.instr.Instruction;
import rs.emulate.modern.script.decomp.instr.OperandType;
import rs.emulate.modern.script.interp.ScriptContext;

import java.util.function.Consumer;

/**
 * A simple type of {@link Instruction} that does not take a single {@code int}/{@code long}/String operand.
 *
 * @author Major
 */
public final class GenericInstruction extends Instruction {

	/**
	 * Creates a GenericInstruction.
	 *
	 * @param name The name of the {@link Instruction}.
	 * @param opcode The opcode of the Instruction.
	 * @param action The {@link ScriptContext} {@link Consumer} that performs the side effect of the Instruction.
	 * @param types The {@link OperandType}s of the Instruction.
	 * @return The GenericInstruction.
	 */
	public static GenericInstruction create(String name, int opcode, Consumer<ScriptContext> action,
	                                        OperandType... types) {
		return new GenericInstruction(name, opcode, action, types);
	}

	/**
	 * The action that occurs when this Instruction is executed.
	 */
	private final Consumer<ScriptContext> action;

	/**
	 * Creates the GenericInstruction.
	 *
	 * @param name The name of this {@link Instruction}.
	 * @param opcode The opcode of this Instruction.
	 * @param action The {@link ScriptContext} {@link Consumer} that performs the side effect of this Instruction.
	 * @param types The {@link OperandType}s of this Instruction.
	 */
	private GenericInstruction(String name, int opcode, Consumer<ScriptContext> action, OperandType... types) {
		super(name, opcode, types);
		this.action = action;
	}

	@Override
	public void evaluate(ScriptContext context) {
		action.accept(context);
	}

}