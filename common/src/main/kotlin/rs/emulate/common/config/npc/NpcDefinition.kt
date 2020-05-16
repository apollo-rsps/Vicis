package rs.emulate.common.config.npc

import rs.emulate.common.config.Definition


data class NpcDefinition(
    override val id: Int,
    var models: List<Int> = listOf(),
    var name: String = "null",
    var description: String? = null,
    var size: Int = 1,
    var standingSequence: Int = -1,
    var walkingSequence: Int = -1,
    var movementSequences: MovementAnimationSet = MovementAnimationSet.EMPTY,
    var actions: Array<String?> = arrayOfNulls(10),
    var replacementColours: MutableMap<Int, Int> = mutableMapOf(),
    var widgetModels: IntArray? = null,
    var visibleOnMinimap: Boolean = true,
    var combatLevel: Int = -1,
    var planarScale: Int = 128,
    var verticalScale: Int = 128,
    var priorityRender: Boolean = false,
    var brightness: Int = 0,
    var diffusion: Int = 0,
    var headIconId: Int = -1,
    var defaultOrientation: Int = 32,
    var morphisms: MorphismSet = MorphismSet.EMPTY,
    var clickable: Boolean = true
) : Definition {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NpcDefinition

        if (id != other.id) return false
        if (models != other.models) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (size != other.size) return false
        if (standingSequence != other.standingSequence) return false
        if (walkingSequence != other.walkingSequence) return false
        if (movementSequences != other.movementSequences) return false
        if (!actions.contentEquals(other.actions)) return false
        if (replacementColours != other.replacementColours) return false
        if (widgetModels != null) {
            if (other.widgetModels == null) return false
            if (!widgetModels!!.contentEquals(other.widgetModels!!)) return false
        } else if (other.widgetModels != null) return false
        if (visibleOnMinimap != other.visibleOnMinimap) return false
        if (combatLevel != other.combatLevel) return false
        if (planarScale != other.planarScale) return false
        if (verticalScale != other.verticalScale) return false
        if (priorityRender != other.priorityRender) return false
        if (brightness != other.brightness) return false
        if (diffusion != other.diffusion) return false
        if (headIconId != other.headIconId) return false
        if (defaultOrientation != other.defaultOrientation) return false
        if (morphisms != other.morphisms) return false
        if (clickable != other.clickable) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + models.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + size
        result = 31 * result + standingSequence
        result = 31 * result + walkingSequence
        result = 31 * result + movementSequences.hashCode()
        result = 31 * result + actions.contentHashCode()
        result = 31 * result + replacementColours.hashCode()
        result = 31 * result + (widgetModels?.contentHashCode() ?: 0)
        result = 31 * result + visibleOnMinimap.hashCode()
        result = 31 * result + combatLevel
        result = 31 * result + planarScale
        result = 31 * result + verticalScale
        result = 31 * result + priorityRender.hashCode()
        result = 31 * result + brightness
        result = 31 * result + diffusion
        result = 31 * result + headIconId
        result = 31 * result + defaultOrientation
        result = 31 * result + morphisms.hashCode()
        result = 31 * result + clickable.hashCode()
        return result
    }

}

