package rs.emulate.legacy.widget

import rs.emulate.legacy.widget.script.LegacyClientScript
import rs.emulate.legacy.widget.type.Option

/**
 * A builder for a [Widget].
 */
class WidgetBuilder {

    /**
     * The alpha of the Widget.
     */
    private var alpha: Int = 0

    /**
     * The content type of the Widget.
     */
    private var content: Int = 0

    /**
     * The WidgetGroup of the Widget.
     */
    private var group: WidgetGroup? = null

    /**
     * The height of the Widget, in pixels.
     */
    private var height: Int = 0

    /**
     * The hover id of the Widget.
     */
    private var hover: Int? = null

    /**
     * The hover text of this Widget.
     */
    private var hoverText: String? = null

    /**
     * The id of the Widget.
     */
    private var id: Int = 0

    /**
     * The Option of this Widget.
     */
    private var option: Option? = null

    /**
     * The WidgetOption of the Widget.
     */
    private var optionType: WidgetOption? = null

    /**
     * The parent of the Widget.
     */
    private var parent: Int? = null

    /**
     * The [LegacyClientScript]s of the Widget.
     */
    private var scripts: List<LegacyClientScript>? = null

    /**
     * The width of this Widget, in pixels.
     */
    private var width: Int = 0

}
