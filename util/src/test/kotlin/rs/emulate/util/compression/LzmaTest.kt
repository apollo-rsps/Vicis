package rs.emulate.util.compression

import io.netty.buffer.Unpooled
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LzmaTest {
    @Test
    fun `end to end`() {
        val buf1 = Unpooled.wrappedBuffer("Hello, world!".toByteArray())
        val buf2 = buf1.slice().lzma().unlzma(buf1.readableBytes())

        assertEquals(buf1, buf2)
    }
}
