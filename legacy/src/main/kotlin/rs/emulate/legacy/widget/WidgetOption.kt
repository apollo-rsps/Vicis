package rs.emulate.legacy.widget

/**
 * The type of option a Widget may have.
 */
enum class WidgetOption(val value: Int) {
    NONE(0),

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

    companion object {

        /**
         * Gets the WidgetOption with the specified integer value.
         */
        fun valueOf(value: Int): WidgetOption {
            return values().find { it.value == value } ?: throw IllegalArgumentException(
                "No WidgetOption with the specified value ($value) exists.")
        }
    }

}
