package rs.emulate.util.cs

import rs.emulate.util.world.Position
import java.util.function.Supplier

/**
 * Provides player-related values used during the interpretation of a ClientScript.
 */
class PlayerProvider(
    val skills: SkillProvider,
    private val positions: () -> Position,
    val settings: (Int) -> Int,
    private val weights: () -> Int,
    private val energies: () -> Int
) {

    /**
     * Gets the combat level from the [SkillProvider] used by this PlayerProvider.
     */
    val combatLevel: Int
        get() {
            val skills = this.skills.getSkills(21)
            val attack = skills[0].maximumLevel
            val defence = skills[1].maximumLevel
            val strength = skills[2].maximumLevel
            val hitpoints = skills[3].maximumLevel
            val prayer = skills[4].maximumLevel
            val ranged = skills[5].maximumLevel
            val magic = skills[6].maximumLevel

            val base = (defence.toDouble() + hitpoints.toDouble() + Math.floor((prayer / 2).toDouble())) * 0.25
            val melee = (attack + strength) * 0.325

            return (base + Math.max(melee, Math.max(ranged, magic) * 0.4875)).toInt()
        }

    /**
     * Gets the next [Position] from the [Supplier] used by this PlayerProvider.
     */
    val position: Position
        get() = positions()

    /**
     * Gets the run energy level of the player.
     */
    val runEnergy: Int
        get() = energies()

    /**
     * Gets the total level from the [SkillProvider] used by this PlayerProvider.
     */
    val totalLevel: Int
        get() = skills.getTotal(21)

    /**
     * Gets the weight of the player.
     */
    val weight: Int
        get() = weights()

    fun skill(id: Int): Skill {
        return skills[id]
    }

    fun setting(id: Int): Int {
        return settings(id)
    }

    /**
     * Provides [Skill]s used during the execution of a ClientScript.
     */
    class SkillProvider(private val provider: (Int) -> Skill) {

        /**
         * Gets the [Skill] with the specified id.
         */
        operator fun get(id: Int): Skill = provider(id)

        /**
         * Gets an array containing each [Skill].
         *
         * @param count The amount of Skills to get.
         */
        fun getSkills(count: Int): Array<Skill> {
            val skills = arrayOfNulls<Skill>(count)
            for (id in skills.indices) {
                skills[id] = get(id)
            }

            return skills.requireNoNulls()
        }

        /**
         * Gets the total level of the [Skill]s in this SkillProvider.
         *
         * @param count The amount of Skills to get.
         */
        fun getTotal(count: Int): Int {
            return getSkills(count).map(Skill::maximumLevel).sum()
        }
    }

    /**
     * A builder for [PlayerProvider]s.
     */
    class Builder {

        var energies = { 100 }
        var positions = { Position(10, 10) }
        var settings: (Int) -> Int = { 1 }
        var skills: SkillProvider =
            SkillProvider {
                Skill(
                    0.0,
                    1,
                    1
                )
            }
        var weights = { 42 }

        fun build(): PlayerProvider {
            return PlayerProvider(skills, positions, settings, weights, energies)
        }

    }

    /**
     * A skill of a player.
     */
    data class Skill(val experience: Double, val currentLevel: Int, val maximumLevel: Int)

    companion object {

        /**
         * Returns the default [PlayerProvider].
         */
        fun defaultProvider(): PlayerProvider {
            return Builder().build()
        }
    }

}
