package rs.emulate.legacy.widget;

import rs.emulate.legacy.archive.Archive;
import rs.emulate.legacy.widget.script.LegacyClientScript;
import rs.emulate.legacy.widget.script.LegacyClientScriptCodec;
import rs.emulate.shared.util.DataBuffer;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

/**
 * Decodes data into {@link Widget}s.
 *
 * @author Major
 */
public final class WidgetDecoder {

	/**
	 * The identifier that indicates that a Widget has a parent.
	 */
	private static final int CHILD_WIDGET_IDENTIFIER = 65_535;

	/**
	 * The name of the file containing the Widget data.
	 */
	private static final String DATA_FILE_NAME = "data";

	/**
	 * The amount of sprites used by an {@link WidgetGroup#INVENTORY} Widget.
	 */
	@SuppressWarnings("unused")
	private static final int SPRITE_COUNT = 20;

	/**
	 * The Archive containing the Widgets.
	 */
	private final DataBuffer buffer;

	/**
	 * Creates the WidgetDecoder.
	 *
	 * @param widgets The {@link Archive} containing the {@link Widget}s.
	 * @throws FileNotFoundException If the {@link #DATA_FILE_NAME data} file could not be found.
	 */
	public WidgetDecoder(Archive widgets) throws FileNotFoundException {
		this.buffer = widgets.getEntry(DATA_FILE_NAME).getBuffer();
	}

	/**
	 * Decodes the {@link Widget}s.
	 *
	 * @return The {@link List} of {@link Widget}s.
	 */
	public List<Widget> decode() {
		List<Widget> widgets = new ArrayList<>();

		while (buffer.hasRemaining()) {
			widgets.add(decodeWidget());
		}

		return widgets;
	}

	/**
	 * Decodes a single {@link Widget}.
	 *
	 * @return The Widget.
	 */
	@SuppressWarnings("unused")
	private Widget decodeWidget() {
		int id = buffer.getUnsignedShort();
		int parent = -1;

		if (id == CHILD_WIDGET_IDENTIFIER) {
			parent = buffer.getUnsignedShort();
			id = buffer.getUnsignedShort();
		}

		WidgetGroup group = WidgetGroup.valueOf(buffer.getUnsignedByte());
		WidgetOption option = WidgetOption.valueOf(buffer.getUnsignedByte());
		int contentType = buffer.getUnsignedByte();

		int width = buffer.getUnsignedShort(), height = buffer.getUnsignedShort();
		int alpha = buffer.getUnsignedByte();

		int hoverId = buffer.getUnsignedByte();
		OptionalInt hover = (hoverId == 0) ? OptionalInt.of((hoverId - 1) << Byte.SIZE | buffer.getUnsignedByte()) : OptionalInt
				.empty();

		List<LegacyClientScript> scripts = LegacyClientScriptCodec.decode(buffer);

		return null;
	}

}