package rs.emulate.legacy.graphics

import io.netty.buffer.ByteBuf
import rs.emulate.legacy.archive.Archive
import rs.emulate.legacy.graphics.GraphicsConstants.INDEX_FILE_NAME
import rs.emulate.legacy.graphics.sprite.MediaId
import java.nio.ByteBuffer

/**
 * A base class for graphics ArchiveEntry decoders.
 *
 * @param graphics The [Archive] containing the graphical data.
 * @param name The name of the graphic file to decode.
 */
abstract class GraphicsDecoder(graphics: Archive, protected val name: MediaId) {

    /**
     * The [ByteBuffer] containing the graphic data.
     */
    protected val data: ByteBuf = when (name) {
        is MediaId.Id -> graphics.entries.find { it.identifier == name.value }?.buffer ?: error("Can't find sprite archive")
        is MediaId.Name -> graphics[name.value + ".dat"].buffer
    }

    /**
     * The [ByteBuffer] containing the file indices.
     */
    protected val index: ByteBuf = graphics[INDEX_FILE_NAME].buffer

}
