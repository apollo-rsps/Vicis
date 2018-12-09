package rs.emulate.modern.fs

sealed class FileStoreOption {
    object Write : FileStoreOption()
    object Lenient : FileStoreOption()
}
