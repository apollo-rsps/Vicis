package rs.emulate.legacy.widget;

import rs.emulate.shared.prop.PropertyType;

/**
 * Contains {@link PropertyType} implementations for {@link WidgetDefinition}s.
 *
 * @author Major
 */
public enum WidgetProperty implements PropertyType {

	/**
	 * The id property.
	 */
	ID,

	/**
	 * The parent id property.
	 */
	PARENT_ID,

	/**
	 * The group property.
	 */
	GROUP,

	/**
	 * The option type property.
	 */
	OPTION_TYPE,

	/**
	 * The content type property.
	 */
	CONTENT_TYPE,

	/**
	 * The width property.
	 */
	WIDTH,

	/**
	 * The height property.
	 */
	HEIGHT,

	/**
	 * The alpha value property.
	 */
	ALPHA,

	/**
	 * The hover widget id property.
	 */
	HOVER_ID,

	/**
	 * The ClientScripts property.
	 */
	CLIENTSCRIPTS,

	/**
	 * The scroll limit property. Used only by {@link WidgetGroup#CONTAINER} widgets.
	 */
	SCROLL_LIMIT,

	/**
	 * The hidden property. Used only by {@link WidgetGroup#CONTAINER} widgets.
	 */
	HIDDEN,

	/**
	 * The child ids property. Used only by {@link WidgetGroup#CONTAINER} widgets.
	 */
	CHILD_IDS,

	/**
	 * The child positions property. Used only by {@link WidgetGroup#CONTAINER} widgets.
	 */
	CHILD_POSITIONS,

	/**
	 * The inventory property. Used by {@link WidgetGroup#INVENTORY} and {@link WidgetGroup#ITEM_LIST}.
	 */
	INVENTORY,

	/**
	 * The swappable item property, indicating that items in the inventory can be swapped over. Used only by
	 * {@link WidgetGroup#INVENTORY} .
	 */
	SWAPPABLE_ITEMS,

	/**
	 * The has menu actions property, specifying whether or not the Widget has actions that are brought up when the
	 * Widget is right-clicked. Used only by {@link WidgetGroup#INVENTORY}.
	 */
	HAS_MENU_ACTIONS,

	/**
	 * The usable items property, specifying that items in the inventory can be selected and used. Used only by
	 * {@link WidgetGroup#INVENTORY}.
	 */
	USABLE_ITEMS,

	/**
	 * The replaceable items property, indicating that items in the inventory may be <strong>moved</strong> only (i.e.
	 * not swapped), overwriting the target slot. Used only by {@link WidgetGroup#INVENTORY}.
	 */
	REPLACEABLE_ITEMS,

	/**
	 * The sprite padding property. Used by {@link WidgetGroup#INVENTORY} and {@link WidgetGroup#ITEM_LIST}.
	 */
	SPRITE_PADDING,

	/**
	 * The sprites property. Used only by {@link WidgetGroup#INVENTORY}.
	 */
	SPRITES,

	/**
	 * The sprite positions property. Used only by {@link WidgetGroup#CONTAINER} widgets.
	 */
	SPRITE_POSITIONS,

	/**
	 * The menu actions property. Used by {@link WidgetGroup#INVENTORY} and {@link WidgetGroup#ITEM_LIST}.
	 */
	MENU_ACTIONS,

	/**
	 * The filled property, specifying that the widget should be drawn filled instead of an outline. Used only by
	 * {@link WidgetGroup#RECTANGULAR}.
	 */
	FILLED,

	/**
	 * The centered text property. Used by {@link WidgetGroup#MODEL_LIST}, {@link WidgetGroup#TEXT}, and
	 * {@link WidgetGroup#ITEM_LIST}.
	 */
	CENTERED_TEXT,

	/**
	 * The font property. Used by {@link WidgetGroup#MODEL_LIST}, {@link WidgetGroup#TEXT}, and
	 * {@link WidgetGroup#ITEM_LIST}.
	 */
	FONT,

	/**
	 * The shadowed text property, specifying that the Widget text should be drawn with a shadow. Used by
	 * {@link WidgetGroup#MODEL_LIST}, {@link WidgetGroup#TEXT}, and {@link WidgetGroup#ITEM_LIST}.
	 */
	SHADOWED_TEXT,

	/**
	 * The hidden text property. Used only by {@link WidgetGroup#TEXT}.
	 */
	HIDDEN_TEXT,

	/**
	 * The text property. Used only by {@link WidgetGroup#TEXT}.
	 */
	TEXT,

	/**
	 * The default colour property, specifying the default text colour (for {@link WidgetGroup#MODEL_LIST},
	 * {@link WidgetGroup#TEXT}, and {@link WidgetGroup#ITEM_LIST}), or the default fill/outline colour (for
	 * {@link WidgetGroup#RECTANGULAR}). Used by {@link WidgetGroup#MODEL_LIST}, {@link WidgetGroup#TEXT},
	 * {@link WidgetGroup#RECTANGULAR}, and {@link WidgetGroup#ITEM_LIST}.
	 */
	DEFAULT_COLOUR,

	/**
	 * The secondary colour, used in place of the {@link #DEFAULT_COLOUR} when a ClientScript state has changed. Used by
	 * {@link WidgetGroup#TEXT} and {@link WidgetGroup#RECTANGULAR}.
	 */
	SECONDARY_COLOUR,

	/**
	 * The default hover colour property. Used by {@link WidgetGroup#TEXT} and {@link WidgetGroup#RECTANGULAR}.
	 */
	DEFAULT_HOVER_COLOUR,

	/**
	 * The secondary hover colour, used in place of the {@link #DEFAULT_HOVER_COLOUR} when a ClientScript state has
	 * changed. Used by {@link WidgetGroup#TEXT} and {@link WidgetGroup#RECTANGULAR}.
	 */
	SECONDARY_HOVER_COLOUR,

	/**
	 * The default sprite property. Used only by {@link WidgetGroup#SPRITE}.
	 */
	DEFAULT_SPRITE,

	/**
	 * The secondary sprite property, used in place of the {@link #DEFAULT_SPRITE} when a ClientScript state has
	 * changed. Used only by {@link WidgetGroup#SPRITE}.
	 */
	SECONDARY_SPRITE,

	/**
	 * The default media property. Used only by {@link WidgetGroup#MODEL}.
	 */
	DEFAULT_MEDIA,

	/**
	 * The secondary media property, used in place of the {@link #DEFAULT_MEDIA} when a ClientScript state has changed.
	 * Used only by {@link WidgetGroup#MODEL}.
	 */
	SECONDARY_MEDIA,

	/**
	 * The default animation property. Used only by {@link WidgetGroup#MODEL}.
	 */
	DEFAULT_ANIMATION,

	/**
	 * The secondary animation property, used in place of the {@link #DEFAULT_ANIMATION} when a ClientScript state has
	 * changed. Used only by {@link WidgetGroup#MODEL}.
	 */
	SECONDARY_ANIMATION,

	/**
	 * The model scale property. Used only by {@link WidgetGroup#MODEL}.
	 */
	MODEL_SCALE,

	/**
	 * The model pitch property. Used only by {@link WidgetGroup#MODEL}.
	 */
	MODEL_PITCH,

	/**
	 * The model roll property. Used only by {@link WidgetGroup#MODEL}.
	 */
	MODEL_ROLL,

	/**
	 * The option circumfix property. Used by {@link WidgetGroup#INVENTORY}, or any Widget which has an
	 * {@link #OPTION_TYPE} of {@link WidgetOption#USABLE}.
	 */
	OPTION_CIRCUMFIX,

	/**
	 * The option text property. Used by {@link WidgetGroup#INVENTORY}, or any Widget which has an {@link #OPTION_TYPE}
	 * of {@link WidgetOption#USABLE}.
	 */
	OPTION_TEXT,

	/**
	 * The hover text property. Used by any Widget which has an {@link #OPTION_TYPE} of {@link WidgetOption#OK},
	 * {@link WidgetOption#TOGGLE_SETTING}, {@link WidgetOption#RESET_SETTING}, or {@link WidgetOption#CONTINUE}.
	 */
	HOVER_TEXT,

}