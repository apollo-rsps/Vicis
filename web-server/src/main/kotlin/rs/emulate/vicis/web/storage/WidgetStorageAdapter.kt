package rs.emulate.vicis.web.storage

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import rs.emulate.legacy.IndexedFileSystem
import rs.emulate.legacy.archive.ArchiveCodec
import rs.emulate.legacy.widget.Widget
import rs.emulate.legacy.widget.WidgetDecoder
import rs.emulate.util.crc32

class WidgetStorageAdapter(val fs: IndexedFileSystem) : DefaultStorageAdapter<Int, Widget>() {
    override fun getElementId(element: Widget) = element.id

    override fun loadWorkingCopy(): Result<StorageWorkingCopy<Int, Widget>, StorageError> {
        return fs.findFile(0, 3)
            .andThen { archiveData ->
                runCatchingStorageError {
                    val archiveCrc = archiveData.crc32()
                    val archive = ArchiveCodec.decode(archiveData)
                    val decoder = WidgetDecoder(archive)
                    val widgets = decoder.decode()

                    StorageWorkingCopy(widgets.associateBy(Widget::id), StorageCacheToken.from(archiveCrc))
                }
            }
    }

    override fun storeWorkingCopy(workingCopy: StorageWorkingCopy<Int, Widget>): Result<Unit, StorageError> {
        TODO("Not yet implemented")
    }
}