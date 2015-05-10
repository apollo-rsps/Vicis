package rs.emulate.legacy.widget.script;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

import rs.emulate.shared.cs.PlayerProvider;

/**
 * An interpreter for {@link LegacyClientScript}s.
 *
 * @author Major
 */
public final class LegacyInterpreter {

	/**
	 * A {@link BinaryOperator} that applies a math function to two integers.
	 */
	enum MathOperator implements BinaryOperator<Integer> {

		/**
		 * The MathOperator that performs division of two integers.
		 */
		DIVIDE((a, b) -> a / b),

		/**
		 * The MathOperator that performs multiplication of two integers.
		 */
		MULTIPLY((a, b) -> a * b),

		/**
		 * The MathOperator that performs addition of two integers.
		 */
		ADD((a, b) -> a + b),

		/**
		 * The MathOperator that performs subtraction of two integers.
		 */
		SUBTRACT((a, b) -> a - b);

		/**
		 * The BiFunction that is evaluated to perform the operation of this MathOperator.
		 */
		private final BiFunction<Integer, Integer, Integer> function;

		/**
		 * Creates the MathOperator.
		 *
		 * @param function The {@link BiFunction} that is evaluated to perform the operation of this MathOperator.
		 */
		private MathOperator(BiFunction<Integer, Integer, Integer> function) {
			this.function = function;
		}

		@Override
		public Integer apply(Integer t, Integer u) {
			return function.apply(t, u);
		}

	}

	/**
	 * The LegacyClientScript being interpreted.
	 */
	private final LegacyClientScript script;

	/**
	 * The MathOperator that will be used to evaluate the next instruction.
	 */
	private MathOperator operator = MathOperator.ADD;

	/**
	 * The ClientScriptContext for the LegacyClientScript being interpreted.
	 */
	private final ClientScriptContext context;

	/**
	 * Creates the LegacyInterpreter.
	 *
	 * @param script The {@link LegacyClientScript} to interpret.
	 * @param context The {@link ClientScriptContext} for the LegacyClientScript being interpreted.
	 */
	public LegacyInterpreter(LegacyClientScript script, ClientScriptContext context) {
		this.script = Objects.requireNonNull(script, "ClientScript must not be null.");
		this.context = Objects.requireNonNull(context, "ClientScriptContext must not be null.");
	}

	/**
	 * Interprets the {@link LegacyClientScript} stored in this interpreter.
	 * 
	 * @return The result of the interpretation.
	 * @throws IllegalStateException If the script does not end with a {@link LegacyInstructionType#RETURN} instruction.
	 */
	public int interpret() {
		for (LegacyInstruction instruction : script.getInstructions()) {
			int value = evaluate(instruction);
			if (context.hasFinished()) {
				return context.getResult();
			}

			context.apply(operator, value);
		}

		throw new IllegalArgumentException("Script must end with a return call.");
	}

	/**
	 * Evaluates the specified {@link LegacyInstruction}.
	 * 
	 * @param instruction The instruction to evaluate.
	 * @return The result of the evaluation.
	 */
	private int evaluate(LegacyInstruction instruction) {
		int[] operands = instruction.getOperands();
		PlayerProvider provider = context.getProvider();

		switch (instruction.getType()) {
			case RETURN:
				context.finish();
				return 0;
			case MOVE_CURRENT_LEVEL:
				return provider.getSkill(operands[0]).getCurrentLevel();
			case MOVE_MAX_LEVEL:
				return provider.getSkill(operands[0]).getMaximumLevel();
			case MOVE_EXPERIENCE:
				return (int) provider.getSkill(operands[0]).getExperience();
			case MOVE_ITEM_AMOUNT:
				break;
			case MOVE_SETTING:
				return provider.getSetting(operands[0]);
			case MOVE_EXPERIENCE_FOR_LEVEL:
				break;
			case MOVE_SETTING2:
				return provider.getSetting(operands[0]) * 100 / 46875;
			case MOVE_COMBAT_LEVEL:
				return provider.getCombatLevel();
			case MOVE_TOTAL_LEVEL:
				return provider.getTotalLevel();
			case MOVE_CONTAINS_ITEM:
				break;
			case MOVE_RUN_ENERGY:
				return provider.getRunEnergy();
			case MOVE_WEIGHT:
				return provider.getWeight();
			case MOVE_SETTING_BIT:
				break;
			case MOVE_VARIABLE_BITS:
				break;
			case SUBTRACT:
				operator = MathOperator.SUBTRACT;
				return 0;
			case MULTIPLY:
				operator = MathOperator.MULTIPLY;
				return 0;
			case DIVISION:
				operator = MathOperator.DIVIDE;
				return 0;
			case MOVE_ABSOLUTE_X:
				return provider.getPosition().getX();
			case MOVE_ABSOLUTE_Y:
				return provider.getPosition().getZ();
			case MOVE:
				return operands[0];
		}

		throw new IllegalStateException("Unrecognised LegacyClientScript instruction " + instruction + ".");
	}

}