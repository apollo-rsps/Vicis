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
 * A {@link WidgetGroup#MODEL} {@link Widget}.
 *
 * @author Major
 */
public final class ModelWidget extends Widget {

	/**
	 * A model used as part of a ModelWidget.
	 */
	public static final class Model {

		/**
		 * The id of the animation.
		 */
		private final OptionalInt animation;

		/**
		 * The id of the model.
		 */
		private final OptionalInt model;

		/**
		 * Creates the Media.
		 *
		 * @param model The model id, as an {@link OptionalInt}.
		 * @param animation The animation id, as an OptionalInt.
		 */
		public Model(OptionalInt model, OptionalInt animation) {
			this.model = model;
			this.animation = animation;
		}

		/**
		 * Gets the id of the animation, as an {@link OptionalInt}.
		 *
		 * @return The animation id.
		 */
		public OptionalInt getAnimation() {
			return animation;
		}

		/**
		 * Gets the id of the model, as an {@link OptionalInt}.
		 *
		 * @return The model id.
		 */
		public OptionalInt getId() {
			return model;
		}

	}

	/**
	 * The pitch applied to the model.
	 */
	private int pitch;

	/**
	 * The primary Model.
	 */
	private Model primary;

	/**
	 * The roll applied to the model.
	 */
	private int roll;

	/**
	 * The scale applied to the model.
	 */
	private int scale;

	/**
	 * The secondary Model.
	 */
	private Model secondary;

	/**
	 * Creates the ModelWidget.
	 *
	 * @param id The id of the ModelWidget.
	 * @param parent The parent id of the ModelWidget, as an {@link OptionalInt}. Must not be {@code null}.
	 * @param optionType The {@link WidgetOption} of the ModelWidget. Must not be {@code null}.
	 * @param content The content type of the ModelWidget.
	 * @param width The width of the ModelWidget, in pixels.
	 * @param height The width of the ModelWidget, in pixels.
	 * @param alpha The alpha of the ModelWidget, in pixels.
	 * @param hover The hover id of the ModelWidget, as an {@link OptionalInt}. Must not be {@code null}.
	 * @param scripts The {@link List} of {@link LegacyClientScript}s. Must not be {@code null}.
	 * @param option The {@link Option} of the ModelWidget, as an {@link Optional}. Must not be {@code null}.
	 * @param hoverText The hover text of the ModelWidget, as an {@link Optional}. Must not be {@code null}.
	 * @param defaultModel The default {@link Model}.
	 * @param secondaryModel The secondary {@link Model}.
	 * @param scale The scale of the model.
	 * @param pitch The pitch of the model.
	 * @param roll The roll of the model.
	 */
	public ModelWidget(int id, OptionalInt parent, WidgetOption optionType, int content, int width, int height, int alpha,
			OptionalInt hover, List<LegacyClientScript> scripts, Optional<Option> option, Optional<String> hoverText,
			Model defaultModel, Model secondaryModel, int scale, int pitch, int roll) {
		super(id, parent, WidgetGroup.MODEL, optionType, content, width, height, alpha, hover, scripts, option, hoverText);

		this.primary = defaultModel;
		this.secondary = secondaryModel;
		this.scale = scale;
		this.pitch = pitch;
		this.roll = roll;
	}

	@Override
	protected DataBuffer encodeBespoke() {
		int size = 3 * Short.BYTES;
		for (Model model : Arrays.asList(primary, secondary)) {
			size += model.getId().isPresent() ? Short.BYTES : Byte.BYTES;
			size += model.getAnimation().isPresent() ? Short.BYTES : Byte.BYTES;
		}

		DataBuffer buffer = DataBuffer.allocate(size);
		for (OptionalInt optional : Arrays.asList(primary.getId(), secondary.getId(), primary.getAnimation(),
				secondary.getAnimation())) {
			if (optional.isPresent()) {
				int id = optional.getAsInt();
				buffer.putByte(id >> 8 + 1);
				buffer.putByte(id);
			} else {
				buffer.putBoolean(false);
			}
		}

		buffer.putShort(scale);
		buffer.putShort(pitch);
		buffer.putShort(roll);

		return buffer.flip().asReadOnlyBuffer();
	}

}