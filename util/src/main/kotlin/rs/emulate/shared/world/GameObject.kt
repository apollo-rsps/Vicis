package rs.emulate.shared.world

/**
 * An object in the game world.
 */
data class GameObject(val id: Int, val position: Position, val type: Int, val orientation: Int)
