package rs.emulate.legacy.config.graphic;

import static rs.emulate.legacy.config.Properties.unsignedByte;
import static rs.emulate.legacy.config.Properties.unsignedShort;
import static rs.emulate.legacy.config.graphic.GraphicDefinition.COLOUR_COUNT;
import static rs.emulate.legacy.config.graphic.GraphicProperty.ANIMATION;
import static rs.emulate.legacy.config.graphic.GraphicProperty.BREADTH_SCALE;
import static rs.emulate.legacy.config.graphic.GraphicProperty.BRIGHTNESS;
import static rs.emulate.legacy.config.graphic.GraphicProperty.DEPTH_SCALE;
import static rs.emulate.legacy.config.graphic.GraphicProperty.MODEL;
import static rs.emulate.legacy.config.graphic.GraphicProperty.ROTATION;
import static rs.emulate.legacy.config.graphic.GraphicProperty.SHADOW;

import java.util.HashMap;
import java.util.Map;

import rs.emulate.legacy.config.ConfigConstants;
import rs.emulate.legacy.config.SerializableProperty;
import rs.emulate.legacy.config.ConfigUtils;
import rs.emulate.legacy.config.DefaultConfigDefinition;

/**
 * A default {@link GraphicDefinition} used as a base for an actual definition.
 *
 * @author Major
 * @param <T> The type of GraphicDefinition this DefaultGraphicDefinition is for.
 */
public class DefaultGraphicDefinition<T extends GraphicDefinition> extends DefaultConfigDefinition<T> {

	@Override
	protected Map<Integer, SerializableProperty<?>> init() {
		Map<Integer, SerializableProperty<?>> defaults = new HashMap<>(27);

		defaults.put(1, unsignedShort(MODEL, 0));
		defaults.put(2, unsignedShort(ANIMATION, -1));
		defaults.put(4, unsignedShort(BREADTH_SCALE, ConfigConstants.DEFAULT_SCALE));
		defaults.put(5, unsignedShort(DEPTH_SCALE, ConfigConstants.DEFAULT_SCALE));
		defaults.put(6, unsignedShort(ROTATION, 0));
		defaults.put(7, unsignedByte(BRIGHTNESS, 0));
		defaults.put(8, unsignedByte(SHADOW, 0));

		for (int slot = 1; slot <= COLOUR_COUNT; slot++) {
			defaults.put(slot + 40, unsignedShort(ConfigUtils.getOriginalColourPropertyName(slot), 0));
			defaults.put(slot + 50, unsignedShort(ConfigUtils.getReplacementColourPropertyName(slot), 0));
		}

		return defaults;
	}

}