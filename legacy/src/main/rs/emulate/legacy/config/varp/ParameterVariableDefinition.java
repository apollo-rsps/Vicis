package rs.emulate.legacy.config.varp;

import rs.emulate.legacy.config.SerializableProperty;
import rs.emulate.legacy.config.ConfigPropertyMap;
import rs.emulate.legacy.config.MutableConfigDefinition;

/**
 * A definition for a parameter variable (a 'varp').
 * 
 * @author Major
 */
public class ParameterVariableDefinition extends MutableConfigDefinition {

	/**
	 * The name of the ArchiveEntry containing the ParameterVariableDefinitions, without the extension.
	 */
	public static final String ENTRY_NAME = "varp";

	/**
	 * Creates the variable ParameterVariableDefinition.
	 *
	 * @param id The id.
	 * @param properties The {@link ConfigPropertyMap}.
	 */
	public ParameterVariableDefinition(int id, ConfigPropertyMap properties) {
		super(id, properties);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the parameter.
	 * 
	 * @return The definition property.
	 */
	public SerializableProperty<Integer> getParameter() {
		return getProperty(ParameterVariableProperty.PARAMETER);
	}

}