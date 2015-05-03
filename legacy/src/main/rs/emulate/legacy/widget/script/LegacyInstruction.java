package rs.emulate.legacy.widget.script;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;

/**
 * An instruction used as part of a {@link LegacyClientScript}.
 *
 * @author Major
 */
public final class LegacyInstruction {

	/**
	 * Creates a LegacyInstruction.
	 * 
	 * @param type The {@link LegacyInstructionType}. Must not be {@code null}.
	 * @param operands The instruction operands. Must not be {@code null}.
	 * @return The LegacyInstruction.
	 * @throws IllegalArgumentException If {@code operands.length} is not equal to the type's operand count.
	 */
	public static LegacyInstruction create(LegacyInstructionType type, int[] operands) {
		Preconditions.checkArgument(operands != null && operands.length == type.getOperandCount(),
				"Operands length must be equal to the InstructionType operand count.");
		return new LegacyInstruction(type, operands);
	}

	/**
	 * The operands of this LegacyInstruction.
	 */
	private final int[] operands;

	/**
	 * The type of this LegacyInstruction.
	 */
	private final LegacyInstructionType type;

	/**
	 * Creates the LegacyInstruction.
	 *
	 * @param type The {@link LegacyInstructionType}.
	 * @param operands The operands of the LegacyInstruction.
	 */
	private LegacyInstruction(LegacyInstructionType type, int[] operands) {
		this.type = type;
		this.operands = operands.clone();
	}

	/**
	 * Gets the operands of this LegacyInstruction.
	 * 
	 * @return The instruction operands. Will not be {@code null}, but may have a length of 0.
	 */
	public int[] getOperands() {
		return operands.clone();
	}

	/**
	 * Gets the {@link LegacyInstructionType} of this LegacyInstruction.
	 * 
	 * @return The LegacyInstructionType.
	 */
	public LegacyInstructionType getType() {
		return type;
	}

	@Override
	public String toString() {
		return type.getMnemonic() + Arrays.stream(operands).mapToObj(Integer::toString).collect(Collectors.joining(", "));
	}

}