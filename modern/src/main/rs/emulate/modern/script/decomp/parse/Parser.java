package rs.emulate.modern.script.decomp.parse;

import com.google.common.collect.ImmutableList;
import rs.emulate.modern.script.decomp.lex.Token;

import java.util.List;

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