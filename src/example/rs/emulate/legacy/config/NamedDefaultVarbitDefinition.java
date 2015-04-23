package rs.emulate.legacy.config;

import java.util.Map;

import rs.emulate.legacy.config.varbit.DefaultBitVariableDefinition;
import rs.emulate.shared.property.DynamicPropertyType;
import rs.emulate.shared.property.Properties;

/**
 * A {@link DefaultBitVariableDefinition} that also includes a name.
 *
 * @author Major
 */
public final class NamedDefaultVarbitDefinition extends DefaultBitVariableDefinition {

	/**
	 * The ConfigPropertyType for the name of a BitVariable.
	 */
	private static final ConfigPropertyType NAME_PROPERTY_TYPE = DynamicPropertyType.valueOf("name", 2);

	@Override
	protected Map<Integer, ConfigProperty<?>> init() {
		Map<Integer, ConfigProperty<?>> properties = super.init();

		properties.put(2, Properties.asciiString(NAME_PROPERTY_TYPE, "null"));

		return properties;
	}

}