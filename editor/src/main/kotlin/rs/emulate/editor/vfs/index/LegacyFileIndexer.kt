package rs.emulate.editor.vfs.index

import rs.emulate.editor.vfs.LegacyFileId
import rs.emulate.editor.vfs.LegacyResourceType
import rs.emulate.legacy.IndexedFileSystem
import rs.emulate.legacy.config.ConfigDecoder
import rs.emulate.legacy.config.ConfigEntryDecoder
import rs.emulate.legacy.config.Definition
import rs.emulate.legacy.config.floor.FloorDefinitionDecoder
import rs.emulate.legacy.config.kit.IdentikitDefinitionDecoder
import rs.emulate.legacy.config.location.LocationDefinitionDecoder
import rs.emulate.legacy.config.npc.NpcDefinitionDecoder
import rs.emulate.legacy.config.obj.ObjectDefinitionDecoder
import rs.emulate.legacy.config.sequence.SequenceDefinitionDecoder
import rs.emulate.legacy.config.spotanim.SpotAnimationDefinitionDecoder
import rs.emulate.legacy.config.varbit.VarbitDefinitionDecoder
import rs.emulate.legacy.config.varp.VarpDefinitionDecoder

class LegacyFileIndexer(
    private val cache: IndexedFileSystem, // TODO inject this (or a similar interface)
    private val metadataStore: IndexMetadataStore<LegacyFileId>
) : VirtualFileIndexer<LegacyFileId, LegacyResourceType> {

    private val configArchive = cache.getArchive(0, 2)

    override fun index(): List<LegacyVirtualFileIndex> {
        return listOf(
            configIndex(LegacyResourceType.Floor, FloorDefinitionDecoder),
            configIndex(LegacyResourceType.Identikit, IdentikitDefinitionDecoder),
            configIndex(LegacyResourceType.Location, LocationDefinitionDecoder) { name },
            configIndex(LegacyResourceType.Npc, NpcDefinitionDecoder) { name },
            configIndex(LegacyResourceType.Object, ObjectDefinitionDecoder) { name },
            configIndex(LegacyResourceType.Sequence, SequenceDefinitionDecoder),
            configIndex(LegacyResourceType.SpotAnim, SpotAnimationDefinitionDecoder),
            configIndex(LegacyResourceType.Varbit, VarbitDefinitionDecoder),
            configIndex(LegacyResourceType.Varp, VarpDefinitionDecoder),
            entryIndex(LegacyResourceType.Model, MODEL_INDEX),
            entryIndex(LegacyResourceType.Frame, FRAME_INDEX),
            entryIndex(LegacyResourceType.Midi, SOUND_INDEX),
            entryIndex(LegacyResourceType.Map, MAP_INDEX)
        )
    }

    private fun entryIndex(type: LegacyResourceType, index: Int): LegacyVirtualFileIndex {
        val modelCount = cache.getFileCount(index)

        val entries = (0 until modelCount).map { file ->
            val id = LegacyFileId.FileEntry(index, file)
            VirtualFileIndexEntry(id, metadataStore.nameOf(id))
        }

        return LegacyVirtualFileIndex(type, entries)
    }

    private fun <T : Definition> configIndex(
        type: LegacyResourceType,
        decoder: ConfigDecoder<T>,
        nameEntry: T.(LegacyFileId) -> String? = { id -> metadataStore.nameOf(id) }
    ): LegacyVirtualFileIndex {
        val entries = ConfigEntryDecoder.decode(configArchive, decoder)
            .map {
                val id = LegacyFileId.ConfigEntry(decoder.entryName, it.id)
                VirtualFileIndexEntry(id, it.nameEntry(id))
            }

        return LegacyVirtualFileIndex(type, entries)
    }

    private companion object { // TODO move these elsewhere
        private const val MODEL_INDEX = 1
        private const val FRAME_INDEX = 2
        private const val SOUND_INDEX = 3
        private const val MAP_INDEX = 4
    }

}
