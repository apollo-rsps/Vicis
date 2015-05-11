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
import java.util.function.Supplier;

import rs.emulate.legacy.config.ConfigProperty;
import rs.emulate.legacy.config.ConfigPropertyMap;
import rs.emulate.legacy.config.ConfigPropertyType;
import rs.emulate.legacy.config.ConfigUtils;
import rs.emulate.legacy.config.DefaultConfigDefinition;
import rs.emulate.shared.util.DataBuffer;

/**
 * A default {@link IdentikitDefinition} used as a base for an actual definition.
 *
 * @author Major
 */
public class DefaultIdentikitDefinition extends DefaultConfigDefinition {

	/**
	 * The DefaultIdentikitDefinition.
	 */
	private static final DefaultIdentikitDefinition DEFAULT = new DefaultIdentikitDefinition();

	/**
	 * A {@link Supplier} that returns a {@link ConfigPropertyMap} copy of this default definition.
	 */
	public static final Supplier<ConfigPropertyMap> SUPPLIER = DEFAULT::toPropertyMap;

	/**
	 * Creates the DefaultIdentikitDefinition.
	 */
	private DefaultIdentikitDefinition() {
		super();
	}

	@Override
	protected Map<Integer, ConfigProperty<?>> init() {
		Map<Integer, ConfigProperty<?>> defaults = new HashMap<>(27);

		defaults.put(1,
				new ConfigProperty<>(PART, Part.NULL, Part::encode, Part::decode, Byte.BYTES, in -> Optional.empty())); // XXX

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

		defaults.put(2, new ConfigProperty<>(MODELS, null, modelsEncoder, modelDecoder, models -> models.length
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