package rs.emulate.modern.script.decomp.lex;

/**
 * The type of a {@link Token}.
 *
 * @author Major
 */
public enum TokenType {

	/**
	 * The instruction lexeme, for opcode mnemonics.
	 */
	INSTRUCTION,

	/**
	 * The string lexeme.
	 */
	STRING,

	/**
	 * The integer lexeme, for integer values no larger than {@link Integer#MAX_VALUE}.
	 */
	INTEGER,

	/**
	 * The long lexeme, for integer values larger than {@link Long#MAX_VALUE}.
	 */
	LONG;

}