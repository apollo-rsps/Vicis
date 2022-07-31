package rs.emulate.vicis.web.storage

import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import rs.emulate.legacy.IndexedFileSystem
import rs.emulate.legacy.archive.ArchiveCodec
import rs.emulate.legacy.archive.entryHash
import rs.emulate.legacy.graphics.GraphicsConstants
import rs.emulate.legacy.graphics.sprite.MediaId
import rs.emulate.legacy.graphics.sprite.Sprite
import rs.emulate.legacy.graphics.sprite.SpriteDecoder
import rs.emulate.util.crc32
import java.time.Instant

data class SpriteId(val container: MediaId, val index: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SpriteId

        if (container != other.container) return false
        if (index != other.index) return false

        return true
    }

    override fun hashCode(): Int {
        var result = container.hashCode()
        result = 31 * result + index
        return result
    }
}

class SpriteStorageAdapter(val fs: IndexedFileSystem) : DefaultStorageAdapter<SpriteId, Sprite>() {
    override fun getElementId(element: Sprite) = SpriteId(element.name, element.index)

    override fun loadWorkingCopy(): Result<StorageWorkingCopy<SpriteId, Sprite>, StorageError> {
        val spritesArchiveData = fs[0, GraphicsConstants.GRAPHICS_FILE_ID]
        val spritesArchiveCrc = spritesArchiveData.crc32()
        val spritesArchive = ArchiveCodec.decode(spritesArchiveData)

        val sprites = spritesArchive.entries
            .filter { it.identifier != "index.dat".entryHash() }
            .flatMap {
                val decoder = SpriteDecoder.create(fs, MediaId.Id(it.identifier))
                decoder.decode()
            }

        return Ok(StorageWorkingCopy(sprites.associateBy { getElementId(it) }, StorageCacheToken.from(spritesArchiveCrc)))
    }

    override fun storeWorkingCopy(workingCopy: StorageWorkingCopy<SpriteId, Sprite>): Result<Unit, StorageError> {
        TODO("Not yet implemented")
    }

}