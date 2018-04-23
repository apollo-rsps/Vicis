package rs.emulate.legacy.config.floor

import rs.emulate.legacy.config.DefaultConfigDefinition
import rs.emulate.legacy.config.Properties.alwaysFalse
import rs.emulate.legacy.config.Properties.asciiString
import rs.emulate.legacy.config.Properties.unsignedByte
import rs.emulate.legacy.config.Properties.unsignedTribyte
import rs.emulate.legacy.config.SerializableProperty
import rs.emulate.legacy.config.floor.FloorProperty.*
import java.util.HashMap

/**
 * A [DefaultConfigDefinition] for [FloorDefinition]s.
 *
 * @param T The type of [FloorDefinition] this default is for.
 */
class DefaultFloorDefinition<T : FloorDefinition> : DefaultConfigDefinition<T>() {

    override fun init(): Map<Int, SerializableProperty<*>> {
        val properties = HashMap<Int, SerializableProperty<*>>()

        properties[1] = unsignedTribyte(Colour, 0)
        properties[2] = unsignedByte(Texture, 0)
        properties[5] = alwaysFalse(Shadowed, true)
        properties[6] = asciiString(Name, "null") // TODO defaults to null (the value) in client
        properties[7] = unsignedTribyte(MinimapColour, 0)

        return properties
    }

}
