package rs.emulate.legacy.widget;

import java.util.Arrays;

/**
 * The type of option a Widget may have.
 * <p>
 * Thanks to Dane for providing information about some of the enumerators.
 * 
 * @author Major
 */
public enum WidgetOption {

	/**
	 * The OK option, indicating that the Widget's hover text will be set to 'OK'.
	 */
	OK(1),

	/**
	 * The usable option, indicating that the Widget is usable, and can be selected as part of the Widget on X frames
	 * (for example, to cast a spell on a player or item).
	 */
	USABLE(2),

	/**
	 * The close option, indicating that the Widget's hover text will be set to 'Close', and that the top-level
	 * interfaces will be closed when the Widget is interacted with.
	 */
	CLOSE(3),

	/**
	 * The toggle setting option, indicating that the Widget's hover text will be set to 'Select', and that the setting
	 * specified by the first instruction in the first ClientScript will have its value toggled when the Widget is
	 * interacted with.
	 */
	TOGGLE_SETTING(4),

	/**
	 * The toggle setting option, indicating that the Widget's hover text will be set to 'Select', and that the setting
	 * specified by the first instruction in the first ClientScript will have its value toggled when the Widget is
	 * interacted with.
	 */
	RESET_SETTING(5),

	/**
	 * The continue option, indicating that the Widget's hover text will be set to 'Continue', and that the client
	 * should send the Continue Dialogue frame when the Widget is interacted with.
	 */
	CONTINUE(6);

	/**
	 * Gets the WidgetOption with the specified integer value.
	 * 
	 * @param value The integer value.
	 * @return The WidgetOption.
	 * @throws IllegalArgumentException If there is no WidgetOption with the specified integer value.
	 */
	public static WidgetOption valueOf(int value) {
		return Arrays.stream(values()).filter(option -> option.value == value).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("No WidgetOption with the specified value exists."));
	}

	/**
	 * The integer value of this WidgetOption.
	 */
	private final int value;

	/**
	 * Creates the WidgetOption.
	 *
	 * @param value The integer value of the WidgetOption.
	 */
	private WidgetOption(int value) {
		this.value = value;
	}

	/**
	 * Gets the integer value of this WidgetOption.
	 * 
	 * @return The integer value.
	 */
	public int toInteger() {
		return value;
	}

}