package rs.emulate.legacy.config.kit;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import rs.emulate.legacy.config.DefaultDefinition;
import rs.emulate.legacy.config.DefinitionUtils;
import rs.emulate.shared.prop.DefinitionProperty;
import rs.emulate.shared.prop.Properties;
import rs.emulate.shared.prop.PropertyMap;
import rs.emulate.shared.prop.PropertyType;
import rs.emulate.shared.util.DataBuffer;

/**
 * A default {@link IdentityKitDefinition} used as a base for an actual definition.
 * 
 * @author Major
 */
public class DefaultIdentityKitDefinition extends DefaultDefinition {

	/**
	 * The DefaultIdentityKitDefinition.
	 */
	private static final DefaultIdentityKitDefinition DEFAULT = new DefaultIdentityKitDefinition();

	/**
	 * A {@link Supplier} that returns a {@link PropertyMap} copy of this default definition.
	 */
	public static final Supplier<PropertyMap> SUPPLIER = DEFAULT::toPropertyMap;

	/**
	 * Creates the DefaultIdentityKitDefinition.
	 */
	private DefaultIdentityKitDefinition() {
		super();
	}

	@Override
	protected Map<Integer, DefinitionProperty<?>> init() {
		Map<Integer, DefinitionProperty<?>> defaults = new HashMap<>(27);

		defaults.put(1, new DefinitionProperty<>(IdentityKitProperty.PART, Part.NULL, Part::encode, Part::decode, Byte.BYTES));

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

		defaults.put(2, new DefinitionProperty<>(IdentityKitProperty.MODELS, null, modelsEncoder, modelDecoder,
				models -> models.length * Short.SIZE + Byte.SIZE));
		defaults.put(3, Properties.alwaysTrue(IdentityKitProperty.PLAYER_DESIGN_STYLE, false));

		for (int slot = 1; slot <= IdentityKitDefinition.COLOUR_COUNT; slot++) {
			defaults.put(slot + 39, Properties.unsignedShort(DefinitionUtils.getOriginalColourPropertyName(slot), 0));
			defaults.put(slot + 49, Properties.unsignedShort(DefinitionUtils.getReplacementColourPropertyName(slot), 0));
		}

		for (int model = 1; model <= IdentityKitDefinition.HEAD_MODEL_COUNT; model++) {
			PropertyType name = DefinitionUtils.createOptionProperty(IdentityKitDefinition.HEAD_MODEL_PREFIX, model);
			defaults.put(model + 59, Properties.unsignedShort(name, -1));
		}

		return defaults;
	}

}