package rs.emulate.modern.fs

import com.google.common.io.MoreFiles
import com.google.common.jimfs.Configuration
import com.google.common.jimfs.Jimfs
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class JagexFileStoreTest {

    @Test
    fun `open no data file`() {
        Jimfs.newFileSystem(Configuration.unix()).use { fs ->
            assertThrows(IllegalArgumentException::class.java) {
                JagexFileStore.open(fs.getPath("/"))
            }
        }
    }

    @Test
    fun `open non-existent dir`() {
        Jimfs.newFileSystem(Configuration.unix()).use { fs ->
            assertThrows(IllegalArgumentException::class.java) {
                JagexFileStore.open(fs.getPath("/non-existent"))
            }
        }
    }

    @Test
    fun `open file`() {
        Jimfs.newFileSystem(Configuration.unix()).use { fs ->
            MoreFiles.touch(fs.getPath("/file"))

            assertThrows(IllegalArgumentException::class.java) {
                JagexFileStore.open(fs.getPath("/file"))
            }
        }
    }

    // TODO add read/write/etc. tests
}
