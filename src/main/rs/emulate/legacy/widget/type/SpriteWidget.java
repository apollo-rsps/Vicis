package rs.emulate.legacy.widget.type;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

import rs.emulate.legacy.widget.WidgetGroup;
import rs.emulate.legacy.widget.WidgetOption;
import rs.emulate.legacy.widget.script.LegacyClientScript;
import rs.emulate.shared.util.DataBuffer;

/**
 * A {@link WidgetGroup#SPRITE} {@link Widget}.
 *
 * @author Major
 */
public final class SpriteWidget extends Widget {

	/**
	 * The name of the default sprite, wrapped in an Optional.
	 */
	private String primary;

	/**
	 * The name of the secondary sprite, wrapped in an Optional.
	 */
	private String secondary;

	/**
	 * Creates the SpriteWidget.
	 *
	 * @param id The id of the SpriteWidget.
	 * @param parent The parent id of the SpriteWidget, as an {@link OptionalInt}. Must not be {@code null}.
	 * @param optionType The {@link WidgetOption} of the SpriteWidget. Must not be {@code null}.
	 * @param content The content type of the SpriteWidget.
	 * @param width The width of the SpriteWidget, in pixels.
	 * @param height The width of the SpriteWidget, in pixels.
	 * @param alpha The alpha of the SpriteWidget, in pixels.
	 * @param hover The hover id of the SpriteWidget, as an {@link OptionalInt}. Must not be {@code null}.
	 * @param scripts The {@link List} of {@link LegacyClientScript}s. Must not be {@code null}.
	 * @param option The {@link Option} of the SpriteWidget, as an {@link Optional}. Must not be {@code null}.
	 * @param hoverText The hover text of the SpriteWidget, as an {@link Optional}. Must not be {@code null}.
	 * @param defaultSprite The name of the default Sprite. Must not be {@code null}.
	 * @param secondarySprite The name of the secondary Sprite. Must not be {@code null}.
	 */
	public SpriteWidget(int id, OptionalInt parent, WidgetOption optionType, int content, int width, int height, int alpha,
			OptionalInt hover, List<LegacyClientScript> scripts, Optional<Option> option, Optional<String> hoverText,
			String defaultSprite, String secondarySprite) {
		super(id, parent, WidgetGroup.SPRITE, optionType, content, width, height, alpha, hover, scripts, option, hoverText);

		this.primary = Objects.requireNonNull(defaultSprite, "Default sprite name must not be null.");
		this.secondary = Objects.requireNonNull(secondarySprite, "Default sprite name must not be null.");
	}

	@Override
	protected DataBuffer encodeBespoke() {
		DataBuffer buffer = DataBuffer.allocate(primary.length() + secondary.length() + 2 * Byte.BYTES);
		buffer.putAsciiString(primary);
		buffer.putAsciiString(secondary);

		return buffer.flip().asReadOnlyBuffer();
	}

}