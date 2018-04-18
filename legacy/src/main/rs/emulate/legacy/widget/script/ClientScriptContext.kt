package rs.emulate.legacy.widget.script

import rs.emulate.shared.cs.PlayerProvider

/**
 * A context for an interpreted [LegacyClientScript], containing information such as the player's skills.
 */
class ClientScriptContext @JvmOverloads constructor(
    val provider: PlayerProvider = PlayerProvider.defaultProvider()) {

    /**
     * Whether or not the script has finished execution.
     */
    var finished: Boolean = false
        private set

    /**
     * The current result of the execution.
     */
    var value: Int = 0
        private set

    /**
     * Gets the result of this ClientScriptContext.
     * @throws IllegalStateException If this ClientScriptContext has not finished execution.
     */
    val result: Int
        get() {
            if (!finished) {
                throw IllegalStateException(
                    "Cannot get the result of a ClientScriptContext that has yet to finish execution.")
            }

            return value
        }

    /**
     * Applies the specified operator to the current value in this ClientScriptContext, using the`value` parameter as
     * the second operand.
     */
    internal fun apply(operator: (Int, Int) -> Int, value: Int) {
        this.value = operator(this.value, value)
    }

    /**
     * Marks this ClientScriptContext as having finished execution.
     */
    internal fun finish() {
        finished = true
    }

}
