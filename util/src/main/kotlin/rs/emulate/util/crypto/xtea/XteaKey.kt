package rs.emulate.util.crypto.xtea

import java.security.SecureRandom

data class XteaKey(private val key: List<Int>) : Iterable<Int> {
    init {
        require(key.size == PARTS)
    }

    val isZero: Boolean
        get() = key.all { it == 0 }

    fun toIntArray(): IntArray {
        return key.toIntArray()
    }

    operator fun get(part: Int): Int = key[part]

    override fun iterator(): Iterator<Int> {
        return key.iterator()
    }

    override fun toString(): String {
        return key.joinToString(separator = "") {
            Integer.toHexString(it).padStart(8, '0')
        }
    }

    companion object {
        const val PARTS = 4
        val NONE = XteaKey(List(PARTS) { 0 })

        private val random = SecureRandom()

        fun create(init: (Int) -> Int): XteaKey {
            return XteaKey(List(PARTS, init))
        }

        fun createRandom(): XteaKey = create {
            random.nextInt()
        }

        fun fromString(hex: String): XteaKey {
            require(hex.length == 8 * PARTS)
            return create {
                Integer.parseUnsignedInt(hex.substring(it * 8, (it + 1) * 8), 16)
            }
        }

        fun fromArray(key: IntArray): XteaKey =
            XteaKey(key.toList())
    }
}
