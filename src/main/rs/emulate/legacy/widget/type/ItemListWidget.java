package rs.emulate.legacy.widget.type;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import rs.emulate.legacy.widget.Widget;
import rs.emulate.legacy.widget.WidgetGroup;
import rs.emulate.legacy.widget.WidgetOption;
import rs.emulate.legacy.widget.script.LegacyClientScript;
import rs.emulate.shared.util.DataBuffer;
import rs.emulate.shared.util.Point;

/**
 * A {@link WidgetGroup#ITEM_LIST} {@link Widget}.
 *
 * @author Major
 */
public final class ItemListWidget extends Widget {

	/**
	 * The List of menu actions.
	 */
	private List<String> actions;

	/**
	 * Whether or not the text is centered.
	 */
	private boolean centered;

	/**
	 * The colour of the text.
	 */
	private int colour;

	/**
	 * The id of the font used for the text.
	 */
	private int font;

	/**
	 * The Point containing the padding for the sprites.
	 */
	private Point padding;

	/**
	 * Whether or not text is drawn with shadowing.
	 */
	private boolean shadowed;

	/**
	 * Creates the ItemListWidget.
	 *
	 * @param id The id of the ItemListWidget.
	 * @param parent The parent id of the ItemListWidget, as an {@link OptionalInt}. Must not be {@code null}.
	 * @param optionType The {@link WidgetOption} of the ItemListWidget. Must not be {@code null}.
	 * @param content The content type of the ItemListWidget.
	 * @param width The width of the ItemListWidget, in pixels.
	 * @param height The width of the ItemListWidget, in pixels.
	 * @param alpha The alpha of the ItemListWidget, in pixels.
	 * @param hover The hover id of the ItemListWidget, as an {@link OptionalInt}. Must not be {@code null}.
	 * @param scripts The {@link List} of {@link LegacyClientScript}s. Must not be {@code null}.
	 * @param option The {@link Option} of the ItemListWidget, as an {@link Optional}. Must not be {@code null}.
	 * @param hoverText The hover text of the ItemListWidget, as an {@link Optional}. Must not be {@code null}.
	 * @param centered Whether or not the text is centered.
	 * @param colour The {@link ColourPair} containing the default and secondary colours of the text.
	 * @param font The id of the font used to draw the text with.
	 * @param shadowed Whether or not text is drawn with shadowing.
	 * @param actions The {@link List} of menu actions.
	 * @param padding The sprite padding.
	 */
	public ItemListWidget(int id, OptionalInt parent, WidgetOption optionType, int content, int width, int height, int alpha,
			OptionalInt hover, List<LegacyClientScript> scripts, Optional<Option> option, Optional<String> hoverText,
			boolean centered, int colour, int font, boolean shadowed, List<String> actions, Point padding) {
		super(id, parent, WidgetGroup.ITEM_LIST, optionType, content, width, height, alpha, hover, scripts, option, hoverText);

		this.centered = centered;
		this.colour = colour;
		this.font = font;
		this.shadowed = shadowed;
		this.actions = actions;
		this.padding = padding;
	}

	@Override
	protected DataBuffer encodeBespoke() {
		int size = actions.stream().mapToInt(String::length).sum() + actions.size();

		DataBuffer action = DataBuffer.allocate(size);
		actions.forEach(action::putAsciiString);
		action.flip();

		DataBuffer buffer = DataBuffer.allocate(4 * Byte.BYTES + 2 * Short.BYTES + Integer.BYTES + action.remaining());
		buffer.putBoolean(centered);
		buffer.putByte(font);
		buffer.putBoolean(shadowed);

		buffer.putInt(colour);
		buffer.putShort(padding.getX()).putShort(padding.getY());

		buffer.putBoolean(!actions.isEmpty());
		buffer.put(action);

		return buffer.flip().asReadOnlyBuffer();
	}

}