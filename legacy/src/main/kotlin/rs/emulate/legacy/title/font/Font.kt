package rs.emulate.legacy.title.font

/**
 * A Font containing a set of character [Glyph]s.
 *
 * @param name The name of this font.
 * @param glyphs The array of Glyphs. Must have a length of `256`
 */
class Font(val name: String, glyphs: Array<Glyph>) {

    val glyphs: Array<Glyph>
        get() = field.clone()

    init {
        require(glyphs.size == 256) { "Font must contain 256 glyphs." }
        this.glyphs = glyphs.clone()
    }

    /**
     * Gets the [Glyph] at the specified index.
     */
    fun glyph(index: Int): Glyph {
        check(index < glyphs.size) { "Glyph index must be between 0 and 255, inclusive." }
        return glyphs[index]
    }

}
