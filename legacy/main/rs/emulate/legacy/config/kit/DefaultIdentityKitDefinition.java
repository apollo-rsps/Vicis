package rs.emulate.legacy.config.kit;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import rs.emulate.legacy.config.ConfigProperty;
import rs.emulate.legacy.config.ConfigPropertyMap;
import rs.emulate.legacy.config.ConfigPropertyType;
import rs.emulate.legacy.config.DefaultConfigDefinition;
import rs.emulate.legacy.config.ConfigUtils;
import rs.emulate.shared.property.Properties;
import rs.emulate.shared.util.DataBuffer;

/**
 * A default {@link IdentityKitDefinition} used as a base for an actual definition.
 * 
 * @author Major
 */
public class DefaultIdentityKitDefinition extends DefaultConfigDefinition {

	/**
	 * The DefaultIdentityKitDefinition.
	 */
	private static final DefaultIdentityKitDefinition DEFAULT = new DefaultIdentityKitDefinition();

	/**
	 * A {@link Supplier} that returns a {@link ConfigPropertyMap} copy of this default definition.
	 */
	public static final Supplier<ConfigPropertyMap> SUPPLIER = DEFAULT::toPropertyMap;

	/**
	 * Creates the DefaultIdentityKitDefinition.
	 */
	private DefaultIdentityKitDefinition() {
		super();
	}

	@Override
	protected Map<Integer, ConfigProperty<?>> init() {
		Map<Integer, ConfigProperty<?>> defaults = new HashMap<>(27);

		defaults.put(1, new ConfigProperty<>(IdentityKitProperty.PART, Part.NULL, Part::encode, Part::decode, Byte.BYTES));

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

		defaults.put(2, new ConfigProperty<>(IdentityKitProperty.MODELS, null, modelsEncoder, modelDecoder,
				models -> models.length * Short.SIZE + Byte.SIZE));
		defaults.put(3, Properties.alwaysTrue(IdentityKitProperty.PLAYER_DESIGN_STYLE, false));

		for (int slot = 1; slot <= IdentityKitDefinition.COLOUR_COUNT; slot++) {
			defaults.put(slot + 39, Properties.unsignedShort(ConfigUtils.getOriginalColourPropertyName(slot), 0));
			defaults.put(slot + 49, Properties.unsignedShort(ConfigUtils.getReplacementColourPropertyName(slot), 0));
		}

		for (int model = 1; model <= IdentityKitDefinition.HEAD_MODEL_COUNT; model++) {
			ConfigPropertyType name = ConfigUtils.createOptionProperty(IdentityKitDefinition.HEAD_MODEL_PREFIX, model);
			defaults.put(model + 59, Properties.unsignedShort(name, -1));
		}

		return defaults;
	}

}