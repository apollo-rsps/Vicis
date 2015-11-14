package rs.emulate.legacy.widget.script;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A ClientScript from legacy client builds (widely known as CS1).
 * <p>
 * Unlike CS2, legacy client scripts operate using a simple accumulator machine, with four registers:
 * <ul>
 * <li>The register holding the program counter, which points to the next instruction to interpret.</li>
 * <li>The operator register, containing the type of operator that should be evaluated by the next instruction.</li>
 * <li>The value register, containing the current value of the ClientScript.</li>
 * <li>The accumulator register, which contains the result of the evaluation of the current instruction, and will
 * modify
 * the current value stored in the value register using the operator type held in the operator register.</li>
 * </ul>
 *
 * @author Major
 */
public final class LegacyClientScript {

	/**
	 * The default value of this LegacyClientScript.
	 */
	private final int defaultValue;

	/**
	 * The List of LegacyInstructions used in this LegacyClientScript.
	 */
	private final List<LegacyInstruction> instructions;

	/**
	 * The RelationalOperator used to determine if the script state has changed.
	 */
	private final RelationalOperator operator;

	/**
	 * Creates the LegacyClientScript.
	 *
	 * @param operator The {@link RelationalOperator} used to evaluate if the script state has changed.
	 * @param defaultValue The default value of the LegacyClientScript.
	 * @param instructions The {@link List} of {@link LegacyInstruction}s that make up this LegacyClientScript.
	 */
	public LegacyClientScript(RelationalOperator operator, int defaultValue, List<LegacyInstruction> instructions) {
		this.operator = operator;
		this.defaultValue = defaultValue;
		this.instructions = ImmutableList.copyOf(instructions);
	}

	/**
	 * Gets the default value of this LegacyClientScript.
	 *
	 * @return The default value.
	 */
	public int getDefault() {
		return defaultValue;
	}

	/**
	 * Gets the {@link List} of {@link LegacyInstruction}s that make up this LegacyClientScript.
	 *
	 * @return The List of LegacyInstructions.
	 */
	public List<LegacyInstruction> getInstructions() {
		return new ArrayList<>(instructions);
	}

	/**
	 * Gets the {@link RelationalOperator} used to evaluate if the script state has changed.
	 *
	 * @return The RelationalOperator.
	 */
	public RelationalOperator getRelationalOperator() {
		return operator;
	}

	@Override
	public String toString() {
		return instructions.stream().map(LegacyInstruction::toString).collect(Collectors.joining("\n"));
	}

}