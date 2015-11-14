package rs.emulate.legacy.config.item;

import rs.emulate.legacy.config.ConfigConstants;
import rs.emulate.legacy.config.ConfigPropertyType;
import rs.emulate.legacy.config.ConfigUtils;
import rs.emulate.legacy.config.DefaultConfigDefinition;
import rs.emulate.legacy.config.DynamicConfigPropertyType;
import rs.emulate.legacy.config.SerializableProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static rs.emulate.legacy.config.Properties.*;
import static rs.emulate.legacy.config.item.ItemProperty.*;

/**
 * A default {@link ItemDefinition} used as a base for actual definitions.
 *
 * @author Major
 * @param <T> The type of ItemDefinition this DefaultItemDefinition is for.
 */
public class DefaultItemDefinition<T extends ItemDefinition> extends DefaultConfigDefinition<T> {

	@Override
	protected Map<Integer, SerializableProperty<?>> init() {
		Map<Integer, SerializableProperty<?>> properties = new HashMap<>();

		properties.put(1, unsignedShort(MODEL, 0));
		properties.put(2, asciiString(NAME, "null"));
		properties.put(3, asciiString(DESCRIPTION, "null"));
		properties.put(4, unsignedShort(SPRITE_SCALE, 2_000));
		properties.put(5, unsignedShort(SPRITE_PITCH, 0));
		properties.put(6, unsignedShort(SPRITE_CAMERA_ROLL, 0));

		properties.put(7, signedShort(SPRITE_TRANSLATE_X, 0));
		properties.put(8, signedShort(SPRITE_TRANSLATE_Y, 0));

		properties.put(10, unsignedShort(DynamicConfigPropertyType.valueOf("unknown", 10), 0));
		properties.put(11, alwaysTrue(STACKABLE, false));
		properties.put(12, unsignedInt(VALUE, 1));
		properties.put(16, alwaysTrue(MEMBERS, false));

		properties.put(23, new SerializableProperty<>(PRIMARY_MALE_MODEL, PrimaryModel.EMPTY, PrimaryModel::encode,
				PrimaryModel::decode, Short.BYTES + Byte.BYTES, input -> Optional.empty())); // FIXME parser
		properties.put(24, unsignedShort(SECONDARY_MALE_MODEL, 0));

		properties.put(25, new SerializableProperty<>(PRIMARY_FEMALE_MODEL, PrimaryModel.EMPTY, PrimaryModel::encode,
				PrimaryModel::decode, Short.BYTES + Byte.BYTES, input -> Optional.empty())); // FIXME parser
		properties.put(26, unsignedShort(SECONDARY_FEMALE_MODEL, 0));

		for (int action = 1; action <= ItemConstants.MENU_ACTION_COUNT; action++) {
			ConfigPropertyType type = ConfigUtils
					.newOptionProperty(ItemConstants.GROUND_ACTION_PROPERTY_PREFIX, action);
			properties.put(action + 29, asciiString(type, "hidden"));

			type = ConfigUtils.newOptionProperty(ItemConstants.INVENTORY_ACTION_PROPERTY_PREFIX, action);
			properties.put(action + 34, asciiString(type, "hidden"));
		}

		properties.put(40, ConfigUtils.newColourProperty(COLOURS));

		properties.put(78, unsignedShort(TERTIARY_MALE_MODEL, 0));
		properties.put(79, unsignedShort(TERTIARY_FEMALE_MODEL, 0));

		properties.put(90, unsignedShort(PRIMARY_MALE_HEAD_PIECE, 0));
		properties.put(91, unsignedShort(PRIMARY_FEMALE_HEAD_PIECE, 0));
		properties.put(92, unsignedShort(SECONDARY_MALE_HEAD_PIECE, 0));
		properties.put(93, unsignedShort(SECONDARY_FEMALE_HEAD_PIECE, 0));

		properties.put(95, unsignedShort(SPRITE_CAMERA_YAW, 0));

		properties.put(97, unsignedShort(NOTE_INFO_ID, 0));
		properties.put(98, unsignedShort(NOTE_TEMPLATE_ID, 0));

		for (int option = 1; option <= ItemConstants.ITEM_STACK_COUNT; option++) {
			ConfigPropertyType type = ConfigUtils.newOptionProperty(ItemConstants.ITEM_STACK_PROPERTY_PREFIX, option);
			properties.put(option + 99, new SerializableProperty<>(type, ItemStack.EMPTY, ItemStack::encode,
					ItemStack::decode, Short.BYTES * 2, input -> Optional.empty())); // FIXME parser
		}

		properties.put(110, unsignedShort(GROUND_SCALE_X, ConfigConstants.DEFAULT_SCALE));
		properties.put(111, unsignedShort(GROUND_SCALE_Y, ConfigConstants.DEFAULT_SCALE));
		properties.put(112, unsignedShort(GROUND_SCALE_Z, ConfigConstants.DEFAULT_SCALE));

		properties.put(113, signedByte(AMBIENCE, 0));
		properties.put(114, new SerializableProperty<>(CONTRAST, 0, (buffer1, contrast) -> buffer1.putByte(contrast / 5),
				buffer -> buffer.getByte() * 5, Byte.BYTES, input -> Optional.empty())); // FIXME parser

		properties.put(115, unsignedByte(TEAM, 0));

		return properties;
	}

}