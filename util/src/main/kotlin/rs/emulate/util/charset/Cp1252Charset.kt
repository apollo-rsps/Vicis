package rs.emulate.util.charset

import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.Charset
import java.nio.charset.CharsetDecoder
import java.nio.charset.CharsetEncoder
import java.nio.charset.CoderResult

object Cp1252Charset : Charset("Cp1252", null) {
    private val ASCII_CLASS = Charsets.US_ASCII.javaClass

    private val CODE_PAGE_1252 = charArrayOf(
        '\u20ac', '\u0000', '\u201a', '\u0192', '\u201e', '\u2026', '\u2020', '\u2021',
        '\u02c6', '\u2030', '\u0160', '\u2039', '\u0152', '\u0000', '\u017d', '\u0000',
        '\u0000', '\u2018', '\u2019', '\u201c', '\u201d', '\u2022', '\u2013', '\u2014',
        '\u02dc', '\u2122', '\u0161', '\u203a', '\u0153', '\u0000', '\u017e', '\u0178'
    )

    private val DECODE_TABLE = CharArray(256)
    private val ENCODE_TABLE = ByteArray(65536)

    init {
        for (index in DECODE_TABLE.indices) {
            val ch = if (index in 128..159) CODE_PAGE_1252[index - 128].toInt() else index
            DECODE_TABLE[index] = ch.toChar()
            ENCODE_TABLE[ch] = index.toByte()
        }
    }

    fun decodeChar(i: Int): Char {
        require(i in 0 until DECODE_TABLE.size)

        return DECODE_TABLE[i].also {
            require(it != 0.toChar())
        }
    }

    fun encodeChar(chr: Char): Int {
        return ENCODE_TABLE[chr.toInt()].toInt().also {
            require(it != 0)
        }
    }

    override fun contains(cs: Charset): Boolean {
        return ASCII_CLASS.isInstance(cs) || cs == Cp1252Charset
    }

    override fun newDecoder(): CharsetDecoder {
        return Cp1252Decoder(this)
    }

    override fun newEncoder(): CharsetEncoder {
        return Cp1252Encoder(this)
    }

    private class Cp1252Decoder(cs: Charset) : CharsetDecoder(cs, 1F, 1F) {
        override fun decodeLoop(input: ByteBuffer, output: CharBuffer): CoderResult {
            while (input.hasRemaining()) {
                if (!output.hasRemaining()) {
                    return CoderResult.OVERFLOW
                }

                var chr = input.get().toInt() and 0xff
                chr = DECODE_TABLE[chr].toInt()

                if (chr == 0) {
                    return CoderResult.unmappableForLength(1)
                }

                output.put(chr.toChar())
            }

            return CoderResult.UNDERFLOW
        }
    }

    private class Cp1252Encoder(cs: Charset) : CharsetEncoder(cs, 1F, 1F) {
        override fun encodeLoop(input: CharBuffer, output: ByteBuffer): CoderResult {
            while (input.hasRemaining()) {
                if (!output.hasRemaining()) {
                    return CoderResult.OVERFLOW
                }

                var chr = input.get().toInt()
                chr = ENCODE_TABLE[chr].toInt()

                if (chr == 0) {
                    return CoderResult.unmappableForLength(1)
                }

                output.put(chr.toByte())
            }

            return CoderResult.UNDERFLOW
        }
    }
}

fun String.hashCodeCp1252(): Int {
    return fold(0) { hash, char -> Cp1252Charset.encodeChar(char) + ((hash shl 5) - hash) }
}
