package rs.emulate.legacy.widget;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

import rs.emulate.legacy.widget.script.LegacyClientScript;
import rs.emulate.legacy.widget.script.LegacyClientScriptCodec;
import rs.emulate.legacy.widget.type.Option;
import rs.emulate.shared.util.DataBuffer;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

/**
 * 
 *
 * @author Major
 */
public abstract class Widget {

	/**
	 * The value indicating that a Widget does not have a hover id.
	 */
	private static final int HOVER_ID_ABSENT = 0;

	/**
	 * The value indicating that a Widget has a parent id.
	 */
	private static final int PARENT_ID_PRESENT = 65_535;

	/**
	 * The alpha of the Widget.
	 */
	private final int alpha;

	/**
	 * The content type of the Widget.
	 */
	private final int content;

	/**
	 * The WidgetGroup of the Widget.
	 */
	private final WidgetGroup group;

	/**
	 * The height of the Widget, in pixels.
	 */
	private final int height;

	/**
	 * The hover id of the Widget, as an OptionalInt.
	 */
	private final OptionalInt hover;

	/**
	 * The hover text of this Widget.
	 */
	private Optional<String> hoverText;

	/**
	 * The id of the Widget.
	 */
	private final int id;

	/**
	 * The Option of this Widget.
	 */
	private final Optional<Option> option;

	/**
	 * The WidgetOption of the Widget.
	 */
	private final WidgetOption optionType;

	/**
	 * The parent of the Widget, as an OptionalInt.
	 */
	private final OptionalInt parent;

	/**
	 * The List of LegacyClientScripts of the Widget.
	 */
	private final List<LegacyClientScript> scripts;

	/**
	 * The width of this Widget, in pixels.
	 */
	private final int width;

	/**
	 * Creates the GenericWidgetProperties.
	 *
	 * @param id The id of the Widget.
	 * @param parent The parent id of the Widget, as an {@link OptionalInt}. Must not be {@code null}.
	 * @param group The {@link WidgetGroup} of the Widget. Must not be {@code null}.
	 * @param optionType The {@link WidgetOption} of the Widget. Must not be {@code null}.
	 * @param content The content type of the Widget.
	 * @param width The width of the Widget, in pixels.
	 * @param height The width of the Widget, in pixels.
	 * @param alpha The alpha of the Widget, in pixels.
	 * @param hover The hover id of the Widget, as an {@link OptionalInt}. Must not be {@code null}.
	 * @param scripts The {@link List} of {@link LegacyClientScript}s. Must not be {@code null}.
	 * @param option The {@link Option} of the Widget, wrapped in an {@link Optional}.
	 * @param hoverText The hover text of the Widget, wrapped in an Optional.
	 */
	public Widget(int id, OptionalInt parent, WidgetGroup group, WidgetOption optionType, int content, int width, int height,
			int alpha, OptionalInt hover, List<LegacyClientScript> scripts, Optional<Option> option, Optional<String> hoverText) {
		this.id = id;
		this.parent = Objects.requireNonNull(parent, "Parent id must not be null.");
		this.group = Objects.requireNonNull(group, "WidgetGroup must not be null.");
		this.optionType = Objects.requireNonNull(optionType, "WidgetOption must not be null.");
		this.content = content;
		this.width = width;
		this.height = height;
		this.alpha = alpha;
		this.hover = Objects.requireNonNull(hover, "Hover id must not be null.");
		this.scripts = ImmutableList.copyOf(Objects.requireNonNull(scripts, "ClientScript List must not be null"));
		this.option = option;
		this.hoverText = hoverText;
	}

	/**
	 * Encodes this Widget into a {@link DataBuffer}.
	 * 
	 * @return The DataBuffer.
	 */
	public final DataBuffer encode() {
		DataBuffer bespoke = encodeBespoke();
		DataBuffer scripts = LegacyClientScriptCodec.encode(this.scripts);
		DataBuffer option = encodeOption();
		DataBuffer hoverText = encodeHoverText();

		int parentSize = parent.isPresent() ? 2 * Short.BYTES : Short.BYTES;
		int hoverSize = hover.isPresent() ? Short.BYTES : Byte.BYTES;
		int metaSize = 4 * Short.BYTES + 3 * Byte.BYTES;
		int specific = scripts.remaining() + bespoke.remaining() + option.remaining() + hoverText.remaining();

		int size = parentSize + metaSize + hoverSize + specific;

		DataBuffer buffer = DataBuffer.allocate(size);

		if (parent.isPresent()) {
			buffer.putShort(PARENT_ID_PRESENT);
			buffer.putShort(id);
			buffer.putShort(parent.getAsInt());
		} else {
			buffer.putShort(id);
		}

		buffer.putByte(group.toInteger());
		buffer.putByte(optionType.toInteger());

		buffer.putShort(content);
		buffer.putShort(width);
		buffer.putShort(height);

		buffer.putByte(alpha);

		if (hover.isPresent()) {
			int value = hover.getAsInt();
			buffer.putByte(value + 1 >> 8);
			buffer.putByte(value);
		} else {
			buffer.putByte(HOVER_ID_ABSENT);
		}

		buffer.put(scripts);
		buffer.put(bespoke);
		buffer.put(option);

		return buffer.flip().asReadOnlyBuffer();
	}

	/**
	 * Gets the alpha.
	 *
	 * @return The alpha.
	 */
	public final int getAlpha() {
		return alpha;
	}

	/**
	 * Gets the content type.
	 *
	 * @return The content type.
	 */
	public final int getContentType() {
		return content;
	}

	/**
	 * Gets the {@link WidgetGroup}.
	 *
	 * @return The WidgetGroup.
	 */
	public final WidgetGroup getGroup() {
		return group;
	}

	/**
	 * Gets the height, in pixels.
	 *
	 * @return The height, in pixels.
	 */
	public final int getHeight() {
		return height;
	}

	/**
	 * Gets the hover id, as an {@link OptionalInt}.
	 *
	 * @return The hover id.
	 */
	public final OptionalInt getHoverId() {
		return hover;
	}

	/**
	 * Gets the id.
	 *
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the {@link WidgetOption}.
	 *
	 * @return The WidgetOption.
	 */
	public final WidgetOption getOption() {
		return optionType;
	}

	/**
	 * Gets the parent id, as an {@link OptionalInt}.
	 *
	 * @return The parent id.
	 */
	public final OptionalInt getParentId() {
		return parent;
	}

	/**
	 * Gets the {@link List} of {@link LegacyClientScript}s.
	 *
	 * @return The List of LegacyClientScripts.
	 */
	public final List<LegacyClientScript> getScripts() {
		return scripts;
	}

	/**
	 * Gets the width, in pixels.
	 *
	 * @return The width, in pixels.
	 */
	public final int getWidth() {
		return width;
	}

	/**
	 * Encodes the bespoke data belonging to this Widget into a {@link DataBuffer}.
	 * 
	 * @return The DataBuffer.
	 */
	protected abstract DataBuffer encodeBespoke();

	/**
	 * Encodes the hover text of this Widget into a {@link DataBuffer}.
	 * 
	 * @return The DataBuffer.
	 */
	private DataBuffer encodeHoverText() {
		if (hoverText.isPresent()) {
			String text = hoverText.get();
			DataBuffer buffer = DataBuffer.allocate(text.length() + Byte.BYTES);
			buffer.putAsciiString(text);

			return buffer.flip().asReadOnlyBuffer();
		}

		return DataBuffer.allocate(0);
	}

	/**
	 * Encodes the {@link Option} of this Widget into a {@link DataBuffer}.
	 * 
	 * @return The DataBuffer.
	 */
	private DataBuffer encodeOption() {
		if (option.isPresent()) {
			Preconditions.checkArgument(group == WidgetGroup.INVENTORY || optionType == WidgetOption.USABLE,
					"Only usable or inventory widgets may have an option.");

			Option option = this.option.get();
			String circumfix = option.getCircumfix(), text = option.getText();

			DataBuffer buffer = DataBuffer.allocate(circumfix.length() + text.length() + 2 * Byte.BYTES + Short.BYTES);
			buffer.putAsciiString(circumfix).putAsciiString(text);

			int attributes = option.getAttributes();
			buffer.putShort(attributes);

			return buffer.flip().asReadOnlyBuffer();
		}

		return DataBuffer.allocate(0);
	}

}