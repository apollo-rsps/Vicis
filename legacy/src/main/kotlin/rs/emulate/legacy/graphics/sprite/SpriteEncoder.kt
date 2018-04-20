package rs.emulate.legacy.graphics.sprite

import rs.emulate.legacy.archive.Archive
import rs.emulate.legacy.graphics.GraphicsEncoder

import java.util.Arrays

/**
 * Encodes a [Sprite] into an archive entry. TODO
 *
 * @param name The name of the [Sprite].
 * @param sprites The Sprites to encode.
 */
class SpriteEncoder(name: String, vararg sprites: Sprite) : GraphicsEncoder(name) {

    private val sprites: List<Sprite> = Arrays.asList(*sprites)

    override fun encodeInto(archive: Archive): Archive {
        TODO()
    }

}
