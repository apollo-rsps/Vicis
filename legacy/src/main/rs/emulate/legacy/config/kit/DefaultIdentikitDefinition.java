package rs.emulate.legacy.config.kit;

import rs.emulate.legacy.config.ConfigPropertyType;
import rs.emulate.legacy.config.ConfigUtils;
import rs.emulate.legacy.config.DefaultConfigDefinition;
import rs.emulate.legacy.config.SerializableProperty;
import rs.emulate.shared.util.DataBuffer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import static rs.emulate.legacy.config.Properties.alwaysTrue;
import static rs.emulate.legacy.config.Properties.unsignedShort;
import static rs.emulate.legacy.config.kit.IdentikitDefinition.COLOUR_COUNT;
import static rs.emulate.legacy.config.kit.IdentikitDefinition.HEAD_MODEL_COUNT;
import static rs.emulate.legacy.config.kit.IdentikitProperty.MODELS;
import static rs.emulate.legacy.config.kit.IdentikitProperty.PART;
import static rs.emulate.legacy.config.kit.IdentikitProperty.PLAYER_DESIGN_STYLE;

/**
 * A default {@link IdentikitDefinition} used as a base for an actual definition.
 *
 * @param <T> The type of IdentikitDefinition this DefaultIdentikitDefinition is for.
 * @author Major
 */
public class DefaultIdentikitDefinition<T extends IdentikitDefinition> extends DefaultConfigDefinition<T> {

	@Override
	protected Map<Integer, SerializableProperty<?>> init() {
		Map<Integer, SerializableProperty<?>> defaults = new HashMap<>(27);

		defaults.put(1, new SerializableProperty<>(PART, Part.NULL, Part::encode, Part::decode, Byte.BYTES, Part.parser()));

		BiConsumer<DataBuffer, int[]> modelsEncoder = (buffer, models) -> {
			buffer.putByte(models.length);
			Arrays.stream(models).forEach(buffer::putShort);
		};

		defaults.put(2, new SerializableProperty<>(MODELS, null, modelsEncoder, ConfigUtils.MODEL_DECODER,
				models -> models.length * Short.SIZE + Byte.SIZE, input -> Optional.empty())); // FIXME parser
		
		defaults.put(3, alwaysTrue(PLAYER_DESIGN_STYLE, false));

		for (int slot = 1; slot <= COLOUR_COUNT; slot++) {
			defaults.put(slot + 39, unsignedShort(ConfigUtils.getOriginalColourPropertyName(slot), 0));
			defaults.put(slot + 49, unsignedShort(ConfigUtils.getReplacementColourPropertyName(slot), 0));
		}

		for (int model = 1; model <= HEAD_MODEL_COUNT; model++) {
			ConfigPropertyType name = ConfigUtils.newOptionProperty(IdentikitDefinition.HEAD_MODEL_PREFIX, model);
			defaults.put(model + 59, unsignedShort(name, -1));
		}

		return defaults;
	}

}