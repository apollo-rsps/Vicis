package rs.emulate.vicis.web.storage

@JvmInline
value class StorageCacheToken(val etag: String) {
    companion object {
        fun from(crc: Int) = StorageCacheToken(crc.toString(16))
    }
}