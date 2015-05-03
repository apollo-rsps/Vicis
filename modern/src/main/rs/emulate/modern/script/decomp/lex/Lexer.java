package rs.emulate.modern.script.decomp.lex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rs.emulate.modern.script.disasm.ClientScriptDumper;

import com.google.common.collect.ImmutableSet;

/**
 * A lexer for disassembled ClientScripts.
 *
 * @author Major
 */
public final class Lexer implements AutoCloseable {

	/**
	 * Indicates that all of the data has been read.
	 */
	private static final int END_OF_STREAM = -1;

	/**
	 * TODO remove.
	 * 
	 * @param args The program args.
	 * @throws IOException If x.
	 */
	public static void main(String[] args) throws IOException {
		Path scripts = Paths.get("./data/dump/cs");
		Path zero = scripts.resolve("0.cscript");
		Set<String> mnemonics = new HashSet<>(ClientScriptDumper.opcodes.values());

		try (Lexer lexer = new Lexer(Files.newBufferedReader(zero), mnemonics)) {
			lexer.lex().forEach(System.out::println);
		}
	}

	/**
	 * The current line number.
	 */
	private int line;

	/**
	 * The Set of instruction mnemonics.
	 */
	private final Set<String> mnemonics;

	/**
	 * The Reader to read input from.
	 */
	private final Reader reader;

	/**
	 * Creates the Lexer.
	 *
	 * @param reader The {@link BufferedReader} to lex input from.
	 * @param mnemonics The Set of mnemonics.
	 */
	public Lexer(BufferedReader reader, Set<String> mnemonics) {
		this.reader = reader;
		this.mnemonics = ImmutableSet.copyOf(mnemonics);
	}

	/**
	 * Creates the Lexer.
	 *
	 * @param input The {@link InputStream} to lex input from.
	 * @param mnemonics The Set of mnemonics.
	 */
	public Lexer(InputStream input, Set<String> mnemonics) {
		this(new InputStreamReader(input), mnemonics);
	}

	/**
	 * Creates the Lexer.
	 *
	 * @param reader The {@link Reader} to lex input from.
	 * @param mnemonics The Set of mnemonics.
	 */
	public Lexer(Reader reader, Set<String> mnemonics) {
		this(new BufferedReader(reader), mnemonics);
	}

	/**
	 * Creates the Lexer.
	 *
	 * @param input The input String to lex.
	 * @param mnemonics The Set of mnemonics.
	 */
	public Lexer(String input, Set<String> mnemonics) {
		this(new StringReader(input), mnemonics);
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}

	/**
	 * Lexes the input in the underlying {@link Reader}, returning the result as a {@link List} of {@link Token}s.
	 * 
	 * @return The List of Tokens.
	 * @throws IOException If there is an error reading from the underlying Reader.
	 */
	public List<Token<?>> lex() throws IOException {
		List<Token<?>> lexemes = new ArrayList<>();

		int next = reader.read();
		while (next != END_OF_STREAM) {
			if (Character.isWhitespace(next)) {
				if (next == '\n') {
					line++;
				}

				next = reader.read();
				continue;
			}

			lexemes.add(parse((char) next));
			next = reader.read();
		}

		return lexemes;
	}

	/**
	 * Gets the numeric value of the specified character. Assumes that {@code character} is between {@code '0'} and
	 * {@code '9'}.
	 * 
	 * @param character The character.
	 * @return The numeric value.
	 */
	private int getNumericValue(char character) {
		return character - '0';
	}

	/**
	 * Returns whether or not the specified character is a digit.
	 * 
	 * @param character The character.
	 * @return {@code true} if the character is a digit, {@code false} if not.
	 */
	private boolean isDigit(int character) {
		return character >= '0' && character <= '9';
	}

	/**
	 * Returns whether or not the specified character is a letter.
	 * 
	 * @param character The character.
	 * @return {@code true} if the character is a digit, {@code false} if not.
	 */
	private boolean isLetter(int character) {
		return (character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z');
	}

	/**
	 * Parses the next {@link Token}, using the {@code first} character is a type hint.
	 * 
	 * @param first The first character.
	 * @return The Lexeme.
	 * @throws IOException If there is an error reading from the {@link Reader}.
	 */
	private Token<?> parse(char first) throws IOException {
		if (Character.isDigit(first) || first == '-') {
			return parseNumber(first);
		} else if (first == '"') {
			return parseString();
		}

		return parseInstruction(first);
	}

	/**
	 * Parses an instruction mnemonic {@link Token}.
	 * 
	 * @param first The first character.
	 * @return The Lexeme.
	 * @throws IOException If there is an error reading from the {@link Reader}.
	 * @throws IllegalArgumentException If the result is not a recognised instruction mnemonic.
	 */
	private Token<String> parseInstruction(char first) throws IOException {
		StringBuilder builder = new StringBuilder().append(first);
		int next = reader.read();

		while (next != END_OF_STREAM && validInstructionCharacter(next)) {
			builder.append((char) next);
			next = reader.read();
		}

		String mnemonic = builder.toString();
		if (!mnemonic.startsWith("op") && !mnemonics.contains(mnemonic)) {
			throw new IllegalArgumentException("Unrecognised instruction mnemonic " + mnemonic + " on line " + line + ".");
		}

		return Token.forInstruction(mnemonic);
	}

	/**
	 * Parses a numerical value into a {@link Token}.
	 * 
	 * @param first The first character.
	 * @return The lexeme.
	 * @throws IOException If there is an error reading from the {@link Reader}.
	 */
	private Token<?> parseNumber(char first) throws IOException {
		boolean negate = first == '-';
		long value = negate ? 0 : getNumericValue(first);

		int next = reader.read();
		while (next != END_OF_STREAM && Character.isDigit(next)) {
			value = value * 10 + getNumericValue((char) next);
			next = reader.read();
		}

		if (negate) {
			value = -value;
		}

		return (value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE) ? Token.forInt((int) value) : Token.forLong(value);
	}

	/**
	 * Parses a String into a {@link Token}.
	 * 
	 * @return The lexeme.
	 * @throws IOException If there is an error reading from the {@link Reader}.
	 */
	private Token<String> parseString() throws IOException {
		StringBuilder builder = new StringBuilder();
		int next = reader.read(), previous = -1;

		while (next != END_OF_STREAM) {
			if (next == '"' && previous != '\\') {
				break;
			}

			builder.append((char) next);
			previous = next;
			next = reader.read();
		}

		return Token.forString(builder.toString());
	}

	/**
	 * Returns whether or not the specified character is valid in an instruction mnemonic.
	 * 
	 * @param character The character.
	 * @return {@code true} if the character is valid, {@code false} if not.
	 */
	private boolean validInstructionCharacter(int character) {
		return isLetter(character) || isDigit(character) || character == '_';
	}

}