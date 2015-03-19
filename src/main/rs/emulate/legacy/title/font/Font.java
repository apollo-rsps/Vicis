package rs.emulate.legacy.title.font;

import rs.emulate.util.Assertions;

import com.google.common.base.Preconditions;

/**
 * A Font containing a set of character {@link Glyph}s.
 * 
 * @author Major
 */
public final class Font {

	/**
	 * The Glyphs of this Font.
	 */
	private final Glyph[] glyphs;

	/**
	 * The name of this Font.
	 */
	private final String name;

	/**
	 * Creates the Font with the specified array of {@link Glyph}s, which must have a length of {@code 256}.
	 * 
	 * @param name The name of this font.
	 * @param glyphs The array of Glyphs, which cannot be null.
	 */
	public Font(String name, Glyph[] glyphs) {
		Assertions.checkNonEmpty(name, "Name cannot be null or empty.");
		Preconditions.checkArgument(glyphs.length == 256, "Font must contain 256 glyphs.");

		this.name = name;
		this.glyphs = glyphs.clone();
	}

	/**
	 * Gets the {@link Glyph} at the specified index.
	 * 
	 * @param index The index.
	 * @return The Glyph.
	 */
	public Glyph getGlyph(int index) {
		Preconditions.checkElementIndex(index, glyphs.length, "Glyph index must be between 0 and 255, inclusive.");
		return glyphs[index];
	}

	/**
	 * Gets the {@link Glyph}s of this font. This method returns a shallow copy (and as Glyphs are immutable, it is
	 * effectively a deep copy).
	 * 
	 * @return The Glyphs.
	 */
	public Glyph[] getGlyphs() {
		return glyphs.clone();
	}

	/**
	 * Gets the name of this Font.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

}