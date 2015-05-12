package rs.emulate.legacy.config.varp;

import static rs.emulate.legacy.config.Properties.unsignedShort;
import static rs.emulate.legacy.config.varp.ParameterVariableProperty.PARAMETER;

import java.util.HashMap;
import java.util.Map;

import rs.emulate.legacy.config.ConfigProperty;
import rs.emulate.legacy.config.DefaultConfigDefinition;

/**
 * A default {@link ParameterVariableDefinition} used as a base.
 *
 * @author Major
 * @param <T> The type of ParameterVariableDefinition this DefaultParameterVariableDefinition is for.
 */
public class DefaultParameterVariableDefinition<T extends ParameterVariableDefinition> extends
		DefaultConfigDefinition<T> {

	@Override
	protected Map<Integer, ConfigProperty<?>> init() {
		Map<Integer, ConfigProperty<?>> map = new HashMap<>(1);

		map.put(5, unsignedShort(PARAMETER, 0));

		return map;
	}

}