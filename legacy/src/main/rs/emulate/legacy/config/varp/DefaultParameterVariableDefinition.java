package rs.emulate.legacy.config.varp;

import static rs.emulate.legacy.config.Properties.unsignedShort;
import static rs.emulate.legacy.config.varp.ParameterVariableProperty.PARAMETER;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import rs.emulate.legacy.config.ConfigProperty;
import rs.emulate.legacy.config.ConfigPropertyMap;
import rs.emulate.legacy.config.DefaultConfigDefinition;

/**
 * A default {@link ParameterVariableDefinition} used as a base.
 *
 * @author Major
 */
public class DefaultParameterVariableDefinition extends DefaultConfigDefinition {

	/**
	 * The DefaultParameterVariableDefinition.
	 */
	private static final DefaultParameterVariableDefinition DEFAULT = new DefaultParameterVariableDefinition();

	/**
	 * A {@link Supplier} that returns a {@link ConfigPropertyMap} copy of this default definition.
	 */
	public static final Supplier<ConfigPropertyMap> SUPPLIER = DEFAULT::toPropertyMap;

	/**
	 * Creates the DefaultParameterVariableDefinition.
	 */
	private DefaultParameterVariableDefinition() {
		super();
	}

	@Override
	protected Map<Integer, ConfigProperty<?>> init() {
		Map<Integer, ConfigProperty<?>> map = new HashMap<>(1);

		map.put(5, unsignedShort(PARAMETER, 0));

		return map;
	}

}