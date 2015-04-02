package rs.emulate.legacy.widget.type;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

import rs.emulate.legacy.widget.WidgetGroup;
import rs.emulate.legacy.widget.WidgetOption;
import rs.emulate.legacy.widget.script.LegacyClientScript;
import rs.emulate.shared.util.DataBuffer;
import rs.emulate.shared.util.Point;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

/**
 * Contains properties used by {@link WidgetGroup#CONTAINER} {@link Widget}s.
 *
 * @author Major
 */
public final class ContainerWidget extends Widget {

	/**
	 * The {@link Point}s of the child Widgets.
	 */
	private List<Point> childPoints;

	/**
	 * The child ids of the Widget.
	 */
	private int[] children;

	/**
	 * Indicates whether or not the Widget is hidden.
	 */
	private boolean hidden;

	/**
	 * The scroll limit of the container Widget.
	 */
	private int scrollLimit;

	/**
	 * Creates the ContainerWidget.
	 * 
	 * @param id The id of the ContainerWidget.
	 * @param parent The parent id of the ContainerWidget, as an {@link OptionalInt}. Must not be {@code null}.
	 * @param optionType The {@link WidgetOption} of the ContainerWidget. Must not be {@code null}.
	 * @param content The content type of the ContainerWidget.
	 * @param width The width of the ContainerWidget, in pixels.
	 * @param height The width of the ContainerWidget, in pixels.
	 * @param alpha The alpha of the ContainerWidget, in pixels.
	 * @param hover The hover id of the ContainerWidget, as an {@link OptionalInt}. Must not be {@code null}.
	 * @param scripts The {@link List} of {@link LegacyClientScript}s. Must not be {@code null}.
	 * @param option The {@link Option} of the ContainerWidget, as an {@link Optional}. Must not be {@code null}.
	 * @param hoverText The hover text of the ContainerWidget, as an {@link Optional}. Must not be {@code null}.
	 * @param scrollLimit The scroll limit of the container ContainerWidget.
	 * @param hidden Whether or not the ContainerWidget is hidden.
	 * @param children The child ids of the ContainerWidget.
	 * @param childPoints The {@link Point}s of the child ContainerWidgets.
	 */
	public ContainerWidget(int id, OptionalInt parent, WidgetOption optionType, int content, int width, int height, int alpha,
			OptionalInt hover, List<LegacyClientScript> scripts, Optional<Option> option, Optional<String> hoverText,
			int scrollLimit, boolean hidden, int[] children, List<Point> childPoints) {
		super(id, parent, WidgetGroup.CONTAINER, optionType, content, width, height, alpha, hover, scripts, option, hoverText);

		this.scrollLimit = scrollLimit;
		this.hidden = hidden;
		this.children = Objects.requireNonNull(children, "Child array must not be null.").clone();
		this.childPoints = ImmutableList.copyOf(Objects.requireNonNull(childPoints, "Child Point List must not be null."));

		Preconditions.checkArgument(children.length == childPoints.size(), "Child ids and child points must be of equal length.");
	}

	@Override
	public DataBuffer encodeBespoke() {
		DataBuffer buffer = DataBuffer.allocate((2 + children.length) * Short.BYTES + Byte.BYTES);
		buffer.putShort(scrollLimit);
		buffer.putBoolean(hidden);

		int size = children.length;
		buffer.putShort(size);

		for (int index = 0; index < size; index++) {
			buffer.putShort(children[index]);

			Point point = childPoints.get(index);
			buffer.putShort(point.getX());
			buffer.putShort(point.getY());
		}

		return buffer.flip().asReadOnlyBuffer();
	}

}