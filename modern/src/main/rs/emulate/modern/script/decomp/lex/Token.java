package rs.emulate.modern.script.decomp.lex;

import com.google.common.base.MoreObjects;

/**
 * A disassembled ClientScript Token.
 *
 * @param <T> The type of the lexeme.
 * @author Major
 */
public final class Token<T> {

	/**
	 * Creates a Token for an instruction.
	 *
	 * @param mnemonic The instruction mnemonic.
	 * @return The Token.
	 */
	public static Token<String> forInstruction(String mnemonic) {
		return new Token<>(TokenType.INSTRUCTION, mnemonic);
	}

	/**
	 * Creates a Token for an {@code int}.
	 *
	 * @param lexeme The lexeme.
	 * @return The Token.
	 */
	public static Token<Integer> forInt(int lexeme) {
		return new Token<>(TokenType.INTEGER, lexeme);
	}

	/**
	 * Creates a Token for a {@code long}.
	 *
	 * @param lexeme The lexeme.
	 * @return The Token.
	 */
	public static Token<Long> forLong(long lexeme) {
		return new Token<>(TokenType.LONG, lexeme);
	}

	/**
	 * Creates a Token for a String constant.
	 *
	 * @param lexeme The lexeme.
	 * @return The Token.
	 */
	public static Token<String> forString(String lexeme) {
		return new Token<>(TokenType.STRING, lexeme);
	}

	/**
	 * The lexeme.
	 */
	private final T lexeme;

	/**
	 * The type of this Token.
	 */
	private final TokenType type;

	/**
	 * Creates the Token.
	 *
	 * @param type The {@link TokenType}.
	 * @param lexeme The lexeme.
	 */
	private Token(TokenType type, T lexeme) {
		this.type = type;
		this.lexeme = lexeme;
	}

	/**
	 * Gets the lexeme of this Token.
	 *
	 * @return The lexeme.
	 */
	public T getLexeme() {
		return lexeme;
	}

	/**
	 * Gets the {@link TokenType}.
	 *
	 * @return The type.
	 */
	public TokenType getType() {
		return type;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("type", type).add("value", lexeme).toString();
	}

}