package rs.emulate.legacy.graphics.sprite;

import java.util.Arrays;
import java.util.List;

import rs.emulate.legacy.archive.Archive;
import rs.emulate.legacy.graphics.GraphicsEncoder;

/**
 * Encodes a {@link Sprite} into an archive entry. TODO
 * 
 * @author Major
 */
public final class SpriteEncoder extends GraphicsEncoder {

	/**
	 * The List of Sprites to encode.
	 */
	@SuppressWarnings("unused")
	private final List<Sprite> sprites;

	/**
	 * Creates the SpriteEncoder.
	 * 
	 * @param name The name of the {@link Sprite}.
	 * @param sprites The Sprites to encode.
	 */
	public SpriteEncoder(String name, Sprite... sprites) {
		super(name);
		this.sprites = Arrays.asList(sprites);
	}

	@Override
	public Archive encodeInto(Archive archive) {
		return null;
	}

}