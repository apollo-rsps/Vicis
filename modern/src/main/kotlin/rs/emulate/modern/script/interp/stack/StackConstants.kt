package rs.emulate.modern.script.interp.stack

/**
 * Contains stack-related constants.
 */
internal object StackConstants {

    /**
     * The default capacity of a stack.
     */
    const val DEFAULT_SIZE = 16

    /**
     * The growth factor of a stack, multiplied with the current stack size to produce the new size of the stack.
     */
    val GROWER: (Int) -> Int = { (it * 1.5).toInt() }

}
