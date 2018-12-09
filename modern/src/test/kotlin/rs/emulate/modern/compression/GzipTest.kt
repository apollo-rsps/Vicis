package rs.emulate.modern.compression

import io.netty.buffer.Unpooled
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GzipTest {
    @Test
    fun `end to end`() {
        val buf1 = Unpooled.wrappedBuffer("Hello, world!".toByteArray())
        val buf2 = buf1.slice().gzip().gunzip(buf1.readableBytes())
        assertEquals(buf1, buf2)
    }
}
