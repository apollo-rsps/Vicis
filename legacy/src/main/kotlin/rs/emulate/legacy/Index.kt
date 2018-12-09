package rs.emulate.legacy

/**
 * An [Index] points to a file in the `main_file_cache.dat` file.
 *
 * @param size The size of the file.
 * @param block The first block of the file.
 */
data class Index(val size: Int, val block: Int) { // TODO verify each fits in 24 bits

    companion object {

        /**
         * The amount of bytes required to store an index.
         */
        const val BYTES = 6
    }

}
