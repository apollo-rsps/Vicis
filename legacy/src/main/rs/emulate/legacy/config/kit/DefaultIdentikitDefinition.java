package rs.emulate.legacy.config.kit;

import static rs.emulate.legacy.config.Properties.alwaysTrue;
import static rs.emulate.legacy.config.Properties.unsignedShort;
import static rs.emulate.legacy.config.kit.IdentikitDefinition.COLOUR_COUNT;
import static rs.emulate.legacy.config.kit.IdentikitDefinition.HEAD_MODEL_COUNT;
import static rs.emulate.legacy.config.kit.IdentikitProperty.MODELS;
import static rs.emulate.legacy.config.kit.IdentikitProperty.PART;
import static rs.emulate.legacy.config.kit.IdentikitProperty.PLAYER_DESIGN_STYLE;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import rs.emulate.legacy.config.SerializableProperty;
import rs.emulate.legacy.config.ConfigPropertyType;
import rs.emulate.legacy.config.ConfigUtils;
import rs.emulate.legacy.config.DefaultConfigDefinition;
import rs.emulate.shared.util.DataBuffer;

/**
 * A default {@link IdentikitDefinition} used as a base for an actual definition.
 *
 * @author Major
 * @param <T> The type of IdentikitDefinition this DefaultIdentikitDefinition is for.
 */
public class DefaultIdentikitDefinition<T extends IdentikitDefinition> extends DefaultConfigDefinition<T> {

	@Override
	protected Map<Integer, SerializableProperty<?>> init() {
		Map<Integer, SerializableProperty<?>> defaults = new HashMap<>(27);

		defaults.put(1,
				new SerializableProperty<>(PART, Part.NULL, Part::encode, Part::decode, Byte.BYTES, in -> Optional.empty())); // XXX

		BiConsumer<DataBuffer, int[]> modelsEncoder = (buffer, models) -> {
			buffer.putByte(models.length);
			Arrays.stream(models).forEach(buffer::putShort);
		};

		Function<DataBuffer, int[]> modelDecoder = buffer -> {
			int count = buffer.getUnsignedByte();
			int[] models = new int[count];

			Arrays.setAll(models, index -> buffer.getUnsignedShort());
			return models;
		};

		defaults.put(2, new SerializableProperty<>(MODELS, null, modelsEncoder, modelDecoder, models -> models.length
				* Short.SIZE + Byte.SIZE, input -> Optional.empty())); // XXX
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