package rs.emulate.editor.vfs

sealed class ResourceType(val name: String)

sealed class LegacyResourceType(name: String) : ResourceType(name) {
    /* index 0 archives, except config */
    object Title : LegacyResourceType("Title")
    object Interface : LegacyResourceType("Widget")
    object Media : LegacyResourceType("Media")
    object Versions : LegacyResourceType("Version list")
    object Texture : LegacyResourceType("Textures")
    object WordEnc : LegacyResourceType("Censor")
    object Sound : LegacyResourceType("Sound effects")

    /* config archive entries */
    object Floor : LegacyResourceType("Floor")
    object Identikit : LegacyResourceType("Identikit")
    object Location : LegacyResourceType("Location")
    object Npc : LegacyResourceType("Npc")
    object Object : LegacyResourceType("Object")
    object Sequence : LegacyResourceType("Sequence")
    object SpotAnim : LegacyResourceType("Spot animation")
    object Varbit : LegacyResourceType("Bit variable")
    object Varp : LegacyResourceType("Player variable")

    /* indices 1-4 */
    object Model : LegacyResourceType("Model")
    object Frame : LegacyResourceType("Frame")
    object Midi : LegacyResourceType("Songs")
    object Map : LegacyResourceType("Map")
}

sealed class ModernResourceType(name: String) : ResourceType(name) {

}
