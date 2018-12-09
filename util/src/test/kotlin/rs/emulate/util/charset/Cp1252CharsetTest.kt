package rs.emulate.util.charset

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Cp1252CharsetTest {

    @Test
    fun `test hashCode`() {
        assertEquals(0, "".hashCodeCp1252())
        assertEquals(99162322, "hello".hashCodeCp1252())
        assertEquals(-862545276, "Hello World".hashCodeCp1252())
        assertEquals(-1168732951, "£$%XD(*&^%$".hashCodeCp1252())
    }

}
