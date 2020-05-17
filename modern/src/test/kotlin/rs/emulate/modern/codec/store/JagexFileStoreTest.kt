package rs.emulate.modern.codec.store

import com.google.common.io.MoreFiles
import com.google.common.jimfs.Configuration
import com.google.common.jimfs.Jimfs
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import rs.emulate.common.config.npc.NpcDefinition
import rs.emulate.modern.ModernCache
import java.nio.file.Paths
import kotlin.test.assertEquals

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

    @Test
    @Tag("integration")
    fun `open osrs cache and read entry`() {
        val cachePath = System.getProperty("VICIS_OSRS_CACHE")
        val cache = ModernCache.open(Paths.get(cachePath), FileStoreOption.Lenient)
        val npcReader = cache.createReader<Int, NpcDefinition>()
        val npc = npcReader.read(0)

        assertEquals("Tool Leprechaun", npc.name)
    }
}
