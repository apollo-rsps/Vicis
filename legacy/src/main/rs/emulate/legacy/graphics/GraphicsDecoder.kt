package rs.emulate.legacy.graphics

import rs.emulate.legacy.archive.Archive
import rs.emulate.legacy.archive.ArchiveEntry
import rs.emulate.shared.util.DataBuffer

import java.io.FileNotFoundException

/**
 * A base class for graphics ArchiveEntry decoders.
 *
 * @param data The [ArchiveEntry] containing the graphical data.
 * @param index The [ArchiveEntry] containing the file indices.
 */
abstract class GraphicsDecoder(data: ArchiveEntry, index: ArchiveEntry) {

    /**
     * The [DataBuffer] containing the graphic data.
     */
    protected val data: DataBuffer = data.buffer.asReadOnlyBuffer()

    /**
     * The [DataBuffer] containing the file indices.
     */
    protected val index: DataBuffer = index.buffer.asReadOnlyBuffer()

    companion object {

        /**
         * Gets an [ArchiveEntry] containing data from the specified [Archive].
         *
         * @param graphics The graphics Archive.
         * @param name The name of the entry, without the [GraphicsConstants.DATA_EXTENSION].
         * @throws FileNotFoundException If the index could not be found.
         */
        @JvmStatic
        protected fun getDataEntry(graphics: Archive, name: String): ArchiveEntry {
            return graphics.getEntry(name + GraphicsConstants.DATA_EXTENSION)
        }

        /**
         * Gets the `index.dat` [ArchiveEntry] from the specified [Archive].
         *
         * @param graphics The graphics Archive.
         * @throws FileNotFoundException If the index could not be found.
         */
        @JvmStatic
        protected fun getIndexEntry(graphics: Archive): ArchiveEntry {
            return graphics.getEntry(GraphicsConstants.INDEX_FILE_NAME)
        }
    }

}
