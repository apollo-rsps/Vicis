package rs.emulate.legacy.tools

import rs.emulate.legacy.AccessMode
import rs.emulate.legacy.IndexedFileSystem
import rs.emulate.legacy.graphics.image.ImageDecoder
import rs.emulate.legacy.graphics.image.IndexedImage
import java.awt.image.BufferedImage
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import javax.imageio.ImageIO

/**
 * Dumps decoded images into files.
 *
 * @param decoder The [ImageDecoder] used to read the [IndexedImage]s from the cache.
 * @param output The [Path] to the output directory.
 * @param name The name of the Image.
 */
class ImageDumper(
    private val decoder: ImageDecoder,
    private val output: Path,
    private val name: String
) {

    /**
     * Dumps all of the [IndexedImage]s into the pre-specified directory.
     *
     * @param format The name of the image format.
     */
    fun dump(format: String) {
        val images = decoder.decode()

        for (index in images.indices) {
            val image = images[index]

            val width = image.width
            val height = image.height
            val buffered = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

            val raster = image.raster
            for (x in 0 until width) {
                for (y in 0 until height) {
                    val colour = raster[x + y * width]
                    buffered.setRGB(x, y, colour)
                }
            }

            ImageIO.write(buffered, format, output.resolve(Paths.get(name + "_" + index + "." + format)).toFile())
        }
    }

    companion object {

        /**
         * The [Path] to the resources directory containing the caches.
         */
        private val RESOURCES_PATH = Paths.get("data/resources")

        /**
         * Creates an [ImageDumper] for the [IndexedImage](s) with the specified name.
         *
         * @param fs The [IndexedFileSystem] containing the Image(s).
         * @param version The version of the cache.
         * @param name The name of the image(s).
         */
        fun create(fs: IndexedFileSystem, version: String, name: String): ImageDumper {
            val output = Paths.get("./data/dump/images/$version")
            Files.createDirectories(output)

            val decoder = ImageDecoder.create(fs, name)
            return ImageDumper(decoder, output, name)
        }

        /**
         * The main entry point of the application.
         */
        @JvmStatic
        fun main(args: Array<String>) {
            if (args.size != 2) {
                System.err.println("ImageDumper must have two arguments: cache version and sprite name.")
                System.exit(1)
            }

            val version = args[0]
            val name = args[1]

            try {
                val fs = IndexedFileSystem(RESOURCES_PATH.resolve(version), AccessMode.READ)
                val dumper = ImageDumper.create(fs, version, name)
                dumper.dump("png")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}
