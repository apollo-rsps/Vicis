package rs.emulate.legacy

/**
 * Holds file system related constants.
 */
internal object FileSystemConstants {

    /**
     * The number of archives in cache 0.
     */
    const val ARCHIVE_COUNT = 9

    /**
     * The size of a chunk.
     */
    const val CHUNK_SIZE = 512

    /**
     * The size of a header.
     */
    const val HEADER_SIZE = 8

    /**
     * The size of a block.
     */
    const val BLOCK_SIZE = HEADER_SIZE + CHUNK_SIZE

}
