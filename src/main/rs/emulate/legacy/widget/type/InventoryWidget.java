package rs.emulate.legacy.widget.type;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

import rs.emulate.legacy.widget.Widget;
import rs.emulate.legacy.widget.WidgetGroup;
import rs.emulate.legacy.widget.WidgetOption;
import rs.emulate.legacy.widget.script.LegacyClientScript;
import rs.emulate.shared.util.DataBuffer;
import rs.emulate.shared.util.Point;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

/**
 * Contains properties used by {@link WidgetGroup#INVENTORY}.
 *
 * @author Major
 */
public final class InventoryWidget extends Widget {

	/**
	 * Whether or not items in the inventory are replaced (instead of swapped).
	 */
	private final boolean replace;

	/**
	 * The Point containing the padding for the sprites.
	 */
	private final Point padding;

	/**
	 * The List of Points for the sprites.
	 */
	private final List<Point> spritePoints;

	/**
	 * The List of sprite names.
	 */
	private final List<Optional<String>> sprites;

	/**
	 * Whether or not items in the inventory are swappable.
	 */
	private final boolean swappable;

	/**
	 * Whether or not the items in the inventory are usable.
	 */
	private final boolean usable;

	/**
	 * The List of menu actions.
	 */
	private final List<String> actions;

	/**
	 * Creates the InventoryProperties.
	 * 
	 * @param id The id of the TextWidget.
	 * @param parent The parent id of the TextWidget, as an {@link OptionalInt}. Must not be {@code null}.
	 * @param optionType The {@link WidgetOption} of the TextWidget. Must not be {@code null}.
	 * @param content The content type of the TextWidget.
	 * @param width The width of the TextWidget, in pixels.
	 * @param height The width of the TextWidget, in pixels.
	 * @param alpha The alpha of the TextWidget, in pixels.
	 * @param hover The hover id of the TextWidget, as an {@link OptionalInt}. Must not be {@code null}.
	 * @param scripts The {@link List} of {@link LegacyClientScript}s. Must not be {@code null}.
	 * @param option The {@link Option} of the InventoryWidget, as an {@link Optional}. Must not be {@code null}.
	 * @param hoverText The hover text of the InventoryWidget, as an {@link Optional}. Must not be {@code null}.
	 * @param swappable Whether or not items in the inventory are swappable.
	 * @param usable Whether or not the items in the inventory are usable.
	 * @param replace Whether or not items in the inventory are replaced (instead of swapped).
	 * @param padding The {@link Point} containing the padding for the sprites.
	 * @param sprites The {@link List} of sprite names.
	 * @param spritePoints The List of Points for the sprites.
	 * @param actions The List of menu actions.
	 */
	public InventoryWidget(int id, OptionalInt parent, WidgetOption optionType, int content, int width, int height, int alpha,
			OptionalInt hover, List<LegacyClientScript> scripts, Optional<Option> option, Optional<String> hoverText,
			boolean swappable, boolean usable, boolean replace, Point padding, List<Optional<String>> sprites,
			List<Point> spritePoints, List<String> actions) {
		super(id, parent, WidgetGroup.INVENTORY, optionType, content, width, height, alpha, hover, scripts, option, hoverText);

		this.swappable = swappable;
		this.usable = usable;
		this.replace = replace;
		this.padding = Objects.requireNonNull(padding, "Sprite padding must not be null.");
		this.sprites = ImmutableList.copyOf(Objects.requireNonNull(sprites, "Sprite name List must not be null."));
		this.spritePoints = ImmutableList.copyOf(Objects.requireNonNull(spritePoints, "Sprite Point List must not be null."));
		this.actions = ImmutableList.copyOf(Objects.requireNonNull(actions, "Actions List must not be null."));

		Preconditions.checkArgument(sprites.size() == spritePoints.size(), "Sprite names and Points must have an equal size.");
	}

	@Override
	public DataBuffer encodeBespoke() {
		int size = actions.stream().mapToInt(String::length).sum() + actions.size();
		size += sprites.stream().mapToInt(name -> name.orElse("").length() + 2 * Short.BYTES + Byte.BYTES).sum();

		DataBuffer action = DataBuffer.allocate(size);
		actions.forEach(action::putAsciiString);
		action.flip();

		size = sprites.stream().mapToInt(name -> name.orElse("").length() + 2 * Short.BYTES + Byte.BYTES).sum();
		DataBuffer sprite = DataBuffer.allocate(size);

		for (int index = 0; index < sprites.size(); index++) {
			Optional<String> name = sprites.get(index);
			boolean present = name.isPresent();
			sprite.putBoolean(present);

			if (present) {
				Point point = spritePoints.get(index);
				sprite.putShort(point.getX()).putShort(point.getY());
				sprite.putAsciiString(name.get());
			}
		}

		sprite.flip();

		DataBuffer buffer = DataBuffer.allocate(6 * Byte.BYTES + action.remaining() + sprite.remaining());

		buffer.putBoolean(swappable);
		buffer.putBoolean(this.actions.size() == 0);
		buffer.putBoolean(usable);
		buffer.putBoolean(replace);

		buffer.putByte(padding.getX()).putBoolean(padding.getY());
		buffer.put(sprite);
		buffer.put(action);

		return buffer.flip().asReadOnlyBuffer();
	}

}