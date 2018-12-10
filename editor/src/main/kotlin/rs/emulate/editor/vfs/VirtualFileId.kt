package rs.emulate.editor.vfs

/**
 * An identifier for a file in a virtual file store.
 */
interface VirtualFileId

sealed class LegacyFileId : VirtualFileId {

    data class FileEntry(val type: Int, val file: Int) : LegacyFileId()

    data class ArchiveEntry(val file: Int, val name: String) : LegacyFileId()

    data class ConfigEntry(val entry: String, val file: Int) : LegacyFileId()

}

sealed class ModernFileId : VirtualFileId
