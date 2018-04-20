package rs.emulate.legacy.model

import com.google.common.primitives.Ints

import java.util.BitSet

/**
 * A bit-packed set of attributes a Model may have.
 *
 * @param attributes The bit-packed attributes.
 */
class ModelAttributes(attributes: Int) {

    /**
     * The BitSet containing the attributes.
     */
    private val bits: BitSet = BitSet.valueOf(Ints.toByteArray(attributes))

    /**
     * Gets the value of the attribute stored in the specified bit (where the first bit is 0b0), as a boolean.
     */
    operator fun get(bit: Int): Boolean {
        return bits.get(bit)
    }

    /**
     * Returns whether or not the Model this set of attributes belongs to is a mandatory file (i.e. must be present
     * before the client will fully load).
     */
    val mandatory: Boolean = get(MANDATORY)

    /**
     * Gets the request priority of the Model this set of attributes belongs to.
     *
     * Despite bit 0 having the lowest priority, if it is set the Model must be present before the client can continue
     * loading (i.e. it is a mandatory file). The rest of the bits indicate the priority used when requesting the
     * "extra" (non-mandatory) files, which range from 0 (lowest) to 10 (highest).
     *
     * @return The request priority.
     */
    fun priority(): Int {
        // TODO methods for these flags
        // bits 1, 5, 6 cause the client to redraw the tabs and dialogue box when the model is loaded.
        // bits 0, 3, 4, 5, and 6 have their model header set to null when the client loads a new region.

        return when {
            bits.get(MAXIMUM_PRIORITY) -> 10
            bits.get(5) -> 9
            bits.get(4) -> 8
            bits.get(6) -> 7
            bits.get(7) -> 6
            bits.get(1) -> 5
            bits.get(2) -> 4
            bits.get(MANDATORY) -> 3
            else -> 0
        }
    }

    /**
     * Sets the value of the attribute stored in the specified bit.
     */
    operator fun set(bit: Int, value: Boolean) {
        bits.set(bit, value)
    }

    companion object {

        /**
         * The bit that indicates the Model these attributes belong to is mandatory (i.e. must be loaded before the
         * client can continue to the login screen).
         */
        private const val MANDATORY = 0 // TODO this is probably a specific type of model

        /**
         * The bit that indicates the Model these attributes belong to has the maximum priority when loaded as an extra
         * file.
         */
        private const val MAXIMUM_PRIORITY = 3
    }

}
