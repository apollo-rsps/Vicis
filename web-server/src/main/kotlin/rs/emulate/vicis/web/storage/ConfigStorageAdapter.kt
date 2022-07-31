package rs.emulate.vicis.web

import com.github.michaelbull.result.*
import rs.emulate.common.config.ConfigDecoder
import rs.emulate.common.config.Definition
import rs.emulate.legacy.IndexedFileSystem
import rs.emulate.util.crc32
import rs.emulate.vicis.web.storage.*
import java.util.*


class ConfigStorageAdapter<T : Definition>(
    private val fs: IndexedFileSystem,
    private val name: String,
    private val decoder: ConfigDecoder<T>,
) : DefaultStorageAdapter<Int, T>() {

    override fun getElementId(element: T) = element.id

    override fun loadWorkingCopy(): Result<StorageWorkingCopy<Int, T>, StorageError> {
        val buffers = binding {
            val configContainer = fs.findArchive(0, 2).bind()
            val data = configContainer.getFile("$name.dat").bind().buffer
            val index = configContainer.getFile("$name.idx").bind().buffer

            Ok(data to index)
        }

        return buffers.andThen { (buffers) ->
            val (data, index) = buffers
            val dataCrc = data.crc32()
            val dataCount = data.getUnsignedShort(0)

            val collection = ArrayList<T>(dataCount)
            var elementPosition = 2

            runCatchingStorageError {
                repeat(dataCount) { id ->
                    val elementLength = index.getUnsignedShort((id + 1) * 2)

                    try {
                        collection.add(decoder.decode(id, data.retainedSlice(elementPosition, elementLength)))
                    } catch (err: Throwable) {
                        throw err
                    }

                    elementPosition += elementLength
                }

                StorageWorkingCopy(collection.associateBy { getElementId(it) }, StorageCacheToken.from(dataCrc))
            }
        }
    }

    override fun storeWorkingCopy(workingCopy: StorageWorkingCopy<Int, T>): Result<Unit, StorageError> {
        TODO("Not yet implemented")
    }

    companion object {
        fun <DefT : Definition> create(
            fs: IndexedFileSystem,
            name: String,
            decoder: ConfigDecoder<DefT>
        ): ConfigStorageAdapter<DefT> {
            return ConfigStorageAdapter(fs, name, decoder)
        }
    }
}