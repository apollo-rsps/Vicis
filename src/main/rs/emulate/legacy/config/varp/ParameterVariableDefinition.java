package rs.emulate.legacy.config.varp;

import rs.emulate.legacy.config.ConfigProperty;
import rs.emulate.legacy.config.MutableConfigDefinition;
import rs.emulate.shared.prop.PropertyMap;

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
	 * @param properties The {@link PropertyMap}.
	 */
	public ParameterVariableDefinition(int id, PropertyMap properties) {
		super(id, properties);
	}

	/**
	 * Gets the {@link ConfigProperty} containing the parameter.
	 * 
	 * @return The definition property.
	 */
	public ConfigProperty<Integer> getParameter() {
		return getProperty(ParameterVariableProperty.PARAMETER);
	}

}