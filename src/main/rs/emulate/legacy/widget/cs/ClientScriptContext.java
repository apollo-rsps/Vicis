package rs.emulate.legacy.widget.cs;

import rs.emulate.legacy.widget.cs.LegacyIntepreter.MathOperator;
import rs.emulate.shared.cs.PlayerProvider;

/**
 * A context for an interpreted {@link LegacyClientScript}, containing information such as the player's skills.
 *
 * @author Major
 */
public final class ClientScriptContext {

	/**
	 * Whether or not the script this ClientScriptContext is for has finished execution.
	 */
	private boolean finished;

	/**
	 * The default PlayerProvider.
	 */
	private final PlayerProvider provider;

	/**
	 * The current value of the ClientScript.
	 */
	private int value;

	/**
	 * Creates the ClientScriptContext using the default PlayerProvider.
	 */
	public ClientScriptContext() {
		this(PlayerProvider.defaultProvider());
	}

	/**
	 * Creates the ClientScriptContext using the specified PlayerProvider.
	 * 
	 * @param provider The PlayerProvider.
	 */
	public ClientScriptContext(PlayerProvider provider) {
		this.provider = provider;
	}

	/**
	 * Gets the {@link PlayerProvider}.
	 * 
	 * @return The PlayerProvider.
	 */
	public PlayerProvider getProvider() {
		return provider;
	}

	/**
	 * Applies the specified {@link MathOperator} to the current value in this ClientScriptContext, using the
	 * {@code value} parameter as the second operand of the MathOperator.
	 * 
	 * @param operator The MathOperator to apply.
	 * @param value The value to use as the second operand.
	 */
	public void apply(MathOperator operator, int value) {
		this.value = operator.apply(this.value, value);
	}

	/**
	 * Marks this ClientScriptContext as having finished execution.
	 */
	public void finish() {
		finished = true;
	}

	/**
	 * Gets the result of this ClientScriptContext.
	 * 
	 * @return The result.
	 * @throws IllegalStateException If this ClientScriptContext has not finished execution.
	 */
	public int getResult() {
		if (!finished) {
			throw new IllegalStateException("Cannot get the result of a ClientScriptContext that has yet to finish execution.");
		}

		return value;
	}

	/**
	 * Gets the current value of this ClientScriptContext.
	 * 
	 * @return The value.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Returns whether or not the script this ClientScriptContext is for has finished execution.
	 * 
	 * @return {@code true} if the script has finished execution, otherwise {@code false}.
	 */
	public boolean hasFinished() {
		return finished;
	}

}