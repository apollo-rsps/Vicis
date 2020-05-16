package rs.emulate.common.config

import rs.emulate.common.CacheItem
import rs.emulate.common.CacheItemReader

interface Definition : CacheItem<Int> {
    override val id: Int
}

object Config {
    const val DEFINITION_TERMINATOR = 0
}
