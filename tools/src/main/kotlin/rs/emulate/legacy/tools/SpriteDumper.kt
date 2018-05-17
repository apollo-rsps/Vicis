package rs.emulate.legacy.tools

import rs.emulate.legacy.AccessMode
import rs.emulate.legacy.IndexedFileSystem
import rs.emulate.legacy.graphics.sprite.Sprite
import rs.emulate.legacy.graphics.sprite.SpriteDecoder
import java.awt.image.BufferedImage
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import javax.imageio.ImageIO

/**
 * Dumps decoded Sprites into files.
 *
 * @param decoder The [SpriteDecoder] used to read the Sprites from the cache.
 * @param output The [Path] to the output directory.
 */
class SpriteDumper(
    private val decoder: SpriteDecoder,
    private val output: Path
) {

    /**
     * Dumps all of the [Sprite]s into the pre-specified directory.
     *
     * @param format The name of the image format.
     * @throws IOException If there is an error decoding the Sprites.
     */
    fun dump(format: String) {
        val sprites = decoder.decode()

        for (index in sprites.indices) {
            val sprite = sprites[index]

            val width = sprite.width
            val height = sprite.height
            val image = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

            val raster = sprite.raster
            for (x in 0 until width) {
                for (y in 0 until height) {
                    val colour = raster[x + y * width]
                    image.setRGB(x, y, colour)
                }
            }

            ImageIO.write(image, format, output.resolve(Paths.get(index.toString() + "." + format)).toFile())
        }
    }

    companion object {

        /**
         * The [Path] to the resources directory containing the caches.
         */
        private val RESOURCES_PATH = Paths.get("data/resources")

        /**
         * Creates a [SpriteDumper] for the [Sprite](s) with the specified name.
         *
         * @param fs The [IndexedFileSystem] containing the Sprite(s).
         * @param version The version of the cache.
         * @param name The name of the Sprite(s).
         */
        fun create(fs: IndexedFileSystem, version: String, name: String): SpriteDumper {
            val output = Paths.get("./data/dump/sprites/$version")
            Files.createDirectories(output)

            val decoder = SpriteDecoder.create(fs, name)
            return SpriteDumper(decoder, output)
        }

        /**
         * The main entry point of the application.
         */
        @JvmStatic
        fun main(args: Array<String>) {
            if (args.size != 2) {
                System.err.println("SpriteDumper must have two arguments: cache version and sprite name.")
                System.exit(1)
            }

            val version = args[0]
            val name = args[1]

            try {
                val fs = IndexedFileSystem(RESOURCES_PATH.resolve(version), AccessMode.READ)
                val dumper = SpriteDumper.create(fs, version, name)
                dumper.dump("png")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}
