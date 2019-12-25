package rs.emulate.editor.core.project

import kotlinx.io.IOException
import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import java.util.*

sealed class ProjectType(private val type: String) {
    object Legacy : ProjectType("legacy")
    object Modern : ProjectType("modern")

    @ExperimentalStdlibApi
    final override fun toString(): String {
        return type.capitalize(Locale.getDefault())
    }

    @Serializer(forClass = ProjectType::class)
    companion object : KSerializer<ProjectType> {
        override val descriptor: SerialDescriptor = StringDescriptor.withName("ProjectType")

        override fun serialize(output: Encoder, obj: ProjectType) {
            output.encodeString(obj.type)
        }

        override fun deserialize(input: Decoder): ProjectType {
            return when (val type = input.decodeString()) {
                "legacy" -> Legacy
                "modern" -> Modern
                else -> throw IOException("Failed to decode ProjectType: unknown type $type.")
            }
        }
    }
}
