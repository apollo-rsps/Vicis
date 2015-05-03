package rs.emulate.legacy.widget;

import java.util.Arrays;

/**
 * The group a Widget belongs in.
 *
 * @author Major
 */
public enum WidgetGroup {

	/**
	 * The container group, specifying that the Widget is a parent of at least one other Widget.
	 */
	CONTAINER(0),

	/**
	 * The model list group, specifying that the Widget contains a list of models. Despite being present since RS2 beta,
	 * even the 377 client did not have full support for this group, and no widgets use it.
	 */
	MODEL_LIST(1),

	/**
	 * The inventory group, specifying that the Widget has an inventory.
	 */
	INVENTORY(2),

	/**
	 * The rectangular group, specifying that the Widget is rectangular and may be drawn as such.
	 */
	RECTANGULAR(3),

	/**
	 * The text group, specifying that the Widget contains text (and also adds support for e.g. displaying an amount
	 * String, calculated using a ClientScript).
	 */
	TEXT(4),

	/**
	 * The sprite group, specifying that the Widget is a container for a Sprite (with optional support for changing the
	 * Sprite to another using a ClientScript).
	 */
	SPRITE(5),

	/**
	 * The model group, specifying that the Widget is a container for a model, which may be animated (and optionally
	 * changed using a ClientScript).
	 */
	MODEL(6),

	/**
	 * The item list group, specifying that a Widget has a bespoke inventory, which serves only as a list of items.
	 */
	ITEM_LIST(7);

	/**
	 * Gets the WidgetGroup with the specified integer value.
	 * 
	 * @param value The integer value.
	 * @return The WidgetGroup.
	 * @throws IllegalArgumentException If no WidgetGroup with the specified integer value exists.
	 */
	public static WidgetGroup valueOf(int value) {
		return Arrays.stream(values()).filter(group -> group.value == value).findAny()
				.orElseThrow(() -> new IllegalArgumentException("No WidgetGroup with a value of " + value + " exists."));
	}

	/**
	 * The integer value of this WidgetGroup.
	 */
	private final int value;

	/**
	 * Creates the WidgetGroup.
	 *
	 * @param value The integer value of this WidgetGroup.
	 */
	private WidgetGroup(int value) {
		this.value = value;
	}

	/**
	 * Gets the integer value of this WidgetGroup.
	 * 
	 * @return The integer value.
	 */
	public int toInteger() {
		return value;
	}

}