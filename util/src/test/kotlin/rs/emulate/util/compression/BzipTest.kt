package rs.emulate.util.compression

import io.netty.buffer.Unpooled
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BzipTest {
    @Test
    fun `end to end`() {
        val buf1 = Unpooled.wrappedBuffer("Hello, world!".toByteArray())
        val buf2 = buf1.slice().bzip2().bunzip2(buf1.readableBytes())
        assertEquals(buf1, buf2)
    }
}
