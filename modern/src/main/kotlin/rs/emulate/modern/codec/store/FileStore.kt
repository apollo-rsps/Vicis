package rs.emulate.modern.codec.store

import io.netty.buffer.ByteBuf
import java.io.Closeable
import java.nio.file.Files
import java.nio.file.Path

interface FileStore : Closeable {

    operator fun contains(index: Int): Boolean

    fun addIndex(index: Int)
    fun removeIndex(index: Int)
    fun listIndexes(): List<Int>

    fun contains(index: Int, file: Int): Boolean
    fun read(index: Int, file: Int): ByteBuf /* returns an immutable buffer */
    fun write(index: Int, file: Int, buf: ByteBuf) /* accepts any buffer */
    fun remove(index: Int, file: Int)
    fun listFiles(index: Int): List<Int>

    companion object {
        const val INDEX_LEN: Int = 256
        const val FILE_LEN: Int = 65536

        fun open(root: Path, vararg options: FileStoreOption): FileStore {
            val dataFile = root.resolve(JagexFileStore.DATA_FILE)

            return if (Files.exists(dataFile)) {
                JagexFileStore.open(root, *options)
            } else {
                FlatFileStore.open(root, *options)
            }
        }
    }
}
