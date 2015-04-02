package rs.emulate.legacy.widget;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import rs.emulate.legacy.archive.Archive;
import rs.emulate.legacy.widget.type.Widget;
import rs.emulate.shared.util.DataBuffer;

/**
 * Decodes data into {@link Widget}s.
 *
 * @author Major
 */
public final class WidgetDecoder {

	/**
	 * The amount of sprites used by an {@link WidgetGroup#INVENTORY} Widget.
	 */
	private static final int SPRITE_COUNT = 20;

	/**
	 * The name of the file containing the Widget data.
	 */
	private static final String DATA_FILE_NAME = "data";

	/**
	 * The Archive containing the Widgets.
	 */
	private final DataBuffer buffer;

	/**
	 * Creates the WidgetDecoder.
	 *
	 * @param widgets The {@link Archive} containing the {@link Widget}s.
	 */
	public WidgetDecoder(Archive widgets) {
		try {
			this.buffer = widgets.getEntry(DATA_FILE_NAME).getBuffer();
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Specified Archive does not contain a " + DATA_FILE_NAME + " entry.", e);
		}
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

		return null;
	}

	/**
	 * Decodes a single {@link Widget}.
	 * 
	 * @return The Widget.
	 */
	private Widget decodeWidget() {

		return null;
	}

}