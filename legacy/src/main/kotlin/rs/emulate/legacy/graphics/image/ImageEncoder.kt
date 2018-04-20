package rs.emulate.legacy.graphics.image

import rs.emulate.legacy.archive.Archive
import rs.emulate.legacy.graphics.GraphicsEncoder

/**
 * Encodes a [IndexedImage] into an archive entry. TODO
 *
 * @param name The name of the [IndexedImage].
 * @param sprites The Images to encode.
 */
class ImageEncoder(name: String, vararg sprites: IndexedImage) : GraphicsEncoder(name) {

    /**
     * The List of Images to encode.
     */
    private val images: List<IndexedImage> = sprites.toList()

    override fun encodeInto(archive: Archive): Archive {
        TODO()
    }

}
