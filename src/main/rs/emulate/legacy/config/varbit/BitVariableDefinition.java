package rs.emulate.legacy.config.varbit;

import java.util.Arrays;
import java.util.List;

import rs.emulate.legacy.config.ConfigProperty;
import rs.emulate.legacy.config.ConfigPropertyMap;
import rs.emulate.legacy.config.MutableConfigDefinition;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;

/**
 * A definition for bit variables (a 'varbit').
 * 
 * @author Major
 */
public class BitVariableDefinition extends MutableConfigDefinition {

	/**
	 * The List of bit masks used by the BitVariables.
	 */
	public static final List<Integer> BIT_MASKS;

	/**
	 * The name of the ArchiveEntry containing the BitVariableDefinitions, without the extension.
	 */
	public static final String ENTRY_NAME = "varbit";

	static {
		int[] masks = new int[32];
		Arrays.parallelSetAll(masks, index -> (int) (Math.pow(2, index) - 1));
		masks[31] = -1;

		BIT_MASKS = ImmutableList.copyOf(Ints.asList(masks)); // Use a list instead of an array for immutability
	}

	/**
	 * Creates the BitVariableDefinition.
	 * 
	 * @param id The id.
	 * @param properties The {@link ConfigPropertyMap}.
	 */
	public BitVariableDefinition(int id, ConfigPropertyMap properties) {
		super(id, properties);
	}

	/**
	 * Gets the bit mask used by this variable.
	 * 
	 * @return The bit mask.
	 */
	public int getBitmask() {
		return BIT_MASKS.get(getHigh() - getLow());
	}

	/**
	 * Gets the high bit mask index of this definition.
	 * 
	 * @return The high index.
	 */
	public int getHigh() {
		return getVariableValue().getHigh();
	}

	/**
	 * Gets the low bit mask index of this definition.
	 * 
	 * @return The low index.
	 */
	public int getLow() {
		return getVariableValue().getLow();
	}

	/**
	 * Gets the {@link ConfigProperty} containing the {@link Variable}.
	 * 
	 * @return The variable.
	 */
	public ConfigProperty<Variable> getVariable() {
		return getProperty(BitVariableProperty.VARIABLE);
	}

	/**
	 * Gets the variable id of this definition.
	 * 
	 * @return The variable id.
	 */
	public int getVariableId() {
		return getVariableValue().getVariable();
	}

	/**
	 * Gets the value of the {@link BitVariableProperty#VARIABLE} {@link ConfigProperty}.
	 * 
	 * @return The {@link Variable}.
	 */
	private Variable getVariableValue() {
		return getVariable().getValue();
	}

}