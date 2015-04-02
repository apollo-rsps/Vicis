package rs.emulate.legacy.widget.type;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import rs.emulate.legacy.widget.WidgetGroup;
import rs.emulate.legacy.widget.WidgetOption;
import rs.emulate.legacy.widget.script.LegacyClientScript;
import rs.emulate.shared.util.DataBuffer;

/**
 * A {@link WidgetGroup#TEXT} {@link Widget}.
 *
 * @author Major
 */
public final class TextWidget extends Widget {

	/**
	 * Whether or not the text is centered.
	 */
	private boolean centered;

	/**
	 * The ColourPair containing the default and secondary colours of the rectangle.
	 */
	private ColourPair colour;

	/**
	 * The default text displayed.
	 */
	private String defaultText;

	/**
	 * The id of the font used for the text.
	 */
	private int font;

	/**
	 * The ColourPair containing the default and secondary hover colours.
	 */
	private ColourPair hover;

	/**
	 * The secondary text, displayed when a ClientScript state has changed.
	 */
	private String secondaryText;

	/**
	 * Whether or not text is drawn with shadowing.
	 */
	private boolean shadowed;

	/**
	 * Creates the TextWidget.
	 *
	 * @param id The id of the TextWidget.
	 * @param parent The parent id of the TextWidget, as an {@link OptionalInt}. Must not be {@code null}.
	 * @param optionType The {@link WidgetOption} of the TextWidget. Must not be {@code null}.
	 * @param content The content type of the TextWidget.
	 * @param width The width of the TextWidget, in pixels.
	 * @param height The width of the TextWidget, in pixels.
	 * @param alpha The alpha of the TextWidget, in pixels.
	 * @param hoverId The hover id of the TextWidget, as an {@link OptionalInt}. Must not be {@code null}.
	 * @param scripts The {@link List} of {@link LegacyClientScript}s. Must not be {@code null}.
	 * @param option The {@link Option} of the TextWidget, as an {@link Optional}. Must not be {@code null}.
	 * @param hoverText The hover text of the TextWidget, as an {@link Optional}. Must not be {@code null}.
	 * @param centered Whether or not the text is centered.
	 * @param colour The {@link ColourPair} containing the default and secondary colours of the text.
	 * @param defaultText The default text displayed.
	 * @param font The id of the font used to draw the text with.
	 * @param hover The {@link ColourPair} containing the default and secondary hover colours.
	 * @param secondaryText The secondary text, displayed when a ClientScript state has changed.
	 * @param shadowed Whether or not text is drawn with shadowing.
	 */
	public TextWidget(int id, OptionalInt parent, WidgetOption optionType, int content, int width, int height, int alpha,
			OptionalInt hoverId, List<LegacyClientScript> scripts, Optional<Option> option, Optional<String> hoverText,
			boolean centered, ColourPair colour, String defaultText, int font, ColourPair hover, String secondaryText,
			boolean shadowed) {
		super(id, parent, WidgetGroup.TEXT, optionType, content, width, height, alpha, hoverId, scripts, option, hoverText);

		this.centered = centered;
		this.colour = colour;
		this.defaultText = defaultText;
		this.font = font;
		this.hover = hover;
		this.secondaryText = secondaryText;
		this.shadowed = shadowed;
	}

	@Override
	protected DataBuffer encodeBespoke() {
		DataBuffer primary = DataBuffer.allocate(defaultText.length() + Byte.BYTES);
		primary.putAsciiString(defaultText).flip();

		DataBuffer secondary = DataBuffer.allocate(secondaryText.length() + Byte.BYTES);
		secondary.putAsciiString(secondaryText).flip();

		DataBuffer buffer = DataBuffer.allocate(3 * Byte.BYTES + 4 * Integer.BYTES + primary.remaining() + secondary.remaining());
		buffer.putBoolean(centered);
		buffer.putByte(font);
		buffer.putBoolean(shadowed);

		buffer.put(primary).put(secondary);

		for (ColourPair pair : Arrays.asList(colour, hover)) {
			buffer.putInt(pair.getDefault());
			buffer.putInt(pair.getSecondary());
		}

		return buffer.flip().asReadOnlyBuffer();
	}

}