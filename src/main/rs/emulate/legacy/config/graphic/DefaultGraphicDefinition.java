package rs.emulate.legacy.config.graphic;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import rs.emulate.legacy.config.ConfigConstants;
import rs.emulate.legacy.config.DefaultConfigDefinition;
import rs.emulate.legacy.config.ConfigDefinitionUtils;
import rs.emulate.shared.prop.DefinitionProperty;
import rs.emulate.shared.prop.Properties;
import rs.emulate.shared.prop.PropertyMap;

/**
 * A default {@link GraphicDefinition} used as a base for an actual definition.
 * 
 * @author Major
 */
public class DefaultGraphicDefinition extends DefaultConfigDefinition {

	/**
	 * The DefaultGraphicDefinition.
	 */
	private static final DefaultGraphicDefinition DEFAULT = new DefaultGraphicDefinition();

	/**
	 * A {@link Supplier} that returns a {@link PropertyMap} copy of this default definition.
	 */
	public static final Supplier<PropertyMap> SUPPLIER = DEFAULT::toPropertyMap;

	/**
	 * Creates the DefaultGraphicDefinition.
	 */
	private DefaultGraphicDefinition() {
		super();
	}

	@Override
	protected Map<Integer, DefinitionProperty<?>> init() {
		Map<Integer, DefinitionProperty<?>> defaults = new HashMap<>(27);

		defaults.put(1, Properties.unsignedShort(GraphicProperty.MODEL, 0));
		defaults.put(2, Properties.unsignedShort(GraphicProperty.ANIMATION, -1));
		defaults.put(4, Properties.unsignedShort(GraphicProperty.BREADTH_SCALE, ConfigConstants.DEFAULT_SCALE));
		defaults.put(5, Properties.unsignedShort(GraphicProperty.DEPTH_SCALE, ConfigConstants.DEFAULT_SCALE));
		defaults.put(6, Properties.unsignedShort(GraphicProperty.ROTATION, 0));
		defaults.put(7, Properties.unsignedByte(GraphicProperty.BRIGHTNESS, 0));
		defaults.put(8, Properties.unsignedByte(GraphicProperty.SHADOW, 0));

		for (int slot = 1; slot <= GraphicDefinition.COLOUR_COUNT; slot++) {
			defaults.put(slot + 40, Properties.unsignedShort(ConfigDefinitionUtils.getOriginalColourPropertyName(slot), 0));
			defaults.put(slot + 50, Properties.unsignedShort(ConfigDefinitionUtils.getReplacementColourPropertyName(slot), 0));
		}

		return defaults;
	}

}