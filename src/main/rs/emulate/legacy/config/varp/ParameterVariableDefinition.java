package rs.emulate.legacy.config.varp;

import rs.emulate.legacy.config.MutableDefinition;
import rs.emulate.shared.prop.DefinitionProperty;
import rs.emulate.shared.prop.PropertyMap;

/**
 * A definition for a parameter variable (a 'varp').
 * 
 * @author Major
 */
public class ParameterVariableDefinition extends MutableDefinition {

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
	 * Gets the {@link DefinitionProperty} containing the parameter.
	 * 
	 * @return The definition property.
	 */
	public DefinitionProperty<Integer> getParameter() {
		return getProperty(ParameterVariableProperty.PARAMETER);
	}

}