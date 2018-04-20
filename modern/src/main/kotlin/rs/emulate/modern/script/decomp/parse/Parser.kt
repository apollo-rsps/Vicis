package rs.emulate.modern.script.decomp.parse

import rs.emulate.modern.script.decomp.lex.Token

/**
 * A Parser for tokenized disassembled clientscripts.
 *
 * @param tokens The [List] of [Token]s to parse.
 */
class Parser(tokens: List<Token<*>>) {

    private val tokens: List<Token<*>> = tokens.toList()

}
