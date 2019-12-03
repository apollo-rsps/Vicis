package rs.emulate.modern.codec

enum class CompressionType(val id: Int) {
    NONE(0),
    BZIP2(1),
    GZIP(2),
    LZMA(3);

    companion object {
        private val TYPES = values().associateBy(CompressionType::id)
        operator fun get(id: Int): CompressionType? = TYPES[id]
    }
}
