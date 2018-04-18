package rs.emulate.modern.script.decomp.lex

/**
 * The type of a [Token].
 */
enum class TokenType {

    /**
     * The instruction lexeme, for opcode mnemonics.
     */
    INSTRUCTION,

    /**
     * The integer lexeme, for integer values no larger than [Integer.MAX_VALUE].
     */
    INTEGER,

    /**
     * The long lexeme, for integer values larger than [Long.MAX_VALUE].
     */
    LONG,

    /**
     * The string lexeme.
     */
    STRING

}
