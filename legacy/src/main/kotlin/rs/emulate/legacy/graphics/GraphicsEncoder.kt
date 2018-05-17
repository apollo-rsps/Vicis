package rs.emulate.legacy.graphics

import rs.emulate.legacy.archive.Archive

/**
 * A base class for graphics archive entry encoders.
 *
 * @param name The name of the archive entry.
 */
abstract class GraphicsEncoder(protected val name: String) {

    /**
     * Encodes these graphics into the given [Archive].
     */
    abstract fun encodeInto(archive: Archive): Archive

}
