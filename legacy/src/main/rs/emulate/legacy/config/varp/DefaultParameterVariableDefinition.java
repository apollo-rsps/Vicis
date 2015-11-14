package rs.emulate.legacy.config.varp;

import rs.emulate.legacy.config.DefaultConfigDefinition;
import rs.emulate.legacy.config.SerializableProperty;

import java.util.HashMap;
import java.util.Map;

import static rs.emulate.legacy.config.Properties.unsignedShort;
import static rs.emulate.legacy.config.varp.ParameterVariableProperty.PARAMETER;

/**
 * A default {@link ParameterVariableDefinition} used as a base.
 *
 * @param <T> The type of ParameterVariableDefinition this DefaultParameterVariableDefinition is for.
 * @author Major
 */
public class DefaultParameterVariableDefinition<T extends ParameterVariableDefinition> extends
		DefaultConfigDefinition<T> {

	@Override
	protected Map<Integer, SerializableProperty<?>> init() {
		Map<Integer, SerializableProperty<?>> map = new HashMap<>(1);

		map.put(5, unsignedShort(PARAMETER, 0));

		return map;
	}

}