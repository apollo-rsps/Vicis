package rs.emulate.editor.vfs.index

import rs.emulate.editor.vfs.LegacyFileId
import rs.emulate.editor.vfs.LegacyResourceType
import rs.emulate.editor.vfs.ModernFileId
import rs.emulate.editor.vfs.ModernResourceType
import rs.emulate.editor.vfs.ResourceType
import rs.emulate.editor.vfs.VirtualFileId
import java.util.Objects

sealed class VirtualFileIndex<out T : VirtualFileId, R : ResourceType>(
    val category: R,
    val entries: List<VirtualFileIndexEntry<out T>>
)

class LegacyVirtualFileIndex(
    category: LegacyResourceType,
    entries: List<VirtualFileIndexEntry<out LegacyFileId>>
) : VirtualFileIndex<LegacyFileId, LegacyResourceType>(category, entries)

class ModernVirtualFileIndex(
    category: ModernResourceType,
    entries: List<VirtualFileIndexEntry<out ModernFileId>>
) : VirtualFileIndex<ModernFileId, ModernResourceType>(category, entries)

class VirtualFileIndexEntry<T : VirtualFileId>(val id: T, name: String?) {
    val name = name ?: id.toString()

    override fun equals(other: Any?): Boolean {
        return id == (other as? VirtualFileIndexEntry<*>)?.id
    }

    override fun hashCode(): Int = Objects.hash(id)

}
