package rs.emulate.legacy.config.item;

import static rs.emulate.shared.property.Properties.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import rs.emulate.legacy.config.ConfigConstants;
import rs.emulate.legacy.config.ConfigProperty;
import rs.emulate.legacy.config.ConfigPropertyMap;
import rs.emulate.legacy.config.ConfigPropertyType;
import rs.emulate.legacy.config.ConfigUtils;
import rs.emulate.legacy.config.DefaultConfigDefinition;
import rs.emulate.shared.property.DynamicPropertyType;

/**
 * A default {@link ItemDefinition} used as a base for actual definitions.
 * 
 * @author Major
 */
public class DefaultItemDefinition extends DefaultConfigDefinition {

	/**
	 * The DefaultItemDefinition.
	 */
	private static final DefaultItemDefinition DEFAULT = new DefaultItemDefinition();

	/**
	 * A {@link Supplier} that returns a {@link ConfigPropertyMap} copy of this default definition.
	 */
	public static final Supplier<ConfigPropertyMap> SUPPLIER = DEFAULT::toPropertyMap;

	/**
	 * Creates the DefaultItemDefinition.
	 */
	private DefaultItemDefinition() {
		super();
	}

	@Override
	protected Map<Integer, ConfigProperty<?>> init() {
		Map<Integer, ConfigProperty<?>> properties = new HashMap<>();

		properties.put(1, unsignedShort(ItemProperty.MODEL, 0));
		properties.put(2, asciiString(ItemProperty.NAME, "null"));
		properties.put(3, asciiString(ItemProperty.DESCRIPTION, "null"));
		properties.put(4, unsignedShort(ItemProperty.SPRITE_SCALE, 2_000));
		properties.put(5, unsignedShort(ItemProperty.SPRITE_PITCH, 0));
		properties.put(6, unsignedShort(ItemProperty.SPRITE_CAMERA_ROLL, 0));

		properties.put(7, signedShort(ItemProperty.SPRITE_TRANSLATE_X, 0));
		properties.put(8, signedShort(ItemProperty.SPRITE_TRANSLATE_Y, 0));

		properties.put(10, unsignedShort(DynamicPropertyType.valueOf("unknown", 10), 0));
		properties.put(11, alwaysTrue(ItemProperty.STACKABLE, false));
		properties.put(12, unsignedInt(ItemProperty.VALUE, 1));
		properties.put(16, alwaysTrue(ItemProperty.MEMBERS, false));

		properties.put(23, new ConfigProperty<>(ItemProperty.PRIMARY_MALE_MODEL, PrimaryModel.EMPTY, PrimaryModel::encode,
				PrimaryModel::decode, Short.BYTES + Byte.BYTES));
		properties.put(24, unsignedShort(ItemProperty.SECONDARY_MALE_MODEL, 0));

		properties.put(25, new ConfigProperty<>(ItemProperty.PRIMARY_FEMALE_MODEL, PrimaryModel.EMPTY, PrimaryModel::encode,
				PrimaryModel::decode, Short.BYTES + Byte.BYTES));
		properties.put(26, unsignedShort(ItemProperty.SECONDARY_FEMALE_MODEL, 0));

		for (int action = 1; action <= ItemConstants.MENU_ACTION_COUNT; action++) {
			ConfigPropertyType type = ConfigUtils.createOptionProperty(ItemConstants.GROUND_ACTION_PROPERTY_PREFIX, action);
			properties.put(action + 29, asciiString(type, "hidden"));

			type = ConfigUtils.createOptionProperty(ItemConstants.INVENTORY_ACTION_PROPERTY_PREFIX, action);
			properties.put(action + 34, asciiString(type, "hidden"));
		}

		properties.put(40, ConfigUtils.createColourProperty(ItemProperty.COLOURS));

		properties.put(78, unsignedShort(ItemProperty.TERTIARY_MALE_MODEL, 0));
		properties.put(79, unsignedShort(ItemProperty.TERTIARY_FEMALE_MODEL, 0));

		properties.put(90, unsignedShort(ItemProperty.PRIMARY_MALE_HEAD_PIECE, 0));
		properties.put(91, unsignedShort(ItemProperty.PRIMARY_FEMALE_HEAD_PIECE, 0));
		properties.put(92, unsignedShort(ItemProperty.SECONDARY_MALE_HEAD_PIECE, 0));
		properties.put(93, unsignedShort(ItemProperty.SECONDARY_FEMALE_HEAD_PIECE, 0));

		properties.put(95, unsignedShort(ItemProperty.SPRITE_CAMERA_YAW, 0));

		properties.put(97, unsignedShort(ItemProperty.NOTE_INFO_ID, 0));
		properties.put(98, unsignedShort(ItemProperty.NOTE_TEMPLATE_ID, 0));

		for (int option = 1; option <= ItemConstants.ITEM_STACK_COUNT; option++) {
			ConfigPropertyType type = ConfigUtils.createOptionProperty(ItemConstants.ITEM_STACK_PROPERTY_PREFIX, option);
			properties.put(option + 99, new ConfigProperty<>(type, ItemStack.EMPTY, ItemStack::encode, ItemStack::decode,
					Short.BYTES * 2));
		}

		properties.put(110, unsignedShort(ItemProperty.GROUND_SCALE_X, ConfigConstants.DEFAULT_SCALE));
		properties.put(111, unsignedShort(ItemProperty.GROUND_SCALE_Y, ConfigConstants.DEFAULT_SCALE));
		properties.put(112, unsignedShort(ItemProperty.GROUND_SCALE_Z, ConfigConstants.DEFAULT_SCALE));

		properties.put(113, signedByte(ItemProperty.AMBIENCE, 0));
		properties.put(114, new ConfigProperty<>(ItemProperty.CONTRAST, 0, (buffer, contrast) -> buffer.putByte(contrast / 5),
				buffer -> buffer.getByte() * 5, Byte.BYTES));

		properties.put(115, unsignedByte(ItemProperty.TEAM, 0));

		return properties;
	}
}