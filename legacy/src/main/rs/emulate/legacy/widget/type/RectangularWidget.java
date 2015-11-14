package rs.emulate.legacy.widget.type;

import rs.emulate.legacy.widget.Widget;
import rs.emulate.legacy.widget.WidgetGroup;
import rs.emulate.legacy.widget.WidgetOption;
import rs.emulate.legacy.widget.script.LegacyClientScript;
import rs.emulate.shared.util.DataBuffer;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * A {@link WidgetGroup#RECTANGULAR RECTANGULAR} {@link Widget}.
 *
 * @author Major
 */
public final class RectangularWidget extends Widget {

	/**
	 * The ColourPair containing the default and secondary colours of the rectangle.
	 */
	private ColourPair colour;

	/**
	 * Whether or not this RectangularWidget is drawn filled.
	 */
	private boolean filled;

	/**
	 * The ColourPair containing the default and secondary hover colours.
	 */
	private ColourPair hover;

	/**
	 * Creates the RectangleWidget.
	 *
	 * @param id The id of the RectangleWidget.
	 * @param parent The parent id of the RectangleWidget, as an {@link OptionalInt}. Must not be {@code null}.
	 * @param optionType The {@link WidgetOption} of the RectangleWidget. Must not be {@code null}.
	 * @param content The content type of the RectangleWidget.
	 * @param width The width of the RectangleWidget, in pixels.
	 * @param height The width of the RectangleWidget, in pixels.
	 * @param alpha The alpha of the RectangleWidget, in pixels.
	 * @param hoverId The hover id of the RectangleWidget, as an OptionalInt. Must not be {@code null}.
	 * @param scripts The {@link List} of {@link LegacyClientScript}s. Must not be {@code null}.
	 * @param option The {@link Option} of the RectangularWidget, as an {@link Optional}. Must not be {@code null}.
	 * @param hoverText The hover text of the RectangularWidget, as an {@link Optional}. Must not be {@code null}.
	 * @param filled Whether or not the RectangularWidget is drawn filled.
	 * @param colour The {@link ColourPair} containing the default and secondary colours of the rectangle.
	 * @param hover The ColourPair containing the default and secondary hover colours.
	 */
	public RectangularWidget(int id, OptionalInt parent, WidgetOption optionType, int content, int width, int height, int alpha,
	                         OptionalInt hoverId, List<LegacyClientScript> scripts, Optional<Option> option, Optional<String> hoverText,
	                         boolean filled, ColourPair colour, ColourPair hover) {
		super(id, parent, WidgetGroup.RECTANGULAR, optionType, content, width, height, alpha, hoverId, scripts, option, hoverText);

		this.filled = filled;
		this.colour = colour;
		this.hover = hover;
	}

	/**
	 * Gets the {@link ColourPair} containing the default and secondary colours of the rectangle.
	 *
	 * @return The ColourPair.
	 */
	public ColourPair getColour() {
		return colour;
	}

	/**
	 * Gets the {@link ColourPair} containing the default and secondary hover colours.
	 *
	 * @return The ColourPair.
	 */
	public ColourPair getHover() {
		return hover;
	}

	/**
	 * Returns whether or not this RectangularWidget is drawn filled.
	 *
	 * @return {@code true} if this RectangularWidget is drawn filled, {@code false} if not.
	 */
	public boolean isFilled() {
		return filled;
	}

	/**
	 * Sets the {@link ColourPair} containing the default and secondary colours of the rectangle.
	 *
	 * @param colour The ColourPair.
	 */
	public void setColour(ColourPair colour) {
		this.colour = colour;
	}

	/**
	 * Sets whether or not this RectangularWidget is drawn filled.
	 *
	 * @param filled {@code true} if this RectangularWidget should be drawn filled, {@code false} if not.
	 */
	public void setFilled(boolean filled) {
		this.filled = filled;
	}

	/**
	 * Sets the {@link ColourPair} containing the default and secondary hover colours.
	 *
	 * @param hover The ColourPair.
	 */
	public void setHover(ColourPair hover) {
		this.hover = hover;
	}

	@Override
	protected DataBuffer encodeBespoke() {
		DataBuffer buffer = DataBuffer.allocate(4 * Integer.BYTES + Byte.BYTES);
		buffer.putBoolean(filled);

		for (ColourPair pair : Arrays.asList(colour, hover)) {
			buffer.putInt(pair.getDefault());
			buffer.putInt(pair.getSecondary());
		}

		return buffer.flip().asReadOnlyBuffer();
	}

}