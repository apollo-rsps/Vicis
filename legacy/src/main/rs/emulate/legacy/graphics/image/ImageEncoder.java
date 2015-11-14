package rs.emulate.legacy.graphics.image;

import rs.emulate.legacy.archive.Archive;
import rs.emulate.legacy.graphics.GraphicsEncoder;

import java.util.Arrays;
import java.util.List;

/**
 * Encodes a {@link IndexedImage} into an archive entry. TODO
 *
 * @author Major
 */
public final class ImageEncoder extends GraphicsEncoder {

	/**
	 * The List of Images to encode.
	 */
	@SuppressWarnings("unused")
	private final List<IndexedImage> images;

	/**
	 * Creates the SpriteEncoder.
	 *
	 * @param name The name of the {@link IndexedImage}.
	 * @param sprites The Images to encode.
	 */
	public ImageEncoder(String name, IndexedImage... sprites) {
		super(name);
		this.images = Arrays.asList(sprites);
	}

	@Override
	public Archive encodeInto(Archive archive) {
		return null;
	}

}