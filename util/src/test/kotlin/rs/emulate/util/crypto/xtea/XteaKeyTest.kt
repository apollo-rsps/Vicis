package rs.emulate.util.crypto.xtea

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import rs.emulate.util.crypto.xtea.XteaKey

class XteaKeyTest {

    @Test
    fun `to hex string`() {
        val key = XteaKey.fromArray(
            intArrayOf(
                0x0000AABB,
                0xFFFFFFFF.toInt(),
                0xCCDD0000.toInt(),
                0xF0F0F0F0.toInt()
            )
        )

        assertEquals("0000aabbffffffffccdd0000f0f0f0f0", key.toString())
    }

    @Test
    fun `from hex string`() {
        val key = XteaKey.fromString("0000aabbffffffffccdd0000f0f0f0f0")

        val expected = intArrayOf(
            0x0000AABB,
            0xFFFFFFFF.toInt(),
            0xCCDD0000.toInt(),
            0xF0F0F0F0.toInt()
        )

        assertArrayEquals(expected, key.toIntArray())
    }
}
