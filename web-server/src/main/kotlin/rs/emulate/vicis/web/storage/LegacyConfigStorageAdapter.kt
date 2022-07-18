package rs.emulate.vicis.web.storage

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import rs.emulate.common.config.ConfigDecoder
import rs.emulate.common.config.Definition
import rs.emulate.legacy.IndexedFileSystem
import rs.emulate.vicis.web.ConfigLoadResult
import rs.emulate.vicis.web.ConfigStorageAdapter
import rs.emulate.vicis.web.ConfigStorageError
import java.util.*


class LegacyConfigStorageAdapter<DefT : Definition>(
    private val fs: IndexedFileSystem,
    private val name: String,
    private val decoder: ConfigDecoder<DefT>
) : ConfigStorageAdapter<DefT> {
    var lastModifiedTime = Date()
        private set

    override fun loadAll(): ConfigLoadResult<DefT> {
        val configContainer = fs.getArchive(ARCHIVE_INDEX, CONFIG_ARCHIVE_ID)
        val data = configContainer.getOrNull("$name.dat")?.buffer ?: return Err(ConfigStorageError.DataFileUnavailable)
        val index =
            configContainer.getOrNull("$name.idx")?.buffer ?: return Err(ConfigStorageError.IndexFileUnavailable)

        val dataCount = data.getUnsignedShort(0)
        val indexCount = index.getUnsignedShort(0)

        val collection = ArrayList<DefT>(dataCount)
        var elementPosition = 2

        repeat(dataCount) { id ->
            val elementLength = index.getUnsignedShort((id + 1) * 2)

            try {
                collection.add(decoder.decode(id, data.retainedSlice(elementPosition, elementLength)))
            } catch (err: Throwable) {
                throw err
            }

            elementPosition += elementLength
        }

        return Ok(collection)
    }

    override fun saveAll(items: List<DefT>) {

    }

    private companion object {
        const val ARCHIVE_INDEX = 0
        const val CONFIG_ARCHIVE_ID = 2
    }

}