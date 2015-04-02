package rs.emulate.modern.script.decomp.instr;

import java.util.Arrays;
import java.util.Set;

import rs.emulate.modern.script.interp.ScriptContext;

import com.google.common.collect.ImmutableSet;

/**
 * A base class for a ClientScript instruction.
 *
 * @author Major
 */
public abstract class Instruction {

	/**
	 * The Set of OperandTypes utilised by this Instruction.
	 */
	protected final Set<OperandType> types;

	/**
	 * The name of this Instruction.
	 */
	private final String name;

	/**
	 * The opcode of this Instruction.
	 */
	private final int opcode;

	/**
	 * Creates the Instruction.
	 * 
	 * @param name The name of the Instruction.
	 * @param opcode The opcode of the Instruction.
	 * @param types The {@link OperandType}s.
	 */
	public Instruction(String name, int opcode, OperandType... types) {
		this.name = name;
		this.opcode = opcode;
		this.types = ImmutableSet.copyOf(types);
	}

	/**
	 * Gets the {@link OperandType} at the specified index.
	 * 
	 * @param type The OperandType.
	 * @return The OperandType.
	 * @throws IndexOutOfBoundsException If the index is out of bounds.
	 */
	public final boolean containsType(OperandType type) {
		return types.contains(type);
	}

	/**
	 * Evaluates this Instruction.
	 * 
	 * @param context The {@link ScriptContext} of the script currently being executed.
	 */
	public abstract void evaluate(ScriptContext context);

	/**
	 * Gets the name of this Instruction.
	 * 
	 * @return The name.
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Gets the opcode of this Instruction.
	 * 
	 * @return The opcode.
	 */
	public final int getOpcode() {
		return opcode;
	}

	/**
	 * Gets the operand count of this Instruction.
	 * 
	 * @return The operand count.
	 */
	public final int getOperandCount() {
		return types.size();
	}

	/**
	 * Returns whether or not the specified {@link OperandType}s match the expected OperandTypes of this Instruction.
	 * 
	 * @param types The OperandTypes.
	 * @return {@code true} if the OperandTypes are valid, {@code false} if not.
	 */
	public final boolean validOperandTypes(OperandType... types) {
		return types.equals(Arrays.asList(types));
	}

}