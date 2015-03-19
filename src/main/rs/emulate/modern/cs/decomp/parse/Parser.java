package rs.emulate.modern.cs.decomp.parse;

import java.util.List;

import rs.emulate.modern.cs.decomp.lex.Token;

import com.google.common.collect.ImmutableList;

/**
 * A Parser for tokenized disassembled clientscripts.
 *
 * @author Major
 */
public final class Parser {

	/**
	 * The List of Tokens.
	 */
	@SuppressWarnings("unused")
	private final List<Token<?>> tokens;

	/**
	 * Creates the Parser.
	 *
	 * @param tokens The {@link List} of {@link Token}s to parse.
	 */
	public Parser(List<Token<?>> tokens) {
		this.tokens = ImmutableList.copyOf(tokens);
	}

}