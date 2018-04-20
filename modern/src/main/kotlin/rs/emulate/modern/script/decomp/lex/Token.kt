package rs.emulate.modern.script.decomp.lex

/**
 * A disassembled ClientScript Token.
 *
 * @param type The [TokenType].
 * @param lexeme The lexeme.
 * @param T The type of the lexeme.
 */
data class Token<out T>(val type: TokenType, val lexeme: T) {

    companion object {

        /**
         * Creates a Token for an instruction.
         */
        fun forInstruction(mnemonic: String): Token<String> {
            return Token(TokenType.INSTRUCTION, mnemonic)
        }

        /**
         * Creates a Token for an `int` constant.
         */
        fun forInt(lexeme: Int): Token<Int> {
            return Token(TokenType.INTEGER, lexeme)
        }

        /**
         * Creates a Token for a `long` constant.
         */
        fun forLong(lexeme: Long): Token<Long> {
            return Token(TokenType.LONG, lexeme)
        }

        /**
         * Creates a Token for a String constant.
         */
        fun forString(lexeme: String): Token<String> {
            return Token(TokenType.STRING, lexeme)
        }
    }

}
