package rs.emulate.legacy.graphics

import rs.emulate.legacy.archive.Archive
import rs.emulate.legacy.graphics.GraphicsConstants.DATA_EXTENSION
import rs.emulate.legacy.graphics.GraphicsConstants.INDEX_FILE_NAME
import rs.emulate.shared.util.DataBuffer

/**
 * A base class for graphics ArchiveEntry decoders.
 *
 * @param graphics The [Archive] containing the graphical data.
 * @param name The name of the graphic file to decode.
 */
abstract class GraphicsDecoder(graphics: Archive, protected val name: String) {

    /**
     * The [DataBuffer] containing the graphic data.
     */
    protected val data: DataBuffer = graphics.getEntry(name + DATA_EXTENSION).buffer.asReadOnlyBuffer()

    /**
     * The [DataBuffer] containing the file indices.
     */
    protected val index: DataBuffer = graphics.getEntry(INDEX_FILE_NAME).buffer.asReadOnlyBuffer()

}
