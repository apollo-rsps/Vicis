package rs.emulate.editor.legacy.config.npc

import org.controlsfx.control.PropertySheet
import rs.emulate.editor.core.content.capabilities.ResourcePropertySupport
import rs.emulate.editor.core.project.Project
import rs.emulate.editor.vfs.LegacyFileId
import rs.emulate.editor.vfs.LegacyFileLoader
import rs.emulate.legacy.config.npc.NpcDefinitionDecoder

class NpcPropertySupport : ResourcePropertySupport<NpcProperty, LegacyFileId.ConfigEntry> {

    override fun createProperties(
        project: Project<LegacyFileId.ConfigEntry>,
        id: LegacyFileId.ConfigEntry
    ): List<PropertySheet.Item> {
        val loader = project.loader
        val buf = loader.load(id) ?: error("Failed to decode $id.")

        val definition = NpcDefinitionDecoder.decode(id.file, buf)
        return NpcPropertySheetItem.from(definition, loader as LegacyFileLoader)
    }

}
