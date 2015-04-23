package rs.emulate.legacy.widget;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import rs.emulate.legacy.widget.script.LegacyClientScript;
import rs.emulate.legacy.widget.type.Option;

/**
 * A builder for a {@link Widget}.
 *
 * @author Major
 */
@SuppressWarnings("unused")
public final class WidgetBuilder {

	/**
	 * The alpha of the Widget.
	 */
	private int alpha;

	/**
	 * The content type of the Widget.
	 */
	private int content;

	/**
	 * The WidgetGroup of the Widget.
	 */
	private WidgetGroup group;

	/**
	 * The height of the Widget, in pixels.
	 */
	private int height;

	/**
	 * The hover id of the Widget, as an OptionalInt.
	 */
	private OptionalInt hover;

	/**
	 * The hover text of this Widget.
	 */
	private Optional<String> hoverText;

	/**
	 * The id of the Widget.
	 */
	private int id;

	/**
	 * The Option of this Widget.
	 */
	private Optional<Option> option;

	/**
	 * The WidgetOption of the Widget.
	 */
	private WidgetOption optionType;

	/**
	 * The parent of the Widget, as an OptionalInt.
	 */
	private OptionalInt parent;

	/**
	 * The List of LegacyClientScripts of the Widget.
	 */
	private List<LegacyClientScript> scripts;

	/**
	 * The width of this Widget, in pixels.
	 */
	private int width;

}