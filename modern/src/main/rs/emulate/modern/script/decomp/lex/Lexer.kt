package rs.emulate.modern.script.decomp.lex

import rs.emulate.modern.script.disasm.ClientScriptDumper
import java.io.BufferedReader
import java.io.Reader
import java.io.StringReader
import java.nio.file.Files
import java.nio.file.Paths
import java.util.ArrayList
import java.util.HashSet

/**
 * A lexer for disassembled ClientScripts.
 *
 * @param reader The [BufferedReader] to lex input from.
 * @param mnemonics The [Set] of mnemonics.
 */
class Lexer(reader: BufferedReader, mnemonics: Set<String>) : AutoCloseable {

    private val mnemonics: Set<String> = mnemonics.toHashSet()

    private val reader: Reader = reader

    /**
     * The current line number.
     */
    private var line: Int = 0

    constructor(reader: Reader, mnemonics: Set<String>) : this(BufferedReader(reader), mnemonics)

    constructor(input: String, mnemonics: Set<String>) : this(StringReader(input), mnemonics)

    override fun close() {
        reader.close()
    }

    /**
     * Lexes the input in the underlying [Reader], returning the result as a [List] of [Token]s.
     */
    fun lex(): List<Token<*>> {
        val lexemes = ArrayList<Token<*>>()

        var next = reader.read()
        while (next != END_OF_STREAM) {
            if (Character.isWhitespace(next)) {
                if (next == '\n'.toInt()) {
                    line++
                }

                next = reader.read()
                continue
            }

            lexemes.add(parse(next.toChar()))
            next = reader.read()
        }

        return lexemes
    }

    /**
     * Gets the numeric value of the specified character. Assumes that `character` is between `'0'` and `'9'`.
     */
    private fun getNumericValue(character: Char): Int {
        return character - '0'
    }

    /**
     * Returns whether or not the specified character is a digit.
     */
    private fun isDigit(character: Int): Boolean {
        return character >= '0'.toInt() && character <= '9'.toInt()
    }

    /**
     * Returns whether or not the specified character is a letter.
     */
    private fun isLetter(character: Int): Boolean {
        return character >= 'a'.toInt() && character <= 'z'.toInt() || character >= 'A'.toInt() && character <= 'Z'.toInt()
    }

    /**
     * Parses the next [Token], using the `first` character is a type hint.
     */
    private fun parse(first: Char): Token<*> {
        if (Character.isDigit(first) || first == '-') {
            return parseNumber(first)
        } else if (first == '"') {
            return parseString()
        }

        return parseInstruction(first)
    }

    /**
     * Parses an instruction mnemonic [Token].
     */
    private fun parseInstruction(first: Char): Token<String> {
        val builder = StringBuilder().append(first)
        var next = reader.read()

        while (next != END_OF_STREAM && validInstructionCharacter(next)) {
            builder.append(next.toChar())
            next = reader.read()
        }

        val mnemonic = builder.toString()
        if (!mnemonic.startsWith("op") && !mnemonics.contains(mnemonic)) {
            throw IllegalArgumentException("Unrecognised instruction mnemonic $mnemonic on line $line.")
        }

        return Token.forInstruction(mnemonic)
    }

    /**
     * Parses a numerical value into a [Token].
     */
    private fun parseNumber(first: Char): Token<*> {
        val negate = first == '-'
        var value = (if (negate) 0 else getNumericValue(first)).toLong()

        var next = reader.read()
        while (next != END_OF_STREAM && Character.isDigit(next)) {
            value = value * 10 + getNumericValue(next.toChar())
            next = reader.read()
        }

        if (negate) {
            value = -value
        }

        return if (value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE) Token.forInt(
            value.toInt()) else Token.forLong(value)
    }

    /**
     * Parses a String into a [Token].
     */
    private fun parseString(): Token<String> {
        val builder = StringBuilder()
        var next = reader.read()
        var previous = -1

        while (next != END_OF_STREAM) {
            if (next == '"'.toInt() && previous != '\\'.toInt()) {
                break
            }

            builder.append(next.toChar())
            previous = next
            next = reader.read()
        }

        return Token.forString(builder.toString())
    }

    /**
     * Returns whether or not the specified character is valid in an instruction mnemonic.
     */
    private fun validInstructionCharacter(character: Int): Boolean {
        return isLetter(character) || isDigit(character) || character == '_'.toInt()
    }

    companion object {

        /**
         * Indicates that all of the data has been read.
         */
        private const val END_OF_STREAM = -1

        @JvmStatic
        fun main(args: Array<String>) { // TODO remove.
            val scripts = Paths.get("./data/dump/cs/1750")
            val zero = scripts.resolve("0.cscript")
            val mnemonics = HashSet(ClientScriptDumper.opcodes.values)

            Lexer(Files.newBufferedReader(zero), mnemonics).use { lexer ->
                lexer.lex().forEach(::println)
            }
        }
    }

}
