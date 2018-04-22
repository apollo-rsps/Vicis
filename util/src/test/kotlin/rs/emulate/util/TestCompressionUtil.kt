package rs.emulate.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import rs.emulate.shared.util.CompressionUtils
import rs.emulate.shared.util.DataBuffer

class TestCompressionUtil {

    /**
     * Tests the [CompressionUtils.bzip2] and [CompressionUtils.bunzip2] methods.
     */
    @Test
    fun testBzip2() {
        val str = "Hello, World!"
        val data = str.toByteArray()
        val compressed = CompressionUtils.bzip2(data)
        CompressionUtils.bunzip2(compressed)
        assertEquals(str, String(data))
    }

    /**
     * Tests the [CompressionUtils.gzip] and [CompressionUtils.gunzip] methods.
     */
    @Test
    fun testGzip() {
        val test = "Hello, World!"
        val data = DataBuffer.wrap(test.toByteArray())
        val compressed = CompressionUtils.gzip(data)
        val decompressed = CompressionUtils.gunzip(compressed)

        assertEquals(test, String(decompressed.array()))
    }

}
