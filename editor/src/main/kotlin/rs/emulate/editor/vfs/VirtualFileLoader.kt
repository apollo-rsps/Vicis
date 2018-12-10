package rs.emulate.editor.vfs

import io.netty.buffer.ByteBuf
import rs.emulate.legacy.IndexedFileSystem
import rs.emulate.legacy.config.ConfigEntryDecoder

interface VirtualFileLoader<T : VirtualFileId> {
    fun load(id: T): ByteBuf?
}

class LegacyFileLoader(
    private val cache: IndexedFileSystem // TODO inject this (or a similar interface)
) : VirtualFileLoader<LegacyFileId> {

    private val configArchive by lazy { cache.getArchive(CONFIG_INDEX, CONFIG_ARCHIVE_ID) }

    override fun load(id: LegacyFileId): ByteBuf? {
        return when (id) {
            is LegacyFileId.ArchiveEntry -> loadArchiveEntry(id)
            is LegacyFileId.ConfigEntry -> loadConfigEntry(id)
            is LegacyFileId.FileEntry -> loadFileEntry(id)
        }
    }

    private fun loadFileEntry(id: LegacyFileId.FileEntry): ByteBuf? {
        return cache[id.type, id.file]
    }

    private fun loadArchiveEntry(id: LegacyFileId.ArchiveEntry): ByteBuf? {
        val archive = cache.getArchive(0, id.file)
        return archive.getOrNull(id.name)?.buffer
    }

    private fun loadConfigEntry(id: LegacyFileId.ConfigEntry): ByteBuf? {
        return ConfigEntryDecoder.get(configArchive, id.entry, id.file)
    }

    private companion object { // TODO move these elsewhere
        const val CONFIG_INDEX = 0
        const val CONFIG_ARCHIVE_ID = 2
    }

}
