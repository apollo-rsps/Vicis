package rs.emulate.modern.codec.store

sealed class FileStoreOption {
    object Write : FileStoreOption()
    object Lenient : FileStoreOption()
}
